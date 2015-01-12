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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.Utils.Utils;
import com.BeeFramework.adapter.BeeBaseAdapter;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class E0_PublishedOrderListAdapter extends BeeBaseAdapter
{
    protected ImageLoader mImageLoader = ImageLoader.getInstance();

    public E0_PublishedOrderListAdapter(Context c, ArrayList orderList)
    {
        super(c, orderList);
    }

    public class E0_OrderCellHolder extends BeeCellHolder
    {
        ImageView   headImage;
        TextView    nickName;
        TextView 	serviceType;
        TextView    serviceStatus;
        TextView    content;
        TextView 	time;
        TextView 	price;

    }

    @Override
    protected BeeCellHolder createCellHolder(View cellView)
    {
        E0_OrderCellHolder holder = new E0_OrderCellHolder();
        holder.headImage 		= (ImageView)	cellView.findViewById(R.id.e0_head_image);
        holder.nickName  		= (TextView)	cellView.findViewById(R.id.e0_nickname);
        holder.serviceType		= (TextView) 	cellView.findViewById(R.id.e0_servicetype);
        holder.serviceStatus  	= (TextView)	cellView.findViewById(R.id.e0_service_status);
        holder.content        	= (TextView)	cellView.findViewById(R.id.e0_content);
        holder.time 			= (TextView) 	cellView.findViewById(R.id.e0_create_time);
        holder.price 			= (TextView) 	cellView.findViewById(R.id.e0_price);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h)
    {
        ORDER_INFO order_info = (ORDER_INFO)dataList.get(position);
		E0_OrderCellHolder holder = (E0_OrderCellHolder) h;
		if(null != order_info.employee)
        {
			holder.headImage.setVisibility(View.VISIBLE);
			holder.nickName.setVisibility(View.VISIBLE);
			if (0 == order_info.employee.id)
            {
				holder.headImage.setImageResource(R.drawable.e9_no_header);
				//holder.nickName.setVisibility(View.GONE);
                if(order_info.service_type != null && order_info.service_type.title != null)
                {
                    holder.nickName.setText(order_info.service_type.title);
                }
			}
            else
            {
				holder.headImage.setImageResource(R.drawable.e8_profile_no_avatar);
				holder.nickName.setVisibility(View.VISIBLE);
                holder.nickName.setText(order_info.employee.nickname);
			}

			if (null != order_info.employee.avatar)
            {
                mImageLoader.displayImage(order_info.employee.avatar.large, holder.headImage, O2OMobile.options_head);
			}

		}
        else
        {
			holder.headImage.setImageResource(R.drawable.e9_no_header);
			//holder.nickName.setVisibility(View.GONE);
            if(order_info.service_type != null && order_info.service_type.title != null)
            {
                holder.nickName.setText(order_info.service_type.title);
            }
		}

//        if(order_info.service_type != null && order_info.service_type.title != null)
//        {
//            holder.serviceType.setText(order_info.service_type.title);
//        }

		if(null != order_info.content && null != order_info.content.text && !order_info.content.text.equals(""))
        {
			holder.content.setText(order_info.content.text);
		}
		holder.serviceStatus.setText(StringUtils.getOrderStatusName(order_info.order_status));
		if(order_info.offer_price != null) {
			holder.price.setText(Utils.formatBalance(order_info.offer_price)+"元");
		}
		if(order_info.created_at != null) {
			holder.time.setText(TimeUtil.timeAgo(order_info.created_at));
		}

        return null;
    }

    @Override
    public View createCellView()
    {
        return mInflater.inflate(R.layout.e0_published_orders_cell,null);
    }
}
