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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.view.CircleProgress;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class C2_PublishOrderDistributeActivity extends BaseActivity{

    private CircleProgress mCircleProgress;
    private TextView mNotifyCountText;
    private int mMaxNotifyCount;
    private Handler handler;
    private float mNotifyCountFloat = 0;
    private float mAvrageAddCount;
    private Timer mTimer;
    private ImageView mBackButton;
    private TextView mFinishButton;
    private ORDER_INFO mOrderInfo;
    private TextView mTopViewTitle;
    private TextView mNotifyTip;
    public final int MAX_PUBLISH_TIME = 5 * 60;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c2_publish_order_distribute);
        mTopViewTitle = (TextView)findViewById(R.id.top_view_title);
        mTopViewTitle.setText(getString(R.string.finding));
        mOrderInfo = (ORDER_INFO)getIntent().getSerializableExtra(O2OMobileAppConst.ORDERINFO);
        mCircleProgress =  (CircleProgress)findViewById(R.id.publish_progress);
        mCircleProgress.startCartoom(MAX_PUBLISH_TIME);

        mNotifyCountText = (TextView)findViewById(R.id.c4_notify_count);
        mNotifyTip = (TextView)findViewById(R.id.c4_notify);

        mBackButton = (ImageView)findViewById(R.id.top_view_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMaxNotifyCount = mOrderInfo.push_number;
        mAvrageAddCount = (float) (mMaxNotifyCount *1.0/MAX_PUBLISH_TIME);
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch(msg.what) {
                    case 1:
                        if (mNotifyCountFloat < mMaxNotifyCount)
                        {
                            double randAdd = Math.random()*1+0.5;
                            mNotifyCountFloat = (float) (mNotifyCountFloat + mAvrageAddCount *randAdd);
                            mNotifyCountText.setText((int) mNotifyCountFloat + "");

                        }
                        else
                        {
                            mTimer.cancel();
                            mTopViewTitle.setText(getString(R.string.find_end));
                            mNotifyTip.setText(getString(R.string.notification_end));
                        }
                        break;
                }
            }

        };

        mTimer = new Timer();

        int duration = 1000;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }

        }, 500, duration);

        mFinishButton = (TextView)findViewById(R.id.c4_finish_button);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(C2_PublishOrderDistributeActivity.this, D1_OrderActivity.class);
                intent.putExtra(D1_OrderActivity.ORDER_ID, mOrderInfo.id);
                C2_PublishOrderDistributeActivity.this.startActivity(intent);
                ((Activity) C2_PublishOrderDistributeActivity.this).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        });

        EventBus.getDefault().register(this);

    }


    /**
     * 产生0～max的随机整数 包括0 不包括max
     *
     * @param max
     *            随机数的上限
     * @return
     */
    public static int getRandom(int max) {
        return new Random().nextInt(max);
    }

    /**
     * 产生 min～max的随机整数 包括 min 但不包括 max
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        int r = getRandom(max - min);
        return r + min;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(Object event)
    {
        Message message = (Message)event;
        if(message.what == MessageConstant.RECEIVE_ORDER_PUSH)
        {
            finish();
        }
    }
}
