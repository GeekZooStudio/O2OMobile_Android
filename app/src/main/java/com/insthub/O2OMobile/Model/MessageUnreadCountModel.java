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
import com.insthub.O2OMobile.Protocol.messageunread_countRequest;
import com.insthub.O2OMobile.Protocol.messageunread_countResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageUnreadCountModel extends BaseModel {

	public int publicUnreadCount = 0;
	
	public MessageUnreadCountModel(Context context) {
		super(context);
	}
	
	public void getMessageUnreadCount() {
		messageunread_countRequest request = new messageunread_countRequest();
		request.sid = SESSION.getInstance().sid;
		request.uid = SESSION.getInstance().uid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					MessageUnreadCountModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        messageunread_countResponse response = new messageunread_countResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            publicUnreadCount = response.unread;
                        	MessageUnreadCountModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	MessageUnreadCountModel.this.callback(url, response.error_code, response.error_desc);
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
		cb.url(ApiInterface.MESSAGE_UNREAD_COUNT).type(JSONObject.class).params(params);
		ajax(cb);
	}
	
}
