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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.O2OMobile.Model.ReportModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class G0_ReportActivity extends BaseActivity implements BusinessResponse {

	private ImageView   mBack;
	private TextView    mTitle;
	private EditText    mComplainEdittext;
	private Button      mComplainButton;
	private ReportModel mReportModel;
	private int         mOrderId;
	private int mUserId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.g0_report);
		mBack = (ImageView) 	findViewById(R.id.top_view_back);
		mTitle = (TextView) 	findViewById(R.id.top_view_title);
		mComplainEdittext = (EditText) 	findViewById(R.id.d13_complain_edittext);
		mComplainButton = (Button) 		findViewById(R.id.d13_complain_button);
		
		Intent intent = getIntent();
		mOrderId = intent.getIntExtra("orderId", 0);
		mUserId = intent.getIntExtra("userId", 0);
		
		mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
		mTitle.setText(getString(R.string.complain));
		
		mReportModel = new ReportModel(this);
		mReportModel.addResponseListener(this);
		
		mComplainButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str = mComplainEdittext.getText().toString();
                if (str.equals("")) {
                    ToastView toast = new ToastView(G0_ReportActivity.this, getString(R.string.input_complain_content));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    mReportModel.report(mOrderId, mUserId, str);
                }
            }
        });
		
		
	}
	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		if(url.endsWith(ApiInterface.REPORT)) {
			ToastView toast = new ToastView(this, getString(R.string.complain_success));
			toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
			finish();
		}
	}
	
}
