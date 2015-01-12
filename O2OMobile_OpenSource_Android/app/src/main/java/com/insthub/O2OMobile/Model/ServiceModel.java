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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceModel extends BaseModel {
    private Context mContext;
    public Boolean publicIsSecondCategory=false;
    public ArrayList<SERVICE_TYPE> publicServiceTypeList = new ArrayList<SERVICE_TYPE>();
    public ArrayList<SERVICE_CATEGORY> publicServiceCategories=new ArrayList<SERVICE_CATEGORY>();
    public ArrayList<SERVICE_CATEGORY> publicSecondCategories=new ArrayList<SERVICE_CATEGORY>();
    public ServiceModel(Context context) {
        super(context);
        this.mContext = context;

    }
    public void modify(int service_id,String price){
        myservicemodifyRequest request =new myservicemodifyRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.service_id=service_id;
        request.price=price;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    ServiceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        myservicemodifyResponse response = new myservicemodifyResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            ServiceModel.this.OnMessageResponse(url,jo,status);
                        } else {
                            ServiceModel.this.callback(url, response.error_code, response.error_desc);
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
        cb.url(ApiInterface.MYSERVICE_MODIFY).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void applyMore(int service_type_id, int firstclass_service_category_id, int secondclass_service_category_id) {
        userapply_serviceRequest request =new userapply_serviceRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.service_type_id=service_type_id;
        request.firstclass_service_category_id=firstclass_service_category_id;
        request.secondclass_service_category_id=secondclass_service_category_id;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    ServiceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        myservicemodifyResponse response = new myservicemodifyResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            ServiceModel.this.OnMessageResponse(url,jo,status);
                        } else {
                            ServiceModel.this.callback(url, response.error_code, response.error_desc);
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
        cb.url(ApiInterface.USER_APPLY_SERVICE).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
    public void getCategoryList(int service_category_id){

        servicecategorylistRequest request=new servicecategorylistRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.service_category_id=service_category_id;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    ServiceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        servicecategorylistResponse response = new servicecategorylistResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            if(publicIsSecondCategory){
                                publicSecondCategories=response.servicecategorys;
                            }else{
                                publicServiceCategories=response.servicecategorys;
                            }
                            ServiceModel.this.OnMessageResponse(url,jo,status);
                        } else {
                            ServiceModel.this.callback(url, response.error_code, response.error_desc);
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
        cb.url(ApiInterface.SERVICECATEGORY_LIST).type(JSONObject.class).params(params);
        ajaxProgress(cb);

    }
    public void getTypeList() {
        servicetypelistRequest request = new servicetypelistRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.by_id=0;
        request.by_no=1;
        request.count=100;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                try {
                    ServiceModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        servicetypelistResponse response = new servicetypelistResponse();
                        response.fromJson(jo);
                        if (response.succeed == 1) {
                            publicServiceTypeList.clear();
                            publicServiceTypeList.addAll(response.services);
                            ServiceModel.this.OnMessageResponse(url, jo, status);
                        } else {
                            ServiceModel.this.callback(url, response.error_code, response.error_desc);
                        }

                    }else{
                        ServiceModel.this.OnMessageResponse(url, jo, status);
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
        ajaxProgress(cb);

    }
}
