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

package com.insthub.O2OMobile.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.O2OMobile.Protocol.ORDER_RECORD;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Utils.StringUtils;

import java.util.List;

public class D11_OrderHistoryAdapter extends BaseAdapter {

	private Context mContext;
	public List<ORDER_RECORD> mList;
	private LayoutInflater mInflater;
	private int mCurrent = 0;
	private int mWidth;
	public D11_OrderHistoryAdapter(Context context, List<ORDER_RECORD> list) {
		this.mContext = context;
		this.mList = list;
		for(int i = 0; i < list.size(); i++) {
            mCurrent = i;
			if(list.get(i).active == 0) {
				if(i > 0) {
                    mCurrent = i - 1;
				}
				break;
			}
		}
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.d2_order_history_item, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.d11_order_history_item_icon);
			holder.text = (TextView) convertView.findViewById(R.id.d11_order_history_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ORDER_RECORD record = mList.get(position);
		
		holder.text.setText(StringUtils.getOrderStatusName(record.order_action));
		if(record.active == 1) {
			holder.icon.setImageResource(R.drawable.d2_circle);
			holder.text.setTextColor(Color.parseColor("#333333"));
		} else {
			holder.icon.setImageResource(R.drawable.c5_circle);
			holder.text.setTextColor(Color.parseColor("#9b9b9b"));
		}
		
		if(position == mCurrent) {
			LayoutParams layoutParams_icon = (LayoutParams) holder.icon.getLayoutParams();
			layoutParams_icon.width = mWidth / 15;
			layoutParams_icon.height = mWidth / 15;
			holder.icon.setLayoutParams(layoutParams_icon);
			holder.text.setTextSize(20);
		} else {
			LayoutParams layoutParams_icon = (LayoutParams) holder.icon.getLayoutParams();
			layoutParams_icon.width = mWidth / 20;
			layoutParams_icon.height = mWidth / 20;
			holder.icon.setLayoutParams(layoutParams_icon);
			holder.text.setTextSize(18);
		}
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView 	icon;
		TextView 	text;
	}
}
