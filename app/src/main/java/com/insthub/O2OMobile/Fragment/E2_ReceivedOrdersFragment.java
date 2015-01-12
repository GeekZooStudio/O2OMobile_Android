//
//       _/_/_/                      _/            _/_/_/_/_/
//    _/          _/_/      _/_/    _/  _/              _/      _/_/      _/_/
//   _/  _/_/  _/_/_/_/  _/_/_/_/  _/_/              _/      _/    _/  _/    _/
//  _/    _/  _/        _/        _/  _/          _/        _/    _/  _/    _/
//   _/_/_/    _/_/_/    _/_/_/  _/    _/      _/_/_/_/_/    _/_/      _/_/
//
//
//  Copyright (c) 2015-2016, Geek Zoo Studio
//  http://www.geek-zoo.com
//
//
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this software and associated documentation files (the "Software"),
//  to deal in the Software without restriction, including without limitation
//  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//  and/or sell copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//  IN THE SOFTWARE.
//

package com.insthub.O2OMobile.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Activity.D1_OrderActivity;
import com.insthub.O2OMobile.Activity.SlidingActivity;
import com.insthub.O2OMobile.Adapter.E2_ReceivedOrderListAdapter;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.ReceivedOrderListModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.orderlistreceivedResponse;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class E2_ReceivedOrdersFragment extends Fragment implements BusinessResponse, IXListViewListener {
    private TextView mUndoneOrderTextView;
    private TextView mDoneOrderTextView;
    private XListView mUnfinishedListView;
    private XListView mFinishedListView;

    private E2_ReceivedOrderListAdapter mUndoListAdapter;
    private E2_ReceivedOrderListAdapter mDoneListAdapter;
    private ReceivedOrderListModel mOrderListModel;

    private  ImageView mEmptyView;
    private  ImageView mBackButton;

    public static int UNDONE_ORDER = 0;
    public static int DONE_ORDER = 1;
    private int mCurrentState = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View mainView = inflater.inflate(R.layout.e0_published_orders,null);
        mainView.setOnClickListener(null);

        mUndoneOrderTextView = (TextView)mainView.findViewById(R.id.e0_undone_order);
        mDoneOrderTextView = (TextView)mainView.findViewById(R.id.e0_done_order);
        mUnfinishedListView = (XListView)mainView.findViewById(R.id.e0_orderlist_undone);
        mFinishedListView = (XListView)mainView.findViewById(R.id.e0_orderlist_done);
        mUnfinishedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < mOrderListModel.publicUnfinishedOrderList.size()) {
                    ORDER_INFO order_info = mOrderListModel.publicUnfinishedOrderList.get(position - 1);
                    Intent intent = new Intent(getActivity(), D1_OrderActivity.class);
                    intent.putExtra(D1_OrderActivity.ORDER_ID, order_info.id);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            }
        });

        mUnfinishedListView.setPullRefreshEnable(true);
        mUnfinishedListView.setPullLoadEnable(true);
        mUnfinishedListView.setXListViewListener(this, 1);

        mFinishedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < mOrderListModel.publicFinishedOrderList.size()) {
                    ORDER_INFO order_info = mOrderListModel.publicFinishedOrderList.get(position - 1);
                    Intent intent = new Intent(getActivity(), D1_OrderActivity.class);
                    intent.putExtra(D1_OrderActivity.ORDER_ID, order_info.id);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            }
        });

        mFinishedListView.setPullRefreshEnable(true);
        mFinishedListView.setPullLoadEnable(true);
        mFinishedListView.setXListViewListener(this, 2);

        mBackButton = (ImageView)mainView.findViewById(R.id.home_menu);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SlidingActivity) getActivity()).showLeft();
            }
        });

        mOrderListModel = new ReceivedOrderListModel(getActivity());
        mOrderListModel.addResponseListener(this);
        mOrderListModel.loadCacheRecivedUnfinished();
        if(mOrderListModel.publicUnfinishedOrderList!=null&& mOrderListModel.publicUnfinishedOrderList.size()>0){
            mUndoListAdapter = new E2_ReceivedOrderListAdapter(getActivity(), mOrderListModel.publicUnfinishedOrderList);
            mUnfinishedListView.setAdapter(mUndoListAdapter);
            mUnfinishedListView.loadMoreHide();
        }

        mOrderListModel.fetchPreUnfinished();

        mUndoneOrderTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyView.setVisibility(View.GONE);
                mUnfinishedListView.setVisibility(View.VISIBLE);
                mFinishedListView.setVisibility(View.GONE);
                mCurrentState = UNDONE_ORDER;
                mUndoneOrderTextView.setTextColor(Color.WHITE);
                mUndoneOrderTextView.setBackgroundResource(R.drawable.e0_nav_left_selected);
                mDoneOrderTextView.setTextColor(getResources().getColor(R.color.select_item));
                mDoneOrderTextView.setBackgroundResource(R.drawable.e0_nav_right_normal);
                if (null == mUndoListAdapter) {
                    if (mOrderListModel.publicUnfinishedOrderList != null && mOrderListModel.publicUnfinishedOrderList.size() > 0) {
                        mUndoListAdapter = new E2_ReceivedOrderListAdapter(getActivity(), mOrderListModel.publicUnfinishedOrderList);
                        mUnfinishedListView.setAdapter(mUndoListAdapter);
                        mUnfinishedListView.loadMoreHide();
                    }
                    mOrderListModel.fetchPreUnfinished();
                }
            }
        });

        mDoneOrderTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyView.setVisibility(View.GONE);
                mFinishedListView.setVisibility(View.VISIBLE);
                mUnfinishedListView.setVisibility(View.GONE);
                mCurrentState = DONE_ORDER;

                mUndoneOrderTextView.setTextColor(getResources().getColor(R.color.select_item));
                mUndoneOrderTextView.setBackgroundResource(R.drawable.e0_nav_left_normal);
                mDoneOrderTextView.setTextColor(Color.WHITE);
                mDoneOrderTextView.setBackgroundResource(R.drawable.e0_nav_right_selected);

                if (null == mDoneListAdapter) {
                    mOrderListModel.loadCacheRecivedfinished();
                    if (mOrderListModel.publicFinishedOrderList != null && mOrderListModel.publicFinishedOrderList.size() > 0) {
                        mDoneListAdapter = new E2_ReceivedOrderListAdapter(getActivity(), mOrderListModel.publicFinishedOrderList);
                        mFinishedListView.setAdapter(mDoneListAdapter);
                        mFinishedListView.loadMoreHide();
                    }
                    mOrderListModel.fetchPrefinished();
                }


            }
        });

        mEmptyView = (ImageView)mainView.findViewById(R.id.e0_empty_view);
        mEmptyView.setVisibility(View.GONE);
        mEmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == UNDONE_ORDER) {
                    mOrderListModel.fetchPreUnfinished();
                } else {
                    mOrderListModel.fetchPrefinished();
                }
            }
        });
        if (!EventBus.getDefault().isregister(this)) {
            EventBus.getDefault().register(this);
        }
        return mainView;
    }
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        if (EventBus.getDefault().isregister(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }
    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
    	mUnfinishedListView.stopRefresh();
    	mUnfinishedListView.stopLoadMore();
    	mFinishedListView.stopRefresh();
    	mFinishedListView.stopLoadMore();
        if(url.endsWith(ApiInterface.ORDERLIST_RECEIVED))
        {
            if (mCurrentState == UNDONE_ORDER)
            {
                if (null != jo)
                {
                    mUnfinishedListView.loadMoreShow();
                    mEmptyView.setVisibility(View.GONE);
                    mUnfinishedListView.setVisibility(View.VISIBLE);
                    if (null == mUndoListAdapter)
                    {
                        mUndoListAdapter = new E2_ReceivedOrderListAdapter(getActivity(), mOrderListModel.publicUnfinishedOrderList);
                        mUnfinishedListView.setAdapter(mUndoListAdapter);
                    }
                    else
                    {
                        mUndoListAdapter.notifyDataSetChanged();
                    }

                    orderlistreceivedResponse response = new orderlistreceivedResponse();
                    response.fromJson(jo);
                    if (0 == response.more)
                    {
                        mUnfinishedListView.stopLoadMore();
                        mUnfinishedListView.setPullLoadEnable(false);
                    }
                    else
                    {
                        mUnfinishedListView.stopLoadMore();
                        mUnfinishedListView.setPullLoadEnable(true);
                    }
                }
                else
                {
                    if (mOrderListModel.publicUnfinishedOrderList.size() == 0)
                    {
                        mEmptyView.setVisibility(View.VISIBLE);
                        mUnfinishedListView.setVisibility(View.GONE);
                    }
                }

            }
            else
            {

                if (null != jo)
                {
                    mFinishedListView.loadMoreShow();
                    mEmptyView.setVisibility(View.GONE);
                    mFinishedListView.setVisibility(View.VISIBLE);
                    if (null == mDoneListAdapter)
                    {
                        mDoneListAdapter = new E2_ReceivedOrderListAdapter(getActivity(), mOrderListModel.publicFinishedOrderList);
                        mFinishedListView.setAdapter(mDoneListAdapter);
                    }
                    else
                    {
                        mDoneListAdapter.notifyDataSetChanged();
                    }

                    orderlistreceivedResponse response = new orderlistreceivedResponse();
                    response.fromJson(jo);
                    if (0 == response.more)
                    {
                        mFinishedListView.stopLoadMore();
                        mFinishedListView.setPullLoadEnable(false);
                    }
                    else
                    {
                        mFinishedListView.stopLoadMore();
                        mFinishedListView.setPullLoadEnable(true);
                    }
                }
                else
                {
                    if (mOrderListModel.publicFinishedOrderList.size() == 0)
                    {
                        mEmptyView.setVisibility(View.VISIBLE);
                        mFinishedListView.setVisibility(View.GONE);
                    }
                }

            }


        }
    }

    @Override
    public void onRefresh(int id) {
        if (id == 1)
        {
            mOrderListModel.fetchPreUnfinished();
        }
        else
        {
            mOrderListModel.fetchPrefinished();
        }
    }

    @Override
    public void onLoadMore(int id) {
        if (id == 1)
        {
            mOrderListModel.fetchNextUnfinished();
        }
        else
        {
            mOrderListModel.fetchNextfinished();
        }
    }
    public void onEvent(Object event) {
        Message message = (Message) event;
        if (message.what == MessageConstant.SIGN_IN_SUCCESS) {
            if (mCurrentState == UNDONE_ORDER)
            {
                mOrderListModel.fetchPreUnfinished();
            }
            else
            {
                mOrderListModel.fetchPrefinished();
            }
        }
    }
}
