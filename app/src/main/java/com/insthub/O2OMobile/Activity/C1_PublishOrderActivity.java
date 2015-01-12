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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;

import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.*;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.BeeFramework.Utils.AnimationUtil;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.timepicker.ScreenInfo;
import com.external.timepicker.WheelMain;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Adapter.C1_PublishOrderAdapter;
import com.insthub.O2OMobile.Model.HomeModel;
import com.insthub.O2OMobile.Model.LocationInfoModel;
import com.insthub.O2OMobile.Model.OrderPublishModel;

public class C1_PublishOrderActivity extends BaseActivity implements BusinessResponse {

    private int MAX_TIME = 30;
    private FrameLayout mServiceTypeView;
    private ListView mServiceTypeListview;
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mTitleView;
    private ImageView mArrowImage;
    private ImageView mClose;
    private EditText mPrice;
    private TextView mTime;
    private EditText mLocation;
    private EditText mText;
    private Button mVoice;
    private Button mVoicePlay;
    private ImageView mVoiceReset;
    private Button mPublish;
    private FrameLayout mVoiceView;
    private ImageView mVoiceAnim;
    private WheelMain mWheelMain;
    private SimpleDateFormat mFormat;

    private AnimationDrawable mAnimationDrawable = null;
    private AnimationDrawable mAnimationDrawable2 = null;
    private MediaRecorder mRecorder = null;//录音
    private Timer mTimer;
    private int mMaxTime;
    private String mFileName = null;

    private OrderPublishModel mOrderPublishModel;
    private LocationInfoModel mLocationInfoModel;
    private HomeModel mHomeModel;
    private C1_PublishOrderAdapter mC1PublishOrderAdapter;
    private SERVICE_TYPE mServiceType;
    private int mServiceTypeId = 0;
    private int mDefaultReceiverId = 0;
    private String service_list;
    public static String DEFAULT_RECEIVER_ID = "default_receiver_id";
    private ArrayList<SERVICE_TYPE> mServiceTypeList = new ArrayList<SERVICE_TYPE>();

    private MediaPlayer mPlayer;
    private SharedPreferences mShared;
    private String mHomeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c1_publish_order);
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        File file = new File(newFileName());
        if (file.exists()) {
            file.delete();
        }

        Intent intent = getIntent();
        mServiceType = (SERVICE_TYPE) intent.getSerializableExtra(O2OMobileAppConst.SERVICE_TYPE);
        mDefaultReceiverId = intent.getIntExtra(DEFAULT_RECEIVER_ID, 0);
        service_list = intent.getStringExtra("service_list");

        mBack = (ImageView) findViewById(R.id.top_view_back);
        mTitle = (TextView) findViewById(R.id.top_view_title);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        mArrowImage = (ImageView) findViewById(R.id.top_view_arrow_image);
        mClose = (ImageView) findViewById(R.id.top_view_right_close);
        mTitleView = (LinearLayout) findViewById(R.id.top_view_title_view);
        mServiceTypeView = (FrameLayout) findViewById(R.id.c1_publish_order_service_type_view);
        mServiceTypeListview = (ListView) findViewById(R.id.c1_publish_order_service_type_list);
        mPrice = (EditText) findViewById(R.id.c1_publish_order_price);
        mTime = (TextView) findViewById(R.id.c1_publish_order_time);
        mLocation = (EditText) findViewById(R.id.c1_publish_order_location);
        mText = (EditText) findViewById(R.id.c1_publish_order_text);
        mVoice = (Button) findViewById(R.id.c1_publish_order_voice);
        mVoicePlay = (Button) findViewById(R.id.c1_publish_order_voicePlay);
        mVoiceReset = (ImageView) findViewById(R.id.c1_publish_order_voiceReset);
        mPublish = (Button) findViewById(R.id.c1_publish_order_publish);
        mVoiceView = (FrameLayout) findViewById(R.id.c1_publish_order_voice_view);
        mVoiceAnim = (ImageView) findViewById(R.id.c1_publish_order_voice_anim);
        mVoiceAnim.setImageResource(R.anim.voice_animation);
        mAnimationDrawable = (AnimationDrawable) mVoiceAnim.getDrawable();
        mAnimationDrawable.setOneShot(false);
        mTitleView.setEnabled(false);
        mServiceTypeView.setOnClickListener(null);
        mServiceTypeListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (mDefaultReceiverId == 0) {
                    mTitle.setText(mHomeModel.publicServiceTypeList.get(position).title);
                    mServiceTypeId = mHomeModel.publicServiceTypeList.get(position).id;
                    mC1PublishOrderAdapter = new C1_PublishOrderAdapter(C1_PublishOrderActivity.this, mHomeModel.publicServiceTypeList, position);
                    mServiceTypeListview.setAdapter(mC1PublishOrderAdapter);
                    mClose.setVisibility(View.GONE);
                    mArrowImage.setImageResource(R.drawable.b3_arrow_down);
                    AnimationUtil.backAnimationFromBottom(mServiceTypeListview);
                    Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            mServiceTypeView.setVisibility(View.GONE);
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 200);
                } else {
                    mTitle.setText(mServiceTypeList.get(position).title);
                    mServiceTypeId = mServiceTypeList.get(position).id;
                    mC1PublishOrderAdapter = new C1_PublishOrderAdapter(C1_PublishOrderActivity.this, mServiceTypeList, position);
                    mServiceTypeListview.setAdapter(mC1PublishOrderAdapter);
                    mClose.setVisibility(View.GONE);
                    mArrowImage.setImageResource(R.drawable.b3_arrow_down);
                    AnimationUtil.backAnimationFromBottom(mServiceTypeListview);
                    Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            mServiceTypeView.setVisibility(View.GONE);
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 200);
                }

            }
        });

        mTitleView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (mServiceTypeView.getVisibility() == View.GONE) {
                    mServiceTypeView.setVisibility(View.VISIBLE);
                    mClose.setVisibility(View.VISIBLE);
                    mArrowImage.setImageResource(R.drawable.b4_arrow_up);
                    AnimationUtil.showAnimationFromTop(mServiceTypeListview);
                    closeKeyBoard();
                } else {
                    mClose.setVisibility(View.GONE);
                    mArrowImage.setImageResource(R.drawable.b3_arrow_down);
                    AnimationUtil.backAnimationFromBottom(mServiceTypeListview);
                    Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            mServiceTypeView.setVisibility(View.GONE);
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 200);
                }
            }
        });

        mClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mClose.setVisibility(View.GONE);
                mArrowImage.setImageResource(R.drawable.b3_arrow_down);
                AnimationUtil.backAnimationFromBottom(mServiceTypeListview);
                Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mServiceTypeView.setVisibility(View.GONE);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 200);
            }
        });

        mPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().length() > 0) {
                    if (s.toString().substring(0, 1).equals(".")) {
                        s = s.toString().substring(1, s.length());
                        mPrice.setText(s);
                    }
                }
                if (s.toString().length() > 1) {
                    if (s.toString().substring(0, 1).equals("0")) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            s = s.toString().substring(1, s.length());
                            mPrice.setText(s);
                            CharSequence charSequencePirce = mPrice.getText();
                            if (charSequencePirce instanceof Spannable) {
                                Spannable spanText = (Spannable) charSequencePirce;
                                Selection.setSelection(spanText, charSequencePirce.length());
                            }
                        }
                    }
                }
                boolean flag = false;
                for (int i = 0; i < s.toString().length() - 1; i++) {
                    String getstr = s.toString().substring(i, i + 1);
                    if (getstr.equals(".")) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    int i = s.toString().indexOf(".");
                    if (s.toString().length() - 3 > i) {
                        String getstr = s.toString().substring(0, i + 3);
                        mPrice.setText(getstr);
                        CharSequence charSequencePirce = mPrice.getText();
                        if (charSequencePirce instanceof Spannable) {
                            Spannable spanText = (Spannable) charSequencePirce;
                            Selection.setSelection(spanText, charSequencePirce.length());
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        initData();

        mHomeModel = new HomeModel(this);
        mHomeModel.addResponseListener(this);
        if (mDefaultReceiverId == 0) {
            mShared = getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
            mHomeData = mShared.getString("home_data", "");
            if ("".equals(mHomeData)) {
                mHomeModel.getServiceTypeList();
            } else {
                try {
                    servicetypelistResponse response = new servicetypelistResponse();
                    response.fromJson(new JSONObject(mHomeData));
                    mHomeModel.publicServiceTypeList = response.services;
                    setServiceTypeAdater();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (service_list != null && !"".equals(service_list)) {
                try {
                    JSONObject userJson = new JSONObject(service_list);
                    myservicelistResponse response = new myservicelistResponse();
                    response.fromJson(userJson);
                    for (int i = 0; i < response.services.size(); i++) {
                        SERVICE_TYPE service = new SERVICE_TYPE();
                        service = response.services.get(i).service_type;
                        mServiceTypeList.add(service);
                    }
                    if (mServiceTypeList.size() > 0) {
                        mTitleView.setEnabled(true);
                        mArrowImage.setVisibility(View.VISIBLE);
                        if (mServiceType != null) {
                            for (int i = 0; i < mServiceTypeList.size(); i++) {
                                if (mServiceType.id == mServiceTypeList.get(i).id) {
                                    mC1PublishOrderAdapter = new C1_PublishOrderAdapter(this, mServiceTypeList, i);
                                    mServiceTypeListview.setAdapter(mC1PublishOrderAdapter);
                                    mTitle.setText(mServiceTypeList.get(i).title);
                                    mServiceTypeId = mServiceTypeList.get(i).id;
                                    break;
                                }
                            }
                        } else {
                            mC1PublishOrderAdapter = new C1_PublishOrderAdapter(this, mServiceTypeList);
                            mServiceTypeListview.setAdapter(mC1PublishOrderAdapter);
                            mTitle.setText(getString(R.string.select_service));
                        }

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                mShared = getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
                mHomeData = mShared.getString("home_data", "");
                if ("".equals(mHomeData)) {
                    mHomeModel.getServiceTypeList();
                } else {
                    try {
                        servicetypelistResponse response = new servicetypelistResponse();
                        response.fromJson(new JSONObject(mHomeData));
                        mHomeModel.publicServiceTypeList = response.services;
                        setServiceTypeAdater();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        mLocationInfoModel = new LocationInfoModel(this);
        mLocationInfoModel.addResponseListener(this);
        mLocationInfoModel.get();

        mOrderPublishModel = new OrderPublishModel(this);
        mOrderPublishModel.addResponseListener(this);

        //设置光标靠右
        CharSequence charSequencePirce = mPrice.getText();
        if (charSequencePirce instanceof Spannable) {
            Spannable spanText = (Spannable) charSequencePirce;
            Selection.setSelection(spanText, charSequencePirce.length());
        }
        CharSequence charSequenceLocation = mLocation.getText();
        if (charSequenceLocation instanceof Spannable) {
            Spannable spanText = (Spannable) charSequenceLocation;
            Selection.setSelection(spanText, charSequenceLocation.length());
        }

        mVoice.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    closeKeyBoard();
                    mVoice.setKeepScreenOn(true);
                    mMaxTime = MAX_TIME;
                    mVoiceView.setVisibility(View.VISIBLE);
                    mAnimationDrawable.start();
                    startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mVoice.setKeepScreenOn(false);
                    mVoiceView.setVisibility(View.GONE);
                    mAnimationDrawable.stop();
                    if (mMaxTime > 28) {
                        mVoice.setEnabled(false);
                        Handler mHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                stopRecording();
                                mVoice.setEnabled(true);
                            }
                        };
                        mHandler.sendEmptyMessageDelayed(0, 500);
                    } else {
                        stopRecording();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mVoice.setKeepScreenOn(false);
                    mVoiceView.setVisibility(View.GONE);
                    mAnimationDrawable.stop();
                    if (mMaxTime > 28) {
                        mVoice.setEnabled(false);
                        Handler mHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                stopRecording();
                                mVoice.setEnabled(true);
                            }
                        };
                        mHandler.sendEmptyMessageDelayed(0, 500);
                    } else {
                        stopRecording();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mVoice.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        mVoicePlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer == null) {
                    File file = new File(mFileName);
                    if (file.exists()) {
                        mPlayer = new MediaPlayer();
                        mVoicePlay.setBackgroundResource(R.anim.record_animation);
                        mAnimationDrawable2 = (AnimationDrawable) mVoicePlay.getBackground();
                        mAnimationDrawable2.setOneShot(false);
                        mAnimationDrawable2.start();
                        try {
                            mPlayer.setDataSource(mFileName);
                            mPlayer.prepare();
                            mPlayer.start();
                            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mp.reset();
                                    mPlayer = null;
                                    mVoicePlay.setBackgroundResource(R.drawable.b5_play_btn);
                                    mAnimationDrawable2.stop();
                                }
                            });
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(C1_PublishOrderActivity.this, getString(R.string.file_does_not_exist), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPlayer.release();
                    mPlayer = null;
                    mVoicePlay.setBackgroundResource(R.drawable.b5_play_btn);
                    mAnimationDrawable2.stop();
                }
            }
        });

        mVoiceReset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null) {
                    mPlayer.release();
                    mPlayer = null;
                    mVoicePlay.setBackgroundResource(R.drawable.b5_play_btn);
                    mAnimationDrawable2.stop();
                }
                File file = new File(mFileName);
                if (file.exists()) {
                    file.delete();
                }
                mVoice.setVisibility(View.VISIBLE);
                mVoicePlay.setVisibility(View.GONE);
                mVoiceReset.setVisibility(View.GONE);
            }
        });

        mPublish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                File file = new File(newFileName());
                int duration = 0;
                if (file.exists()) {
                    MediaPlayer mp = MediaPlayer.create(C1_PublishOrderActivity.this, Uri.parse(mFileName));
                    if (null != mp) {
                        duration = mp.getDuration();//即为时长 是ms
                        mp.release();
                    }
                    if (duration % 1000 > 500) {
                        duration = duration / 1000 + 1;
                    } else {
                        duration = duration / 1000;
                    }
                } else {
                    file = null;
                }
                int num = 0;
                try {    // 判断预约时间是否大于当前时间
                    Date date = new Date();
                    Date date1 = mFormat.parse(mFormat.format(date));
                    Date date2 = mFormat.parse(mTime.getText().toString());
                    num = date2.compareTo(date1);

                    if (num < 0) {
                        long diff = date1.getTime() - date2.getTime();
                        long mins = diff / (1000 * 60);
                        if (mins < 3) {
                            num = 1;
                        }
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (mServiceTypeId == 0) {
                    ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.select_service));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mPrice.getText().toString().equals("")) {
                    ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.price_range));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mPrice.getText().toString().equals("0.")) {
                    ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.right_price));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mTime.getText().toString().equals("")) {
                    ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.appoint_time));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (num < 0) {
                    ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.wrong_appoint_time_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mLocation.getText().toString().equals("")) {
                    ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.appoint_location_hint));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    mOrderPublishModel.publish(mPrice.getText().toString(), mTime.getText().toString(), mLocation.getText().toString(), mText.getText().toString(), file, mServiceTypeId, mDefaultReceiverId, duration);
                }
            }
        });
    }

    public void initData() {
        Date date = new Date();
        mTime.setText(mFormat.format(date));
        mTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                closeKeyBoard();
                LayoutInflater inflater = LayoutInflater.from(C1_PublishOrderActivity.this);
                final View timepickerview = inflater.inflate(R.layout.timepicker, null);
                ScreenInfo screenInfo = new ScreenInfo(C1_PublishOrderActivity.this);
                mWheelMain = new WheelMain(timepickerview, true);
                mWheelMain.screenheight = screenInfo.getHeight();
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(mFormat.parse(mTime.getText().toString()));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                mWheelMain.initDateTimePicker(year, month, day, hour, min);
                new AlertDialog.Builder(C1_PublishOrderActivity.this)
                        .setTitle(getString(R.string.choose_time))
                        .setView(timepickerview)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTime.setText(mWheelMain.getTime());
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    // 关闭键盘
    public void closeKeyBoard() {
        mPrice.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPrice.getWindowToken(), 0);
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        mFileName = newFileName();
        //设置输出文件
        try {
            mRecorder = new MediaRecorder();
            //设置音源从麦克风进行录音
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置封装格式（输出格式）
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioSamplingRate(8000);
            mRecorder.setAudioEncodingBitRate(16);
            //设置编码格式
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
        }

        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mMaxTime--;
                Message msg = new Message();
                msg.what = mMaxTime;
                handler.sendMessage(msg);
            }
        };
        mTimer.schedule(timerTask, 1000, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what < 0) {
                stopRecording();
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mTimer = null;
                mVoiceView.setVisibility(View.GONE);
                mAnimationDrawable.stop();
            }
        }
    };

    /**
     * 停止录音
     */
    private void stopRecording() {
        if (mTimer != null) {
            mTimer.cancel(); //取消定时操作
        }
        mTimer = null;
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        mRecorder = null;

        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(mFileName));
        if (null != mp) {
            int duration = mp.getDuration();//即为时长 是ms
            if (duration > 3000) {
                mVoice.setVisibility(View.GONE);
                mVoicePlay.setVisibility(View.VISIBLE);
                mVoiceReset.setVisibility(View.VISIBLE);
            } else {
                ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.record_time_too_short));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                File file = new File(newFileName());
                if (file.exists()) {
                    file.delete();
                }
            }
            mp.release();
        }
        mVoice.setKeepScreenOn(false);
    }

    private String newFileName() {
        String mFileName = O2OMobileAppConst.FILEPATH;
        File fileDir = new File(O2OMobileAppConst.FILEPATH + "media/");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return mFileName += "media/voice.amr";
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        // TODO Auto-generated method stub
        if (url.endsWith(ApiInterface.ORDER_PUBLISH)) {
            orderpublishResponse response = new orderpublishResponse();
            response.fromJson(jo);
            ToastView toast = new ToastView(C1_PublishOrderActivity.this, getString(R.string.public_success));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            File file = new File(newFileName());
            if (file.exists()) {
                file.delete();
            }
            if (mDefaultReceiverId == 0) {
                Intent it = new Intent(this, C2_PublishOrderDistributeActivity.class);
                it.putExtra(O2OMobileAppConst.ORDERINFO, response.order_info);
                startActivity(it);
                finish();
            } else {
                finish();
            }

        } else if (url.endsWith(ApiInterface.LOCATION_INFO)) {
            mLocation.setText(mLocationInfoModel.publicLocationName);
        } else if (url.endsWith(ApiInterface.SERVICETYPE_LIST)) {
            setServiceTypeAdater();
        }
    }

    private void setServiceTypeAdater() {
        if (mHomeModel.publicServiceTypeList.size() > 0) {
            mTitleView.setEnabled(true);
            mArrowImage.setVisibility(View.VISIBLE);

            if (mServiceType == null) {
                mC1PublishOrderAdapter = new C1_PublishOrderAdapter(this, mHomeModel.publicServiceTypeList);
                mServiceTypeListview.setAdapter(mC1PublishOrderAdapter);
                mTitle.setText(getString(R.string.select_service));
            } else {
                for (int i = 0; i < mHomeModel.publicServiceTypeList.size(); i++) {
                    if (mServiceType.id == mHomeModel.publicServiceTypeList.get(i).id) {
                        mC1PublishOrderAdapter = new C1_PublishOrderAdapter(this, mHomeModel.publicServiceTypeList, i);
                        mServiceTypeListview.setAdapter(mC1PublishOrderAdapter);
                        mTitle.setText(mHomeModel.publicServiceTypeList.get(i).title);
                        mServiceTypeId = mHomeModel.publicServiceTypeList.get(i).id;
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        File file = new File(newFileName());
        if (file.exists()) {
            file.delete();
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mVoicePlay.setBackgroundResource(R.drawable.b5_play_btn);
            mAnimationDrawable2.stop();
        }
    }
}
