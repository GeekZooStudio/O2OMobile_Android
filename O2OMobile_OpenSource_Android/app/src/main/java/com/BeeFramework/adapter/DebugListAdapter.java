package com.BeeFramework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BeeFramework.model.BeeCallback;
import com.BeeFramework.model.DebugMessageModel;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class DebugListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	
	public DebugListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DebugMessageModel.messageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return DebugMessageModel.messageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.debug_message_item, null);
			holder.time = (TextView) convertView.findViewById(R.id.debug_item_time);
			holder.message = (TextView) convertView.findViewById(R.id.debug_item_message);
			holder.request = (TextView) convertView.findViewById(R.id.debug_item_request);
			holder.response = (TextView) convertView.findViewById(R.id.debug_item_response);
			holder.netSize = (TextView) convertView.findViewById(R.id.debug_item_netSize);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int size = DebugMessageModel.messageList.size();

        BeeCallback tempCallBack = DebugMessageModel.messageList.get(size-1-position);
        if (null != tempCallBack)
        {
            tempCallBack.toString();

            String timeDesc =  "";
            if (0 != tempCallBack.endTimestamp)
            {
                timeDesc = (tempCallBack.endTimestamp - tempCallBack.startTimestamp)*1.0/1000 +"秒";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分ss秒SSS");

            holder.time.setText("开始时间: " + sdf.format(new Date(tempCallBack.startTimestamp)));
            holder.message.setText(tempCallBack.message);
            holder.request.setText(tempCallBack.requset);

            String serverUseage = "";

            if (null != tempCallBack.getStatus() && null != tempCallBack.getStatus().getData())
            {
                String str = null;
                try {
                    str = new String(tempCallBack.getStatus().getData(), tempCallBack.getEncoding());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(null!=response)
                {
                    serverUseage = response.optString("time_usage");
                }


            }


            holder.netSize.setText(tempCallBack.netSize + "  \n耗时:" + timeDesc +" \nPHP耗时: "+ serverUseage);
        }



		
		return convertView;
	}
	
	class ViewHolder {
		private TextView time;
		private TextView message;
		private TextView request;
		private TextView response;
		private TextView netSize;
	}

}
