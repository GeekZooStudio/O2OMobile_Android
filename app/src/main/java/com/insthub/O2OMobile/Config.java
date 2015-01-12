package com.insthub.O2OMobile;


public interface Config {
    // 应用的key 请到官方申请正式的appkey替换APP_KEY
    public static final String SINA_APP_KEY = "<Your information>";

    // 替换为开发者REDIRECT_URL
    public static final String SINA_REDIRECT_URL = "<Your information>";

    // 新支持scope：支持传入多个scope权限，用逗号分隔
    public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    public static final int SHARE_TYPE_SINA = 2;
    public static final int SHARE_TYPE_TENCENT = 1;


    public static final String BAIDU_USERID = "baidu_userid";
    public static final String DEVICE_UUID = "device_uuid";

    // 在百度开发者中心查询应用的API Key

    public static final String API_KEY = "<Your information>";
    public static final String API_KEY_TEST = "<Your information>";
    public static final String WEIXIN_APP_ID = "<Your information>";
    public static final String WEIXIN_APP_KEY = "<Your information>";

    //QQ Key
    public static final String QQZone_API_ID="<Your information>";
    public static final String QQZone_API_KEY="<Your information>";
    //Baidu地图 ProdName
    public static final String BAIDU_MAP_PRODNAME="O20Mobile";

}
