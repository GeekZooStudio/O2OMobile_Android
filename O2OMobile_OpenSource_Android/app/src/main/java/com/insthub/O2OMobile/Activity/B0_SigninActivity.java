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
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.UserModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.usersigninResponse;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class B0_SigninActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {
    private EditText mMobile;
    private EditText mPassword;
    private TextView mSignup;
    private Button mLogin;
    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.b0_signin);
        mMobile = (EditText) findViewById(R.id.et_mobile);
        mPassword = (EditText) findViewById(R.id.et_password);
        mSignup = (TextView) findViewById(R.id.tv_signup);
        mSignup.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mMobile = (EditText) findViewById(R.id.et_mobile);
        mLogin = (Button) findViewById(R.id.btn_login);

        mSignup.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onClick(View v) {
        String mobile = mMobile.getText().toString();
        String password = mPassword.getText().toString();

        switch (v.getId()) {
            case R.id.tv_signup:
                Intent intent = new Intent(this, B1_SignupVerifyActivity.class);
                startActivity(intent);
                CloseKeyBoard();
                break;
            case R.id.btn_login:
                if ("".equals(mobile)) {
                    ToastView toast = new ToastView(this, getString(R.string.please_input_mobile_phone));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mMobile.requestFocus();
                } else if ("".equals(password)) {
                    ToastView toast = new ToastView(this, getString(R.string.please_input_password));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mPassword.requestFocus();
                } else if (mobile.length() < 11) {
                    ToastView toast = new ToastView(this, getString(R.string.wrong_mobile_phone));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mMobile.requestFocus();
                } else if (password.length() < 6 || password.length() > 20) {
                    ToastView toast = new ToastView(this, getString(R.string.please_enter_correct_password_format));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mPassword.requestFocus();
                } else {
                    CloseKeyBoard();
                    mUserModel = new UserModel(B0_SigninActivity.this);
                    mUserModel.addResponseListener(this);
                    mUserModel.login(mobile, password);
                }
                break;
        }
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        if (url.endsWith(ApiInterface.USER_SIGNIN)) {
            usersigninResponse usersigninResponse = new usersigninResponse();
            usersigninResponse.fromJson(jo);
            if (usersigninResponse.succeed == 1) {
                Message msg = new Message();
                msg.what = MessageConstant.SIGN_IN_SUCCESS;
                EventBus.getDefault().post(msg);
                Intent intent = new Intent(B0_SigninActivity.this, SlidingActivity.class);
                startActivity(intent);
                finish();
            }else{
                ToastView toast = new ToastView(this, getString(R.string.mobile_phone_or_password_error));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                mPassword.requestFocus();
            }

        }

    }

    // 关闭键盘
    private void CloseKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMobile.getWindowToken(), 0);
    }


	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit=false;
		}
	};

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Object event) {
        Message message = (Message) event;
        if (message.what == MessageConstant.SIGN_UP_SUCCESS) {
            finish();
        }
    }
    private boolean isExit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isExit==false){
                isExit=true;
                ToastView toast = new ToastView(getApplicationContext(), getString(R.string.exit_again));
				toast.setGravity(Gravity.CENTER, 0, 0);
	            toast.show();
                handler.sendEmptyMessageDelayed(0, 3000);
                return true;
            } else {
            	if(SESSION.getInstance().uid == 0) {
            		finish();
            	} else {
            		Intent intent = new Intent(this, SlidingActivity.class);
                	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	startActivity(intent);
                    finish();
            	}
                return false;
            }
        }
        return true;
    }
}
