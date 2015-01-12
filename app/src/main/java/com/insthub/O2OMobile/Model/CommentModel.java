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
import com.insthub.O2OMobile.Protocol.COMMENT;
import com.insthub.O2OMobile.Protocol.CONTENT;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.commentlistRequest;
import com.insthub.O2OMobile.Protocol.commentlistResponse;
import com.insthub.O2OMobile.Protocol.commentsendRequest;
import com.insthub.O2OMobile.Protocol.commentsendResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentModel extends BaseModel
{
    public ArrayList<COMMENT> publicDataList = new ArrayList<COMMENT>();
    public ORDER_INFO publicOrder;
    public static final int NUMPERPAGE = 10;
    public CommentModel(Context context)
    {
        super(context);
    }


    public void fetchPre(int user_id)
    {
        commentlistRequest request = new commentlistRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.count = NUMPERPAGE;
        request.by_no = 1;
        request.user = user_id;
        request.ver = O2OMobileAppConst.VERSION_CODE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    CommentModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        commentlistResponse response = new commentlistResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            publicDataList.clear();
                            publicDataList.addAll(response.comments);
                            CommentModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            CommentModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        CommentModel.this.OnMessageResponse(url,jo,status);
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
        if(isSendingMessage(ApiInterface.COMMENT_LIST)){
            return;
        }
        cb.url(ApiInterface.COMMENT_LIST).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

    public void fetchNext(int user_id)
    {
        commentlistRequest request = new commentlistRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.count = NUMPERPAGE;
        request.ver = O2OMobileAppConst.VERSION_CODE;

        if (publicDataList.size() > 0)
        {
            request.by_no = (int)Math.ceil(publicDataList.size()*1.0/NUMPERPAGE) +1;
        }

        request.user = user_id;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    CommentModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        commentlistResponse response = new commentlistResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                            publicDataList.addAll(response.comments);
                            CommentModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            CommentModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        CommentModel.this.OnMessageResponse(url,jo,status);
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
        if(isSendingMessage(ApiInterface.COMMENT_LIST)){
            return;
        }
        cb.url(ApiInterface.COMMENT_LIST).type(JSONObject.class).params(params);
        ajax(cb);
    }

    public void comment(String content,int rank, int order_id)
    {
        commentsendRequest request = new commentsendRequest();

        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.content = new CONTENT();
        request.content.text = content;
        request.rank = rank;
        request.order_id = order_id;
        request.ver = O2OMobileAppConst.VERSION_CODE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    CommentModel.this.callback(this, url, jo, status);
                    if (null != jo)
                    {
                        commentsendResponse response = new commentsendResponse();
                        response.fromJson(jo);

                        if(response.succeed == 1)
                        {
                        	publicOrder = response.order_info;
                            CommentModel.this.OnMessageResponse(url,jo,status);
                        }
                        else
                        {
                            CommentModel.this.callback(url, response.error_code, response.error_desc);
                        }
                    }
                    else
                    {
                        CommentModel.this.OnMessageResponse(url,jo,status);
                    }

                } catch (JSONException e) {

                }
            }
        };

        Map<String, Object> params = new HashMap<String, Object>();
        try {
            JSONObject requestJson = request.toJson();
            params.put("json", requestJson.toString());

        } catch (JSONException e) {

        }
        cb.url(ApiInterface.COMMENT_SEND).type(JSONObject.class).params(params);
        ajaxProgress(cb);
    }

}
