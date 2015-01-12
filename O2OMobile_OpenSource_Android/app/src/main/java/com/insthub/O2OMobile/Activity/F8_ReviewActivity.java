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

package com.insthub.O2OMobile.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Adapter.F8_ReviewAdapter;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Model.CommentModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.commentlistResponse;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class F8_ReviewActivity extends BaseActivity implements BusinessResponse, IXListViewListener {

    private  XListView mReviewList;
    private F8_ReviewAdapter mListAdapter;
    private CommentModel mDataModel;
    private ImageView mBackButton;
    private TextView mTitleTextView;
    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f8_review);

        mUserId = getIntent().getIntExtra(O2OMobileAppConst.USERID,0);
        mReviewList = (XListView)findViewById(R.id.f8_reviews_list);
        mReviewList.setXListViewListener(this, 0);
        mReviewList.setPullLoadEnable(true);
        mReviewList.setPullRefreshEnable(true);
        mTitleTextView = (TextView)findViewById(R.id.top_view_title);
        mTitleTextView.setText(getString(R.string.evaluation_list));
        mBackButton = (ImageView)findViewById(R.id.top_view_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDataModel = new CommentModel(this);
        mDataModel.addResponseListener(this);
        mDataModel.fetchPre(mUserId);

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
    	mReviewList.stopRefresh();
    	mReviewList.stopLoadMore();
        if (url.endsWith(ApiInterface.COMMENT_LIST))
        {
            if (null == mListAdapter)
            {
                mListAdapter = new F8_ReviewAdapter(this, mDataModel.publicDataList);
                mReviewList.setAdapter(mListAdapter);
            }
            else
            {
                mListAdapter.notifyDataSetChanged();
            }

            commentlistResponse response = new commentlistResponse();
            response.fromJson(jo);
            if (0 == response.more)
            {
                mReviewList.stopLoadMore();
                mReviewList.setPullLoadEnable(false);
            }

        }
    }

    @Override
    public void onRefresh(int id) {
        mDataModel.fetchPre(mUserId);
    }

    @Override
    public void onLoadMore(int id) {
        mDataModel.fetchNext(mUserId);
    }
}
