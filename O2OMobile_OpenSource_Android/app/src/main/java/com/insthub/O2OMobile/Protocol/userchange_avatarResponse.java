
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "userchange_avatarResponse")
public class userchange_avatarResponse  extends DataBaseModel
{

     @Column(name = "succeed")
     public int succeed;

     @Column(name = "error_code")
     public int error_code;

     @Column(name = "user")
     public USER   user;

     @Column(name = "error_desc")
     public String   error_desc;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.succeed = jsonObject.optInt("succeed");

          this.error_code = jsonObject.optInt("error_code");
          USER  user = new USER();
          user.fromJson(jsonObject.optJSONObject("user"));
          this.user = user;

          this.error_desc = jsonObject.optString("error_desc");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("succeed", succeed);
          localItemObject.put("error_code", error_code);
          if(null != user)
          {
            localItemObject.put("user", user.toJson());
          }
          localItemObject.put("error_desc", error_desc);
          return localItemObject;
     }

}
