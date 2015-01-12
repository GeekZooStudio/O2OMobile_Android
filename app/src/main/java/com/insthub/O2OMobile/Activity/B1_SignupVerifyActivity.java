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
import android.os.CountDownTimer;
import android.os.Message;
import android.view.Gravity;
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
import com.insthub.O2OMobile.Protocol.userverifycodeResponse;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class B1_SignupVerifyActivity extends BaseActivity implements BusinessResponse ,View.OnClickListener{
    private EditText mMobile;
    private EditText mVerifyCode;
    private Button mGetVerifyCodeAgain;
    private Button mNext;
    private TextView mLogin;
    private UserModel mUserModel;
    private TimeCount mTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.b1_signup_verify);

        mMobile =(EditText)findViewById(R.id.et_mobile);
        mVerifyCode =(EditText)findViewById(R.id.et_verify_code);
        mNext =(Button)findViewById(R.id.btn_next);
        mLogin =(TextView)findViewById(R.id.tv_login);
        mLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mGetVerifyCodeAgain =(Button)findViewById(R.id.btn_get_verify_code_again);

        mNext.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mGetVerifyCodeAgain.setOnClickListener(this);
        mUserModel =new UserModel(this);
        mUserModel.addResponseListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        String mobile= mMobile.getText().toString();
        String verify_code= mVerifyCode.getText().toString().trim();
        switch(v.getId()) {
            case R.id.tv_login:
                CloseKeyBoard();
                finish();
                break;
            case R.id.btn_next:
                if("".equals(mobile)) {
                    ToastView toast = new ToastView(this,getString(R.string.please_input_mobile_phone));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mMobile.requestFocus();
                }else if(mobile.length()<11){
                    ToastView toast = new ToastView(this, getString(R.string.wrong_mobile_phone));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mMobile.requestFocus();
                }else if("".equals(verify_code)){
                    ToastView toast = new ToastView(this,getString(R.string.please_input_verify_code));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mVerifyCode.setText("");
                    mVerifyCode.requestFocus();
                } else if(verify_code.length()<4||!"1234".equals(verify_code)){
                    ToastView toast = new ToastView(this,"输入验证码1234完成注册第一步");
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mVerifyCode.requestFocus();
                } else if("1234".equals(verify_code)){
                     Intent intent=new Intent(this,B2_SignupActivity.class);
                     intent.putExtra(B2_SignupActivity.MOBILE, mMobile.getText().toString());
                     startActivity(intent);
                }
                break;
            case  R.id.btn_get_verify_code_again:
                if("".equals(mobile)) {
                    ToastView toast = new ToastView(this,getString(R.string.please_input_mobile_phone));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mMobile.requestFocus();
                }else if(mobile.length()<11){
                    ToastView toast = new ToastView(this, getString(R.string.wrong_mobile_phone));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mMobile.requestFocus();
                }else{
                    ToastView toast = new ToastView(this,"输入验证码1234完成注册第一步");
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mVerifyCode.requestFocus();
                }

                break;
        }

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if(url.endsWith(ApiInterface.USER_VERIFYCODE)) {
            userverifycodeResponse response = new userverifycodeResponse();
            response.fromJson(jo);
            if(response.succeed==1){
                mTime = new TimeCount(60000, 1000);//构造CountDownTimer对象
                mTime.start();
            }else{
                mMobile.requestFocus();
            }
        }else if(url.endsWith(ApiInterface.USER_VALIDCODE)){
//            Intent intent=new Intent(this,B2_Logon_stepTwoActivity.class);
//            intent.putExtra(B2_Logon_stepTwoActivity.MOBILE, mMobile.getText().toString());
//            startActivity(intent);
        }
    }
    // 关闭键盘
    private void CloseKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMobile.getWindowToken(), 0);
    }
    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            mGetVerifyCodeAgain.setText(getString(R.string.get_verify_code_again));
            mGetVerifyCodeAgain.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            mGetVerifyCodeAgain.setClickable(false);
            mGetVerifyCodeAgain.setText(millisUntilFinished / 1000 + getString(R.string.resend_after));
        }
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    public void onEvent(Object event)
    {
        Message message = (Message)event;
        if(message.what == MessageConstant.SIGN_UP_SUCCESS)
        {
            finish();
        }
    }
}
