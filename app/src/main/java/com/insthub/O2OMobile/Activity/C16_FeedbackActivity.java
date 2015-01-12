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

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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


public class C16_FeedbackActivity extends BaseActivity implements BusinessResponse {
    private ImageView mBack;
    private TextView mTitle;
    private EditText mFeedBack;
    private Button mFeedbackButton;
    private ReportModel mReportModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c16_feedback);
        mBack = (ImageView) 	findViewById(R.id.top_view_back);
        mTitle = (TextView) 	findViewById(R.id.top_view_title);
        mFeedBack = (EditText) 	findViewById(R.id.feedback_edittext);
        mFeedbackButton = (Button) 		findViewById(R.id.feedback_button);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        mTitle.setText(getString(R.string.feedback));

        mReportModel = new ReportModel(this);
        mReportModel.addResponseListener(this);

        mFeedbackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str = mFeedBack.getText().toString();
                if (str.equals("")) {
                    ToastView toast = new ToastView(C16_FeedbackActivity.this, getString(R.string.please_input_feedback));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    mReportModel.feedback(str);
                }
            }
        });
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if(url.endsWith(ApiInterface.FEEDBACK)) {
            ToastView toast = new ToastView(this, getString(R.string.feedback_success));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            finish();
        }
    }

}
