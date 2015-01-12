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

package com.BeeFramework.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONTokener;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
    private static Toast mToast;
    // 验证邮箱的正则表达式
	public static boolean isEmail(String email){
    	String str = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
	
	// 格式化金额
	public static String formatBalance(String balance) {
		BigDecimal num = new BigDecimal(balance);
		DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");  
	    return fmt.format(num); 
	}
	
	// 50 - 100随机数
	public static int random() {
		Random rd = new Random();
		return 50 + rd.nextInt(50);
	}

    public static Object parseResponse(String responseBody) throws JSONException {
        Object result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
        responseBody = responseBody.trim();
        if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
            result = new JSONTokener(responseBody).nextValue();
        }
        if (result == null) {
            result = responseBody;
        }
        return result;
    }

    public static Bitmap scaleBitmap(Bitmap bm,int pixel){
        int srcHeight = bm.getHeight();
        int srcWidth = bm.getWidth();


        if(srcHeight>pixel || srcWidth>pixel)
        {
            float scale_y = 0;
            float scale_x = 0;
            if (srcHeight > srcWidth)
            {
                scale_y = ((float)pixel)/srcHeight;
                scale_x = scale_y;
            }
            else
            {
                scale_x = ((float)pixel)/srcWidth;
                scale_y = scale_x;
            }

            Matrix  matrix = new Matrix();
            matrix.postScale(scale_x, scale_y);
            Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, srcWidth, srcHeight, matrix, true);
            return dstbmp;
        }
        else{
            return Bitmap.createBitmap(bm);
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm,int dstHeight,int dstWidth){
        if(bm == null) return null;//java.lang.NullPointerException
        int srcHeight = bm.getHeight();
        int srcWidth = bm.getWidth();
        if(srcHeight>dstHeight || srcWidth>dstWidth){
            float scale_y = ((float)dstHeight)/srcHeight;
            float scale_x = ((float)dstWidth)/srcWidth;
            float scale = scale_y<scale_x?scale_y:scale_x;
            Matrix  matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, srcWidth, srcHeight, matrix, true);
            return dstbmp;
        }
        else{
            return Bitmap.createBitmap(bm);
        }
    }

    /**
     * 手机号验证
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[0678])[0-9]{8}"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    public static final void toastMessage(final Activity activity,
                                          final String message) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    public static int getStatusBarHeight(final Context mContext){

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.e("EXCEPTION", "get status bar height fail");
            e1.printStackTrace();
        }
        System.out.println("x = " + x + "，sbar = " + sbar) ;
        return sbar;

    }
}
