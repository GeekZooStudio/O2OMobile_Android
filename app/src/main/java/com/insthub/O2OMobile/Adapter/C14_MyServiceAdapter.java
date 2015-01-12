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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.Utils;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.Protocol.MY_SERVICE;
import com.insthub.O2OMobile.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class C14_MyServiceAdapter extends BaseAdapter {
    private Context mContext;
    public List<MY_SERVICE> publicList;
    private LayoutInflater mInflater;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();
    public C14_MyServiceAdapter(Context context, List<MY_SERVICE> list) {
        this.mContext = context;
        this.publicList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return publicList.size();
    }

    @Override
    public Object getItem(int position) {
        return publicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.c14_my_sevice_list_item, null);
            holder.service_list_item = (RelativeLayout) convertView.findViewById(R.id.service_list_item);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MY_SERVICE my_service = publicList.get(position);
        if(my_service.service_type!=null&&my_service.service_type.icon!=null) {
            mImageLoader.displayImage(my_service.service_type.icon, holder.icon, O2OMobile.options);
        }
        if(my_service.service_type != null && my_service.service_type.title != null) {
            holder.name.setText(my_service.service_type.title);
        }
        if(my_service.price!= null&&!"".equals(my_service.price)) {
            holder.price.setText(Utils.formatBalance(my_service.price));
        }
        return convertView;
    }



    class ViewHolder {
        RelativeLayout  service_list_item;
        ImageView icon;
        TextView name;
        TextView price;
    }
}
