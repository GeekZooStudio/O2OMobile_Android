
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "LOCATION")
public class LOCATION  extends DataBaseModel
{

     @Column(name = "lon")
     public double   lon;

     @Column(name = "name")
     public String   name;

     @Column(name = "lat")
     public double   lat;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.lon = jsonObject.optDouble("lon");

          this.name = jsonObject.optString("name");

          this.lat = jsonObject.optDouble("lat");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("lon", lon);
          localItemObject.put("name", name);
          localItemObject.put("lat", lat);
          return localItemObject;
     }

}
