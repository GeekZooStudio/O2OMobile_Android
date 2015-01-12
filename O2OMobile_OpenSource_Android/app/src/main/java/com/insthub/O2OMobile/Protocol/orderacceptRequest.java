
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "orderacceptRequest")
public class orderacceptRequest  extends DataBaseModel
{

     @Column(name = "uid")
     public int uid;

     @Column(name = "sid")
     public String   sid;

     @Column(name = "ver")
     public int ver;

     @Column(name = "order_id")
     public int order_id;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.uid = jsonObject.optInt("uid");

          this.sid = jsonObject.optString("sid");

          this.ver = jsonObject.optInt("ver");

          this.order_id = jsonObject.optInt("order_id");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("uid", uid);
          localItemObject.put("sid", sid);
          localItemObject.put("ver", ver);
          localItemObject.put("order_id", order_id);
          return localItemObject;
     }

}
