
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "locationinfoResponse")
public class locationinfoResponse  extends DataBaseModel
{

     @Column(name = "succeed")
     public int succeed;

     @Column(name = "location")
     public LOCATION   location;

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
          LOCATION  location = new LOCATION();
          location.fromJson(jsonObject.optJSONObject("location"));
          this.location = location;

          this.error_code = jsonObject.optInt("error_code");

          this.error_desc = jsonObject.optString("error_desc");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("succeed", succeed);
          if(null != location)
          {
            localItemObject.put("location", location.toJson());
          }
          localItemObject.put("error_code", error_code);
          localItemObject.put("error_desc", error_desc);
          return localItemObject;
     }

}
