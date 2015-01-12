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
import com.insthub.O2OMobile.Protocol.MESSAGE;
import com.insthub.O2OMobile.Protocol.messagelistRequest;
import com.insthub.O2OMobile.Protocol.messagelistResponse;
import com.insthub.O2OMobile.Protocol.messagereadRequest;
import com.insthub.O2OMobile.Protocol.messagesyslistRequest;
import com.insthub.O2OMobile.Protocol.messagesyslistResponse;
import com.insthub.O2OMobile.Protocol.messageunread_countResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageListModel extends BaseModel {

	public static final int NUMPERPAGE = 10;
	public ArrayList<MESSAGE> publicMessageList = new ArrayList<MESSAGE>();
	public ArrayList<MESSAGE> publicMessageSysList = new ArrayList<MESSAGE>();
	public int publicMore;
	public int publicMoreSys;
    private String  MSG = "message";
    private String  SYS_MSG = "sys_message";
	public MessageListModel(Context context) {
		super(context);
	}
	
	public void getList() {
		messagelistRequest request = new messagelistRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.by_no = 1;
		request.count = NUMPERPAGE;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					MessageListModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        messagelistResponse response = new messagelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if(response.succeed == 1) {
                            publicMessageList.clear();
                            publicMessageList.addAll(response.messages);
                            editor.putString(MSG+SESSION.getInstance().uid,jo.toString());
                            editor.commit();
                        	MessageListModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	MessageListModel.this.callback(url, response.error_code, response.error_desc);
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
        if(isSendingMessage(ApiInterface.MESSAGE_LIST)){
            return;
        }
		cb.url(ApiInterface.MESSAGE_LIST).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	public void getListMore() {
		messagelistRequest request = new messagelistRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.by_no = (int)Math.ceil(publicMessageList.size()*1.0/NUMPERPAGE) +1;
		request.count = NUMPERPAGE;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					MessageListModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        messagelistResponse response = new messagelistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if(response.succeed == 1) {
                            publicMessageList.addAll(response.messages);
                        	MessageListModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	MessageListModel.this.callback(url, response.error_code, response.error_desc);
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
        if(isSendingMessage(ApiInterface.MESSAGE_LIST)){
            return;
        }
		cb.url(ApiInterface.MESSAGE_LIST).type(JSONObject.class).params(params);
		ajax(cb);
	}
	
	/**
	 * 读消息
	 * @param message
	 */
	public void read(int id) {
		messagereadRequest request = new messagereadRequest();
		request.sid = SESSION.getInstance().sid;
		request.uid = SESSION.getInstance().uid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.message = id;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					MessageListModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        messageunread_countResponse response = new messageunread_countResponse();
                        response.fromJson(jo);
                        if(response.succeed == 1) {
                            messagelistResponse messagelistResponse=new messagelistResponse();
                            messagelistResponse.messages= publicMessageList;
                            messagelistResponse.succeed=1;
                        	MessageListModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	MessageListModel.this.callback(url, response.error_code, response.error_desc);
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
		cb.url(ApiInterface.MESSAGE_READ).type(JSONObject.class).params(params);
		ajax(cb);
	}
	
	public void getSysList() {
		messagesyslistRequest request = new messagesyslistRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.by_no = 1;
		request.count = NUMPERPAGE;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					MessageListModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        messagesyslistResponse response = new messagesyslistResponse();
                        response.fromJson(jo);
                        publicMoreSys = response.more;
                        if(response.succeed == 1) {
                            publicMessageSysList.clear();
                            publicMessageSysList.addAll(response.messages);
                            editor.putString(SYS_MSG+SESSION.getInstance().uid,jo.toString());
                            editor.commit();
                        	MessageListModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	MessageListModel.this.callback(url, response.error_code, response.error_desc);
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
        if(isSendingMessage(ApiInterface.MESSAGE_SYSLIST)){
            return;
        }
		cb.url(ApiInterface.MESSAGE_SYSLIST).type(JSONObject.class).params(params);
		ajaxProgress(cb);
	}
	
	public void getMessageSysListMore() {
		messagesyslistRequest request = new messagesyslistRequest();
		request.uid = SESSION.getInstance().uid;
		request.sid = SESSION.getInstance().sid;
		request.ver = O2OMobileAppConst.VERSION_CODE;
		request.by_no = (int)Math.ceil(publicMessageSysList.size()*1.0/NUMPERPAGE) +1;
		request.count = NUMPERPAGE;
		
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				try {
					MessageListModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        messagesyslistResponse response = new messagesyslistResponse();
                        response.fromJson(jo);
                        publicMoreSys = response.more;
                        if(response.succeed == 1) {
                            publicMessageSysList.addAll(response.messages);
                        	MessageListModel.this.OnMessageResponse(url,jo,status);
                        } else {
                        	MessageListModel.this.callback(url, response.error_code, response.error_desc);
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
        if(isSendingMessage(ApiInterface.MESSAGE_SYSLIST)){
            return;
        }
		cb.url(ApiInterface.MESSAGE_SYSLIST).type(JSONObject.class).params(params);
		ajax(cb);
	}
	public void loadCacheSysMsg(){
        String sys_msg = shared.getString(SYS_MSG+SESSION.getInstance().uid,null);
        if (null != sys_msg) {
            try {
                JSONObject jsonObject = new JSONObject(sys_msg);
                final messagelistResponse messagelistResponse = new messagelistResponse();
                messagelistResponse.fromJson(jsonObject);
                if (messagelistResponse.succeed == 1) {
                    publicMessageSysList.clear();
                    publicMessageSysList.addAll(messagelistResponse.messages);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void loadCacheMsg(){
        String msg = shared.getString(MSG+SESSION.getInstance().uid,null);
        if (null != msg) {
            try {
                JSONObject jsonObject = new JSONObject(msg);
                final messagelistResponse messagelistResponse = new messagelistResponse();
                messagelistResponse.fromJson(jsonObject);
                if (messagelistResponse.succeed == 1) {
                    publicMessageList.clear();
                    publicMessageList.addAll(messagelistResponse.messages);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
