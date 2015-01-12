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

package com.insthub.O2OMobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;

import com.BeeFramework.BeeFrameworkApp;
import com.insthub.O2OMobile.Utils.LocationManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class O2OMobile extends BeeFrameworkApp {

	public static DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	public static DisplayImageOptions options_head; // DisplayImageOptions是用于设置图片显示的类
	public static DisplayImageOptions options_home; 
	@Override
	public void onCreate() {
		super.onCreate();

		String device_id = O2OMobile.getDeviceId(getApplicationContext());
		SharedPreferences shared;
		SharedPreferences.Editor editor;

		shared = this.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
		editor = shared.edit();
		editor.putString(Config.DEVICE_UUID, device_id);
		editor.commit();

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_image) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_image) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_image) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		options_head = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.e8_profile_no_avatar)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.e8_profile_no_avatar)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.e8_profile_no_avatar)		// 设置图片加载或解码过程中发生错误显示的图片
		.cacheInMemory(true)						            // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							            // 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(30))	            // 设置成圆角图片
		.build();
		
		options_home = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.home_icon_bg)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.home_icon_bg)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.home_icon_bg)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.build();

        LocationManager locationManager = new LocationManager(this);
        locationManager.refreshLocation();

	}

	public int getCacheUserId() {
		SharedPreferences shared;
		SharedPreferences.Editor editor;

		shared = this.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
		editor = shared.edit();
		int userId = shared.getInt("uid", 0);
		return userId;
	}

	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		return deviceId;
	}

}
