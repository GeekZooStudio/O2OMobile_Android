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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.ImageUtil;
import com.BeeFramework.Utils.Utils;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.RoundedWebImageView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.Adapter.F0_ProfileServiceListAdapter;
import com.insthub.O2OMobile.Adapter.F0_ProfileServiceListGridAdapter;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.UserBalanceModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_USER_GROUP;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.Protocol.USER;
import com.insthub.O2OMobile.Protocol.userprofileResponse;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.View.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;


public class F0_ProfileActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {
    public static final String USER_ID = "user_id";
    private int mUserId;
    private ImageView mBack;
    private ImageView mSetting;
    private ListView mListview;
    private View mHeaderView;
    private RoundedWebImageView mAvatar;
    private TextView mName;
    private TextView mSignature;
    private TextView mBalance;
    private UserBalanceModel mUserBalance;
    private USER mUser;
    private TextView mBrief;
    private TextView mBrief_detail;
//    private RelativeLayout mMyCash;
    private RelativeLayout mComment;
    private TextView mCommentCount;
    private TextView mCommentGoodrate;
    private MyGridView mGridviewServiceList;
    private Button mHelp;
    private LinearLayout mGridview;
    private LinearLayout mBirefLayout;
    private SharedPreferences mShared;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    private SERVICE_TYPE mServiceType;
    private TextView mComplain;
    private ImageView mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f0_profile);
        mUserId = getIntent().getIntExtra(USER_ID, 0);
        mServiceType = (SERVICE_TYPE) getIntent().getSerializableExtra(O2OMobileAppConst.SERVICE_TYPE);
        mListview = (ListView) findViewById(R.id.profile_listview);
        mHelp = (Button) findViewById(R.id.btn_help);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mHeaderView = layoutInflater.inflate(R.layout.f0_profile_header, null);
        mBack = (ImageView) mHeaderView.findViewById(R.id.top_view_back);
        mSetting = (ImageView) mHeaderView.findViewById(R.id.top_view_setting);
        mAvatar = (RoundedWebImageView) mHeaderView.findViewById(R.id.iv_avarta);
        mName = (TextView) mHeaderView.findViewById(R.id.tv_name);
        mBalance = (TextView) mHeaderView.findViewById(R.id.tv_balance);
        mSignature = (TextView) mHeaderView.findViewById(R.id.tv_signature);
        mBrief = (TextView) mHeaderView.findViewById(R.id.tv_brief);
        mBrief_detail = (TextView) mHeaderView.findViewById(R.id.brief_detail);
//        mMyCash = (RelativeLayout) mHeaderView.findViewById(R.id.my_cash);
        mComment = (RelativeLayout) mHeaderView.findViewById(R.id.comment);
        mCommentCount = (TextView) mHeaderView.findViewById(R.id.tv_comment_count);
        mCommentGoodrate = (TextView) mHeaderView.findViewById(R.id.tv_comment_goodrate);
        mComplain = (TextView) mHeaderView.findViewById(R.id.complain);
        mGridviewServiceList = (MyGridView) mHeaderView.findViewById(R.id.grid_view_service_list);
        mGridview = (LinearLayout) mHeaderView.findViewById(R.id.gridview);
        mBirefLayout = (LinearLayout) mHeaderView.findViewById(R.id.ll_brief);
        mRefresh = (ImageView) mHeaderView.findViewById(R.id.refresh);
        mGridviewServiceList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListview.addHeaderView(mHeaderView);
        mListview.setAdapter(null);
        mUserBalance = new UserBalanceModel(this);
        mUserBalance.addResponseListener(this);
        mUserBalance.getProfile(mUserId);
        mBack.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mBrief_detail.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mComplain.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mRefresh.setOnClickListener(this);
        if (mUserId != SESSION.getInstance().uid) {
            mSetting.setVisibility(View.GONE);
            mComplain.setVisibility(View.VISIBLE);
        } else {
            mUserBalance.get();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        if (mUserId == SESSION.getInstance().uid) {
            mUserBalance.get();
            mShared = getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
            String userStr = mShared.getString("user", "");
            try {
                if (userStr != null) {
                    JSONObject userJson = new JSONObject(userStr);
                    mUser = new USER();
                    mUser.fromJson(userJson);
                    mImageLoader.displayImage(mUser.avatar.thumb, mAvatar, O2OMobile.options_head);
                    if (!mUser.nickname.equals("") && mUser.nickname != null) {
                        mName.setText(mUser.nickname);
                    }
                    mCommentCount.setText(mUser.comment_count + "");
                    if (!mUser.comment_goodrate.equals("") && mUser.comment_goodrate != null) {
                        mCommentGoodrate.setText((int) (Double.parseDouble(mUser.comment_goodrate) * 100) + "%");
                    }
                    if (mUser.signature.equals("")) {
                        mSignature.setText("没有签名");
                    } else {
                        mSignature.setText(mUser.signature);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mUser.user_group == ENUM_USER_GROUP.FREEMAN.value()) {
                if (mUser.brief.equals("")) {
                    mBirefLayout.setVisibility(View.GONE);
                } else {
                    mBirefLayout.setVisibility(View.VISIBLE);
                    mBrief.setText("" + mUser.brief);
                }
            }
        }


        super.onResume();
    }

    private JSONObject myServiceListJo;
    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if (url.endsWith(ApiInterface.USER_BALANCE)) {
            mBalance.setVisibility(View.VISIBLE);
            mBalance.setText("我的余额：" + Utils.formatBalance(mUserBalance.publicBalance) + "元");
        } else if (url.endsWith(ApiInterface.MYSERVICE_LIST)) {
        	this.myServiceListJo = jo;
            if (mUserBalance.publicMyServiceList.size() > 0) {
                F0_ProfileServiceListAdapter listAdapteradapter = new F0_ProfileServiceListAdapter(this, mUserBalance.publicMyServiceList);
                mListview.setAdapter(listAdapteradapter);
                View foot = new View(this);
                foot.setLayoutParams(new AbsListView.LayoutParams(mListview.getMeasuredWidth(), ImageUtil.Dp2Px(this, 48)));
                mListview.addFooterView(foot);
            }
        } else if (url.endsWith(ApiInterface.USER_PROFILE)) {
            userprofileResponse response = new userprofileResponse();
            response.fromJson(jo);
            mUser = response.user;
            mImageLoader.displayImage(mUser.avatar.thumb, mAvatar, O2OMobile.options_head);
            if (!mUser.nickname.equals("") && mUser.nickname != null) {
                mName.setText(mUser.nickname);
            }
            mCommentCount.setText(mUser.comment_count + "");
            if (!mUser.comment_goodrate.equals("") && mUser.comment_goodrate != null) {
                mCommentGoodrate.setText((int) (Double.parseDouble(mUser.comment_goodrate) * 100) + "%");
            }
            if (mUser.signature.equals("")) {
                mSignature.setText("没有签名");
            } else {
                mSignature.setText(mUser.signature);
            }
            if (mUser.id == SESSION.getInstance().uid) {
                if (mUser.user_group == ENUM_USER_GROUP.FREEMAN.value()) {
                    mUserBalance.getServiceList(mUser.id);
                    if (mUser.brief.equals("")) {
                        mBirefLayout.setVisibility(View.GONE);
                    } else {
                        mBirefLayout.setVisibility(View.VISIBLE);
                        mBrief.setText(mUser.brief);
                    }
                }
            } else {
                if (mUser.brief.equals("")) {
                    mBirefLayout.setVisibility(View.GONE);
                } else {
                    mBirefLayout.setVisibility(View.VISIBLE);
                    mBrief.setText(mUser.brief);
                }
                mUserBalance.getServiceList(mUserId);
                mHelp.setVisibility(View.VISIBLE);
            }
            if (mUser.my_certification.size() == 0) {
                mGridview.setVisibility(View.GONE);
            } else {
                mGridview.setVisibility(View.VISIBLE);
                F0_ProfileServiceListGridAdapter listAdapter = new F0_ProfileServiceListGridAdapter(this, mUser.my_certification);
                mGridviewServiceList.setAdapter(listAdapter);

            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.top_view_setting:
                intent = new Intent(F0_ProfileActivity.this, F9_SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.brief_detail:
                intent = new Intent(F0_ProfileActivity.this, ProfileBriefDetailActivity.class);
                intent.putExtra(ProfileBriefDetailActivity.BRIEF, mBrief.getText().toString());
                startActivity(intent);
                break;
//Todo
            case R.id.btn_help:
                intent = new Intent(F0_ProfileActivity.this, C1_PublishOrderActivity.class);
                intent.putExtra(C1_PublishOrderActivity.DEFAULT_RECEIVER_ID, mUserId);
                if(myServiceListJo!=null&&!"".equals(myServiceListJo)){
                    intent.putExtra("service_list", myServiceListJo.toString());
                }
                intent.putExtra(O2OMobileAppConst.SERVICE_TYPE, mServiceType);
                startActivity(intent);
                break;
            case R.id.comment: {
                intent = new Intent(F0_ProfileActivity.this, F8_ReviewActivity.class);
                intent.putExtra(O2OMobileAppConst.USERID, mUserId);
                startActivity(intent);
                break;

            }
            case R.id.complain: {
                intent = new Intent(F0_ProfileActivity.this, G0_ReportActivity.class);
                intent.putExtra("userId", mUserId);
                startActivity(intent);
                break;

            }
            case R.id.iv_avarta: {
                intent = new Intent(F0_ProfileActivity.this, AvatarDetailActivity.class);
                intent.putExtra(AvatarDetailActivity.AVATAR_URL, mUser.avatar.large);
                startActivity(intent);
                break;

            }
            case R.id.refresh:{
                if(mUserId ==SESSION.getInstance().uid){
                    mUserBalance.get();
                }
                mUserBalance.getProfile(mUserId);
                break;

            }
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Object event) {
        SharedPreferences shared = getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        Message message = (Message) event;
        if (message.what == MessageConstant.LOGINOUT) {
            finish();
        }

        if (message.what == MessageConstant.Change_Seivice) {
            mUserBalance.getServiceList(mUser.id);
        }


    }
}
