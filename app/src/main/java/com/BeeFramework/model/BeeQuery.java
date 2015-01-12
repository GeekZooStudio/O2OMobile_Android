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

import com.external.androidquery.AQuery;
import com.external.androidquery.callback.AjaxCallback;

import java.util.Map;

public class BeeQuery<T> extends AQuery {
	public BeeQuery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static final int ENVIROMENT_PRODUCTION = 1;
	public static final int ENVIROMENT_DEVELOPMENT = 2;
	public static final int ENVIROMENT_MOCKSERVER = 3;
	
	public static int environment() 
	{
		return ENVIROMENT_DEVELOPMENT;
	}
	
	public static String serviceUrl()
	{
		if (ENVIROMENT_PRODUCTION == BeeQuery.environment())
		{
			return "http://demo.o2omobile.net/api";
		}
		else 
		{
            return "http://dev.o2omobile.net/api";

		}
	}
    public static String hostUrl()
    {
        if (ENVIROMENT_PRODUCTION == BeeQuery.environment())
        {
        	return "http://demo.o2omobile.net";
        }
        else
        {
        	return "http://dev.o2omobile.net/api";
        }
    }

	public <K> AQuery ajax(AjaxCallback<K> callback){

		if (BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
		{
			MockServer.ajax(callback);
			DebugMessageModel.addSendingMessage((BeeCallback)callback);
			return null;
		}
        else
        {
            String url = callback.getUrl();
            String absoluteUrl = getAbsoluteUrl(url);

            callback.url(absoluteUrl);

        }

        DebugMessageModel.addSendingMessage((BeeCallback)callback);


		return (BeeQuery)super.ajax(callback);
	}

    public <K> AQuery ajaxAbsolute(AjaxCallback<K> callback){

		return (BeeQuery)super.ajax(callback);
	}

	public <K> AQuery ajax(String url, Map<String, ?> params, Class<K> type, BeeCallback<K> callback){
						
		callback.type(type).url(url).params(params);
		
		if (BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
		{
			MockServer.ajax(callback);
			return null;
		}
        else
        {
            String absoluteUrl = getAbsoluteUrl(url);
            callback.url(absoluteUrl);
        }
		return ajax(callback);
	}


    private static String getAbsoluteUrl(String relativeUrl) {
        return  BeeQuery.serviceUrl() + relativeUrl;
    }
}