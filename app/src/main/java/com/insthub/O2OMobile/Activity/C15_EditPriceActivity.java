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
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.ServiceModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.MY_SERVICE;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class C15_EditPriceActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {
    public final static String SERVICE = "my_service";
    private TextView nTitle;
    private ImageView mBack;
    private TextView mType;
    private EditText mPrice;
    private Button mSave;
    private ServiceModel mServiceModel;
    private MY_SERVICE mMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c15_edit_price);
        mMyService =(MY_SERVICE)getIntent().getSerializableExtra(SERVICE);
        nTitle = (TextView) findViewById(R.id.top_view_title);
        nTitle.setText(getString(R.string.modify_service));
        mBack = (ImageView) findViewById(R.id.top_view_back);
        mType = (TextView) findViewById(R.id.service_type);
        mPrice = (EditText) findViewById(R.id.price);
        mSave = (Button) findViewById(R.id.save);
        if(mMyService !=null){
            mType.setText(mMyService.service_type.title);
            mPrice.setText(mMyService.price);
            mPrice.setSelection(mMyService.price.length());
        }
        mServiceModel =new ServiceModel(this);
        mServiceModel.addResponseListener(this);
        mSave.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() > 0) {
                    if (s.toString().substring(0, 1).equals(".")) {
                        s = s.toString().substring(1, s.length());
                        mPrice.setText(s);
                    }
                }
                if (s.toString().length() > 1) {
                    if (s.toString().substring(0, 1).equals("0")) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            s = s.toString().substring(1, s.length());
                            mPrice.setText(s);
                            CharSequence charSequencePirce = mPrice.getText();
                            if (charSequencePirce instanceof Spannable) {
                                Spannable spanText = (Spannable) charSequencePirce;
                                Selection.setSelection(spanText, charSequencePirce.length());
                            }
                        }
                    }
                }
                boolean flag = false;
                for (int i = 0; i < s.toString().length() - 1; i++) {
                    String getstr = s.toString().substring(i, i + 1);
                    if (getstr.equals(".")) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    int i = s.toString().indexOf(".");
                    if (s.toString().length() - 3 > i) {
                        String getstr = s.toString().substring(0, i + 3);
                        mPrice.setText(getstr);
                        CharSequence charSequencePirce = mPrice.getText();
                        if (charSequencePirce instanceof Spannable) {
                            Spannable spanText = (Spannable) charSequencePirce;
                            Selection.setSelection(spanText, charSequencePirce.length());
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
            if(url.endsWith(ApiInterface.MYSERVICE_MODIFY)){
                ToastView toast = new ToastView(this,getString(R.string.price_edit_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                CloseKeyBoard();
                Message msg = new Message();
                msg.what = MessageConstant.Change_Seivice;
                EventBus.getDefault().post(msg);
                Intent intent = new Intent();
                intent.putExtra("ok", "ok");
                setResult(RESULT_OK, intent);
                finish();
            }
    }

    @Override
    public void onClick(View v) {
        String price = mPrice.getText().toString().trim();
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.save:
                if(price.length()==0&"".equals(price)){
                    ToastView toast = new ToastView(this,getString(R.string.privce_not_null));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mPrice.requestFocus();
                }else{
                    mServiceModel.modify(mMyService.id, price);
                }
                break;
        }
    }
    // 关闭键盘
    private void CloseKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPrice.getWindowToken(), 0);
    }
}
