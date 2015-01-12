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

@Table(name = "SIMPLE_USER")
public class SIMPLE_USER  extends DataBaseModel
{

     @Column(name = "comment_count")
     public int comment_count;

     @Column(name = "SIMPLE_USER_id")
     public int id;

     @Column(name = "mobile_phone")
     public String   mobile_phone;

     @Column(name = "user_group")
     public int user_group;

     @Column(name = "location")
     public LOCATION   location;

     @Column(name = "nickname")
     public String   nickname;

     @Column(name = "current_service_price")
     public String   current_service_price;

     @Column(name = "joined_at")
     public String   joined_at;

     @Column(name = "comment_goodrate")
     public String   comment_goodrate;

     @Column(name = "gender")
     public int gender;

     @Column(name = "avatar")
     public PHOTO   avatar;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.comment_count = jsonObject.optInt("comment_count");

          this.id = jsonObject.optInt("id");

          this.mobile_phone = jsonObject.optString("mobile_phone");

          this.user_group = jsonObject.optInt("user_group");
          LOCATION  location = new LOCATION();
          location.fromJson(jsonObject.optJSONObject("location"));
          this.location = location;

          this.nickname = jsonObject.optString("nickname");

          this.current_service_price = jsonObject.optString("current_service_price");

          this.joined_at = jsonObject.optString("joined_at");

          this.comment_goodrate = jsonObject.optString("comment_goodrate");

          this.gender = jsonObject.optInt("gender");
          PHOTO  avatar = new PHOTO();
          avatar.fromJson(jsonObject.optJSONObject("avatar"));
          this.avatar = avatar;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("comment_count", comment_count);
          localItemObject.put("id", id);
          localItemObject.put("mobile_phone", mobile_phone);
          localItemObject.put("user_group", user_group);
          if(null != location)
          {
            localItemObject.put("location", location.toJson());
          }
          localItemObject.put("nickname", nickname);
          localItemObject.put("current_service_price", current_service_price);
          localItemObject.put("joined_at", joined_at);
          localItemObject.put("comment_goodrate", comment_goodrate);
          localItemObject.put("gender", gender);
          if(null != avatar)
          {
            localItemObject.put("avatar", avatar.toJson());
          }
          return localItemObject;
     }

}
