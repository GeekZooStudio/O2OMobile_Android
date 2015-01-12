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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.Utils.Utils;
import com.BeeFramework.view.RoundedWebImageView;
import com.insthub.O2OMobile.Activity.D1_OrderActivity;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Utils.LocationManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class A1_LbsListRequestAdapter extends BaseAdapter {

	private Context mContext;
	public List<ORDER_INFO> publicList;
	private LayoutInflater mInflater;
	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	public A1_LbsListRequestAdapter(Context context, List<ORDER_INFO> list) {
		this.mContext = context;
		this.publicList = list;
        mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return publicList.size();
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
			convertView = mInflater.inflate(R.layout.a0_request_list_cell, null);
			holder.item = (LinearLayout) convertView.findViewById(R.id.a1_lbs_list_cell_item);
			holder.head = (RoundedWebImageView) convertView.findViewById(R.id.a1_lbs_list_cell_avatar);
			holder.title = (TextView) convertView.findViewById(R.id.a1_lbs_list_cell_title);
			holder.location = (TextView) convertView.findViewById(R.id.a1_lbs_list_cell_location);
			holder.content = (TextView) convertView.findViewById(R.id.a1_lbs_list_cell_content);
			holder.time = (TextView) convertView.findViewById(R.id.a1_lbs_list_cell_time);
			holder.price = (TextView) convertView.findViewById(R.id.a1_lbs_list_cell_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ORDER_INFO order = publicList.get(position);
		if(order.employer != null && order.employer.avatar != null && order.employer.avatar.thumb != null) {
            mImageLoader.displayImage(order.employer.avatar.thumb, holder.head, O2OMobile.options_head);
		}
		if(order.service_type != null && order.service_type.title != null) {
			holder.title.setText(order.service_type.title);
		}
		if(order.location != null) {
			holder.location.setText(LocationManager.getLocation(order.location.lat, order.location.lon));
		}
		if(order.content != null && order.content.text != null && !order.content.text.equals("")) {
			holder.content.setText(order.content.text);
		} else {
			holder.content.setText(mContext.getString(R.string.no_content));
		}
		if(order.created_at != null) {
			holder.time.setText(TimeUtil.timeAgo(order.created_at));
		}
		if(order.offer_price != null) {
			holder.price.setText(Utils.formatBalance(order.offer_price)+mContext.getString(R.string.yuan));
		}
		
		holder.item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, D1_OrderActivity.class);
				intent.putExtra(D1_OrderActivity.ORDER_ID, order.id);
                mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		return convertView;
	}
	
	class ViewHolder {
		LinearLayout item;
		RoundedWebImageView head;
		TextView title;
		TextView location;
		TextView content;
		TextView time;
		TextView price;
	}

}
