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

package com.BeeFramework.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.BeeFramework.Utils.JsonFormatTool;
import com.external.androidquery.callback.AjaxCallback;
import com.external.androidquery.callback.AjaxStatus;
import com.external.androidquery.util.AQUtility;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BeeCallback<T> extends AjaxCallback<T>
{
    public String message;
    public String requset;
    public String response;
    public String netSize;
    public long startTimestamp;
    public long endTimestamp;

    static Handler mHandler = null;
    static Date bandwidthMeasurementDate = new Date();
    public static Date throttleWakeUpTime = null;
    public static long maxBandwidthPerSecond = 14800;
    //上一秒所有线程传输数据大小
    public static long bandwidthUsedInLastSecond = 0;
    public static long averageBandwidthUsedPerSecond = 0;
    static boolean forceThrottleBandwith = false;
    static Object bandwidthThrottlingLock = new Object();

    Timer   timer;
    TimerTask task;

    public BeeCallback()
    {
        super();
        startTimestamp = System.currentTimeMillis();

        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT)
        {
            if (null == mHandler)
            {
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1)
                        {
                            BeeCallback.this.performThrottling();
                        }
                    }
                };
            }


            task = new TimerTask(){
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            };
            timer = new Timer(true);

            if (BeeCallback.isBandwidthThrottled())
            {
                timer.schedule(task,250, 250);
            }
        }


    }

    @Override
    public void run()
    {
        long interval = 0;
        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT)
        {
            try {
                synchronized (bandwidthThrottlingLock)
                {
                    if (null != throttleWakeUpTime)
                    {
                        if (throttleWakeUpTime.after(new Date()))
                        {
                            if (!AQUtility.isUIThread())
                            {
                                Date nowDate = new Date();
                                interval = throttleWakeUpTime.getTime() - nowDate.getTime();
                            }
                        }
                        else
                        {
                            //TODO schedule callback
                        }
                    }
                }

                if (interval > 0)
                {
                    Thread.sleep(interval);
                }
            }
            catch (Exception e)
            {

            }
        }
        super.run();
     
    }

    @Override
    public void callback()
    {
        super.callback();

        int bytes = 0;
        if (null != status.getData())
        {
            bytes = status.getData().length;
        }

        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT)
        {
            if (bytes > 0)
            {
                try {
                    synchronized (bandwidthThrottlingLock)
                    {
                        BeeCallback.incrementBandwidthUsedInLastSecond(bytes);
                    }
                }
                catch (Exception e)
                {

                }
            }
            timer.cancel();
        }


    }
	
	public void callback(String url, T object, AjaxStatus status)
    {

	}

    public void callback(String url, JSONObject jo, AjaxStatus status)
    {
        DebugMessageModel.finishSendingMessage(this);

        endTimestamp = System.currentTimeMillis();

    }
    public static ArrayList<Integer> bandwidthUseageTracker = new ArrayList<Integer>();

    static void recordBandwidthUsage()
    {
        if (bandwidthUsedInLastSecond == 0)
        {
            bandwidthUseageTracker.clear();
        }
        else
        {
            Date nowDate = new Date();
            long interval = bandwidthMeasurementDate.getTime() - nowDate.getTime();

            while ((interval < 0 || bandwidthUseageTracker.size() > 5) && bandwidthUseageTracker.size() > 0)
            {
                bandwidthUseageTracker.remove(0);
                interval ++;
            }
        }

        Log.d("THROTTLING", "[THROTTLING] ===Used:" + bandwidthUsedInLastSecond + " bytes of bandwidth in last measurement period===");

        bandwidthUseageTracker.add(Integer.valueOf((int) bandwidthUsedInLastSecond));
        bandwidthMeasurementDate = new Date();
        bandwidthMeasurementDate.setTime(bandwidthMeasurementDate.getTime()+1000);

        bandwidthUsedInLastSecond = 0;

        long totalBytes = 0;
        for (int i = 0; i< bandwidthUseageTracker.size(); i++)
        {
            Integer bytes = bandwidthUseageTracker.get(i);
            totalBytes += bytes.longValue();
        }

        averageBandwidthUsedPerSecond = totalBytes/bandwidthUseageTracker.size();
    }

    static void measureBandwidthUsage()
    {
        try
        {
            if (BeeCallback.isBandwidthThrottled())
            {
                synchronized (bandwidthThrottlingLock)
                {
                    if(null == bandwidthMeasurementDate || bandwidthMeasurementDate.before(new Date()))
                    {
                        BeeCallback.recordBandwidthUsage();
                    }

                    long bytesRemaining = maxBandwidthPerSecond - bandwidthUsedInLastSecond;

                    if (bytesRemaining < 0)
                    {
                        double extraSleepTime = (-bytesRemaining/(maxBandwidthPerSecond*1.0));
                        throttleWakeUpTime = new Date();
                        throttleWakeUpTime.setTime(throttleWakeUpTime.getTime() + (int)extraSleepTime*1000);
                    }
                }

                if (null != throttleWakeUpTime)
                {
                    String throttle =  "[THROTTLING] Sleeping request until after " + throttleWakeUpTime.toString();
                    System.out.print(throttle);
                }
            }

        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    public void performThrottling()
    {
        if (BeeCallback.isBandwidthThrottled())
        {
            BeeCallback.measureBandwidthUsage();
        }

    }

    static long maxUploadReadLength()
    {
        long toRead = maxBandwidthPerSecond/4;
        try
        {
            synchronized (bandwidthThrottlingLock)
            {
                if (maxBandwidthPerSecond > 0 && (bandwidthUsedInLastSecond + toRead > maxBandwidthPerSecond))
                {
                    toRead = maxBandwidthPerSecond - bandwidthUsedInLastSecond;
                    if (toRead < 0)
                    {
                        toRead = 0;
                    }
                }

                if ( 0 == toRead || null == bandwidthMeasurementDate || bandwidthMeasurementDate.before(new Date()))
                {
                    throttleWakeUpTime = bandwidthMeasurementDate;
                }
            }
        }
        catch (Exception e)
        {

        }
        return toRead;
    }

    public static void incrementBandwidthUsedInLastSecond(long bytes)
    {
        try
        {
           synchronized (bandwidthThrottlingLock)
           {
               bandwidthUsedInLastSecond += bytes;
           }
        }
        catch (Exception e)
        {

        }
    }

    public static boolean isBandwidthThrottled()
    {
       if (forceThrottleBandwith)
       {
           return true;
       }

        return false;
    }

    public static void setForceThrottleBandwidth(boolean throttle)
    {
        forceThrottleBandwith = throttle;
    }

    public static void setMaxBandwidthPerSecond(int bandWidth)
    {
        maxBandwidthPerSecond = bandWidth;
    }

    @Override
    public void async(Context context) {
        super.async(context);
    }

    @Override
    public String toString()
    {
        String msgDesc = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分ss秒SSS");

        msgDesc += "创建时间："+ sdf.format(new Date(startTimestamp)) +"\n\n";
        
        if (0 != this.endTimestamp)
        {
            msgDesc += "结束时间："+sdf.format(new Date(endTimestamp)) +"\n\n";
        }

        msgDesc += "消息："+ this.getUrl()+"\n\n";
        message = "消息："+this.getUrl();

        if (null != this.params)
        {
            msgDesc += "请求："+this.params.toString()+"\n\n";
            requset = "请求：\n"+JsonFormatTool.formatJson(this.params.toString(), "    ");
        }
        else
        {
            msgDesc += "请求：{}\n\n";
            requset = "请求："+"{}";
        }

        if(null != this.result)
        {
            msgDesc += "响应："+this.getResult().toString()+"\n\n";
            response = "响应：\n"+"json:"+JsonFormatTool.formatJson(this.getResult().toString(), "    ");
            
            float f = this.getResult().toString().getBytes().length;
            if(this.getResult().toString().getBytes().length > 1024)
            {
            	float a = f/1024;
            	DecimalFormat df = new DecimalFormat("#.##");
            	msgDesc += "网络包大小："+df.format(a)+"k";
            	netSize = "网络包大小："+df.format(a)+"k";
            }
            else
            {
            	msgDesc += "网络包大小："+this.getResult().toString().getBytes().length+"b";
            	netSize = "网络包大小："+this.getResult().toString().getBytes().length+"b";
            }

            msgDesc += "网络请求时间: " + (endTimestamp - startTimestamp)/1000;

        }
        else
        {
            String str = null;
            try {
                if (null != this.getStatus() && null != this.getStatus().getData())
                {
                    str = new String(this.getStatus().getData(), this.getEncoding());
                    msgDesc += "响应："+str+"\n\n";
                    response = "响应："+str;
                }
                else
                {
                    str = this.getStatus().getError();
                    msgDesc += "响应："+str+"\n\n";
                    response = "响应："+str;
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        return msgDesc;
    }

}
