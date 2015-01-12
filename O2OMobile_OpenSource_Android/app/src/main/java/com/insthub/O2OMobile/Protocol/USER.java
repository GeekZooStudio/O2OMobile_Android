
package com.insthub.O2OMobile.Protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.DataBaseModel;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "USER")
public class USER  extends DataBaseModel
{

     @Column(name = "comment_count")
     public int comment_count;

     @Column(name = "user_group")
     public int user_group;

     @Column(name = "nickname")
     public String   nickname;

     @Column(name = "location")
     public LOCATION   location;

     public ArrayList<MY_CERTIFICATION>   my_certification = new ArrayList<MY_CERTIFICATION>();

     @Column(name = "comment_goodrate")
     public String   comment_goodrate;

     @Column(name = "avatar")
     public PHOTO   avatar;

     @Column(name = "USER_id")
     public int id;

     @Column(name = "mobile_phone")
     public String   mobile_phone;

     @Column(name = "current_service_price")
     public String   current_service_price;

     @Column(name = "joined_at")
     public String   joined_at;

     @Column(name = "gender")
     public int gender;

     @Column(name = "signature")
     public String   signature;

     @Column(name = "brief")
     public String   brief;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;

          this.comment_count = jsonObject.optInt("comment_count");

          this.user_group = jsonObject.optInt("user_group");

          this.nickname = jsonObject.optString("nickname");
          LOCATION  location = new LOCATION();
          location.fromJson(jsonObject.optJSONObject("location"));
          this.location = location;

          subItemArray = jsonObject.optJSONArray("my_certification");
          if(null != subItemArray)
           {
              for(int i = 0;i < subItemArray.length();i++)
               {
                  JSONObject subItemObject = subItemArray.getJSONObject(i);
                  MY_CERTIFICATION subItem = new MY_CERTIFICATION();
                  subItem.fromJson(subItemObject);
                  this.my_certification.add(subItem);
               }
           }


          this.comment_goodrate = jsonObject.optString("comment_goodrate");
          PHOTO  avatar = new PHOTO();
          avatar.fromJson(jsonObject.optJSONObject("avatar"));
          this.avatar = avatar;

          this.id = jsonObject.optInt("id");

          this.mobile_phone = jsonObject.optString("mobile_phone");

          this.current_service_price = jsonObject.optString("current_service_price");

          this.joined_at = jsonObject.optString("joined_at");

          this.gender = jsonObject.optInt("gender");

          this.signature = jsonObject.optString("signature");

          this.brief = jsonObject.optString("brief");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("comment_count", comment_count);
          localItemObject.put("user_group", user_group);
          localItemObject.put("nickname", nickname);
          if(null != location)
          {
            localItemObject.put("location", location.toJson());
          }

          for(int i =0; i< my_certification.size(); i++)
          {
              MY_CERTIFICATION itemData =my_certification.get(i);
              JSONObject itemJSONObject = itemData.toJson();
              itemJSONArray.put(itemJSONObject);
          }
          localItemObject.put("my_certification", itemJSONArray);
          localItemObject.put("comment_goodrate", comment_goodrate);
          if(null != avatar)
          {
            localItemObject.put("avatar", avatar.toJson());
          }
          localItemObject.put("id", id);
          localItemObject.put("mobile_phone", mobile_phone);
          localItemObject.put("current_service_price", current_service_price);
          localItemObject.put("joined_at", joined_at);
          localItemObject.put("gender", gender);
          localItemObject.put("signature", signature);
          localItemObject.put("brief", brief);
          return localItemObject;
     }

}
