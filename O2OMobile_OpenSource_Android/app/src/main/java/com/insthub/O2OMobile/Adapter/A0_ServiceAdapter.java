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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.ImageUtil;
import com.BeeFramework.Utils.Utils;
import com.insthub.O2OMobile.Activity.C0_ServiceListActivity;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class A0_ServiceAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    public ArrayList<SERVICE_TYPE> publicList;
    private int mStatusBarHeight;
    public A0_ServiceAdapter(Context context, ArrayList<SERVICE_TYPE> list) {
        this.mContext = context;
        this.publicList = list;
        mInflater = LayoutInflater.from(context);
        mStatusBarHeight= Utils.getStatusBarHeight(mContext);
    }

    @Override
    public int getCount() {
        if (publicList.size() % 2 > 0) {
            return publicList.size() / 2 + 1;
        } else {
            return publicList.size() / 2;
        }
    }

    @Override
    public Object getItem(int position) {
        return publicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.a0_service_item, null);
            holder.home_need_help_item_two = (LinearLayout) convertView.findViewById(R.id.home_need_help_item_two);
            holder.home_need_help_item_left = (ImageView) convertView.findViewById(R.id.home_need_help_item_left);
            holder.home_need_help_item_right = (ImageView) convertView.findViewById(R.id.home_need_help_item_right);
            holder.service_title_left= (TextView) convertView.findViewById(R.id.sevice_title_left);
            holder.service_title_right= (TextView) convertView.findViewById(R.id.sevice_title_right);
            holder.right= (LinearLayout) convertView.findViewById(R.id.right);
            holder.left= (LinearLayout) convertView.findViewById(R.id.left);
            convertView.setTag(holder);
            ViewGroup.LayoutParams params_left = holder.left.getLayoutParams();
            params_left.width = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth()/2;
            params_left.height =( ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight()-mStatusBarHeight- ImageUtil.Dp2Px(mContext,50))/3;
            holder.right.setLayoutParams(params_left);

            ViewGroup.LayoutParams params_right = holder.right.getLayoutParams();
            holder.left.setLayoutParams(params_left);
;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.home_need_help_item_right.setVisibility(View.VISIBLE);
        final SERVICE_TYPE tv_home_need_help_itenm_left = publicList.get(position*2);
        final SERVICE_TYPE service_type_right = publicList.get(position*2+1);
        if(service_type_right.title!=null){
            holder.service_title_right.setText(service_type_right.title);
        }
        if(tv_home_need_help_itenm_left.title!=null){
            holder.service_title_left.setText(tv_home_need_help_itenm_left.title);
        }
        ImageLoader.getInstance().displayImage(tv_home_need_help_itenm_left.large_icon, holder.home_need_help_item_left, O2OMobile.options_home);

        if(service_type_right.icon == null) {
            holder.home_need_help_item_right.setVisibility(View.INVISIBLE);
        } else {
            ImageLoader.getInstance().displayImage(service_type_right.large_icon, holder.home_need_help_item_right, O2OMobile.options_home);
        }

        holder.home_need_help_item_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(mContext, C0_ServiceListActivity.class);
                it.putExtra(O2OMobileAppConst.SERVICE_TYPE, tv_home_need_help_itenm_left);
                mContext.startActivity(it);
                ((Activity)mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        holder.home_need_help_item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, C0_ServiceListActivity.class);
                it.putExtra(O2OMobileAppConst.SERVICE_TYPE, service_type_right);
                mContext.startActivity(it);
                ((Activity)mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout home_need_help_item_two;
        ImageView home_need_help_item_left;
        ImageView home_need_help_item_right;
        TextView service_title_left;
        TextView service_title_right;
        LinearLayout left;
        LinearLayout right;


    }
}
