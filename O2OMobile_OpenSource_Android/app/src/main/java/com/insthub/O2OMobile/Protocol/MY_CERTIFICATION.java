
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "MY_CERTIFICATION")
public class MY_CERTIFICATION  extends DataBaseModel
{

     @Column(name = "MY_CERTIFICATION_id")
     public int id;

     @Column(name = "certification")
     public CERTIFICATION   certification;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.id = jsonObject.optInt("id");
          CERTIFICATION  certification = new CERTIFICATION();
          certification.fromJson(jsonObject.optJSONObject("certification"));
          this.certification = certification;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("id", id);
          if(null != certification)
          {
            localItemObject.put("certification", certification.toJson());
          }
          return localItemObject;
     }

}
