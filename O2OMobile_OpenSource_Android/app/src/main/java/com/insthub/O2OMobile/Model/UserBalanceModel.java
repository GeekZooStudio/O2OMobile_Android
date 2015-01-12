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

package com.insthub.O2OMobile.Model;

import android.content.Context;

import com.BeeFramework.model.BaseModel;
import com.BeeFramework.model.BeeCallback;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.CONFIG;
import com.insthub.O2OMobile.Protocol.MY_SERVICE;
import com.insthub.O2OMobile.Protocol.USER;
import com.insthub.O2OMobile.Protocol.myservicelistRequest;
import com.insthub.O2OMobile.Protocol.myservicelistResponse;
import com.insthub.O2OMobile.Protocol.pushswitchRequest;
import com.insthub.O2OMobile.Protocol.pushswitchResponse;
import com.insthub.O2OMobile.Protocol.userbalanceRequest;
import com.insthub.O2OMobile.Protocol.userbalanceResponse;
import com.insthub.O2OMobile.Protocol.userchange_avatarRequest;
import com.insthub.O2OMobile.Protocol.userchange_avatarResponse;
import com.insthub.O2OMobile.Protocol.userprofileRequest;
import com.insthub.O2OMobile.Protocol.userprofileResponse;
import com.insthub.O2OMobile.Protocol.usersignoutRequest;
import com.insthub.O2OMobile.Protocol.usersignoutResponse;
import com.insthub.O2OMobile.Protocol.withdrawmoneyRequest;
import com.insthub.O2OMobile.Protocol.withdrawmoneyResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBalanceModel extends BaseModel {

    public String publicBalance;

    public UserBalanceModel(Context context) {
        super(context);
    }

    public List<MY_SERVICE> publicMyServiceList = new ArrayList<MY_SERVICE>();
    private int NUMPERPAGE = 20;
    public int publicMore;
    public USER publicUser;
    public pushswitchResponse publicPushSwitchResponse;

    public void get() {
        userbalanceRequest request = new userbalanceRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userbalanceResponse response = new userbalanceResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            publicBalance = response.balance;
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.USER_BALANCE).type(JSONObject.class).params(params);
        ajax(cb);
    }

    public void withdrawMoney(String amount) {
        withdrawmoneyRequest request = new withdrawmoneyRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.amount = amount;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        withdrawmoneyResponse response = new withdrawmoneyResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.WITHDRAW_MONEY).type(JSONObject.class).params(params);
        ajax(cb);
    }

    public void getServiceList(int user) {
        myservicelistRequest request = new myservicelistRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.user = user;
        request.by_no = 1;
        request.count = NUMPERPAGE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        myservicelistResponse response = new myservicelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if (response.succeed == 1) {
                            publicMyServiceList.clear();
                            publicMyServiceList.addAll(response.services);
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    } else {
                        UserBalanceModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.MYSERVICE_LIST).type(JSONObject.class).params(params);
        ajax(cb);
    }

    public void getProfile(final int user) {
        userprofileRequest request = new userprofileRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.user = user;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userprofileResponse response = new userprofileResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            if (response.user.id == SESSION.getInstance().uid) {
                                editor.putString("user", response.user.toJson().toString());
                                editor.commit();
                            }
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.USER_PROFILE).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void getServiceListMore(int user) {
        myservicelistRequest request = new myservicelistRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.user = user;
        request.by_no = (int) Math.ceil(publicMyServiceList.size() * 1.0 / NUMPERPAGE) + 1;
        ;
        request.count = NUMPERPAGE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        myservicelistResponse response = new myservicelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if (response.succeed == 1) {
                            publicMyServiceList.addAll(response.services);
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.MYSERVICE_LIST).type(JSONObject.class).params(params);
        ajax(cb);
    }

    public void changeAvatar(File avatar) {
        userchange_avatarRequest request = new userchange_avatarRequest();
        request.uid = SESSION.getInstance().uid;
        request.sid = SESSION.getInstance().sid;
        request.ver = O2OMobileAppConst.VERSION_CODE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userchange_avatarResponse response = new userchange_avatarResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            publicUser = response.user;
                            editor.putString("user", publicUser.toJson().toString());
                            editor.commit();
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
            params.put("avatar", avatar);
        } catch (JSONException e) {

        }
        cb.url(ApiInterface.USER_CHANGE_AVATAR).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void pushSwitch(int push_switch) {
        pushswitchRequest request = new pushswitchRequest();
        request.push_switch = push_switch;
        request.uid = SESSION.getInstance().uid;
        request.sid = SESSION.getInstance().sid;
        request.UUID = O2OMobile.getDeviceId(mContext);
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        publicPushSwitchResponse = new pushswitchResponse();
                        publicPushSwitchResponse.fromJson(jo);
                        if (publicPushSwitchResponse.succeed == 1) {
                            CONFIG config = publicPushSwitchResponse.config;
                            int push = config.push;
                            if (push == 1) {
                                editor.putBoolean("push", true);
                            } else {
                                editor.putBoolean("push", false);
                            }
                            editor.commit();
                            UserBalanceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserBalanceModel.this.callback(url, publicPushSwitchResponse.error_code, publicPushSwitchResponse.error_desc);
                        }
                    }

                } catch (JSONException e) {

                }
                ;
            }
        };
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }

        cb.url(ApiInterface.PUSH_SWITCH).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void signout() {

        usersignoutRequest request = new usersignoutRequest();
        request.uid = SESSION.getInstance().uid;
        request.sid = SESSION.getInstance().sid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserBalanceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        usersignoutResponse response = new usersignoutResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                        } else {
                            UserBalanceModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    UserBalanceModel.this.OnMessageResponse(url, jo, status);

                } catch (JSONException e) {

                }
            }
        };
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {

        }

        cb.url(ApiInterface.USER_SIGNOUT).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
}
