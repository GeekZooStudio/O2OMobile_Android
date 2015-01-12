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
import com.insthub.O2OMobile.Protocol.WITHDRAW_ORDER;
import com.insthub.O2OMobile.Protocol.withdrawlistRequest;
import com.insthub.O2OMobile.Protocol.withdrawlistResponse;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrawModel extends BaseModel {
    private int NUMPERPAGE = 10;
    public int publicMore;
    private Context montext;
    public ArrayList<WITHDRAW_ORDER> publicWithdrawOrders = new ArrayList<WITHDRAW_ORDER>();

    public DrawModel(Context context) {
        super(context);
        this.montext = context;

    }
   public void getList(){
       withdrawlistRequest request=new withdrawlistRequest();
       request.sid = SESSION.getInstance().sid;
       request.uid = SESSION.getInstance().uid;
       request.ver = O2OMobileAppConst.VERSION_CODE;
       request.by_no = 1;
       request.count = NUMPERPAGE;
       BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

           @Override
           public void callback(String url, JSONObject jo, AjaxStatus status) {
               try {
                   DrawModel.this.callback(this, url, jo, status);
                   if (null != jo) {
                       withdrawlistResponse response = new withdrawlistResponse();
                       response.fromJson(jo);
                       publicMore = response.more;
                       if(response.succeed == 1) {
                           publicWithdrawOrders.clear();
                           publicWithdrawOrders.addAll(response.withdraws);
                           DrawModel.this.OnMessageResponse(url,jo,status);
                       } else {
                           DrawModel.this.callback(url, response.error_code, response.error_desc);
                       }
                   }else{
                       DrawModel.this.OnMessageResponse(url,jo,status);
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
       if(isSendingMessage(ApiInterface.WITHDRAW_LIST)){
           return;
       }
       cb.url(ApiInterface.WITHDRAW_LIST).type(JSONObject.class).params(params);
       //aq.ajax(cb);
       ajaxProgress(cb);

   }
    public void getListMore(){
        withdrawlistRequest request = new withdrawlistRequest();
        request.sid = SESSION.getInstance().sid;
        request.uid = SESSION.getInstance().uid;
        request.ver = O2OMobileAppConst.VERSION_CODE;
        request.by_no = (int)Math.ceil(publicWithdrawOrders.size()*1.0/NUMPERPAGE) +1;;
        request.count = NUMPERPAGE;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {
                    DrawModel.this.callback(this, url, jo, status);
                    if (null != jo) {
                        withdrawlistResponse response = new withdrawlistResponse();
                        response.fromJson(jo);
                        publicMore = response.more;
                        if(response.succeed == 1) {
                            publicWithdrawOrders.addAll(response.withdraws);
                            DrawModel.this.OnMessageResponse(url,jo,status);
                        } else {
                            DrawModel.this.callback(url, response.error_code, response.error_desc);
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
        if(isSendingMessage(ApiInterface.WITHDRAW_LIST)){
            return;
        }
        cb.url(ApiInterface.WITHDRAW_LIST).type(JSONObject.class).params(params);
        ajax(cb);
    }
}
