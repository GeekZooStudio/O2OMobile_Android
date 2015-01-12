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
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.orderacceptRequest;
import com.insthub.O2OMobile.Protocol.orderacceptResponse;
import com.insthub.O2OMobile.Protocol.ordercancelRequest;
import com.insthub.O2OMobile.Protocol.ordercancelResponse;
import com.insthub.O2OMobile.Protocol.orderconfirm_payRequest;
import com.insthub.O2OMobile.Protocol.orderconfirm_payResponse;
import com.insthub.O2OMobile.Protocol.orderinfoRequest;
import com.insthub.O2OMobile.Protocol.orderinfoResponse;
import com.insthub.O2OMobile.Protocol.orderpayRequest;
import com.insthub.O2OMobile.Protocol.orderpayResponse;
import com.insthub.O2OMobile.Protocol.orderwork_doneRequest;
import com.insthub.O2OMobile.Protocol.orderwork_doneResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderInfoModel extends BaseModel {

	public ORDER_INFO publicOrder;
	public OrderInfoModel(Context context) {
		super(context);
	}

	public void get(int orderId) {
		orderinfoRequest request = new orderinfoRequest();
		request.sid = SESSION.getInstance().sid;
		request.uid = SESSION.getInstance().uid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.order_id = orderId;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderInfoModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderinfoResponse response = new orderinfoResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            publicOrder = response.order_info;
                        	OrderInfoModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderInfoModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    } else {
                    	OrderInfoModel.this.OnMessageResponse(url,jo,status);
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
		cb.url(ApiInterface.ORDER_INFO).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	/**
	 * 取消订单
	 * @param orderId
	 */
	public void cancel(int orderId) {
		ordercancelRequest request = new ordercancelRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.order_id = orderId;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderInfoModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        ordercancelResponse response = new ordercancelResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            publicOrder = response.order_info;
                        	OrderInfoModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderInfoModel.this.callback(url, response.error_code, response.error_desc);
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
		cb.url(ApiInterface.ORDER_CANCEL).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	/**
	 * 接单
	 * @param orderId
	 */
	public void accept(int orderId) {
		orderacceptRequest request = new orderacceptRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.order_id = orderId;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderInfoModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderacceptResponse response = new orderacceptResponse();
                        response.fromJson(jo);
                        if(response.order_info != null) {
                            publicOrder = response.order_info;
                        }
                        OrderInfoModel.this.OnMessageResponse(url,jo,status);
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
		cb.url(ApiInterface.ORDER_ACCEPT).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	/**
	 * 订单完成
	 * @param orderId
	 * @param transaction_price
	 */
	public void done(int orderId, String transaction_price) {
		orderwork_doneRequest request = new orderwork_doneRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.order_id = orderId;
		request.transaction_price = transaction_price;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderInfoModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderwork_doneResponse response = new orderwork_doneResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            publicOrder = response.order_info;
                        	OrderInfoModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderInfoModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
				} catch (JSONException e) {

				}
			}
		};

		Map<String, Object> params = new HashMap<String, Object>();
		try {
			JSONObject jo = request.toJson();
            if(transaction_price.equals("")){
            	jo.remove("transaction_price");
            }
            params.put("json", jo.toString());
		} catch (JSONException e) { 
			
		}
		cb.url(ApiInterface.ORDER_WORK_DONE).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	/**
	 * 支付订单
	 * @param orderId
	 * @param payCode	支付方式
	 */
	public void pay(int orderId, int payCode) {
		orderpayRequest request = new orderpayRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.order_id = orderId;
		request.pay_code = payCode;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderInfoModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderpayResponse response = new orderpayResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            publicOrder = response.order_info;
                        	OrderInfoModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderInfoModel.this.callback(url, response.error_code, response.error_desc);
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
		cb.url(ApiInterface.ORDER_PAY).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	/**
	 * 确认支付
	 * @param orderId
	 */
	public void confirmPay(int orderId) {
		orderconfirm_payRequest request = new orderconfirm_payRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.order_id = orderId;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					OrderInfoModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        orderconfirm_payResponse response = new orderconfirm_payResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            publicOrder = response.order_info;
                        	OrderInfoModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	OrderInfoModel.this.callback(url, response.error_code, response.error_desc);
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
		cb.url(ApiInterface.ORDER_CONFIRM_PAY).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	
}
