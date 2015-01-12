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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C1_PublishOrderAdapter extends BaseAdapter {

	private Context mContext;
	public List<SERVICE_TYPE> mList;
	private LayoutInflater mInflater;
	public static Map<Integer, Boolean> isSelected;
	
	public C1_PublishOrderAdapter(Context context, List<SERVICE_TYPE> list) {
		this.mContext = context;
		this.mList = list;
        mInflater = LayoutInflater.from(context);
		init();
	}
	
	public C1_PublishOrderAdapter(Context context, List<SERVICE_TYPE> list, int position) {
		this.mContext = context;
		this.mList = list;
        mInflater = LayoutInflater.from(context);
		init(position);
	}
	
	private void init() {
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < mList.size(); i++){
			isSelected.put(i, false);
		}
	}
	
	private void init(int position) {
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < mList.size(); i++){
			if(position == i) {
				isSelected.put(i, true);
			} else {
				isSelected.put(i, false);
			}
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = mInflater.inflate(R.layout.c1_publish_order_cell, null);
			holder.item = (FrameLayout) convertView.findViewById(R.id.c1_publish_order_cell_view);
			holder.title = (TextView) convertView.findViewById(R.id.c1_publish_order_cell_type);
			holder.icon = (ImageView) convertView.findViewById(R.id.c1_publish_order_cell_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SERVICE_TYPE service_type = mList.get(position);
		if(service_type.title != null) {
			holder.title.setText(service_type.title);
		}
		
		if(isSelected.get(position)) {
			holder.icon.setVisibility(View.VISIBLE);
			holder.title.setTextColor(Color.parseColor("#39BCED"));
		} else {
			holder.icon.setVisibility(View.GONE);
			holder.title.setTextColor(Color.parseColor("#555555"));
		}
		
		return convertView;
	}
	
	class ViewHolder {
		FrameLayout item;
		TextView title;
		ImageView icon;
	}

}
