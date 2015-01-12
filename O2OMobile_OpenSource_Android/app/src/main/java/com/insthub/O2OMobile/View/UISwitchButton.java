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

package com.insthub.O2OMobile.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.insthub.O2OMobile.R;

public class UISwitchButton extends CheckBox {
	private Paint mPaint;
	private RectF mSaveLayerRectF;
	private float mFirstDownY;
	private float mFirstDownX;
	private int mClickTimeout;
	private int mTouchSlop;
	private final int MAX_ALPHA = 255;
	private int mAlpha = MAX_ALPHA;
	private boolean mChecked = true; 
	private boolean mBroadcasting;// 标示是否正在执行监听事件中
	private boolean mTurningOn;// 标示位置是否达到开启状态
	private PerformClick mPerformClick;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
	private boolean mAnimating;// 标示是否继续执行移动动画
	private final float VELOCITY = 350;// 定义按钮动画移动的最大长度
	private float mVelocity;// 按钮动画移动的最大像素长度
	private float mAnimationPosition;// 按钮动画移动的当前位置
	private float mAnimatedVelocity;// 按钮动画移动的实际位移(+mVelocity/-mVelocity)
	private Bitmap mBmBgGreen;// 绿色背景
	private Bitmap mBmBgWhite;// 白色背景
	private Bitmap mBmBtnNormal;// 未按下时按钮
	private Bitmap mBmBtnPressed;// 按下时按钮
	private Bitmap mBmCurBtnPic;// 当前显示的按钮图片
	private Bitmap mBmCurBgPic;// 当前背景图片
	private float mBgWidth;// 背景宽度
	private float mBgHeight;// 背景宽度
	private float mBtnWidth;// 按钮宽度
	private float mOffBtnPos;// 按钮关闭时位置
	private float mOnBtnPos;// 按钮开启时位置
	private float mCurBtnPos;// 按钮当前位置
	private float mStartBtnPos;// 开始按钮位置
	private int COMMON_WIDTH_IN_PIXEL = 82;// 默认宽度
	private int COMMON_HEIGHT_IN_PIXEL = 50;// 默认高度
	
	private int mScreenWidth;

	public UISwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.checkboxStyle);
	}

	public UISwitchButton(Context context) {
		this(context, null);
	}

	public UISwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		Resources resources = context.getResources();

		WindowManager windowManager = ((Activity) context).getWindow()
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		
		COMMON_WIDTH_IN_PIXEL = mScreenWidth / 8;
		COMMON_HEIGHT_IN_PIXEL = mScreenWidth / 14;
		
		// get attrConfiguration
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.SwitchButton);
		int width = (int) array.getDimensionPixelSize(
				R.styleable.SwitchButton_bmWidth, 0);
		int height = (int) array.getDimensionPixelSize(
				R.styleable.SwitchButton_bmHeight, 0);
		array.recycle();

		// size width or height
		if (width <= 0 || height <= 0) {
			width = COMMON_WIDTH_IN_PIXEL;
			height = COMMON_HEIGHT_IN_PIXEL;
		} else {
			float scale = (float) COMMON_WIDTH_IN_PIXEL
					/ COMMON_HEIGHT_IN_PIXEL;
			if ((float) width / height > scale) {
				width = (int) (height * scale);
			} else if ((float) width / height < scale) {
				height = (int) (width / scale);
			}
		}

		// get viewConfiguration
		mClickTimeout = ViewConfiguration.getPressedStateDuration()
				+ ViewConfiguration.getTapTimeout();
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		// get Bitmap
		mBmBgGreen = BitmapFactory.decodeResource(resources,
                R.drawable.switch_btn_bg_blue);
		mBmBgWhite = BitmapFactory.decodeResource(resources,
                R.drawable.switch_btn_bg_white);
		mBmBtnNormal = BitmapFactory.decodeResource(resources,
                R.drawable.switch_btn_normal);
		mBmBtnPressed = BitmapFactory.decodeResource(resources,
                R.drawable.switch_btn_pressed);

		// size Bitmap
		mBmBgGreen = Bitmap.createScaledBitmap(mBmBgGreen, width, height, true);
		mBmBgWhite = Bitmap.createScaledBitmap(mBmBgWhite, width, height, true);
		mBmBtnNormal = Bitmap.createScaledBitmap(mBmBtnNormal, height, height,
                true);
		mBmBtnPressed = Bitmap.createScaledBitmap(mBmBtnPressed, height, height,
                true);

		mBmCurBtnPic = mBmBtnNormal;// 初始按钮图片
		mBmCurBgPic = mChecked ? mBmBgGreen : mBmBgWhite;// 初始背景图片
		mBgWidth = mBmBgGreen.getWidth();// 背景宽度
		mBgHeight = mBmBgGreen.getHeight();// 背景高度
		mBtnWidth = mBmBtnNormal.getWidth();// 按钮宽度
		mOffBtnPos = 0;// 关闭时在最左边
		mOnBtnPos = mBgWidth - mBtnWidth;// 开始时在右边
		mCurBtnPos = mChecked ? mOnBtnPos : mOffBtnPos;// 按钮当前为初始位置

		// get density
		float density = resources.getDisplayMetrics().density;
		mVelocity = (int) (VELOCITY * density + 0.5f);// 动画距离
		mSaveLayerRectF = new RectF(0, 0, mBgWidth, mBgHeight);
	}

	@Override
	public void setEnabled(boolean enabled) {
		mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA / 3;
		super.setEnabled(enabled);
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void toggle() {
		setChecked(!mChecked);
	}

	private void setCheckedDelayed(final boolean checked) {
		postDelayed(new Runnable() {
			@Override
			public void run() {
				setChecked(checked);
			}
		}, 10);
	}

	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;

			// 初始化按钮位置
			mCurBtnPos = checked ? mOnBtnPos : mOffBtnPos;
			// 改变背景图片
			mBmCurBgPic = checked ? mBmBgGreen : mBmBgWhite;
			invalidate();

			if (mBroadcasting) {
				// NO-OP
				return;
			}
			// 正在执行监听事件
			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(UISwitchButton.this,
						mChecked);
			}
			if (mOnCheckedChangeWidgetListener != null) {
				mOnCheckedChangeWidgetListener.onCheckedChanged(
						UISwitchButton.this, mChecked);
			}
			// 监听事件结束
			mBroadcasting = false;
		}
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeWidgetListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		float deltaX = Math.abs(x - mFirstDownX);
		float deltaY = Math.abs(y - mFirstDownY);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			ViewParent mParent = getParent();
			if (mParent != null) {
				// 通知父控件不要拦截本view的触摸事件
				mParent.requestDisallowInterceptTouchEvent(true);
			}
			mFirstDownX = x;
			mFirstDownY = y;
			mBmCurBtnPic = mBmBtnPressed;
			mStartBtnPos = mChecked ? mOnBtnPos : mOffBtnPos;
			break;
		case MotionEvent.ACTION_MOVE:
			float time = event.getEventTime() - event.getDownTime();
			mCurBtnPos = mStartBtnPos + event.getX() - mFirstDownX;
			if (mCurBtnPos >= mOnBtnPos) {
				mCurBtnPos = mOnBtnPos;
			}
			if (mCurBtnPos <= mOffBtnPos) {
				mCurBtnPos = mOffBtnPos;
			}
			mTurningOn = mCurBtnPos > mBgWidth / 2 - mBtnWidth / 2;
			break;
		case MotionEvent.ACTION_UP:
			mBmCurBtnPic = mBmBtnNormal;
			time = event.getEventTime() - event.getDownTime();
			if (deltaY < mTouchSlop && deltaX < mTouchSlop
					&& time < mClickTimeout) {
				if (mPerformClick == null) {
					mPerformClick = new PerformClick();
				}
				if (!post(mPerformClick)) {
					performClick();
				}
			} else {
				startAnimation(mTurningOn);
			}
			break;
		}
		invalidate();
		return isEnabled();
	}

	private class PerformClick implements Runnable {
		public void run() {
			performClick();
		}
	}

	@Override
	public boolean performClick() {
		startAnimation(!mChecked);
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		// 绘制底部图片
		canvas.drawBitmap(mBmCurBgPic, 0, 0, mPaint);

		// 绘制按钮
		canvas.drawBitmap(mBmCurBtnPic, mCurBtnPos, 0, mPaint);

		canvas.restore();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) mBgWidth, (int) mBgHeight);
	}

	private void startAnimation(boolean turnOn) {
		mAnimating = true;
		mAnimatedVelocity = turnOn ? mVelocity : -mVelocity;
		mAnimationPosition = mCurBtnPos;
		new SwitchAnimation().run();
	}

	private void stopAnimation() {
		mAnimating = false;
	}

	private final class SwitchAnimation implements Runnable {
		@Override
		public void run() {
			if (!mAnimating) {
				return;
			}
			doAnimation();
			requestAnimationFrame(this);
		}
	}

	private void doAnimation() {
		mAnimationPosition += mAnimatedVelocity * ANIMATION_FRAME_DURATION
				/ 1000;
		if (mAnimationPosition <= mOffBtnPos) {
			stopAnimation();
			mAnimationPosition = mOffBtnPos;
			setCheckedDelayed(false);
		} else if (mAnimationPosition >= mOnBtnPos) {
			stopAnimation();
			mAnimationPosition = mOnBtnPos;
			setCheckedDelayed(true);
		}
		mCurBtnPos = mAnimationPosition;
		invalidate();
	}

	private static final int MSG_ANIMATE = 1000;
	public static final int ANIMATION_FRAME_DURATION = 1000 / 60;

	public void requestAnimationFrame(Runnable runnable) {
		Message message = new Message();
		message.what = MSG_ANIMATE;
		message.obj = runnable;
		mHandler.sendMessageDelayed(message, ANIMATION_FRAME_DURATION);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			switch (m.what) {
			case MSG_ANIMATE:
				if (m.obj != null) {
					((Runnable) m.obj).run();
				}
				break;
			}
		}
	};
}
