package com.BeeFramework.model;

import com.insthub.O2OMobile.Protocol.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.androidquery.callback.AjaxCallback;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.CONFIG;
import com.insthub.O2OMobile.Protocol.CONTENT;
import com.insthub.O2OMobile.Protocol.LOCATION;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.Protocol.PHOTO;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.Protocol.USER;
import com.insthub.O2OMobile.Protocol.userbalanceResponse;
import com.insthub.O2OMobile.Protocol.usersigninResponse;

/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */

public class MockServer
{
    private static MockServer instance;
    public static MockServer getInstance()
    {
        if (instance == null) {
            instance = new MockServer();
        }
        return instance;
    }

    public static <K> void ajax(AjaxCallback<K> callback)
    {
		JSONObject responseJsonObject = new JSONObject();

        if(callback.getUrl().endsWith(ApiInterface.USER_VERIFYCODE)){
            userverifycodeResponse response=new userverifycodeResponse();
            response.succeed=1;

            try
            {
                responseJsonObject = response.toJson();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            };
        }

        if(callback.getUrl().endsWith(ApiInterface.USER_VERIFYCODE)){
            userverifycodeResponse response=new userverifycodeResponse();

            response.succeed=1;
            response.verify_code="123456";
            try
            {
                responseJsonObject = response.toJson();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            };
        }

        if(callback.getUrl().endsWith(ApiInterface.USER_SIGNIN)){
            usersigninResponse response= new usersigninResponse();
            response.succeed=1;
            response.sid="dnjdhwjkhdjkahwjkchasjkhcsbd";
            CONFIG config= new CONFIG();
            config.push=1;
            response.config=config;
            USER user= new USER();

            PHOTO photo= new PHOTO();
            photo.thumb="http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
            photo.large="http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
            user.avatar=photo;
            user.gender=1;
            user.id=1;
            user.joined_at= "活动组";
            user.nickname= "苍老湿";
            user.user_group = ENUM_USER_GROUP.FREEMAN.value();
            user.signature="我总在牛A与牛C之间徘徊。数据库接口实里";
            user.brief="专业维修核潜艇，回收二手航母，大修核反应堆，拆洗导弹发动机，航天飞机保养换三滤，高空作业擦洗卫星表面除尘，南极冰川修复，批发原子弹，中子弹，东风全系列巡航导弹。并提供原子对撞机，提供捕捉反物质原子技术支持。量大从优，团购7折，秒杀5折，有正规发票。";
            user.comment_count=100;
            user.comment_goodrate="99%";
            response.user=user;
            response.error_desc="用户名不存在";
            try
            {
                responseJsonObject = response.toJson();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        if(callback.getUrl().endsWith(ApiInterface.USER_SIGNUP)){
            usersignupResponse response= new usersignupResponse();
            response.succeed=1;
            response.sid="dnjdhwjkhdjkahwjkchasjkhcsbd";
            CONFIG config= new CONFIG();
            config.push=1;
            response.config=config;
            USER user= new USER();

            PHOTO photo= new PHOTO();
            photo.thumb="http://g.hiphotos.baidu.com/image/w%3D2048/sign=e808e4dab8389b5038ffe752b10de7dd/9d82d158ccbf6c815ed3d22cbd3eb13532fa400a.jpg";
            photo.large="www.google.com";
            user.avatar=photo;
            user.gender=1;
            user.id=1;
            user.joined_at= "活动组";
            user.nickname= "苍老师";
            user.user_group = 10;
            response.user=user;
            response.error_desc="用户名不存在";
            try
            {
                responseJsonObject = response.toJson();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        if(callback.getUrl().endsWith(ApiInterface.SERVICETYPE_LIST)){
            servicetypelistResponse response= new servicetypelistResponse();
            response.succeed=1;
            response.count = 10;
            response.total = 100;
            response.more = 0;

            for (int i = 1; i < 8; i++) {
                SERVICE_TYPE service_type = new SERVICE_TYPE();
                service_type.id = i;
                service_type.title = "大保健"+i;
                service_type.large_icon="http://wuzheng.qiniudn.com/B0-icon-"+i+".png";
                response.services.add(service_type);
            }

            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (callback.getUrl().endsWith(ApiInterface.USER_LIST))
        {
            userlistResponse userResponse = new userlistResponse();
            for (int i = 0;i < 10; i++)
            {
                SIMPLE_USER user = new SIMPLE_USER();
                user.nickname = "howie";
                user.comment_goodrate = "0.75";
                user.location = new LOCATION();
                user.location.lon = 116.404;
                user.location.lat = 39.915;
                user.avatar = new PHOTO();
                user.avatar.large =  "http://img3.douban.com/view/status/median/public/a82195e6e8381ba.jpg";
                user.avatar.thumb = "http://img3.douban.com/view/status/median/public/a82195e6e8381ba.jpg";

                userResponse.users.add(user);
            }

            userResponse.succeed = 1;
            userResponse.more  = 1;

            try
            {
                responseJsonObject = userResponse.toJson();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else if (callback.getUrl().endsWith(ApiInterface.ORDERLIST_PUBLISHED))
        {
            orderlistpublishedResponse response = new orderlistpublishedResponse();
            for (int i = 0;i < 10; i++)
            {
                 ORDER_INFO order_info = getOrder();

                response.orders.add(order_info);
            }

            response.succeed = 1;
            response.more  = 1;

            try
            {
                responseJsonObject = response.toJson();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

		else if (callback.getUrl().endsWith(ApiInterface.USER_BALANCE)) {

			userbalanceResponse response = new userbalanceResponse();
			response.balance = "100";
			response.succeed = 1;
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
        
		else if (callback.getUrl().endsWith(ApiInterface.ORDER_INFO)) 
		{
			orderinfoResponse response = new orderinfoResponse();
			response.succeed = 1;
			ORDER_INFO order = new ORDER_INFO();
			order.id = 1;
			order.order_sn = "201406181111";
			order.order_status = 2;

			order.offer_price = "999.99";
			order.transaction_price = "6666.66";

			order.created_at = "2014/06/10 19:29:52 +0800";
			
			SERVICE_TYPE service_type = new SERVICE_TYPE();
            service_type.id = 1;
            service_type.title = "大保健";
            service_type.icon="http://wuzheng.qiniudn.com/B0-icon-1.png";
			order.service_type = service_type;
			
			LOCATION location = new LOCATION();
			location.lat = 39.921008;
			location.lon = 116.466367;
			location.name = "北京市朝阳区旺座中心东塔3317";
			order.location = location;
			
			CONTENT content = new CONTENT();
			content.text = "开着拖拉机带我去拉萨开着拖拉机带我去拉萨开着拖拉机带我去拉萨开着拖拉机带我去拉萨";
			order.content = content;
			
			SIMPLE_USER user = new SIMPLE_USER();
            PHOTO photo = new PHOTO();
            photo.thumb = "http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
            photo.large = "http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
            user.avatar = photo;
            user.gender = 1;
            user.id = 88;
            user.mobile_phone = "18210023312";
            user.joined_at = "活动组";
            user.nickname = "苍老湿";
            user.user_group = ENUM_USER_GROUP.FREEMAN.value();
            //user.signature = "我总在牛A与牛C之间徘徊。数据库接口实里";
            //user.brief = "专业维修核潜艇，回收二手航母，大修核反应堆，拆洗导弹发动机，航天飞机保养换三滤，高空作业擦洗卫星表面除尘，南极冰川修复，批发歼１０，Ｆ２２，Ｆ３５，Ｂ２轰炸机，氢弹，原子弹，中子弹，东风全系列巡航导弹。并提供原子对撞机，提供捕捉反物质原子技术支持。量大从优，团购7折，秒杀5折，有正规发票";
            order.employer = user;
            
            SIMPLE_USER user2 = new SIMPLE_USER();
            PHOTO photo2 = new PHOTO();
            photo2.thumb = "http://image.xinmin.cn/2014/01/04/20140104144723280000.jpg";
            photo2.large = "http://image.xinmin.cn/2014/01/04/20140104144723280000.jpg";
            user2.avatar = photo2;
            user2.gender = 1;
            user2.id = 99;
            user2.mobile_phone = "10086";
            user2.joined_at = "活动组";
            user2.nickname = "波多野结衣";
            user2.user_group = ENUM_USER_GROUP.FREEMAN.value();
            //user2.signature ="我总在牛A与牛C之间徘徊。数据库接口实里";
            //user2.brief = "专业维修核潜艇，回收二手航母，大修核反应堆，拆洗导弹发动机，航天飞机保养换三滤，高空作业擦洗卫星表面除尘，南极冰川修复，批发歼１０，Ｆ２２，Ｆ３５，Ｂ２轰炸机，氢弹，原子弹，中子弹，东风全系列巡航导弹。并提供原子对撞机，提供捕捉反物质原子技术支持。量大从优，团购7折，秒杀5折，有正规发票";
            order.employee = user2;
            	
			response.order_info = order;
			
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
        
		else if (callback.getUrl().endsWith(ApiInterface.MESSAGE_LIST)) {

			messagelistResponse response = new messagelistResponse();
			response.succeed = 1;
			response.count = 10;
			response.total = 100;
			response.more = 1;
			for(int i=0;i<10;i++) {
				MESSAGE message = new MESSAGE();
				message.id = i;
				message.is_readed = 0;
				message.content = "消息内容消息内容消息内容消息内容消息内容消息内容消息内容消息内容消息内容";
				message.created_at = "2014/06/10 19:29:52 +0800";
				response.messages.add(message);
			}
			
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
        
		else if (callback.getUrl().endsWith(ApiInterface.LOCATION_INFO)) {

			locationinfoResponse response = new locationinfoResponse();
			response.succeed = 1;
			LOCATION location = new LOCATION();
			location.lat = 39.921008;
			location.lon = 116.466367;
			location.name = "北京市朝阳区旺座中心东塔3317";
			response.location = location;
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
        
		else if (callback.getUrl().endsWith(ApiInterface.MESSAGE_UNREAD_COUNT)) {

			messageunread_countResponse response = new messageunread_countResponse();
			response.succeed = 1;
			response.unread = 99;
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
        
		else if (callback.getUrl().endsWith(ApiInterface.MESSAGE_READ)) {

			messagereadResponse response = new messagereadResponse();
			response.succeed = 1;
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
		
		else if (callback.getUrl().endsWith(ApiInterface.ORDER_PUBLISH)) {

			orderpublishResponse response = new orderpublishResponse();
			response.succeed = 1;
			ORDER_INFO order = new ORDER_INFO();
			order.created_at = "2014/06/10 19:29:52 +0800";
			order.offer_price = "99";
			SIMPLE_USER user = new SIMPLE_USER();
			PHOTO photo = new PHOTO();
			photo.thumb = "http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
			user.avatar = photo;
			order.employer = user;
			SERVICE_TYPE service = new SERVICE_TYPE();
			service.title = "开车";
			order.service_type = service;
			LOCATION location = new LOCATION();
			location.lat = 39.921008;
			location.lon = 116.466367;
			order.location = location;
			CONTENT content = new CONTENT();
			content.text = "开着拖拉机带我去拉萨";
			order.content = content;
			response.order_info = order;
			
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
        else if (callback.getUrl().endsWith(ApiInterface.ORDERLIST_RECEIVED))
        {
            orderlistreceivedResponse response = new orderlistreceivedResponse();
            response.succeed = 1;
            for (int i = 0;i < 10; i++)
            {
                ORDER_INFO order_info = getOrder();

                response.orders.add(order_info);
            }

            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
		else if(callback.getUrl().endsWith(ApiInterface.ORDERLIST_AROUND)) {
			orderlistaroundResponse response = new orderlistaroundResponse();
			response.succeed = 1;
			response.count = 10;
			response.total = 100;
			response.more = 1;
			for(int i = 0; i < 10; i++) {
				ORDER_INFO order = new ORDER_INFO();
				order.created_at = "2014/06/10 19:29:52 +0800";
				order.offer_price = "99";
				SIMPLE_USER user = new SIMPLE_USER();
				PHOTO photo = new PHOTO();
				photo.thumb = "http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
				user.avatar = photo;
				order.employer = user;
				SERVICE_TYPE service = new SERVICE_TYPE();
				service.title = "开车";
				order.service_type = service;
				LOCATION location = new LOCATION();
				location.lat = 39.921008;
				location.lon = 116.466367;
				order.location = location;
				CONTENT content = new CONTENT();
				content.text = "开着拖拉机带我去拉萨";
				order.content = content;
				response.orders.add(order);
			}
			try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
			
		}
        else if(callback.getUrl().endsWith(ApiInterface.WITHDRAW_LIST)) {
            withdrawlistResponse response = new withdrawlistResponse();
            response.succeed = 1;
            response.count = 10;
            response.total = 100;
            response.more = 1;
            for(int i = 0; i < 10; i++) {
                WITHDRAW_ORDER order = new WITHDRAW_ORDER();
                order.created_at = "2014/06/10 19:29:52 +0800";
                //order.amount="200";
                order.state=ENUM_WITHDRAW_STATE.Processing.value();
                response.withdraws.add(order);
            }
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if(callback.getUrl().endsWith(ApiInterface.MYSERVICE_LIST)) {
            myservicelistResponse response = new myservicelistResponse();
            response.succeed = 1;
            response.count = 10;
            response.total = 100;
            response.more = 1;
            for(int i = 0; i < 10; i++) {
                MY_SERVICE my_service = new MY_SERVICE();
                my_service.id=i;
                SERVICE_TYPE service_type=new SERVICE_TYPE();
                service_type.title="跑腿服务";
                my_service.price="50";
                service_type.icon="http://wuzheng.qiniudn.com/B0-icon-1.png";
                my_service.service_type=service_type;
                response.services.add(my_service);
            }
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if(callback.getUrl().endsWith(ApiInterface.WITHDRAW_MONEY)) {
            withdrawmoneyResponse response = new withdrawmoneyResponse();
            response.succeed = 1;

            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        else if (callback.getUrl().endsWith(ApiInterface.COMMENT_LIST))
        {
            commentlistResponse response = new commentlistResponse();
            response.succeed = 1;
            response.count = 10;
            response.total = 100;
            response.more = 1;
            for(int i = 0; i < 10; i++) {
                COMMENT comment = new COMMENT();
                comment.content = new CONTENT();
                comment.content.text = "我爱大保健";
                comment.rank = 3;
                comment.user = new SIMPLE_USER();
                comment.user.nickname = "howie";
                comment.user.avatar = new PHOTO();
                comment.user.avatar.large = "http://img3.douban.com/view/photo/photo/public/p1369618723.jpg";
                comment.user.avatar.thumb = "http://img3.douban.com/view/photo/photo/public/p1369618723.jpg";
                response.comments.add(comment);
            }
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (callback.getUrl().endsWith(ApiInterface.USER_PROFILE))
        {
            userprofileResponse response = new userprofileResponse();
            response.succeed = 1;
            USER user = new USER();
            PHOTO photo= new PHOTO();
            photo.thumb="http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
            photo.large="http://www.s1979.com/uploads/allimg/110509/100-110509101150.jpg";
            user.avatar=photo;
            user.gender=1;
            user.id=1;
            user.joined_at= "活动组";
            user.nickname= "苍老湿";
            user.user_group = ENUM_USER_GROUP.FREEMAN.value();
            user.signature="我总在牛A与牛C之间徘徊。数据库接口实里";
            user.brief="专业维修核潜艇，回收二手航母，大修核反应堆，拆洗导弹发动机，航天飞机保养换三滤，高空作业擦洗卫星表面除尘，南极冰川修复，批发歼１０，Ｆ２２，Ｆ３５，Ｂ２轰炸机，氢弹，原子弹，中子弹，东风全系列巡航导弹。并提供原子对撞机，提供捕捉反物质原子技术支持。量大从优，团购7折，秒杀5折，有正规发票。";
            user.comment_count=100;
            user.comment_goodrate="99%";
            for(int i = 0; i < 10; i++) {
                MY_CERTIFICATION my_certification=new MY_CERTIFICATION();
                CERTIFICATION certification=new CERTIFICATION();
                certification.name="活好便宜";
                certification.id=i;
                my_certification.certification=certification;
                user.my_certification.add(my_certification);
            }
            response.user=user;
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else if (callback.getUrl().endsWith(ApiInterface.MYSERVICE_MODIFY)) {

            myservicemodifyResponse response = new myservicemodifyResponse();
            response.succeed = 1;
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (callback.getUrl().endsWith(ApiInterface.USER_APPLY_SERVICE)) {

            userapply_serviceResponse response = new userapply_serviceResponse();
            response.succeed = 1;
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (callback.getUrl().endsWith(ApiInterface.USER_CHANGE_PROFILE)) {

            userchange_profileResponse response = new userchange_profileResponse();
            response.succeed = 1;
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (callback.getUrl().endsWith(ApiInterface.USER_CHANGE_PASSWORD)) {

            userchange_passwordResponse response = new userchange_passwordResponse();
            response.succeed = 1;
            try {
                responseJsonObject = response.toJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
		callback.result = (K) responseJsonObject.toString();
		((BeeCallback) callback).callback(callback.getUrl(), responseJsonObject, callback.getStatus());
   }

    public static ORDER_INFO getOrder()
    {
        SIMPLE_USER user = new SIMPLE_USER();
        user.nickname = "howie";
        user.comment_goodrate = "0.75";
        user.location = new LOCATION();
        user.location.lon = 116.404;
        user.location.lat = 39.915;
        user.avatar = new PHOTO();
        user.avatar.large =  "http://img3.douban.com/view/status/median/public/a82195e6e8381ba.jpg";
        user.avatar.thumb = "http://img3.douban.com/view/status/median/public/a82195e6e8381ba.jpg";

        ORDER_INFO order_info = new ORDER_INFO();
        order_info.content = new CONTENT();
        order_info.content.text = "买份啤酒加炸鸡";
        order_info.employee = user;
        order_info.employer = user;
        return order_info;
    }

}
