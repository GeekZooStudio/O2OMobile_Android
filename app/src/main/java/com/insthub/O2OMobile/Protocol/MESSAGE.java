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

package com.insthub.O2OMobile.Protocol;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Table(name = "MESSAGE")
public class MESSAGE  extends DataBaseModel
{

     @Column(name = "content")
     public String   content;

     @Column(name = "MESSAGE_id",unique = true)
     public int id;

     @Column(name = "is_readed")
     public int is_readed;

     @Column(name = "created_at")
     public String   created_at;

     @Column(name = "order_id")
     public int order_id;

     @Column(name = "type")
     public int type;

     @Column(name = "user")
     public SIMPLE_USER   user;

     @Column(name = "url")
     public String   url;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.content = jsonObject.optString("content");

          this.id = jsonObject.optInt("id");

          this.is_readed = jsonObject.optInt("is_readed");

          this.created_at = jsonObject.optString("created_at");

          this.order_id = jsonObject.optInt("order_id");

          this.type = jsonObject.optInt("type");
          SIMPLE_USER  user = new SIMPLE_USER();
          user.fromJson(jsonObject.optJSONObject("user"));
          this.user = user;

          this.url = jsonObject.optString("url");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("content", content);
          localItemObject.put("id", id);
          localItemObject.put("is_readed", is_readed);
          localItemObject.put("created_at", created_at);
          localItemObject.put("order_id", order_id);
          localItemObject.put("type", type);
          if(null != user)
          {
            localItemObject.put("user", user.toJson());
          }
          localItemObject.put("url", url);
          return localItemObject;
     }

}
