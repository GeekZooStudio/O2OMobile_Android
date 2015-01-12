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

package com.BeeFramework.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.insthub.O2OMobile.R;

import java.util.Timer;
import java.util.TimerTask;

public class ToastView {
	
	public static Toast toast;
	private int time;
	private Timer timer;
	
	public ToastView(Context context, String text) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.toast_view, null);
		TextView t = (TextView) view.findViewById(R.id.toast_text);
		t.setText(text);
		if(toast != null) {
			toast.cancel();
		}
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
	}
	
	public ToastView(Context context, int text) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.toast_view, null);
		TextView t = (TextView) view.findViewById(R.id.toast_text);
		t.setText(text);
		if(toast != null) {
			toast.cancel();
		}
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
	}
	
	//设置toast显示位置
	public void setGravity(int gravity, int xOffset, int yOffset) {
		//toast.setGravity(Gravity.CENTER, 0, 0); //居中显示
		toast.setGravity(gravity, xOffset, yOffset);
	}
	
	//设置toast显示时间
	public void setDuration(int duration) {
		toast.setDuration(duration);
	}
	
	//设置toast显示时间(自定义时间)
	public void setLongTime(int duration) {
		//toast.setDuration(duration);
		time = duration;
		timer = new Timer();
        timer.schedule(new TimerTask(){
        	@Override
        	public void run() {
        		// TODO Auto-generated method stub
        		if(time-1000 >= 0) {
        			show();
        			time= time - 1000;
        		} else {
        			timer.cancel();
        		}
        	}
        }, 0, 1000);
	}
	
	public void show() {
		toast.show();
	}
	
	public static void cancel() {
		if(toast != null) {
			toast.cancel();
		}
	}

}
