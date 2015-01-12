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
import com.insthub.O2OMobile.Protocol.LOCATION;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.orderlistaroundRequest;
import com.insthub.O2OMobile.Protocol.orderlistaroundResponse;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.Utils.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderListAroundModel extends BaseModel {

	public static final int NUMPERPAGE = 10;
	public ArrayList<ORDER_INFO> publicOrderList = new ArrayList<ORDER_INFO>();
	public int publicMore;
	
	public OrderListAroundModel(Context context) {
		super(context);
	}
	
	public void getList(int sort_by) {
        orderlistaroundRequest request = new orderlistaroundRequest();
		request.sid = SESSION.getInstance().sid;
		request.uid = SESSION.getInstance().uid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.sort_by = sort_by;
		request.by_no = 1;
		request.count = NUMPERPAGE;
		LOCATION location = new LOCATION();
		location.lat = LocationManager.getLatitude();
		location.lon = LocationManager.getLongitude();
		request.location = location;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderListAroundModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderlistaroundResponse response = new orderlistaroundResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if(response.succeed == 1) {
                            publicOrderList.clear();
                            publicOrderList.addAll(response.orders);
                        	OrderListAroundModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderListAroundModel.this.callback(url, response.error_code, response.error_desc);
                        }
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
        if(isSendingMessage(ApiInterface.ORDERLIST_AROUND)){
               return;
        }
		cb.url(ApiInterface.ORDERLIST_AROUND).type(JSONObject.class).params(params);
		ajax(cb);
	}
	
	public void getListMore(int sort_by) {
        orderlistaroundRequest request = new orderlistaroundRequest();
		request.sid = SESSION.getInstance().sid;
		request.uid = SESSION.getInstance().uid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.sort_by = sort_by;
		request.by_no = (int)Math.ceil(publicOrderList.size()*1.0/NUMPERPAGE) +1;
		request.count = NUMPERPAGE;
		LOCATION location = new LOCATION();
		location.lat = LocationManager.getLatitude();
		location.lon = LocationManager.getLongitude();
		request.location = location;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderListAroundModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderlistaroundResponse response = new orderlistaroundResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if(response.succeed == 1) {
                            publicOrderList.addAll(response.orders);
                        	OrderListAroundModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderListAroundModel.this.callback(url, response.error_code, response.error_desc);
                        }
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
        if(isSendingMessage(ApiInterface.ORDERLIST_AROUND)){
            return;
        }
		cb.url(ApiInterface.ORDERLIST_AROUND).type(JSONObject.class).params(params);
		ajax(cb);
	}
	
}
