package com.BeeFramework.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.BeeFramework.Utils.ImageUtil;
import com.insthub.O2OMobile.R;

import java.util.Timer;
import java.util.TimerTask;


public class CircleProgress extends View{

	
	 private  static final int DEFAULT_MAX_VALUE = 100;					// 默认进度条最大值
	 private  static final int DEFAULT_PAINT_WIDTH = 6;				// 默认画笔宽度
	 private  static final int DEFAULT_PAINT_COLOR = 0xff39BCED;		// 默认画笔颜色
	 private  static final boolean DEFAULT_FILL_MODE = false;			// 默认填充模式
	 private  static final int DEFAULT_INSIDE_VALUE = 0;				// 默认缩进距离
	
	 public int   mBottomPaintColor = 0xc439BCED;  // 默认圆形的背景
	 public int   mBottomPaintWidth = 3;  // 默认圆形的宽度
	
	 private CircleAttribute mCircleAttribute;			// 圆形进度条基本属性

	 private int mMaxProgress;							// 进度条最大值	 	 																												
	 private int mMainCurProgress;						// 主进度条当前值 
	 private int mSubCurProgress;						// 子进度条当前值 
	
	 private CartoomEngine mCartoomEngine;				// 动画引擎
	
	 private Drawable mBackgroundPicture;				// 背景图
	 
	 private Bitmap processBitmap;

     private TextPaint mPaint;
     private  Context mContext;

	 
	 public CircleProgress(Context context)
	 {
			super(context);		
			defaultParam();
	}

	public CircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
        mContext = context;
		Resources resources = getResources();
		processBitmap=BitmapFactory.decodeResource(resources, R.drawable.d2_circle);
		defaultParam();

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
	        
	    mMaxProgress = array.getInteger(R.styleable.ProgressBar_max, DEFAULT_MAX_VALUE); 				// 获取进度条最大值	
	    
	    boolean bFill = array.getBoolean(R.styleable.ProgressBar_fill, DEFAULT_FILL_MODE);			// 获取填充模式
	    int paintWidth = array.getInt(R.styleable.ProgressBar_Paint_Width, DEFAULT_PAINT_WIDTH);		// 获取画笔宽度	    
        mCircleAttribute.setFill(bFill);
        if (bFill == false)
        {
        	mCircleAttribute.setPaintWidth(paintWidth);
        }
        
        int paintColor = array.getColor(R.styleable.ProgressBar_Paint_Color, DEFAULT_PAINT_COLOR);	 // 获取画笔颜色

        Log.i("", "paintColor = " + Integer.toHexString(paintColor));
        mCircleAttribute.setPaintColor(paintColor);
        
        
	    mCircleAttribute.mSidePaintInterval = array.getInt(R.styleable.ProgressBar_Inside_Interval, DEFAULT_INSIDE_VALUE);// 圆环缩进距离

	    array.recycle(); //一定要调用，否则会有问题


        mPaint = new TextPaint();
        mPaint.setStyle(TextPaint.Style.STROKE);
        mPaint.setTextSize(ImageUtil.Dp2Px(context,12));
        mPaint.setColor(Color.WHITE);
        mPaint.setShadowLayer(1.0F, 1.0F, 1.0F, Color.BLACK);

    }

	/*
	 * 默认参数
	 */
	private void defaultParam()
	{
		mCircleAttribute = new CircleAttribute();
		 
		mCartoomEngine = new CartoomEngine();
		
		mMaxProgress = DEFAULT_MAX_VALUE;								 																												
		mMainCurProgress = 0;						
	    mSubCurProgress = 0;					
		 
	}
	 
	 
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {		// 设置视图大小
		// TODO Auto-generated method stub
	//	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		
		mBackgroundPicture = getBackground();
		if (mBackgroundPicture != null)
		{
			width = mBackgroundPicture.getMinimumWidth();
			height = mBackgroundPicture.getMinimumHeight();
		}
		
		setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(width, heightMeasureSpec));
		
	}
	 
	 
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		mCircleAttribute.autoFix(w, h);

	}
	
	
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		       	
		if (mBackgroundPicture == null)									// 没背景图的话就绘制底色
		{
			canvas.drawArc(mCircleAttribute.mRoundOval, 0, 360, mCircleAttribute.mBRoundPaintsFill, mCircleAttribute.mBottomPaint);
		}
		
		
		float subRate = (float)mSubCurProgress / mMaxProgress;
		float subSweep = 360 * subRate;
		canvas.drawArc(mCircleAttribute.mRoundOval, mCircleAttribute.mDrawPos, subSweep, mCircleAttribute.mBRoundPaintsFill, mCircleAttribute.mSubPaint);
		
		float rate = (float)mMainCurProgress / mMaxProgress;
		float sweep = 360 * rate;
		canvas.drawArc(mCircleAttribute.mRoundOval, mCircleAttribute.mDrawPos, sweep, mCircleAttribute.mBRoundPaintsFill, mCircleAttribute.mMainPaints);

        long seconds = mMainCurProgress*mCartoomEngine.mTimerInterval/1000;

        long days = Math.abs(seconds / (24*60*60));
        long hours = Math.abs((seconds -days*24*60*60) / (60*60));
        long minutes = Math.abs((seconds  -days*24*60*60 - hours*60*60)/ 60);
        seconds = Math.abs(seconds  -days*24*60*60 - hours*60*60 - minutes*60);

        String secondsStr = "";
        if(seconds < 10)
        {
            secondsStr = "0"+seconds;
        }
        else
        {
            secondsStr = ""+seconds;
        }

        String minutesStr = "";
        if (minutes < 10)
        {
            minutesStr = "0"+minutes;
        }
        else
        {
            minutesStr = ""+minutes;
        }

        String timeDesc = ""+minutesStr+":"+secondsStr;

        float x_angle = (float) Math.cos(rate * 2 * 3.14 - 0.5*3.14);
        float y_angle =  (float) Math.sin(rate * 2 * 3.14 - 0.5*3.14);
        float x = x_angle*mCircleAttribute.mRoundOval.height()/2;
        float y = y_angle*mCircleAttribute.mRoundOval.height()/2;
        float center_x =   mCircleAttribute.mRoundOval.centerX();
        float center_y =   mCircleAttribute.mRoundOval.centerY();

        float rect_center_x = x + center_x;
        float rect_center_y = y+center_y;
        float length = mPaint.measureText(timeDesc);
        int height = ImageUtil.Dp2Px(mContext,12);
        int margin = ImageUtil.Dp2Px(mContext,2);

        mPaint.setStyle(TextPaint.Style.FILL);
        mPaint.setColor(mCircleAttribute.mPaintColor);
        canvas.drawCircle(rect_center_x, rect_center_y, length / 2 + margin, mPaint);
        mPaint.setStyle(TextPaint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        //LogUtil.output(timeDesc);
        canvas.drawText(timeDesc,rect_center_x- (int)length/2,rect_center_y+height/2,mPaint);
	
	}
	
	
	/*
	 * 设置主进度值
	 */
	 public synchronized void setMainProgress (int progress)
	 {
	    	mMainCurProgress = progress;
	    	if (mMainCurProgress < 0)
	    	{
	    		mMainCurProgress = 0;
	    	}
	    	
	    	if (mMainCurProgress > mMaxProgress)
	    	{
	    		mMainCurProgress = mMaxProgress;
	    	}

	    	invalidate();
	}
	    
    public synchronized int getMainProgress()
    {
    	return mMainCurProgress;
    }
	   
    /*
     *  设置子进度值
     */
    public synchronized void setSubProgress (int progress)
    {
    	mSubCurProgress = progress;
    	if (mSubCurProgress < 0)
    	{
    		mSubCurProgress = 0;
    	}
    	
    	if (mSubCurProgress > mMaxProgress)
    	{
    		mSubCurProgress = mMaxProgress;
    	}
    	
    	invalidate();
    }
	    
    public synchronized int getSubProgress()
    {
    	return mSubCurProgress;
    }
    
    /*
     * 开启动画
     */
	public  void  startCartoom(int time)
	{
		mCartoomEngine.startCartoom(time);

	}
	
	/*
	 * 结束动画
	 */
	public  void  stopCartoom()
	{
		mCartoomEngine.stopCartoom();
	}
	
	
	
	class CircleAttribute
	{
		 public RectF  	mRoundOval;					// 圆形所在矩形区域
		 public boolean mBRoundPaintsFill;			// 是否填充以填充模式绘制圆形
		 public int  	mSidePaintInterval;			// 圆形向里缩进的距离
		 public int    	mPaintWidth;				// 圆形画笔宽度（填充模式下无视）
		 public int     mPaintColor;				// 画笔颜色  （即主进度条画笔颜色，子进度条画笔颜色为其半透明值） 
		 public int     mDrawPos;					// 绘制圆形的起点（默认为-90度即12点钟方向）
		 
		 public Paint   mMainPaints;				// 主进度条画笔 	    
		 public Paint   mSubPaint;    				// 子进度条画笔 
		 
		 public Paint   mBottomPaint;				// 无背景图时绘制所用画笔
		 
		 
		 
		 public CircleAttribute()
		 {
			mRoundOval = new RectF();
			mBRoundPaintsFill = DEFAULT_FILL_MODE;
			mSidePaintInterval = DEFAULT_INSIDE_VALUE;
			mPaintWidth = 0;
			mPaintColor = DEFAULT_PAINT_COLOR;
			mDrawPos = -90;
			 
			mMainPaints = new Paint();		
			mMainPaints.setAntiAlias(true);
			mMainPaints.setStyle(Paint.Style.FILL);
			mMainPaints.setStrokeWidth(mPaintWidth);
			mMainPaints.setColor(0xffff0000);
		     
			mSubPaint = new Paint();
			mSubPaint.setAntiAlias(true);
			mSubPaint.setStyle(Paint.Style.FILL);
			mSubPaint.setStrokeWidth(mPaintWidth);
			mSubPaint.setColor(0xff00ff00);
			
			
			mBottomPaint = new Paint();
			mBottomPaint.setAntiAlias(true);
			mBottomPaint.setStyle(Paint.Style.FILL);
			mBottomPaint.setStrokeWidth(mBottomPaintWidth);
			mBottomPaint.setColor(mBottomPaintColor);
			
		 }
	
		 
		 /*
		  * 设置画笔宽度
		  */
		 public void setPaintWidth(int width)
		 {
			 mMainPaints.setStrokeWidth(width);
			 mSubPaint.setStrokeWidth(width);
			 mBottomPaint.setStrokeWidth(mBottomPaintWidth);
		 }
		 
		 /*
		  * 设置画笔颜色
		  */
		 public void setPaintColor(int color)
		 {
			 mMainPaints.setColor(color);
		     int color1 = color & 0x00ffffff | 0x66000000;
		     mSubPaint.setColor(color1);	        
		 }
		 
		 /*
		  * 设置填充模式
		  */
		 public void setFill(boolean fill)
		 {
			 mBRoundPaintsFill = fill;
			 if (fill)
			 {
				 mMainPaints.setStyle(Paint.Style.FILL);
				 mSubPaint.setStyle(Paint.Style.FILL);	 
				 mBottomPaint.setStyle(Paint.Style.FILL);	
			 }else{
				 mMainPaints.setStyle(Paint.Style.STROKE);
				 mSubPaint.setStyle(Paint.Style.STROKE);	
				 mBottomPaint.setStyle(Paint.Style.STROKE);	
			 }	
		 }
		 
		/*
		 * 自动修正
		 */
		 public void autoFix(int w, int h)
		 {
			if (mSidePaintInterval != 0)
			{
				mRoundOval.set(mPaintWidth/2 + mSidePaintInterval, mPaintWidth/2 + mSidePaintInterval,
				w - mPaintWidth/2 - mSidePaintInterval, h - mPaintWidth/2 - mSidePaintInterval);	
			}else{

				int sl = getPaddingLeft();
				int sr = getPaddingRight();
				int st = getPaddingTop();
				int sb = getPaddingBottom();
			
						
				mRoundOval.set(sl + mPaintWidth/2, st + mPaintWidth/2, w - sr - mPaintWidth/2, h - sb - mPaintWidth/2);	
			}	
		 }
			
	}
	
	class CartoomEngine
	{
		public Handler mHandler; 
		public boolean mBCartoom;					// 是否正在作动画 
		public Timer   mTimer;						// 用于作动画的TIMER 
		public		 MyTimerTask	mTimerTask;			// 动画任务
		public int 	 mSaveMax;						// 在作动画时会临时改变MAX值，该变量用于保存值以便恢复	 
		public int     mTimerInterval;				// 定时器触发间隔时间(ms)	 
		public float   mCurFloatProcess;			// 作动画时当前进度值 

		private long timeMil;
		
		public CartoomEngine()
		{
			mHandler = new Handler()
			{

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch(msg.what)
					{
						case TIMER_ID:
						{
							if (mBCartoom == false)
							{
								return ;
							}
							
						
							mCurFloatProcess += 1;
							setMainProgress((int) mCurFloatProcess);
							
							long curtimeMil = System.currentTimeMillis();
									
							
							timeMil = curtimeMil;
							
							if (mCurFloatProcess >= mMaxProgress)
							{
								stopCartoom();
							}
						}
						break;
					}
				}
				
			};
			
			mBCartoom = false;
			mTimer = new Timer();
			mSaveMax = 0;
			mTimerInterval = 50;
			mCurFloatProcess = 0;
			
		}
		
		public synchronized void  startCartoom(int time)
		{
			if (time <= 0 || mBCartoom == true)
			{
				return ;
			}
			
			timeMil = 0;
			
			mBCartoom = true;

			setMainProgress(0);
			setSubProgress(0);
			
			mSaveMax = mMaxProgress;
			mMaxProgress = (1000 / mTimerInterval) * time;
			mCurFloatProcess = 0;
		
			
			mTimerTask = new MyTimerTask();
			mTimer.schedule(mTimerTask, mTimerInterval, mTimerInterval);

		}
		
		public synchronized void  stopCartoom()
		{

			if (mBCartoom == false)
			{
				return ;
			}
			
			mBCartoom = false;
			//mMaxProgress = mSave;
			
//			setMainProgress(0);
//			setSubProgress(0);
			
			if (mTimerTask != null)
			{
				mTimerTask.cancel();
				mTimerTask = null;
			}
		}
		
		private final static int TIMER_ID = 0x0010;
		
		class MyTimerTask extends TimerTask{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mHandler.obtainMessage(TIMER_ID);
				msg.sendToTarget();
		
			}
			
		}
		
		
	}
	
	
}
