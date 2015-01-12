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

package com.insthub.O2OMobile.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.insthub.O2OMobile.Config;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.R;

public class LocationManager implements BDLocationListener
{
    public LocationClient mLocationClient = null;
    private static LocationManager Instance;
    private static double          latitude = 0; //经度
    private static double          longitude = 0; //纬度
    
    public static SharedPreferences shared;
	public static SharedPreferences.Editor editor;
	public static Context context;


    public LocationManager(Context context)
    {
    	LocationManager.context = context;
    	shared = context.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        editor = shared.edit();
        
        latitude = shared.getFloat("latitude", 0.0f);
        longitude = shared.getFloat("longitude", 0.0f);
    	
        Instance = this;
        mLocationClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式
        option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(false);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
        option.setProdName(Config.BAIDU_MAP_PRODNAME);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();

    }

    public static LocationManager getInstance()
    {
        return Instance;
    }

    public void refreshLocation()
    {
        mLocationClient.requestOfflineLocation();
    }

    public BDLocation getLocation()
    {
        return mLocationClient.getLastKnownLocation();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation)
    {
        if (bdLocation == null)
            return ;
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(bdLocation.getTime());
        sb.append("\nerror code : ");
        sb.append(bdLocation.getLocType());
        sb.append("\nlatitude : ");
        sb.append(bdLocation.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(bdLocation.getLongitude());
        sb.append("\nradius : ");
        sb.append(bdLocation.getRadius());
        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
            sb.append("\nspeed : ");
            sb.append(bdLocation.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(bdLocation.getSatelliteNumber());
        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\naddr : ");
            sb.append(bdLocation.getAddrStr());
        }

        latitude = bdLocation.getLatitude();
        longitude = bdLocation.getLongitude();
        
        if (latitude > 1 && longitude > 1)
        {
        	editor.putFloat("latitude", (float) latitude);
            editor.putFloat("longitude", (float) longitude);
            editor.commit();
        }
        else
        {
            if (mLocationClient.isStarted())
            {

//                int result =mLocationClient.requestOfflineLocation();
//                Log.d("LocSDK3", "result:" + result);

            }
            else
            {
               Log.d("LocSDK3", "locClient is null or not started");
            }

        }

        Log.e("location", sb.toString());
    }

    
    public static String getLocation(double lat, double lon) {
    	String loc = null;
    	double s = gps2m(getLatitude(), getLongitude(), lat, lon);
    	if(s > 1000) {
    		s = s / 1000.0;
    		s = Math.ceil(s * 100+.5)/100;
    		loc = s + context.getString(R.string.kilometre);
    	} else {
    		s = Math.ceil(s * 100+.5)/100;
    		loc = s + context.getString(R.string.meter);
    	}
    	return loc;
    }
    
    public static final double EARTH_RADIUS = 6378137.0;
    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public void destory()
    {
        mLocationClient.stop();
    }
    
    public static double getLatitude() {
    	shared = context.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        editor = shared.edit();
        latitude = shared.getFloat("latitude", 0.0f);
    	return latitude;
    } 
    public static double getLongitude() {
    	shared = context.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        editor = shared.edit();
        longitude = shared.getFloat("longitude", 0.0f);
    	return longitude;
    } 
    
}
