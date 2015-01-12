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

package com.insthub.O2OMobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.O2OMobile.Adapter.D11_OrderHistoryAdapter;
import com.insthub.O2OMobile.Model.OrderHistoryModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class D2_OrderHistoryActivity extends BaseActivity implements BusinessResponse {

	private ImageView mBack;
	private TextView mTitle;
	private View mTimeLine;
	private ListView mListView;
	private int mOrderId;
	
	private OrderHistoryModel mOrderHistoryModel;
	private D11_OrderHistoryAdapter mOrderHistoryAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d2_order_history);
		mBack = (ImageView) findViewById(R.id.top_view_back);
		mTitle = (TextView) findViewById(R.id.top_view_title);
		mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
		mTitle.setText("订单状态");
		
		Intent intent = getIntent();
		mOrderId = intent.getIntExtra("order_id", 0);
		mTimeLine = findViewById(R.id.d11_order_history_timeline);
		mListView = (ListView) findViewById(R.id.d11_order_history_list);
		
		mOrderHistoryModel = new OrderHistoryModel(this);
		mOrderHistoryModel.addResponseListener(this);
		mOrderHistoryModel.get(mOrderId);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		if(url.endsWith(ApiInterface.ORDER_HISTORY)) {
			mTimeLine.setVisibility(View.VISIBLE);
			mOrderHistoryAdapter = new D11_OrderHistoryAdapter(this, mOrderHistoryModel.publicRecodeList);
			mListView.setAdapter(mOrderHistoryAdapter);
		}
	}
	
}
