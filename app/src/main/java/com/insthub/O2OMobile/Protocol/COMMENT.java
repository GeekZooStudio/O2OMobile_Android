
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "COMMENT")
public class COMMENT  extends DataBaseModel
{

     @Column(name = "content")
     public CONTENT   content;

     @Column(name = "COMMENT_id")
     public int id;

     @Column(name = "rank")
     public int rank;

     @Column(name = "created_at")
     public String   created_at;

     @Column(name = "user")
     public SIMPLE_USER   user;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
          CONTENT  content = new CONTENT();
          content.fromJson(jsonObject.optJSONObject("content"));
          this.content = content;

          this.id = jsonObject.optInt("id");

          this.rank = jsonObject.optInt("rank");

          this.created_at = jsonObject.optString("created_at");
          SIMPLE_USER  user = new SIMPLE_USER();
          user.fromJson(jsonObject.optJSONObject("user"));
          this.user = user;
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
          localItemObject.put("id", id);
          localItemObject.put("rank", rank);
          localItemObject.put("created_at", created_at);
          if(null != user)
          {
            localItemObject.put("user", user.toJson());
          }
          return localItemObject;
     }

}
