
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "myservicemodifyResponse")
public class myservicemodifyResponse  extends DataBaseModel
{

     @Column(name = "succeed")
     public int succeed;

     @Column(name = "service")
     public MY_SERVICE   service;

     @Column(name = "error_code")
     public int error_code;

     @Column(name = "error_desc")
     public String   error_desc;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.succeed = jsonObject.optInt("succeed");
          MY_SERVICE  service = new MY_SERVICE();
          service.fromJson(jsonObject.optJSONObject("service"));
          this.service = service;

          this.error_code = jsonObject.optInt("error_code");

          this.error_desc = jsonObject.optString("error_desc");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("succeed", succeed);
          if(null != service)
          {
            localItemObject.put("service", service.toJson());
          }
          localItemObject.put("error_code", error_code);
          localItemObject.put("error_desc", error_desc);
          return localItemObject;
     }

}
