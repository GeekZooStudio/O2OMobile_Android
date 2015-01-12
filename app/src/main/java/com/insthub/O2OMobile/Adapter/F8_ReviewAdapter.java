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
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.adapter.BeeBaseAdapter;
import com.insthub.O2OMobile.Activity.F0_ProfileActivity;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.Protocol.COMMENT;
import com.insthub.O2OMobile.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class F8_ReviewAdapter extends BeeBaseAdapter{

    protected ImageLoader mImageLoader = ImageLoader.getInstance();

    public F8_ReviewAdapter(Context c, ArrayList dataList)
    {
        super(c, dataList);
    }

    public class F8_CommentCellHolder extends BeeCellHolder
    {
        ImageView   headImage;

        TextView    nickName;
        TextView    content;

        ImageView   starOne;
        ImageView   starTwo;
        ImageView   starThree;
        ImageView   starFour;
        ImageView   starFive;
    }
    @Override
    protected BeeCellHolder createCellHolder(View cellView) {

        F8_CommentCellHolder holder = new F8_CommentCellHolder();
        holder.headImage = (ImageView)cellView.findViewById(R.id.f8_head_image);
        holder.nickName  = (TextView)cellView.findViewById(R.id.f8_nickname);
        holder.content  = (TextView)cellView.findViewById(R.id.f8_comment_content);

        holder.starOne   = (ImageView)cellView.findViewById(R.id.f8_star_one);
        holder.starTwo   = (ImageView)cellView.findViewById(R.id.f8_star_two);
        holder.starThree = (ImageView)cellView.findViewById(R.id.f8_star_three);
        holder.starFour  = (ImageView)cellView.findViewById(R.id.f8_star_four);
        holder.starFive  = (ImageView)cellView.findViewById(R.id.f8_star_five);
        return holder;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h) {

        final COMMENT comment = (COMMENT)dataList.get(position);
        F8_CommentCellHolder holder = (F8_CommentCellHolder)h;
        mImageLoader.displayImage(comment.user.avatar.large, holder.headImage, O2OMobile.options_head);
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_profile= new Intent(mContext, F0_ProfileActivity.class);
                intent_profile.putExtra(F0_ProfileActivity.USER_ID,comment.user.id);
                mContext.startActivity(intent_profile);
            }
        });
        holder.nickName.setText(comment.user.nickname);

        if (null != comment.content && null != comment.content.text)
        {
            holder.content.setText(comment.content.text);
        } else {
        	holder.content.setText(mContext.getString(R.string.no_content));
        }

        ArrayList<ImageView> starList = new ArrayList<ImageView>();
        starList.add(holder.starOne);
        starList.add(holder.starTwo);
        starList.add(holder.starThree);
        starList.add(holder.starFour);
        starList.add(holder.starFive);

        for(int i = 0; i < comment.rank; i++)
        {
            ImageView starImage = starList.get(i);
            starImage.setImageResource(R.drawable.b7_star_on);
        }

        for(int i = comment.rank; i < starList.size(); i++)
        {
            ImageView starImage = starList.get(i);
            starImage.setImageResource(R.drawable.b7_star_off);
        }
        starList.clear();
        return null;
    }

    @Override
    public View createCellView() {
        return mInflater.inflate(R.layout.f8_review_cell,null);
    }
}
