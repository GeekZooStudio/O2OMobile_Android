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

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BusinessMessage 
{
	public static final int SUCCESS_MESSAGE = 0;
    public static final int FAILURE_MESSAGE = 1;
    public static final int SENDING_MESSAGE = 2;
    public static final int CANCEL_MESSAGE = 3;
    
	public String messageString; 
	public JSONObject response;
	public Map<String, Object>  requestParams;
	public int   messageState;
	
	public String timeStamp;
	
	List<NameValuePair> mParametersList;

    public static Stack<BusinessMessage> messageList = new Stack<BusinessMessage>();
    public static ArrayList<BusinessMessage> sendingMessageList = new ArrayList<BusinessMessage>();
	
	public BusinessMessage(String msg, int MessageState, Map<String, Object>  requestParams,JSONObject responseDataJsonObject)
	{
		this.messageString = msg;
		this.messageState = MessageState;
		this.requestParams = requestParams;
		this.response = responseDataJsonObject;
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分ss秒");
		this.timeStamp = sdf.format(new Date(currentTimestamp));

        BusinessMessage.addMessage(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String msgDesc = "";
		msgDesc += "时间: "+this.timeStamp +"\n\n";
		msgDesc += "消息："+ this.messageString+"\n\n";
		msgDesc += "请求："+this.requestParams.toString()+"\n\n";

		if(null != this.response) {
			msgDesc += "响应:"+this.response.toString();
		} 
		
		return msgDesc;
	}

    public static void addMessage(BusinessMessage msg)
    {
        messageList.push(msg);
        sendingMessageList.add(msg);
    }


    public static void finishMessage(BusinessMessage msg)
    {
        sendingMessageList.remove(msg);
    }

    public static boolean isSendingMessage(String url)
    {
        for (int i = 0; i < sendingMessageList.size(); i++)
        {
            BusinessMessage msg = sendingMessageList.get(i);
            if(msg.messageState == BusinessMessage.SENDING_MESSAGE && 0 == msg.messageString.compareToIgnoreCase(url))
            {
                return true;
            }
        }

        return false;
    }
}
