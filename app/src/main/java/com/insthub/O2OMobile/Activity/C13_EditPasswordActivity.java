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
import com.insthub.O2OMobile.APIErrorCode;
import com.insthub.O2OMobile.Model.UserModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.userchange_passwordResponse;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class C13_EditPasswordActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {
    private EditText mNowPassword;
    private EditText mNewPassword;
    private EditText mReNewPassword;
    private Button mSave;
    private UserModel mUserModel;
    private TextView mTitle;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c13_edit_password);
        mTitle = (TextView) findViewById(R.id.top_view_title);
        mTitle.setText(getString(R.string.change_login_password));
        mBack = (ImageView) findViewById(R.id.top_view_back);
        mNowPassword = (EditText) findViewById(R.id.now_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mReNewPassword = (EditText) findViewById(R.id.re_new_password);
        mSave = (Button) findViewById(R.id.save);
        mSave.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mUserModel = new UserModel(this);
        mUserModel.addResponseListener(this);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
            if(url.endsWith(ApiInterface.USER_CHANGE_PASSWORD)){
                userchange_passwordResponse response = new userchange_passwordResponse();
                response.fromJson(jo);
                if(response.succeed==1){
                    ToastView toast = new ToastView(C13_EditPasswordActivity.this, getString(R.string.change_password_success));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                }else {
                    if(response.error_code== APIErrorCode.ERROR_PASSWORD){
                        mNowPassword.requestFocus();
                    }
                }

            }
    }

    @Override
    public void onClick(View v) {
        String now_password = mNowPassword.getText().toString().trim();
        String new_password = mNewPassword.getText().toString().trim();
        String re_new_password = mReNewPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.save:
                if ("".equals(now_password)) {
                    ToastView toast = new ToastView(C13_EditPasswordActivity.this, getString(R.string.input_password));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mNowPassword.requestFocus();
                    mNowPassword.setText("");
                }
                else if (now_password.length() < 6 || now_password.length() > 20) {
                    ToastView toast = new ToastView(C13_EditPasswordActivity.this, getString(R.string.password_wrong_format_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mNowPassword.requestFocus();
                } else if ("".equals(new_password)) {
                    ToastView toast = new ToastView(C13_EditPasswordActivity.this, getString(R.string.input_new_password));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mNewPassword.requestFocus();
                    mNewPassword.setText("");
                }else if (new_password.length() < 6 || new_password.length() > 20) {
                    ToastView toast = new ToastView(C13_EditPasswordActivity.this,getString(R.string.new_password_wrong_format_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mNewPassword.requestFocus();
                }else if (!new_password.equals(re_new_password)) {
                    ToastView toast = new ToastView(C13_EditPasswordActivity.this, getString(R.string.two_passwords_differ_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mReNewPassword.requestFocus();
                }else{
                   mUserModel.changePassword(now_password, new_password);
                }
                break;


        }
    }
}
