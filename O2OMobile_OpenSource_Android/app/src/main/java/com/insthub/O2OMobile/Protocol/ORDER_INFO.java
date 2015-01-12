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

@Table(name = "ORDER_INFO")
public class ORDER_INFO  extends DataBaseModel
{

     @Column(name = "location")
     public LOCATION   location;

     @Column(name = "employer_comment")
     public COMMENT   employer_comment;

     @Column(name = "service_type")
     public SERVICE_TYPE   service_type;

     @Column(name = "employer")
     public SIMPLE_USER   employer;

     @Column(name = "order_status")
     public int order_status;

     @Column(name = "offer_price")
     public String   offer_price;

     @Column(name = "content")
     public CONTENT   content;

     @Column(name = "ORDER_INFO_id")
     public int id;

     @Column(name = "duration")
     public int duration;

     @Column(name = "push_number")
     public int push_number;

     @Column(name = "appointment_time")
     public String   appointment_time;

     @Column(name = "employee_comment")
     public COMMENT   employee_comment;

     @Column(name = "transaction_price")
     public String   transaction_price;

     @Column(name = "created_at")
     public String   created_at;

     @Column(name = "order_sn")
     public String   order_sn;

     @Column(name = "accept_time")
     public String   accept_time;

     @Column(name = "employee")
     public SIMPLE_USER   employee;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
          LOCATION  location = new LOCATION();
          location.fromJson(jsonObject.optJSONObject("location"));
          this.location = location;
          COMMENT  employer_comment = new COMMENT();
          employer_comment.fromJson(jsonObject.optJSONObject("employer_comment"));
          this.employer_comment = employer_comment;
          SERVICE_TYPE  service_type = new SERVICE_TYPE();
          service_type.fromJson(jsonObject.optJSONObject("service_type"));
          this.service_type = service_type;
          SIMPLE_USER  employer = new SIMPLE_USER();
          employer.fromJson(jsonObject.optJSONObject("employer"));
          this.employer = employer;

          this.order_status = jsonObject.optInt("order_status");

          this.offer_price = jsonObject.optString("offer_price");
          CONTENT  content = new CONTENT();
          content.fromJson(jsonObject.optJSONObject("content"));
          this.content = content;

          this.id = jsonObject.optInt("id");

          this.duration = jsonObject.optInt("duration");

          this.push_number = jsonObject.optInt("push_number");

          this.appointment_time = jsonObject.optString("appointment_time");
          COMMENT  employee_comment = new COMMENT();
          employee_comment.fromJson(jsonObject.optJSONObject("employee_comment"));
          this.employee_comment = employee_comment;

          this.transaction_price = jsonObject.optString("transaction_price");

          this.created_at = jsonObject.optString("created_at");

          this.order_sn = jsonObject.optString("order_sn");

          this.accept_time = jsonObject.optString("accept_time");
          SIMPLE_USER  employee = new SIMPLE_USER();
          employee.fromJson(jsonObject.optJSONObject("employee"));
          this.employee = employee;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          if(null != location)
          {
            localItemObject.put("location", location.toJson());
          }
          if(null != employer_comment)
          {
            localItemObject.put("employer_comment", employer_comment.toJson());
          }
          if(null != service_type)
          {
            localItemObject.put("service_type", service_type.toJson());
          }
          if(null != employer)
          {
            localItemObject.put("employer", employer.toJson());
          }
          localItemObject.put("order_status", order_status);
          localItemObject.put("offer_price", offer_price);
          if(null != content)
          {
            localItemObject.put("content", content.toJson());
          }
          localItemObject.put("id", id);
          localItemObject.put("duration", duration);
          localItemObject.put("push_number", push_number);
          localItemObject.put("appointment_time", appointment_time);
          if(null != employee_comment)
          {
            localItemObject.put("employee_comment", employee_comment.toJson());
          }
          localItemObject.put("transaction_price", transaction_price);
          localItemObject.put("created_at", created_at);
          localItemObject.put("order_sn", order_sn);
          localItemObject.put("accept_time", accept_time);
          if(null != employee)
          {
            localItemObject.put("employee", employee.toJson());
          }
          return localItemObject;
     }

}
