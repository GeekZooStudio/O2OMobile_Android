package com.insthub.O2OMobile.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;

import com.BeeFramework.view.ToastView;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.Fragment.A0_HomeFragment;
import com.insthub.O2OMobile.Fragment.A2_MenuFragment;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Fragment.RightFragment;
import com.insthub.O2OMobile.Fragment.SlidingMenu;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.R;

public class SlidingActivity extends FragmentActivity {
	
	public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_PUSHCLICK = "bccsclient.action.PUSHCLICK";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";
    public static final String CUSTOM_CONTENT ="CustomContent";

    public static final String ACTION_SHOW_MESSAGE_LIST = "ACTION_SHOW_MESSAGE_LIST";
    
	
	private SlidingMenu mSlidingMenu;
	private A2_MenuFragment mA2MenuFragment;
	private RightFragment mRightFragment;
	private A0_HomeFragment mA0HomeFragment;
	
	private Fragment mFragment;
	
	private     SharedPreferences mShared;
    private     SharedPreferences.Editor mEditor;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
        handleIntent(getIntent());
		
		init();
		
		mShared = this.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        mEditor = mShared.edit();
        EventBus.getDefault().register(this);
	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		mA2MenuFragment = new A2_MenuFragment();
		t.replace(R.id.left_frame, mA2MenuFragment);

		mRightFragment = new RightFragment();
		t.replace(R.id.right_frame, mRightFragment);

		mA0HomeFragment = new A0_HomeFragment();
		t.replace(R.id.center_frame, mA0HomeFragment);
		t.commit();
		
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}
	
	public void switchContent(Fragment fragment) {
		
		this.mFragment = fragment;
		
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		
		t.replace(R.id.center_frame, this.mFragment);
		t.commit();
		
		showLeft();
		
    }
	
	public void isShowLeft(boolean Left) {
		mSlidingMenu.setCanSliding(Left, false);
	}
	
	// 退出操作
	private boolean isExit = false;

	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mSlidingMenu.mScroller.getCurrX() != 0) {
				showLeft();
				return true;
			} else {
				if (isExit == false) {
					isExit = true;
					//Toast.makeText(getApplicationContext(), "再按一次退出娜娜日记",Toast.LENGTH_SHORT).show();
					ToastView toast = new ToastView(getApplicationContext(), getString(R.string.exit_again));
		            toast.setGravity(Gravity.CENTER, 0, 0);
		            toast.show();
					handler.sendEmptyMessageDelayed(0, 3000);
					return true;
				} else {
					finish();
					return false;
				}
			}
		}
		return true;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};
	
	
	@Override
    protected void onStart() {
        super.onStart();
      //Todo
    }
	
	@Override
	protected void onNewIntent(Intent intent) {
		// 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
	//Todo


	}
	
	private void handleIntent(Intent intent) {
        //Todo
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    public void onEvent(Object event)
    {
        Message message = (Message)event;
        if(message.what == MessageConstant.LOGINOUT)
        {
            finish();
        }
    }
}
