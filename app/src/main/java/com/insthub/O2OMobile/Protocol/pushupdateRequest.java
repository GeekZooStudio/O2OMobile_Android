
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "pushupdateRequest")
public class pushupdateRequest  extends DataBaseModel
{

     @Column(name = "uid")
     public int uid;

     @Column(name = "platform")
     public String   platform;

     @Column(name = "sid")
     public String   sid;

     @Column(name = "location")
     public LOCATION   location;

     @Column(name = "token")
     public String   token;

     @Column(name = "UUID")
     public String   UUID;

     @Column(name = "ver")
     public int ver;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.uid = jsonObject.optInt("uid");

          this.platform = jsonObject.optString("platform");

          this.sid = jsonObject.optString("sid");
          LOCATION  location = new LOCATION();
          location.fromJson(jsonObject.optJSONObject("location"));
          this.location = location;

          this.token = jsonObject.optString("token");

          this.UUID = jsonObject.optString("UUID");

          this.ver = jsonObject.optInt("ver");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("uid", uid);
          localItemObject.put("platform", platform);
          localItemObject.put("sid", sid);
          if(null != location)
          {
            localItemObject.put("location", location.toJson());
          }
          localItemObject.put("token", token);
          localItemObject.put("UUID", UUID);
          localItemObject.put("ver", ver);
          return localItemObject;
     }

}
