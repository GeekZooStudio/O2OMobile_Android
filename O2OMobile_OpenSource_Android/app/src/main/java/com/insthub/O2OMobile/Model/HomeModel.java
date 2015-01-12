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
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.Protocol.servicetypelistRequest;
import com.insthub.O2OMobile.Protocol.servicetypelistResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeModel extends BaseModel {
    private int NUMPERPAGE = 20;
    public int publicMore;
    public ArrayList<SERVICE_TYPE> publicServiceTypeList = new ArrayList<SERVICE_TYPE>();
    public ArrayList<SERVICE_TYPE> publicServiceTypeLisSort = new ArrayList<SERVICE_TYPE>();


    public HomeModel(Context context) {
        super(context);
    }

    public void getServiceTypeList() {
        servicetypelistRequest request = new servicetypelistRequest();
        request.by_no = 1;
        request.count = NUMPERPAGE;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                try {
                    HomeModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        servicetypelistResponse response = new servicetypelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if (response.succeed == 1) {
                            publicServiceTypeList.clear();
                            publicServiceTypeLisSort.clear();
                            publicServiceTypeList.addAll(response.services);
                            publicServiceTypeLisSort.addAll(response.services);
                            sortList();
                            editor.putString("home_data",jo.toString());
                            editor.commit();
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            HomeModel.this.callback(url, response.error_code, response.error_desc);
                        }

                    }else{
                        HomeModel.this.OnMessageResponse(url, jo, status);
                    }

                } catch (JSONException e) {

                }
            }

            ;
        };
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());

        } catch (JSONException e) {

        }
        cb.url(ApiInterface.SERVICETYPE_LIST).type(JSONObject.class).params(params);
        ajax(cb);


    }

    public void sortList() {
        int a = publicServiceTypeList.size() % 2;
        SERVICE_TYPE empty = new SERVICE_TYPE();
        if (a == 1) {
            publicServiceTypeLisSort.add(empty);
        }
    }

    public void getServiceTypeListMore() {
        servicetypelistRequest request = new servicetypelistRequest();
        request.by_no = (int)Math.ceil(publicServiceTypeList.size()*1.0/NUMPERPAGE) +1;;
        request.count = NUMPERPAGE;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                try {
                    HomeModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        servicetypelistResponse response = new servicetypelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if (response.succeed == 1) {
                            publicServiceTypeList.addAll(response.services);
                            publicServiceTypeLisSort.addAll(response.services);
                            sortList();
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            HomeModel.this.callback(url, response.error_code, response.error_desc);
                        }

                    }

                } catch (JSONException e) {

                }
            }

            ;
        };
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.SERVICETYPE_LIST)){
            return;
        }
        cb.url(ApiInterface.SERVICETYPE_LIST).type(JSONObject.class).params(params);
        ajax(cb);

    }
    public void getServiceTypeListFresh(final Boolean isFresh) {
        servicetypelistRequest request = new servicetypelistRequest();
        request.by_no = 1;
        request.count = NUMPERPAGE;
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                try {
                    HomeModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        servicetypelistResponse response = new servicetypelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if (response.succeed == 1) {
                            publicServiceTypeList.clear();
                            publicServiceTypeLisSort.clear();
                            publicServiceTypeList.addAll(response.services);
                            publicServiceTypeLisSort.addAll(response.services);
                            sortList();
                            editor.putString("home_data",jo.toString());
                            editor.commit();
                            if(isFresh){
                                HomeModel.this.OnMessageResponse(url, jo, status);
                            }
                        } else {
                            HomeModel.this.callback(url, response.error_code, response.error_desc);
                        }

                    }else{
                        HomeModel.this.OnMessageResponse(url, jo, status);
                    }

                } catch (JSONException e) {

                }
            }

            ;
        };
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("json", request.toJson().toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.SERVICETYPE_LIST)){
            return;
        }
        cb.url(ApiInterface.SERVICETYPE_LIST).type(JSONObject.class).params(params);
        ajax(cb);
    }
}