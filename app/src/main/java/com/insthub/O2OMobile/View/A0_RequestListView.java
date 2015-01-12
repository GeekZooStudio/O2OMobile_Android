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
import android.graphics.Color;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.AnimationUtil;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Adapter.A1_LbsListRequestAdapter;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.OrderListAroundModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_SEARCH_ORDER;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class A0_RequestListView extends LinearLayout implements BusinessResponse, IXListViewListener, OnClickListener {

	private Context mContext;
	private XListView mListView;
	private OrderListAroundModel mOrderModel;
	private A1_LbsListRequestAdapter mHomeDemandAdapter;
	
	private View mFilterBg;
	private LinearLayout mFilterView;
	private TextView mFilterLocation;
	private TextView mFilterTime;
	private TextView mFilterPriceDesc;
	private TextView mFilterPriceAsc;
	
	private ImageView mFilterLocationIcon;
	private ImageView mFilterTimeIcon;
	private ImageView mFilterPriceDescIcon;
	private ImageView mFilterPriceAscIcon;
	
	private int mSoryBy = 0;
	
	public A0_RequestListView(Context context) {
        super(context);
        mContext = context;
    }

    public A0_RequestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public A0_RequestListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
    
    @Override
    protected void onFinishInflate() {
    	super.onFinishInflate();
    	init();
    }
    
    public void init() {
    	mSoryBy = ENUM_SEARCH_ORDER.location_asc.value();
    	
    	mListView = (XListView) findViewById(R.id.a1_lbs_list_listview);
    	mListView.setXListViewListener(this, 0);
		mListView.setPullLoadEnable(true);
		mListView.setRefreshTime();
		
		mFilterBg = findViewById(R.id.a1_lbs_list_filter_bg);
		mFilterView = (LinearLayout) findViewById(R.id.a1_lbs_list_filter_view);
		
		mFilterLocation = (TextView) findViewById(R.id.a1_lbs_list_filter_location);
		mFilterTime = (TextView) findViewById(R.id.a1_lbs_list_filter_time);
		mFilterPriceDesc = (TextView) findViewById(R.id.a1_lbs_list_filter_price_desc);
		mFilterPriceAsc = (TextView) findViewById(R.id.a1_lbs_list_filter_price_asc);
		
		mFilterLocationIcon = (ImageView) findViewById(R.id.a1_lbs_list_filter_location_icon);
		mFilterTimeIcon = (ImageView) findViewById(R.id.a1_lbs_list_filter_time_icon);
		mFilterPriceDescIcon = (ImageView) findViewById(R.id.a1_lbs_list_filter_price_desc_icon);
		mFilterPriceAscIcon = (ImageView) findViewById(R.id.a1_lbs_list_filter_price_asc_icon);
		changeTextColor(mFilterLocation);
		
		mFilterBg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideView();
            }
        });
		
		mFilterLocation.setOnClickListener(this);
		mFilterTime.setOnClickListener(this);
		mFilterPriceDesc.setOnClickListener(this);
		mFilterPriceAsc.setOnClickListener(this);
		
    	mOrderModel = new OrderListAroundModel(mContext);
    	mOrderModel.addResponseListener(this);
    	mOrderModel.getList(mSoryBy);
    	
    	mListView.setAdapter(null);
    }
    
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		mOrderModel.getList(mSoryBy);
		mListView.setRefreshTime();
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		mOrderModel.getListMore(mSoryBy);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		mListView.stopRefresh();
		mListView.stopLoadMore();
		if(url.endsWith(ApiInterface.ORDERLIST_AROUND)) {
			setAdapterCont();
			if(mOrderModel.publicMore == 0) {
				mListView.setPullLoadEnable(false);
			} else {
				mListView.setPullLoadEnable(true);
			}
		}
	}
	
	private void setAdapterCont() {
		if(mHomeDemandAdapter == null) {
			mHomeDemandAdapter = new A1_LbsListRequestAdapter(mContext, mOrderModel.publicOrderList);
			mListView.setAdapter(mHomeDemandAdapter);
		} else {
			mHomeDemandAdapter.publicList = mOrderModel.publicOrderList;
			mHomeDemandAdapter.notifyDataSetChanged();
		}
	}
	
	public void showView() {
		if(mFilterView.getVisibility() == View.GONE) {
			mFilterBg.setVisibility(View.VISIBLE);
			mFilterView.setVisibility(View.VISIBLE);
			AnimationUtil.showAnimationFromTop(mFilterView);
			
			Message msg = new Message();
            msg.what = MessageConstant.SHOW_SEARCH_VIEW;
            EventBus.getDefault().post(msg);
		} else {
			hideView();
		}
	}
	
	public void hideView() {
		mFilterBg.setVisibility(View.GONE);
		mFilterView.setVisibility(View.GONE);
		AnimationUtil.backAnimationFromBottom(mFilterView);
		
		Message msg = new Message();
        msg.what = MessageConstant.HIDE_SEARCH_VIEW;
        EventBus.getDefault().post(msg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.a1_lbs_list_filter_price_desc:
			hideView();
			if(mSoryBy == ENUM_SEARCH_ORDER.price_desc.value()) {
				return;
			} else {
				changeTextColor(mFilterPriceDesc);
				mSoryBy = ENUM_SEARCH_ORDER.price_desc.value();
			}
			break;
		case R.id.a1_lbs_list_filter_price_asc:
			hideView();
			if(mSoryBy == ENUM_SEARCH_ORDER.price_asc.value()) {
				return;
			} else {
				changeTextColor(mFilterPriceAsc);
				mSoryBy = ENUM_SEARCH_ORDER.price_asc.value();
			}
			break;
		case R.id.a1_lbs_list_filter_time:
			hideView();
			if(mSoryBy == ENUM_SEARCH_ORDER.time_desc.value()) {
				return;
			} else {
				changeTextColor(mFilterTime);
				mSoryBy = ENUM_SEARCH_ORDER.time_desc.value();
			}
			break;
		case R.id.a1_lbs_list_filter_location:
			hideView();
			if(mSoryBy == ENUM_SEARCH_ORDER.location_asc.value()) {
				return;
			} else {
				changeTextColor(mFilterLocation);
				mSoryBy = ENUM_SEARCH_ORDER.location_asc.value();
			}
			break;
		}
		mHomeDemandAdapter = null;
		mOrderModel.getList(mSoryBy);
	}
	
	private void changeTextColor(View view) {
		if(view == mFilterPriceDesc) {
			mFilterPriceDesc.setTextColor(Color.parseColor("#39BCED"));
			mFilterPriceAsc.setTextColor(Color.parseColor("#333333"));
			mFilterTime.setTextColor(Color.parseColor("#333333"));
			mFilterLocation.setTextColor(Color.parseColor("#333333"));
			mFilterPriceDescIcon.setVisibility(View.VISIBLE);
			mFilterPriceAscIcon.setVisibility(View.GONE);
			mFilterTimeIcon.setVisibility(View.GONE);
			mFilterLocationIcon.setVisibility(View.GONE);
		} else if(view == mFilterPriceAsc) {
			mFilterPriceDesc.setTextColor(Color.parseColor("#333333"));
			mFilterPriceAsc.setTextColor(Color.parseColor("#39BCED"));
			mFilterTime.setTextColor(Color.parseColor("#333333"));
			mFilterLocation.setTextColor(Color.parseColor("#333333"));
			mFilterPriceDescIcon.setVisibility(View.GONE);
			mFilterPriceAscIcon.setVisibility(View.VISIBLE);
			mFilterTimeIcon.setVisibility(View.GONE);
			mFilterLocationIcon.setVisibility(View.GONE);
		} else if(view == mFilterTime) {
			mFilterPriceDesc.setTextColor(Color.parseColor("#333333"));
			mFilterPriceAsc.setTextColor(Color.parseColor("#333333"));
			mFilterTime.setTextColor(Color.parseColor("#39BCED"));
			mFilterLocation.setTextColor(Color.parseColor("#333333"));
			mFilterPriceDescIcon.setVisibility(View.GONE);
			mFilterPriceAscIcon.setVisibility(View.GONE);
			mFilterTimeIcon.setVisibility(View.VISIBLE);
			mFilterLocationIcon.setVisibility(View.GONE);
		} else if(view == mFilterLocation) {
			mFilterPriceDesc.setTextColor(Color.parseColor("#333333"));
			mFilterPriceAsc.setTextColor(Color.parseColor("#333333"));
			mFilterTime.setTextColor(Color.parseColor("#333333"));
			mFilterLocation.setTextColor(Color.parseColor("#39BCED"));
			mFilterPriceDescIcon.setVisibility(View.GONE);
			mFilterPriceAscIcon.setVisibility(View.GONE);
			mFilterTimeIcon.setVisibility(View.GONE);
			mFilterLocationIcon.setVisibility(View.VISIBLE);
		}
	}

}
