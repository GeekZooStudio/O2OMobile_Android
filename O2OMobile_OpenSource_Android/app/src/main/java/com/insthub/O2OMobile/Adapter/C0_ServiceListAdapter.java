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

import com.BeeFramework.Utils.Utils;
import com.BeeFramework.adapter.BeeBaseAdapter;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.Protocol.SIMPLE_USER;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Utils.LocationManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class C0_ServiceListAdapter extends BeeBaseAdapter
{
    protected ImageLoader mImageLoader = ImageLoader.getInstance();

    public C0_ServiceListAdapter(Context c, ArrayList dataList)
    {
        super(c, dataList);
    }

    public class C0_UserCellHolder extends BeeCellHolder
    {
        ImageView   headImage;

        TextView    nickName;
        TextView    location;
        TextView 	price;

        ImageView   starOne;
        ImageView   starTwo;
        ImageView   starThree;
        ImageView   starFour;
        ImageView   starFive;
    }

    @Override
    protected BeeCellHolder createCellHolder(View cellView)
    {
        C0_UserCellHolder holder = new C0_UserCellHolder();
        holder.headImage = (ImageView)cellView.findViewById(R.id.c0_head_image);
        holder.nickName  = (TextView)cellView.findViewById(R.id.c0_nickname);
        holder.location  = (TextView)cellView.findViewById(R.id.c0_location);
        holder.price  	 = (TextView) cellView.findViewById(R.id.c0_price);
        
        holder.starOne   = (ImageView)cellView.findViewById(R.id.c0_star_one);
        holder.starTwo   = (ImageView)cellView.findViewById(R.id.c0_star_two);
        holder.starThree = (ImageView)cellView.findViewById(R.id.c0_star_three);
        holder.starFour  = (ImageView)cellView.findViewById(R.id.c0_star_four);
        holder.starFive  = (ImageView)cellView.findViewById(R.id.c0_star_five);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h)
    {
        SIMPLE_USER user = (SIMPLE_USER)dataList.get(position);
        C0_UserCellHolder holder = (C0_UserCellHolder)h;
        mImageLoader.displayImage(user.avatar.large, holder.headImage, O2OMobile.options_head);
        holder.nickName.setText(user.nickname);

        if (null != user.location && user.location.lat > 0 && user.location.lon > 0)
        {
            holder.location.setText(LocationManager.getLocation(user.location.lat, user.location.lon));
        }
        if(user.current_service_price != null) {
        	holder.price.setText(Utils.formatBalance(user.current_service_price)+"元");
        }

        ArrayList<ImageView> starList = new ArrayList<ImageView>();
        starList.add(holder.starOne);
        starList.add(holder.starTwo);
        starList.add(holder.starThree);
        starList.add(holder.starFour);
        starList.add(holder.starFive);

        float comment_goodrate = Float.valueOf(user.comment_goodrate);
        int comment_rank = (int) Math.floor(comment_goodrate*5);

        for(int i = 0; i < comment_rank; i++)
        {
            ImageView starImage = starList.get(i);
            starImage.setImageResource(R.drawable.b7_star_on);
        }

        for(int i = comment_rank; i < starList.size(); i++)
        {
            ImageView starImage = starList.get(i);
            starImage.setImageResource(R.drawable.b7_star_off);
        }
        starList.clear();
        holder.starOne.setImageResource(R.drawable.b7_star_on);
        return null;
    }

    @Override
    public View createCellView()
    {
        return mInflater.inflate(R.layout.c0_user_cell,null);
    }
}
