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
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.CONTENT;
import com.insthub.O2OMobile.Protocol.reportRequest;
import com.insthub.O2OMobile.Protocol.reportResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportModel extends BaseModel {

	public ReportModel(Context context) {
		super(context);
	}
	
	public void report(int orderId, int userId, String text) {
		reportRequest request = new reportRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.order_id = orderId;
        request.user = userId;
        CONTENT content = new CONTENT();
        content.text = text;
        request.content = content;
		
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					ReportModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        reportResponse response = new reportResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                        	ReportModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	ReportModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
				} catch (JSONException e) {

				}
			}
		};

		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("json", request.toJson().toString());
			
			JSONObject jsonRequest = request.toJson();
            if(orderId == 0){
            	jsonRequest.remove("order_id");
            }
            if(userId == 0) {
            	jsonRequest.remove("user");
            }
            params.put("json", jsonRequest.toString());
		} catch (JSONException e) { 
			
		}
		cb.url(ApiInterface.REPORT).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
    public void feedback(String text) {
        reportRequest request = new reportRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        CONTENT content = new CONTENT();
        content.text = text;
        request.content = content;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    ReportModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        reportResponse response = new reportResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            ReportModel.this.OnMessageResponse(url,jo,status);
                        } else {
                            ReportModel.this.callback(url, response.error_code, response.error_desc);
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
        cb.url(ApiInterface.FEEDBACK).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }
}
