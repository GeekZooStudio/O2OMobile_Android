
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "SERVICE_TYPE")
public class SERVICE_TYPE  extends DataBaseModel
{

     @Column(name = "SERVICE_TYPE_id")
     public int id;

     @Column(name = "icon")
     public String   icon;

     @Column(name = "title")
     public String   title;

     @Column(name = "large_icon")
     public String   large_icon;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.id = jsonObject.optInt("id");

          this.icon = jsonObject.optString("icon");

          this.title = jsonObject.optString("title");

          this.large_icon = jsonObject.optString("large_icon");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("id", id);
          localItemObject.put("icon", icon);
          localItemObject.put("title", title);
          localItemObject.put("large_icon", large_icon);
          return localItemObject;
     }

}
