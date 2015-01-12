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
import com.insthub.O2OMobile.APIErrorCode;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.CONFIG;
import com.insthub.O2OMobile.Protocol.ENUM_USER_GENDER;
import com.insthub.O2OMobile.Protocol.LOCATION;
import com.insthub.O2OMobile.Protocol.USER;
import com.insthub.O2OMobile.Protocol.usercertifyRequest;
import com.insthub.O2OMobile.Protocol.usercertifyResponse;
import com.insthub.O2OMobile.Protocol.userchange_passwordRequest;
import com.insthub.O2OMobile.Protocol.userchange_passwordResponse;
import com.insthub.O2OMobile.Protocol.userchange_profileRequest;
import com.insthub.O2OMobile.Protocol.userchange_profileResponse;
import com.insthub.O2OMobile.Protocol.userinvite_codeRequest;
import com.insthub.O2OMobile.Protocol.userinvite_codeResponse;
import com.insthub.O2OMobile.Protocol.usersigninRequest;
import com.insthub.O2OMobile.Protocol.usersigninResponse;
import com.insthub.O2OMobile.Protocol.usersignupRequest;
import com.insthub.O2OMobile.Protocol.usersignupResponse;
import com.insthub.O2OMobile.Protocol.uservalidcodeRequest;
import com.insthub.O2OMobile.Protocol.uservalidcodeResponse;
import com.insthub.O2OMobile.Protocol.userverifycodeRequest;
import com.insthub.O2OMobile.Protocol.userverifycodeResponse;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.Utils.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserModel extends BaseModel {
    private Context context;
    public String invite_code;
    public UserModel(Context context) {
        super(context);
        this.context = context;

    }

    public void login(String mobile, String password) {

        usersigninRequest request = new usersigninRequest();
        request.mobile_phone =mobile;
        request.password = password;
        request.UUID = O2OMobile.getDeviceId(context);
        request.platform = "android";
        request.ver = O2OMobileAppConst.VERSION_CODE;
        LOCATION location = new LOCATION();
        location.lat = LocationManager.getLatitude();
        location.lon = LocationManager.getLongitude();
        request.location=location;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);

                    if (null != jo) {
                        usersigninResponse usersigninResponse = new usersigninResponse();
                        usersigninResponse.fromJson(jo);
                        if (usersigninResponse.succeed == 1) {
                            USER user = usersigninResponse.user;
                            CONFIG config = usersigninResponse.config;
                            int push = config.push;
                            if (push == 1) {
                                editor.putBoolean("push", true);
                            } else {
                                editor.putBoolean("push", false);
                            }
                            editor.putBoolean("isLogin", true);
                            editor.putString("user", user.toJson().toString());
                            editor.putInt("uid", usersigninResponse.user.id);
                            editor.putString("sid", usersigninResponse.sid);
                            editor.putString("nickename",user.nickname);
                            editor.commit();
                            SESSION.getInstance().uid = usersigninResponse.user.id;
                            SESSION.getInstance().sid = usersigninResponse.sid;

                            PushModel pushModel = new PushModel(context);
                            pushModel.update();
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            if (usersigninResponse.error_code == APIErrorCode.MOBILE_NOT_EXIST||usersigninResponse.error_code==APIErrorCode.ERROR_PASSWORD) {
                                UserModel.this.OnMessageResponse(url, jo, status);
                            }else{
                                UserModel.this.callback(url, usersigninResponse.error_code, usersigninResponse.error_desc);
                            }

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

        cb.url(ApiInterface.USER_SIGNIN).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void getVerifyCode(String mobile) {
        userverifycodeRequest request = new userverifycodeRequest();
        request.mobile_phone = mobile;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);

                    if (null != jo) {
                        userverifycodeResponse response = new userverifycodeResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
                            if(response.error_code==APIErrorCode.MOBILE_EXIST){
                                UserModel.this.OnMessageResponse(url, jo, status);
                            }
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

        cb.url(ApiInterface.USER_VERIFYCODE).type(JSONObject.class).params(params);
        ajaxProgress(cb);

    }

    public void valid_verifycode(String mobile, String verifycode) {
        uservalidcodeRequest request = new uservalidcodeRequest();
        request.mobile_phone = mobile;
        request.verify_code = verifycode;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        uservalidcodeResponse response = new uservalidcodeResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_VALIDCODE).type(JSONObject.class).params(params);
        ajaxProgress(cb);

    }

    public void signup(String mobile, String password, String nickname) {
        usersignupRequest request = new usersignupRequest();
        request.mobile_phone =mobile;
        request.nickname = nickname;
        request.invite_code = "";
        request.password = password;
        request.platform = "android";
        request.ver = O2OMobileAppConst.VERSION_CODE;
        LOCATION location = new LOCATION();
        location.lat = LocationManager.getLatitude();
        location.lon = LocationManager.getLongitude();
        request.location=location;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);

                    if (null != jo) {
                        usersignupResponse response = new usersignupResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            USER user = response.user;
                            CONFIG config = response.config;
                            int push = config.push;
                            if (push == 1) {
                                editor.putBoolean("push", true);
                            } else {
                                editor.putBoolean("push", false);
                            }
                            editor.putBoolean("isLogin", true);
                            editor.putString("user", user.toJson().toString());
                            editor.putInt("uid", response.user.id);
                            editor.putString("sid", response.sid);
                            editor.commit();
                            SESSION.getInstance().uid = response.user.id;
                            SESSION.getInstance().sid = response.sid;
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            if(response.error_code==APIErrorCode.NICKNAME_EXIST){

                                UserModel.this.OnMessageResponse(url, jo, status);
                            }
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_SIGNUP).type(JSONObject.class).params(params);
        //aq.ajax(cb);
        ajaxProgress(cb);
    }

    public void changeNickname(String nickname) {
        userchange_profileRequest request = new userchange_profileRequest();
        request.nickname = nickname;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userchange_profileResponse response = new userchange_profileResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            USER user = response.user;
                            editor.putString("user", user.toJson().toString());
                            editor.commit();
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_CHANGE_PROFILE).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void changeSignature(String signture) {
        userchange_profileRequest request = new userchange_profileRequest();
        request.signature = signture;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userchange_profileResponse response = new userchange_profileResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            USER user = response.user;
                            ;
                            editor.putString("user", user.toJson().toString());
                            editor.commit();
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_CHANGE_PROFILE).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void changeBrief(String brief) {
        userchange_profileRequest request = new userchange_profileRequest();
        request.brief = brief;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userchange_profileResponse response = new userchange_profileResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            USER user = response.user;
                            ;
                            editor.putString("user", user.toJson().toString());
                            editor.commit();
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_CHANGE_PROFILE).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
    public void changePassword(String oldPassword,String newPassword) {
        userchange_passwordRequest request = new userchange_passwordRequest();
        request.old_password = oldPassword;
        request.new_password=newPassword;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userchange_passwordResponse response = new userchange_passwordResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            if(response.error_code==APIErrorCode.ERROR_PASSWORD){
                                UserModel.this.OnMessageResponse(url, jo, status);
                            }
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_CHANGE_PASSWORD).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
    public void getInviteCode() {
        userinvite_codeRequest request = new userinvite_codeRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        userinvite_codeResponse response = new userinvite_codeResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            invite_code=response.invite_code;
                            editor.putString("invitecode_"+SESSION.getInstance().uid, invite_code);
                            editor.commit();
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
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

        cb.url(ApiInterface.USER_INVITE_CODE).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
    public void certify(String name,String identity,String bankId,ENUM_USER_GENDER gender, File avatar) {
        usercertifyRequest request = new usercertifyRequest();
        request.name = name;
        request.identity_card = identity;
        request.bankcard = bankId;

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.gender = gender.value();
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        usercertifyResponse response = new usercertifyResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            UserModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            UserModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }

                } catch (JSONException e) {

                }
                ;
            }
        };
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());
            if (null != avatar)
            {
                params.put("avatar", avatar);
            }

        } catch (JSONException e) {

        }

        cb.url(ApiInterface.USER_CERTIFY).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
}

