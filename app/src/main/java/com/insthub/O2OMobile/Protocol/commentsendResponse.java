
package com.insthub.O2OMobile.Protocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "commentsendResponse")
public class commentsendResponse  extends DataBaseModel
{

     @Column(name = "succeed")
     public int succeed;

     @Column(name = "order_info")
     public ORDER_INFO   order_info;

     @Column(name = "error_code")
     public int error_code;

     @Column(name = "comment")
     public COMMENT   comment;

     @Column(name = "error_desc")
     public String   error_desc;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.succeed = jsonObject.optInt("succeed");
          ORDER_INFO  order_info = new ORDER_INFO();
          order_info.fromJson(jsonObject.optJSONObject("order_info"));
          this.order_info = order_info;

          this.error_code = jsonObject.optInt("error_code");
          COMMENT  comment = new COMMENT();
          comment.fromJson(jsonObject.optJSONObject("comment"));
          this.comment = comment;

          this.error_desc = jsonObject.optString("error_desc");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("succeed", succeed);
          if(null != order_info)
          {
            localItemObject.put("order_info", order_info.toJson());
          }
          localItemObject.put("error_code", error_code);
          if(null != comment)
          {
            localItemObject.put("comment", comment.toJson());
          }
          localItemObject.put("error_desc", error_desc);
          return localItemObject;
     }

}
