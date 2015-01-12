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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.APIErrorCode;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.UserModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.usersignupResponse;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class B2_SignupActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {

    private UserModel mUserModel;
    private EditText mNickname;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mSignupComplete;
    public static String MOBILE = "mobile";
    private String mMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.b2_signup);

        mMobile = getIntent().getStringExtra(MOBILE);
        mNickname = (EditText) findViewById(R.id.et_nickname);
        mPassword = (EditText) findViewById(R.id.et_password);
        mPasswordAgain = (EditText) findViewById(R.id.et_password_again);
        mSignupComplete = (Button) findViewById(R.id.btn_signup_complete);
        mSignupComplete.setOnClickListener(this);

        mUserModel = new UserModel(this);
        mUserModel.addResponseListener(this);
    }

    @Override
    public void onClick(View v) {
        String nickname = mNickname.getText().toString().trim();
        String password = mPassword.getText().toString();
        String password_again = mPasswordAgain.getText().toString();
        switch (v.getId()) {
            case R.id.btn_signup_complete:
                if ("".equals(nickname)) {
                    ToastView toast = new ToastView(B2_SignupActivity.this, getString(R.string.please_input_nickname));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mNickname.setText("");
                    mNickname.requestFocus();
                } else if (nickname.length() < 1 || nickname.length() > 16) {
                    ToastView toast = new ToastView(B2_SignupActivity.this, getString(R.string.nickname_wrong_format_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mNickname.requestFocus();
                } else if ("".equals(password)) {
                    ToastView toast = new ToastView(B2_SignupActivity.this, getString(R.string.please_input_password));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mPassword.requestFocus();
                } else if (password.length() < 6 || password.length() > 20) {
                    ToastView toast = new ToastView(B2_SignupActivity.this, getString(R.string.password_wrong_format_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mPassword.requestFocus();
                } else if (!password.equals(password_again)) {
                    ToastView toast = new ToastView(B2_SignupActivity.this, getString(R.string.two_passwords_differ_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mPasswordAgain.requestFocus();
                } else {
                    mUserModel.signup(mMobile, password, nickname);
                    CloseKeyBoard();
                }
                break;
        }

    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if (url.endsWith(ApiInterface.USER_SIGNUP)) {
            usersignupResponse response = new usersignupResponse();
            response.fromJson(jo);
           if (response.succeed == 1) {
               Intent intent = new Intent(this, SlidingActivity.class);
               startActivity(intent);
               finish();
               Message msg = new Message();
               msg.what = MessageConstant.SIGN_UP_SUCCESS;
               EventBus.getDefault().post(msg);
            }else {
               if (response.error_code == APIErrorCode.NICKNAME_EXIST) {
                   mNickname.requestFocus();
               }
           }
        }
    }

    // 关闭键盘
    private void CloseKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNickname.getWindowToken(), 0);
    }


}
