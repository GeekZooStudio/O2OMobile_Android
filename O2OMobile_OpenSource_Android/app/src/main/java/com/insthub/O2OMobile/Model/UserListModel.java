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
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.*;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.Utils.LocationManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserListModel extends BaseModel
{
    public ArrayList<SIMPLE_USER> dataList = new ArrayList<SIMPLE_USER>();
    public static final int NUMPERPAGE = 10;
    public UserListModel(Context context)
    {
        super(context);
    }

    public void fetPreService(int serviceId,ENUM_SEARCH_ORDER search_order)
    {
        userlistRequest userrequest = new userlistRequest();
        userrequest.service_type = serviceId;
        userrequest.sid = SESSION.getInstance().sid;
        userrequest.uid = SESSION.getInstance().uid;
        userrequest.sort_by = search_order.value();
        userrequest.by_no = 1;
        userrequest.ver = O2OMobileAppConst.VERSION_CODE;
        userrequest.count = NUMPERPAGE;
        userrequest.location = new LOCATION();
        userrequest.location.lat = LocationManager.getLatitude();
        userrequest.location.lon = LocationManager.getLongitude();


        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserListModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        userlistResponse response = new userlistResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            dataList.clear();
                            dataList.addAll(response.users);
                            UserListModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            UserListModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }else{
                        UserListModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = userrequest.toJson();
            params.put("json", userrequest.toJson().toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.USER_LIST)){
            return;
        }
        cb.url(ApiInterface.USER_LIST).type(JSONObject.class).params(params);
        ajaxProgress(cb);

    }

    public void fetNextService(int serviceId,ENUM_SEARCH_ORDER search_order)
    {
        userlistRequest userrequest = new userlistRequest();
        userrequest.service_type = serviceId;
        userrequest.sid = SESSION.getInstance().sid;
        userrequest.uid = SESSION.getInstance().uid;
        userrequest.sort_by = search_order.value();
        userrequest.ver = O2OMobileAppConst.VERSION_CODE;
        if (dataList.size() > 0)
        {
            userrequest.by_no = (int)Math.ceil(dataList.size()*1.0/NUMPERPAGE) +1;;
        }

        userrequest.count = NUMPERPAGE;
        userrequest.location = new LOCATION();
        userrequest.location.lat = LocationManager.getLatitude();
        userrequest.location.lon = LocationManager.getLongitude();


        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    UserListModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        userlistResponse response = new userlistResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            dataList.addAll(response.users);
                            UserListModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            UserListModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }else{
                        UserListModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject requestJson = userrequest.toJson();
            requestJson.remove("by_id");
            params.put("json", requestJson.toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.USER_LIST)){
            return;
        }
        cb.url(ApiInterface.USER_LIST).type(JSONObject.class).params(params);
        ajax(cb);

    }
}
