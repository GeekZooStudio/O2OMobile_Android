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

package com.insthub.O2OMobile.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.ImageUtil;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.activity.WebViewActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.MyDialog;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.UserBalanceModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_USER_GROUP;
import com.insthub.O2OMobile.Protocol.USER;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class F9_SettingActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {
    private TextView mTitle;
    private ImageView mBack;
    private Button mLogout;
    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;
    private RelativeLayout mChangeNickname;
    private RelativeLayout mChangeAvartar;
    private RelativeLayout mChangeSinagture;
    private RelativeLayout mChangeBrief;
    private RelativeLayout mMyService;
    private RelativeLayout mChangePassword;
    private RelativeLayout mFeedBack;
    private RelativeLayout mAbout;
    private Dialog mDialog;
    private File mFileDir;
    private File mFile;
    private String mFileName = "";
    private final int REQUEST_CAMERA = 1;
    private final int REQUEST_PHOTO = 2;
    private final int REQUEST_PHOTOZOOM = 3;
    private File mFileZoomDir;
    private String mImagePath;
    private UserBalanceModel mUserBalance;
    private USER mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f9_setting);
        mShared = getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        mEditor = mShared.edit();
        mTitle = (TextView) findViewById(R.id.top_view_title);
        mTitle.setText(getString(R.string.setting));
        mBack = (ImageView) findViewById(R.id.top_view_back);
        mLogout = (Button) findViewById(R.id.btn_logout);
       //Todo
        mChangeNickname = (RelativeLayout) findViewById(R.id.change_nickname);
        mChangeNickname.setOnClickListener(this);
        mChangeAvartar = (RelativeLayout) findViewById(R.id.change_avartar);
        mChangeAvartar.setOnClickListener(this);
        mChangeSinagture = (RelativeLayout) findViewById(R.id.change_sinagture);
        mChangeSinagture.setOnClickListener(this);
        mChangeBrief = (RelativeLayout) findViewById(R.id.change_brief);
        mChangeBrief.setOnClickListener(this);
        mMyService = (RelativeLayout) findViewById(R.id.my_sevice);
        mMyService.setOnClickListener(this);
        String userStr = mShared.getString("user", "");
        try {
            if (userStr != null) {
                JSONObject userJson = new JSONObject(userStr);
                mUser = new USER();
                mUser.fromJson(userJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mUser.user_group == ENUM_USER_GROUP.FREEMAN.value()) {
            mMyService.setVisibility(View.VISIBLE);
        }
        mChangePassword = (RelativeLayout) findViewById(R.id.change_password);
        mChangePassword.setOnClickListener(this);
        mAbout = (RelativeLayout) findViewById(R.id.about);
        mAbout.setOnClickListener(this);
        mFeedBack = (RelativeLayout) findViewById(R.id.feedback);
        mFeedBack.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mUserBalance = new UserBalanceModel(this);
        mUserBalance.addResponseListener(this);
      //Todo
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if (url.endsWith(ApiInterface.USER_CHANGE_AVATAR)) {
            ToastView toast = new ToastView(this, getString(R.string.change_avatar_success));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if (url.endsWith(ApiInterface.PUSH_SWITCH)) {
          //Todo
        }
        else if (url.endsWith(ApiInterface.USER_SIGNOUT))
        {
            mEditor.putBoolean("isLogin", false);
            mEditor.putString("user", "");
            mEditor.putInt("uid", 0);
            mEditor.putString("sid", "");
            mEditor.commit();
            SESSION.getInstance().uid = mShared.getInt("uid", 0);
            SESSION.getInstance().sid = mShared.getString("sid", "");
            ToastView toast = new ToastView(F9_SettingActivity.this, getString(R.string.logout_success));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Message msg = new Message();
            msg.what = MessageConstant.LOGINOUT;
            EventBus.getDefault().post(msg);
            Intent intent = new Intent(F9_SettingActivity.this, B0_SigninActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.btn_logout:

                final MyDialog myDialog = new MyDialog(F9_SettingActivity.this, "是否注销该账号?");
                myDialog.show();
                myDialog.positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mUserBalance.signout();
                        myDialog.dismiss();
                    }
                });
                myDialog.negative.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        myDialog.dismiss();
                    }

                });
                break;
            case R.id.change_nickname:
                intent = new Intent(F9_SettingActivity.this, C3_EditNameActivity.class);
                startActivity(intent);
                break;
            case R.id.change_sinagture:
                intent = new Intent(F9_SettingActivity.this, C12_EditSignatureActivity.class);
                startActivity(intent);
                break;
            case R.id.change_brief:
                intent = new Intent(F9_SettingActivity.this, C4_EditIntroActivity.class);
                startActivity(intent);
                break;
            case R.id.my_sevice:
                intent = new Intent(F9_SettingActivity.this, C14_MyServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.change_password:
                intent = new Intent(F9_SettingActivity.this, C13_EditPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(F9_SettingActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.WEBURL, "http://www.o2omobile.com.cn");
                startActivity(intent);
                break;
            case R.id.feedback:
                intent = new Intent(F9_SettingActivity.this, C16_FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.change_avartar:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.photo_dialog, null);
        mDialog = new Dialog(this, R.style.dialog);
        mDialog.setContentView(view);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        LinearLayout requsetCameraLayout = (LinearLayout) view.findViewById(R.id.register_camera);
        LinearLayout requestPhotoLayout = (LinearLayout) view.findViewById(R.id.register_photo);

        requsetCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
                if (mFileDir == null) {
                    mFileDir = new File(O2OMobileAppConst.FILEPATH + "img/");
                    if (!mFileDir.exists()) {
                        mFileDir.mkdirs();
                    }
                }
                mFileName = O2OMobileAppConst.FILEPATH + "img/" + "temp.jpg";
                mFile = new File(mFileName);
                Uri imageuri = Uri.fromFile(mFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                intent.putExtra("return-data", false);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        requestPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, REQUEST_PHOTO);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File files = new File(mFileName);
                if (files.exists()) {
                    mImagePath = mFileName;
                    mImagePath = startPhotoZoom(Uri.fromFile(new File(mImagePath)));
                }
            } else if (requestCode == REQUEST_PHOTO) {
                Uri selectedImage = data.getData();
                mImagePath = startPhotoZoom(selectedImage);
            } else if (requestCode == REQUEST_PHOTOZOOM) {
                File f = new File(mImagePath);
                if (f.exists()) {
                    File file = new File(ImageUtil.zoomImage(mImagePath, 350));
                    mUserBalance.changeAvatar(file);
                } else {
                    ToastView toast = new ToastView(this, getString(R.string.photo_not_exsit));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    }

    private String startPhotoZoom(Uri uri) {

        if (mFileZoomDir == null) {
            mFileZoomDir = new File(O2OMobileAppConst.FILEPATH + "img/");
            if (!mFileZoomDir.exists()) {
                mFileZoomDir.mkdirs();
            }
        }

        String fileName;
        fileName = "/temp.jpg";

        String filePath = mFileZoomDir + fileName;
        File loadingFile = new File(filePath);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 400);
        intent.putExtra("aspectY", 400);
        intent.putExtra("output", Uri.fromFile(loadingFile));// 输出到文件
        intent.putExtra("outputFormat", "PNG");// 返回格式
        intent.putExtra("noFaceDetection", true); // 去除面部检测
        intent.putExtra("return-data", false); // 不要通过Intent传递截获的图片
        startActivityForResult(intent, REQUEST_PHOTOZOOM);

        return filePath;

    }
}
