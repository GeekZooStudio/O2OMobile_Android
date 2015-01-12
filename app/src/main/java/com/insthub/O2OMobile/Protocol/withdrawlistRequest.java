
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "withdrawlistRequest")
public class withdrawlistRequest  extends DataBaseModel
{

     @Column(name = "uid")
     public int uid;

     @Column(name = "sid")
     public String   sid;

     @Column(name = "by_id")
     public int by_id;

     @Column(name = "by_no")
     public int by_no;

     @Column(name = "count")
     public int count;

     @Column(name = "ver")
     public int ver;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.uid = jsonObject.optInt("uid");

          this.sid = jsonObject.optString("sid");

          this.by_id = jsonObject.optInt("by_id");

          this.by_no = jsonObject.optInt("by_no");

          this.count = jsonObject.optInt("count");

          this.ver = jsonObject.optInt("ver");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("uid", uid);
          localItemObject.put("sid", sid);
          localItemObject.put("by_id", by_id);
          localItemObject.put("by_no", by_no);
          localItemObject.put("count", count);
          localItemObject.put("ver", ver);
          return localItemObject;
     }

}
