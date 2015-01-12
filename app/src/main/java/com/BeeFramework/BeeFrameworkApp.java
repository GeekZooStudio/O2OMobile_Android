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

package com.BeeFramework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.BeeFramework.Utils.CustomExceptionHandler;
import com.BeeFramework.activity.DebugCancelDialogActivity;
import com.BeeFramework.activity.DebugTabActivity;
import com.external.activeandroid.app.Application;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

public class BeeFrameworkApp extends Application implements OnClickListener{
    private static BeeFrameworkApp instance;
    private ImageView bugImage;
    public Context currContext;

    private WindowManager wManager ;
    private boolean flag = true;
    
    public Handler messageHandler;

    public static BeeFrameworkApp getInstance()
    {
        if (instance == null) {
            instance = new BeeFrameworkApp();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        //Todo
        initConfig();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConst.LOG_DIR_PATH;
        File storePath = new File(path);
        storePath.mkdirs();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
                path, null));
        
        initImageLoader(this);

    }
    
    void initConfig() {
        SharedPreferences shared;
        shared = this.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        SESSION.getInstance().uid = shared.getInt("uid", 0);
        SESSION.getInstance().sid = shared.getString("sid", "");
        
    }
    
    public void showBug(final Context context)
    {
        BeeFrameworkApp.getInstance().currContext = context;
        wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutParams wmParams = new LayoutParams();
        wmParams.type = LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        if(bugImage != null) { //判断bugImage是否存在，如果存在则移除，必须加在 new ImageView(context) 前面
        	wManager.removeView(bugImage);
        }
        
        bugImage = new ImageView(context);
        bugImage.setImageResource(R.drawable.bug);
        wManager.addView(bugImage, wmParams);
        bugImage.setOnClickListener(this);
        
        bugImage.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				DebugCancelDialogActivity.parentHandler = messageHandler;
				Intent intent = new Intent(BeeFrameworkApp.getInstance().currContext,DebugCancelDialogActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				flag = false;
				return false;
			}
		});
        
        messageHandler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                	wManager.removeView(bugImage);
                	bugImage = null; // 必须要把bugImage清空，否则再次进入debug模式会与102行冲突
                }
            }
        };
    }

    public void onClick(View v) {
    	if(flag != false) {
    		Intent intent = new Intent(BeeFrameworkApp.getInstance().currContext,DebugTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    	}
        flag = true;
    }
    
    public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(2)
				.threadPoolSize(2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.wri zteDebugLogs() // Remove for release app
				.memoryCache(new WeakMemoryCache())
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}


}
