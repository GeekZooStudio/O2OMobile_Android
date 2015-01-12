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
import android.widget.TextView;

import com.insthub.O2OMobile.Protocol.SERVICE_CATEGORY;
import com.insthub.O2OMobile.R;

import java.util.ArrayList;

public class SecondCategoryPopAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<SERVICE_CATEGORY> publicSecondCategoryList;
    private LayoutInflater mInflater;

    public SecondCategoryPopAdapter(Context context, ArrayList<SERVICE_CATEGORY>  second_category_list) {
        this.mContext = context;
        this. publicSecondCategoryList =  second_category_list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return  publicSecondCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return  publicSecondCategoryList.get(position);
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
        convertView = mInflater.inflate(R.layout. second_category_pop_item, null);
        holder.title = (TextView) convertView.findViewById(R.id.tiltle);
        convertView.setTag(holder);
    } else {
        holder = (ViewHolder) convertView.getTag();
    }
    SERVICE_CATEGORY sevice_category=  publicSecondCategoryList.get(position);
    if(sevice_category.title!=null){
        holder.title.setText(sevice_category.title);
    }

    return convertView;
    }
    class ViewHolder {
        TextView title;
    }
}
