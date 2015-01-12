package com.BeeFramework.model;

/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;

import com.BeeFramework.view.MyProgressDialog;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.AQuery;
import com.external.androidquery.callback.AjaxCallback;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.O2OMobile.APIErrorCode;
import com.insthub.O2OMobile.Activity.B0_SigninActivity;
import com.insthub.O2OMobile.O2OMobileAppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseModel implements BusinessResponse {

    protected BeeQuery aq;
    protected ArrayList<BusinessResponse> businessResponseArrayList = new ArrayList<BusinessResponse>();
    public ArrayList<BeeCallback> sendingmessageList = new ArrayList<BeeCallback>();
    protected Context mContext;

    public SharedPreferences shared;
    public SharedPreferences.Editor editor;

    public BaseModel() {

    }

    public BaseModel(Context context) {
        aq = new BeeQuery(context);
        mContext = context;
        shared = context.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        editor = shared.edit();
    }

    protected void saveCache() {
        return;
    }

    protected void cleanCache() {
        return;
    }

    public void addResponseListener(BusinessResponse listener) {
        if (!businessResponseArrayList.contains(listener)) {
            businessResponseArrayList.add(listener);
        } else {

        }
    }

    public void removeResponseListener(BusinessResponse listener) {
        businessResponseArrayList.remove(listener);
    }

    //公共的错误处理
    public void callback(BeeCallback callback, String url, JSONObject jo, AjaxStatus status) {
        DebugMessageModel.finishSendingMessage(callback);
        finishMessage(callback);
        if (null == jo) {
            ToastView toast = new ToastView(mContext, "网络错误，请检查网络设置");
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

    }

    //公共的错误处理
    public boolean callback(String url, int errorCode, String errorDesc) {
        if (errorCode == APIErrorCode.SESSION_EXPIRE) {
            ToastView toast = new ToastView(mContext, errorDesc);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            editor.putBoolean("isLogin", false);
            editor.commit();
            Intent intent = new Intent(mContext, B0_SigninActivity.class);
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
            return true;
        } else if (null != errorDesc) {
            ToastView toast = new ToastView(mContext, errorDesc);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return true;
        } else {
            return true;
        }


    }

    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        for (BusinessResponse iterable_element : businessResponseArrayList) {
            iterable_element.OnMessageResponse(url, jo, status);
        }

        if (businessResponseArrayList.size() == 0) {

        }
    }

    public void addMessage(BeeCallback msg) {
        sendingmessageList.add(msg);
    }


    public boolean isSendingMessage(String url) {
        for (int i = 0; i < sendingmessageList.size(); i++) {
            AjaxCallback msg = sendingmessageList.get(i);
            if (msg.getUrl().endsWith(url)) {
                return true;
            }
        }

        return false;
    }

    public void finishMessage(BeeCallback msg) {
        if (sendingmessageList.contains(msg)) {
            sendingmessageList.remove(msg);
        }

    }

    public <K> AQuery ajax(AjaxCallback<K> callback) {
        addMessage((BeeCallback) callback);
        return aq.ajax(callback);
    }

    public <K> AQuery ajaxProgress(AjaxCallback<K> callback) {
        addMessage((BeeCallback) callback);
        MyProgressDialog pro = new MyProgressDialog(mContext, "请稍候...");
        return (AQuery) aq.progress(pro.mDialog).ajax(callback);

    }

}
