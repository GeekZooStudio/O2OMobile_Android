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

@Table(name = "userapply_serviceRequest")
public class userapply_serviceRequest  extends DataBaseModel
{

     @Column(name = "uid")
     public int uid;

     @Column(name = "sid")
     public String   sid;

     @Column(name = "firstclass_service_category_id")
     public int firstclass_service_category_id;

     @Column(name = "ver")
     public int ver;

     @Column(name = "secondclass_service_category_id")
     public int secondclass_service_category_id;

     @Column(name = "service_type_id")
     public int service_type_id;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.uid = jsonObject.optInt("uid");

          this.sid = jsonObject.optString("sid");

          this.firstclass_service_category_id = jsonObject.optInt("firstclass_service_category_id");

          this.ver = jsonObject.optInt("ver");

          this.secondclass_service_category_id = jsonObject.optInt("secondclass_service_category_id");

          this.service_type_id = jsonObject.optInt("service_type_id");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("uid", uid);
          localItemObject.put("sid", sid);
          localItemObject.put("firstclass_service_category_id", firstclass_service_category_id);
          localItemObject.put("ver", ver);
          localItemObject.put("secondclass_service_category_id", secondclass_service_category_id);
          localItemObject.put("service_type_id", service_type_id);
          return localItemObject;
     }

}
