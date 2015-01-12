/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */
package com.BeeFramework.model;

import android.content.Context;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;

import com.external.androidquery.callback.AjaxCallback;
import com.external.androidquery.callback.AjaxStatus;

/**
 * @author mc374
 *
 */
public class DebugMessageModel extends BaseModel {

	public static Stack<BeeCallback> messageList = new Stack<BeeCallback>();
    public static ArrayList<BeeCallback> sendingmessageList = new ArrayList<BeeCallback>();
    public static ArrayList<String> stompList = new ArrayList<String>();
	
    
	public DebugMessageModel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static void addSendingMessage(BeeCallback msg)
	{
        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT || BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
	{
		messageList.push(msg);
        sendingmessageList.add(msg);
	}
	}

    public static void addStomp(String stomp)
    {
        stompList.add(stomp);
    }

    public static void finishSendingMessage(BeeCallback msg)
    {
        msg.endTimestamp = System.currentTimeMillis();
        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT || BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
        {
        if(sendingmessageList.contains(msg))
        {
        sendingmessageList.remove(msg);
    }
        }

    }

    public static boolean DebugisSendingMessage(String url)
    {
        for (int i = 0; i < sendingmessageList.size(); i++)
        {
        	AjaxCallback msg = sendingmessageList.get(i);
            if(msg.getUrl().endsWith(url) )
            {
                return true;
            }
        }

        return false;
    }

}
