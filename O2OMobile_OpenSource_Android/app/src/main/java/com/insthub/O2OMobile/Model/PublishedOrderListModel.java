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
import com.insthub.O2OMobile.Protocol.ENUM_PUBLISHED_ORDER_STATE;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.orderlistpublishedRequest;
import com.insthub.O2OMobile.Protocol.orderlistpublishedResponse;
import com.insthub.O2OMobile.Protocol.orderlistreceivedResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublishedOrderListModel extends BaseModel
{
    public ArrayList<ORDER_INFO> publicFinishedOrderList = new ArrayList<ORDER_INFO>();
    public ArrayList<ORDER_INFO> publicUnfinishedOrderList = new ArrayList<ORDER_INFO>();
    private String  PUBLISHED_ORDER_LIST_UNFINISH = "published_order_list_unfinsh";
    private String  PUBLISHED_ORDER_LIST_FINISH = "published_order_list_finsh";
    public static final int NUMPERPAGE = 10;

    public PublishedOrderListModel(Context context)
    {
        super(context);
    }

    public void fetchPreUnfinished()
    {
        orderlistpublishedRequest request = new orderlistpublishedRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.count = NUMPERPAGE;
        request.by_no = 1;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.published_order = ENUM_PUBLISHED_ORDER_STATE.PUBLISHED_ORDER_UNDONE.value();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    PublishedOrderListModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        orderlistpublishedResponse response = new orderlistpublishedResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            publicUnfinishedOrderList.clear();
                            publicUnfinishedOrderList.addAll(response.orders);
                            editor.putString(PUBLISHED_ORDER_LIST_UNFINISH+SESSION.getInstance().uid,jo.toString());
                            editor.commit();
                            PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            PublishedOrderListModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject requestJson = request.toJson();
            requestJson.remove("by_id");
            params.put("json", requestJson.toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.ORDERLIST_PUBLISHED)){
            return;
        }
        cb.url(ApiInterface.ORDERLIST_PUBLISHED).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void fetchNextUnfinished()
    {
        orderlistpublishedRequest request = new orderlistpublishedRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.count = NUMPERPAGE;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.by_no = (int)Math.ceil(publicUnfinishedOrderList.size()*1.0/NUMPERPAGE) +1;;

        request.published_order = ENUM_PUBLISHED_ORDER_STATE.PUBLISHED_ORDER_UNDONE.value();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    PublishedOrderListModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        orderlistpublishedResponse response = new orderlistpublishedResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            publicUnfinishedOrderList.addAll(response.orders);
                            PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            PublishedOrderListModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject requestJson = request.toJson();
            requestJson.remove("by_id");
            params.put("json", requestJson.toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.ORDERLIST_PUBLISHED)){
            return;
        }
        cb.url(ApiInterface.ORDERLIST_PUBLISHED).type(JSONObject.class).params(params);
        ajax(cb);
    }

    public void fetchPrefinished()
    {
        orderlistpublishedRequest request = new orderlistpublishedRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.count = NUMPERPAGE;
        request.by_no = 1;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.published_order = ENUM_PUBLISHED_ORDER_STATE.PUBLISHED_ORDER_DONE.value();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    PublishedOrderListModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        orderlistpublishedResponse response = new orderlistpublishedResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            publicFinishedOrderList.clear();
                            publicFinishedOrderList.addAll(response.orders);
                            editor.putString(PUBLISHED_ORDER_LIST_FINISH+SESSION.getInstance().uid,jo.toString());
                            editor.commit();
                            PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            PublishedOrderListModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject requestJson = request.toJson();
            requestJson.remove("by_id");
            params.put("json", requestJson.toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.ORDERLIST_PUBLISHED)){
            return;
        }
        cb.url(ApiInterface.ORDERLIST_PUBLISHED).type(JSONObject.class).params(params);
        ajaxProgress(cb);

    }

    public void fetchNextfinished()
    {
        orderlistpublishedRequest request = new orderlistpublishedRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.count = NUMPERPAGE;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.by_no = (int)Math.ceil(publicFinishedOrderList.size()*1.0/NUMPERPAGE) +1;;

        request.published_order = ENUM_PUBLISHED_ORDER_STATE.PUBLISHED_ORDER_DONE.value();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    PublishedOrderListModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        orderlistpublishedResponse response = new orderlistpublishedResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            publicFinishedOrderList.clear();
                            publicFinishedOrderList.addAll(response.orders);
                            PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            PublishedOrderListModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        PublishedOrderListModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject requestJson = request.toJson();
            requestJson.remove("by_id");
            params.put("json", requestJson.toString());

        } catch (JSONException e) {

        }
        if(isSendingMessage(ApiInterface.ORDERLIST_PUBLISHED)){
            return;
        }
        cb.url(ApiInterface.ORDERLIST_PUBLISHED).type(JSONObject.class).params(params);
        ajax(cb);

    }
    public void loadCacheUnFinished(){
        String received_order_list_unfinsh = shared.getString(PUBLISHED_ORDER_LIST_UNFINISH+SESSION.getInstance().uid,null);
        if (null != received_order_list_unfinsh) {
            try {
                JSONObject jsonObject = new JSONObject(received_order_list_unfinsh);
                final orderlistreceivedResponse orderlistreceivedResponse = new orderlistreceivedResponse();
                orderlistreceivedResponse.fromJson(jsonObject);
                if (orderlistreceivedResponse.succeed == 1) {
                    publicUnfinishedOrderList.clear();
                    publicUnfinishedOrderList.addAll(orderlistreceivedResponse.orders);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadCacheFinished(){
        String received_order_list_finish = shared.getString(PUBLISHED_ORDER_LIST_FINISH+SESSION.getInstance().uid,null);
        if (null != received_order_list_finish) {
            try {
                JSONObject jsonObject = new JSONObject(received_order_list_finish);
                final orderlistreceivedResponse orderlistreceivedResponse = new orderlistreceivedResponse();
                orderlistreceivedResponse.fromJson(jsonObject);
                if (orderlistreceivedResponse.succeed == 1) {
                    publicFinishedOrderList.clear();
                    publicFinishedOrderList.addAll(orderlistreceivedResponse.orders);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
