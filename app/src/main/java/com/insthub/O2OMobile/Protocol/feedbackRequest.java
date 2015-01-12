
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "feedbackRequest")
public class feedbackRequest  extends DataBaseModel
{

     @Column(name = "content")
     public CONTENT   content;

     @Column(name = "uid")
     public int uid;

     @Column(name = "sid")
     public String   sid;

     @Column(name = "ver")
     public int ver;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
          CONTENT  content = new CONTENT();
          content.fromJson(jsonObject.optJSONObject("content"));
          this.content = content;

          this.uid = jsonObject.optInt("uid");

          this.sid = jsonObject.optString("sid");

          this.ver = jsonObject.optInt("ver");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          if(null != content)
          {
            localItemObject.put("content", content.toJson());
          }
          localItemObject.put("uid", uid);
          localItemObject.put("sid", sid);
          localItemObject.put("ver", ver);
          return localItemObject;
     }

}
