
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "pushswitchRequest")
public class pushswitchRequest  extends DataBaseModel
{

     @Column(name = "push_switch")
     public int push_switch;

     @Column(name = "uid")
     public int uid;

     @Column(name = "sid")
     public String   sid;

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

          this.push_switch = jsonObject.optInt("push_switch");

          this.uid = jsonObject.optInt("uid");

          this.sid = jsonObject.optString("sid");

          this.UUID = jsonObject.optString("UUID");

          this.ver = jsonObject.optInt("ver");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("push_switch", push_switch);
          localItemObject.put("uid", uid);
          localItemObject.put("sid", sid);
          localItemObject.put("UUID", UUID);
          localItemObject.put("ver", ver);
          return localItemObject;
     }

}
