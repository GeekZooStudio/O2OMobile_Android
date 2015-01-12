//
//       _/_/_/                      _/            _/_/_/_/_/
//    _/          _/_/      _/_/    _/  _/              _/      _/_/      _/_/
//   _/  _/_/  _/_/_/_/  _/_/_/_/  _/_/              _/      _/    _/  _/    _/
//  _/    _/  _/        _/        _/  _/          _/        _/    _/  _/    _/
//   _/_/_/    _/_/_/    _/_/_/  _/    _/      _/_/_/_/_/    _/_/      _/_/
//
//
//  Copyright (c) 2015-2016, Geek Zoo Studio
//  http://www.geek-zoo.com
//
//
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this software and associated documentation files (the "Software"),
//  to deal in the Software without restriction, including without limitation
//  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//  and/or sell copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//  IN THE SOFTWARE.
//

package com.insthub.O2OMobile.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.BeeFramework.view.ToastView;
import com.insthub.O2OMobile.Config;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Request;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class WXEntryActivity extends Activity implements OnClickListener, Request, Response, IWXAPIEventHandler {

    private LinearLayout share_sina;
    private LinearLayout share_weixin;
    private LinearLayout share_sms;
    private LinearLayout share_copy;
    private Button share_cancel;

    private LinearLayout share_friendline_comment;
    private LinearLayout share_sina_comment;
    private LinearLayout share_qzone_comment;
    private Button share_cancel_comment;
    private IWeiboShareAPI mWeiboShareAPI;
    private Bitmap detaultShareImage = null;
    private Bitmap shareImage = null;

    private IWXAPI weixinAPI = null;
    private Tencent mTencent;
    //QZone分享， SHARE_TO_QQ_TYPE_DEFAULT 图文，SHARE_TO_QQ_TYPE_IMAGE 纯图
    private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
    public static final String IS_COMMENT = "is_comment";
    private View contentView;
    private String invitecode;
    public static final String INVITECODE = "invitecode";
    private SharedPreferences shared;
    private int order_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        shared=getSharedPreferences(O2OMobileAppConst.USERINFO,0);
        if (getIntent().getBooleanExtra(IS_COMMENT, false)) {
            contentView = getLayoutInflater().inflate(R.layout.share_comment, null);
            setContentView(contentView);
            order_id=getIntent().getIntExtra("order_id",0);
            mTencent = Tencent.createInstance(Config.QQZone_API_ID, WXEntryActivity.this.getApplicationContext());
            share_friendline_comment = (LinearLayout) findViewById(R.id.share_friendline_comment);
            share_sina_comment = (LinearLayout) findViewById(R.id.share_sina_comment);
            share_qzone_comment = (LinearLayout) findViewById(R.id.share_qzone_comment);
            share_cancel_comment = (Button) findViewById(R.id.share_cancel_comment);
            share_friendline_comment.setOnClickListener(this);
            share_sina_comment.setOnClickListener(this);
            share_qzone_comment.setOnClickListener(this);
            share_cancel_comment.setOnClickListener(this);
        } else {
            contentView = getLayoutInflater().inflate(R.layout.share_view, null);
            setContentView(contentView);
            invitecode=getIntent().getStringExtra(INVITECODE);
            share_sina = (LinearLayout) findViewById(R.id.share_sina);
            share_weixin = (LinearLayout) findViewById(R.id.share_weixin);
            share_sms = (LinearLayout) findViewById(R.id.share_sms);
            share_copy = (LinearLayout) findViewById(R.id.share_copy);
            share_cancel = (Button) findViewById(R.id.share_cancel);

            share_sina.setOnClickListener(this);
            share_weixin.setOnClickListener(this);
            share_sms.setOnClickListener(this);
            share_copy.setOnClickListener(this);
            share_cancel.setOnClickListener(this);
        }
        android.view.ViewGroup.LayoutParams cursorParams = (android.view.ViewGroup.LayoutParams) contentView.getLayoutParams();
        cursorParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(cursorParams);
        contentView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        detaultShareImage = BitmapFactory.decodeResource(getResources(), R.drawable.icon_512_low);
        if (shareImage == null) {
            shareImage = detaultShareImage;
        }
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Config.SINA_APP_KEY);
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                    Toast.makeText(WXEntryActivity.this, "取消下载", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboRequest(getIntent(), this);
        }

        if (null == weixinAPI) {
            weixinAPI = WXAPIFactory.createWXAPI(this, Config.WEIXIN_APP_ID, true);
            weixinAPI.registerApp(Config.WEIXIN_APP_ID);
        }
        weixinAPI.handleIntent(getIntent(), this);

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        mWeiboShareAPI.handleWeiboResponse(intent, this);
        weixinAPI.handleIntent(intent, this);
    }

    @Override
    public void onClick(View v) {
        String shareContent=getString(R.string.share_content);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.share_sina:
                if (mWeiboShareAPI.checkEnvironment(true)) {
                    mWeiboShareAPI.registerApp();
                }
                sendMessage(shareContent, "http://www.o2omobile.com.cn");
                break;
            case R.id.share_weixin:
                shareToWeiXin(shareContent, "http://www.o2omobile.com.cn");
                break;
            case R.id.share_sms:
                Uri uri = Uri.parse("smsto:");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", shareContent);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case R.id.share_copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(shareContent);
                ToastView toast = new ToastView(WXEntryActivity.this, "已复制到粘贴板");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case R.id.share_cancel:
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case R.id.share_cancel_comment:
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case R.id.share_friendline_comment:
                shareToFriendLine(shareContent, "http://www.o2omobile.com.cn");
                break;
            case R.id.share_sina_comment:
                if (mWeiboShareAPI.checkEnvironment(true)) {
                    mWeiboShareAPI.registerApp();
                }
                sendMessage(shareContent, "http://www.o2omobile.com.cn");
                break;
            case R.id.share_qzone_comment:
                if(mTencent.isReady()){

                }
                ArrayList<String> imageUrls = new ArrayList<String>();
                final Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, getString(R.string.share_title));
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareContent);
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,"http://www.o2omobile.com.cn");
                imageUrls.add("http://dev.o2omobile.net/frontend/images/icon_512.png");
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
                doShareToQzone(params);
                break;
        }
    }

    @Override
    public void onRequest(BaseRequest arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     *
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    private void sendMessage(String shareTitle, String shareUrl) {

        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                sendMultiMessage(shareTitle, shareUrl);
            } else {
                sendSingleMessage(shareTitle, shareUrl);
            }
        } else {
            //Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link com.sina.weibo.sdk.api.share.IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     */
    private void sendSingleMessage(String shareTitle, String shareUrl) {
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (null != shareTitle) {
            weiboMessage.mediaObject = getTextObj(shareTitle);
        }

        if (null != shareUrl) {
            weiboMessage.mediaObject = getWebpageObj(shareTitle, shareUrl);
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    private void sendMultiMessage(String shareTitle, String shareUrl) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (null != shareUrl) {
            weiboMessage.mediaObject = getWebpageObj(shareTitle, shareUrl);
        } else if (null != shareTitle) {
            weiboMessage.textObject = getTextObj(shareTitle);
        }
        if (null != shareImage) {
            weiboMessage.imageObject = getImageObj();
        }
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String shareText) {
        TextObject textObject = new TextObject();
        textObject.text = shareText;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(shareImage);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String title, String shareUrl) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        // 设置 Bitmap 类型的图片到视频对象里
        if (null != shareImage) {
            mediaObject.setThumbImage(shareImage);
        } else {
            mediaObject.setThumbImage(detaultShareImage);
        }
        mediaObject.title = "O2OMobile";
        mediaObject.actionUrl = shareUrl;
        mediaObject.defaultText = title;
        mediaObject.description = title;
        return mediaObject;
    }

    /**
     * 新浪微博返回状态
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        ToastView toast;
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                toast = new ToastView(WXEntryActivity.this, "分享成功");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                toast = new ToastView(WXEntryActivity.this, "取消分享");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                toast = new ToastView(WXEntryActivity.this, "分享失败" + "Error Message: " + baseResp.errMsg);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
        }
        finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
    }


    /**
     * 微信返回状态
     */
    @Override
    public void onResp(BaseResp baseResp) {
        ToastView toast;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                toast = new ToastView(WXEntryActivity.this, "发送成功");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                toast = new ToastView(WXEntryActivity.this, "取消发送");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                toast = new ToastView(WXEntryActivity.this, "发送被拒绝");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            default:
                toast = new ToastView(WXEntryActivity.this, "发送返回");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
        }
        finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    /**
     * 分享到微信
     *
     * @param title      标题
     * @param urlContent 链接
     */
    public void shareToWeiXin(String title, String urlContent) {
        if (weixinAPI.isWXAppInstalled()) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = urlContent;
            WXMediaMessage msg = new WXMediaMessage(webpage);

            msg.title = title;

            msg.description = title;

            if (null != shareImage) {
                msg.setThumbImage(shareImage);
            } else {
                msg.setThumbImage(detaultShareImage);
            }

            msg.description = title;

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;

            weixinAPI.sendReq(req);
        } else {
            Toast.makeText(this, "未安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    public void shareToFriendLine(String title, String urlContent) {
        if (weixinAPI.isWXAppInstalled()) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = urlContent;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = getString(R.string.share_title);

            msg.description = getString(R.string.share_title);
            if (null != shareImage) {
                msg.setThumbImage(shareImage);
            } else {
                msg.setThumbImage(detaultShareImage);
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneTimeline;

            weixinAPI.sendReq(req);
        } else {
            Toast.makeText(this, "未安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            return false;
        }
        return true;
    }

    /**
     * 用异步方式启动分享
     *
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        final Activity activity = WXEntryActivity.this;
        // TODO Auto-generated method stub
        mTencent.shareToQzone(activity, params, new IUiListener() {

            @Override
            public void onCancel() {
                ToastView toast = new ToastView(WXEntryActivity.this, "发送取消");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onError(UiError e) {
                ToastView toast = new ToastView(WXEntryActivity.this, "发送失败");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onComplete(Object response) {
                // TODO Auto-generated method stub
                ToastView toast = new ToastView(WXEntryActivity.this, "发送成功");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        });
    }


}
