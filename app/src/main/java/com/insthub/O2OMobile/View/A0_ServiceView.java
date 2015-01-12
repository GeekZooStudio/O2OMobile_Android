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

package com.insthub.O2OMobile.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Adapter.A0_ServiceAdapter;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Model.HomeModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.Protocol.servicetypelistResponse;
import com.insthub.O2OMobile.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class A0_ServiceView extends FrameLayout implements BusinessResponse, IXListViewListener {

    private Context mContext;
    private XListView mXlistview_home_need_help;
    private HomeModel mHomeModel;
    private A0_ServiceAdapter mHomeNeedHelpAdapter;
    private ImageView mE0EmptyView;
    private TextView mEmptyView;
    private SharedPreferences mShared;
    private ArrayList<SERVICE_TYPE> mServiceTypes;
    private String mHomeData;

    public A0_ServiceView(Context context) {
        super(context);
        mContext = context;
    }

    public A0_ServiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public A0_ServiceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
        if (null == mXlistview_home_need_help) {
            mXlistview_home_need_help = (XListView) findViewById(R.id.xlistview_home_need_help);
        }
        if (null == mE0EmptyView) {
            mE0EmptyView = (ImageView) findViewById(R.id.e0_empty_view);
        }
        if (null == mEmptyView) {
            mEmptyView = (TextView) findViewById(R.id.empty_view);
        }

        mXlistview_home_need_help.setXListViewListener(this, 0);
        mXlistview_home_need_help.setPullLoadEnable(true);
        mXlistview_home_need_help.setRefreshTime();
        mHomeModel = new HomeModel(mContext);
        mHomeModel.addResponseListener(this);
        mE0EmptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeModel.getServiceTypeList();
            }
        });
        mShared = mContext.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        mHomeData = mShared.getString("home_data", "");
        if ("".equals(mHomeData)) {
            mHomeModel.getServiceTypeList();
        } else {
            try {
                servicetypelistResponse response = new servicetypelistResponse();
                response.fromJson(new JSONObject(mHomeData));
                mServiceTypes = response.services;
                sortList();
                mHomeNeedHelpAdapter = new A0_ServiceAdapter(mContext, mServiceTypes);
                mXlistview_home_need_help.setAdapter(mHomeNeedHelpAdapter);
                mXlistview_home_need_help.loadMoreHide();
                mHomeModel.getServiceTypeListFresh(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        mXlistview_home_need_help.stopRefresh();
        mXlistview_home_need_help.stopLoadMore();
        if (url.endsWith(ApiInterface.SERVICETYPE_LIST)) {
            if (jo == null) {
                if("".equals(mShared.getString("home_data", ""))){
                    mE0EmptyView.setVisibility(VISIBLE);
                }
            } else {
                    mE0EmptyView.setVisibility(GONE);
                    setListAdapter();
                    if (mHomeModel.publicMore == 0) {
                        mXlistview_home_need_help.setPullLoadEnable(false);
                        mXlistview_home_need_help.loadMoreHide();
                    } else {
                        mXlistview_home_need_help.setPullLoadEnable(true);
                    }
                    if (mHomeModel.publicServiceTypeLisSort.size() == 0) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        mXlistview_home_need_help.setVisibility(View.GONE);
                    } else {
                        mEmptyView.setVisibility(View.GONE);
                        mXlistview_home_need_help.setVisibility(View.VISIBLE);
                    }
            }
        }
    }


    public void setListAdapter() {
        if (mHomeNeedHelpAdapter == null) {
            mHomeNeedHelpAdapter = new A0_ServiceAdapter(mContext, mHomeModel.publicServiceTypeLisSort);
            mXlistview_home_need_help.setAdapter(mHomeNeedHelpAdapter);
        } else {
            mHomeNeedHelpAdapter.publicList = mHomeModel.publicServiceTypeLisSort;
            mHomeNeedHelpAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh(int id) {
        mHomeModel.getServiceTypeListFresh(true);
    }

    @Override
    public void onLoadMore(int id) {
        mHomeModel.getServiceTypeListMore();
    }

    private void sortList() {
        int a = mServiceTypes.size() % 2;
        SERVICE_TYPE empty = new SERVICE_TYPE();
        if (a == 1) {
            mServiceTypes.add(empty);
        }
    }
}
