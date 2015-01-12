package com.insthub.O2OMobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.TimeUtil;
import com.insthub.O2OMobile.Protocol.MESSAGE;
import com.insthub.O2OMobile.R;

import java.util.List;

public class H0_MessageAdapter extends BaseAdapter {

	private Context mContext;
	public List<MESSAGE> publicList;
	private boolean mIsPerson;
	private LayoutInflater mInflater;
	public H0_MessageAdapter(Context context, List<MESSAGE> list, boolean isPerson) {
		this.mContext = context;
		this.publicList = list;
		this.mIsPerson = isPerson;
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
			convertView = mInflater.inflate(R.layout.h0_message_item, null);
			holder.item = (LinearLayout) convertView.findViewById(R.id.message_item_view);
			holder.isRead = (ImageView) convertView.findViewById(R.id.message_item_isread);
			holder.text = (TextView) convertView.findViewById(R.id.message_item_text);
			holder.time = (TextView) convertView.findViewById(R.id.message_item_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MESSAGE message = publicList.get(position);
		if(message.content != null) {
			holder.text.setText(message.content);
		} else {
			holder.text.setText(mContext.getString(R.string.no_content));
		}
		if(message.created_at != null) {
			holder.time.setText(TimeUtil.timeAgo(message.created_at));
		}
		
		if(mIsPerson) {
			if(message.is_readed == 0) {
				holder.isRead.setVisibility(View.VISIBLE);
			} else {
				holder.isRead.setVisibility(View.INVISIBLE);
			}
		} else {
			holder.isRead.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	class ViewHolder {
		LinearLayout item;
		ImageView isRead;
		TextView text;
		TextView time;
	}

}
