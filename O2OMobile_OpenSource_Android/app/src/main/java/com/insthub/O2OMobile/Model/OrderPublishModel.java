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
import com.insthub.O2OMobile.Protocol.LOCATION;
import com.insthub.O2OMobile.Protocol.orderpublishRequest;
import com.insthub.O2OMobile.Protocol.orderpublishResponse;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.Utils.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OrderPublishModel extends BaseModel {

	public OrderPublishModel(Context context) {
		super(context);
	}
	
	public void publish(String price, String time, String locationName, String text, File voice, int service_type_id, int default_receiver_id, int duration) {
		orderpublishRequest request = new orderpublishRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.start_time = time;
		request.offer_price = price;
		request.service_type_id = service_type_id;
		request.default_receiver_id = default_receiver_id;
		request.duration = duration;
		LOCATION location = new LOCATION();
		location.lat = LocationManager.getLatitude();
		location.lon = LocationManager.getLongitude();
		location.name = locationName;
		request.location = location;
		CONTENT content = new CONTENT();
		content.text = text;
		request.content = content;
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderPublishModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderpublishResponse response = new orderpublishResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                        	
                        	OrderPublishModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderPublishModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
				} catch (JSONException e) {

				}
			}
		};

		Map<String, Object> params = new HashMap<String, Object>();
		try {
			JSONObject publishRequest = request.toJson();
            if(default_receiver_id == 0){
            	publishRequest.remove("default_receiver_id");
            }
            if(service_type_id == 0){
            	publishRequest.remove("service_type_id");
            }
            if(duration == 0) {
            	publishRequest.remove("duration");
            }
			params.put("json", publishRequest.toString());
			if(voice != null) {
				params.put("voice", voice);
			}
		} catch (JSONException e) { 
			
		}
		cb.url(ApiInterface.ORDER_PUBLISH).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
}
