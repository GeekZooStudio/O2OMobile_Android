
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "PHOTO")
public class PHOTO  extends DataBaseModel
{

     @Column(name = "height")
     public int height;

     @Column(name = "width")
     public int width;

     @Column(name = "thumb")
     public String   thumb;

     @Column(name = "large")
     public String   large;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.height = jsonObject.optInt("height");

          this.width = jsonObject.optInt("width");

          this.thumb = jsonObject.optString("thumb");

          this.large = jsonObject.optString("large");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("height", height);
          localItemObject.put("width", width);
          localItemObject.put("thumb", thumb);
          localItemObject.put("large", large);
          return localItemObject;
     }

}
