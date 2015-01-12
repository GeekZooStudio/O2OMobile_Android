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

@Table(name = "orderpublishRequest")
public class orderpublishRequest  extends DataBaseModel
{

     @Column(name = "content")
     public CONTENT   content;

     @Column(name = "uid")
     public int uid;

     @Column(name = "sid")
     public String   sid;

     @Column(name = "duration")
     public int duration;

     @Column(name = "location")
     public LOCATION   location;

     @Column(name = "start_time")
     public String   start_time;

     @Column(name = "ver")
     public int ver;

     @Column(name = "default_receiver_id")
     public int default_receiver_id;

     @Column(name = "service_type_id")
     public int service_type_id;

     @Column(name = "offer_price")
     public String   offer_price;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
          CONTENT  content = new CONTENT();
          content.fromJson(jsonObject.optJSONObject("content"));
          this.content = content;

          this.uid = jsonObject.optInt("uid");

          this.sid = jsonObject.optString("sid");

          this.duration = jsonObject.optInt("duration");
          LOCATION  location = new LOCATION();
          location.fromJson(jsonObject.optJSONObject("location"));
          this.location = location;

          this.start_time = jsonObject.optString("start_time");

          this.ver = jsonObject.optInt("ver");

          this.default_receiver_id = jsonObject.optInt("default_receiver_id");

          this.service_type_id = jsonObject.optInt("service_type_id");

          this.offer_price = jsonObject.optString("offer_price");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          if(null != content)
          {
            localItemObject.put("content", content.toJson());
          }
          localItemObject.put("uid", uid);
          localItemObject.put("sid", sid);
          localItemObject.put("duration", duration);
          if(null != location)
          {
            localItemObject.put("location", location.toJson());
          }
          localItemObject.put("start_time", start_time);
          localItemObject.put("ver", ver);
          localItemObject.put("default_receiver_id", default_receiver_id);
          localItemObject.put("service_type_id", service_type_id);
          localItemObject.put("offer_price", offer_price);
          return localItemObject;
     }

}
