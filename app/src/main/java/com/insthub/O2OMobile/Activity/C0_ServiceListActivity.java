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

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.AnimationUtil;
import com.BeeFramework.Utils.ImageUtil;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Adapter.C0_ServiceListAdapter;
import com.insthub.O2OMobile.Model.UserListModel;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_SEARCH_ORDER;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.Protocol.SIMPLE_USER;
import com.insthub.O2OMobile.Protocol.userlistResponse;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Utils.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

public class C0_ServiceListActivity extends BaseActivity implements BusinessResponse, IXListViewListener {
    C0_ServiceListAdapter mListWithServiceAdapter;
    XListView                       mListView;
    UserListModel                   mDataModel;
    SERVICE_TYPE                    mServiceType;
    ImageView                       mFilterButton;
    LinearLayout                    mFilterLayout;

    TextView                        mPriceDesc;
    ImageView                       mPriceDescSelected;
    TextView                        mPriceAsc;
    ImageView                       mPriceAscSelected;
    TextView                        mRankDesc;
    ImageView                       mRankDescSelected;
    TextView                        mLocationAsc;
    ImageView                       mLocationAscSelected;
    View                            mMaskView;
    ImageView                       mBackButton;
    TextView                        mTitleTextView;
    TextView                        mPublishButton;

    ENUM_SEARCH_ORDER               search_order = ENUM_SEARCH_ORDER.location_asc;
    View footView               ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c0_service_list);
        mTitleTextView = (TextView)findViewById(R.id.top_view_title);
        mListView = (XListView)findViewById(R.id.c0_user_list);
        mListView.setXListViewListener(this, 0);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0 && position - 1 < mDataModel.dataList.size()) {
                    SIMPLE_USER user = mDataModel.dataList.get(position - 1);
                    Intent intent_profile = new Intent(C0_ServiceListActivity.this, F0_ProfileActivity.class);
                    intent_profile.putExtra(F0_ProfileActivity.USER_ID, user.id);
                    intent_profile.putExtra(O2OMobileAppConst.SERVICE_TYPE, mServiceType);
                    startActivity(intent_profile);
                    C0_ServiceListActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }


            }
        });
        mBackButton = (ImageView)findViewById(R.id.top_view_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMaskView = (View)findViewById(R.id.c0_mask_view);
        mMaskView.setClickable(true);
        mMaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilter();
            }
        });

        mDataModel = new UserListModel(this);
        mDataModel.addResponseListener(this);
        mServiceType = (SERVICE_TYPE) getIntent().getSerializableExtra(O2OMobileAppConst.SERVICE_TYPE);
        if (null != mServiceType.title)
        {
            mTitleTextView.setText(mServiceType.title);
        }
        mDataModel.fetPreService(mServiceType.id, ENUM_SEARCH_ORDER.location_asc);
        mFilterLayout = (LinearLayout)findViewById(R.id.c0_filter_layout);
        mFilterButton = (ImageView)findViewById(R.id.top_view_right_image);
        mFilterButton.setImageResource(R.drawable.b1_icon_filter);
        mFilterButton.setVisibility(View.VISIBLE);

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });

        mPublishButton = (TextView)findViewById(R.id.c0_publish_button);
        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(C0_ServiceListActivity.this, C1_PublishOrderActivity.class);
                it.putExtra(O2OMobileAppConst.SERVICE_TYPE, mServiceType);
                startActivity(it);
            }
        });


        initFilter();

        LocationManager.getInstance().refreshLocation();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void showFilter()
    {
        mMaskView.setVisibility(View.VISIBLE);
        showView();
        mFilterButton.setImageResource(R.drawable.b2_close);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilter();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void hideFilter()
    {
        mMaskView.setVisibility(View.GONE);
        hideView();
        mFilterButton.setImageResource(R.drawable.b1_icon_filter);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });
    }



    public void showView() {
        if(mFilterLayout.getVisibility() == View.GONE) {
            mFilterLayout.setVisibility(View.VISIBLE);
            AnimationUtil.showAnimationFromTop(mFilterLayout);
        } else {
            hideView();
        }
    }

    public void hideView() {
        mFilterLayout.setVisibility(View.GONE);
        AnimationUtil.backAnimationFromBottom(mFilterLayout);
    }

    void selectedSearchOrder(ENUM_SEARCH_ORDER   search_order)
    {
        if (search_order == ENUM_SEARCH_ORDER.price_desc)
        {
            mPriceDesc.setTextColor(getResources().getColor(R.color.select_item));
            mPriceDescSelected.setVisibility(View.VISIBLE);

            mPriceAsc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mPriceAscSelected.setVisibility(View.INVISIBLE);

            mRankDesc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mRankDescSelected.setVisibility(View.INVISIBLE);

            mLocationAsc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mLocationAscSelected.setVisibility(View.INVISIBLE);
        }
        else if (search_order == ENUM_SEARCH_ORDER.price_asc)
        {
            mPriceDesc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mPriceDescSelected.setVisibility(View.INVISIBLE);

            mPriceAsc.setTextColor(getResources().getColor(R.color.select_item));
            mPriceAscSelected.setVisibility(View.VISIBLE);

            mRankDesc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mRankDescSelected.setVisibility(View.INVISIBLE);

            mLocationAsc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mLocationAscSelected.setVisibility(View.INVISIBLE);
        }
        else if (search_order == ENUM_SEARCH_ORDER.rank_desc)
        {
            mPriceDesc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mPriceDescSelected.setVisibility(View.INVISIBLE);

            mPriceAsc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mPriceAscSelected.setVisibility(View.INVISIBLE);

            mRankDesc.setTextColor(getResources().getColor(R.color.select_item));
            mRankDescSelected.setVisibility(View.VISIBLE);

            mLocationAsc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mLocationAscSelected.setVisibility(View.INVISIBLE);
        }
        else if(search_order == ENUM_SEARCH_ORDER.location_asc)
        {
            mPriceDesc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mPriceDescSelected.setVisibility(View.INVISIBLE);

            mPriceAsc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mPriceAscSelected.setVisibility(View.INVISIBLE);

            mRankDesc.setTextColor(getResources().getColor(R.color.filter_text_color));
            mRankDescSelected.setVisibility(View.INVISIBLE);

            mLocationAsc.setTextColor(getResources().getColor(R.color.select_item));
            mLocationAscSelected.setVisibility(View.VISIBLE);
        }
    }

    public void initFilter()
    {
        mPriceDesc = (TextView)findViewById(R.id.c0_price_desc);
        mPriceDescSelected = (ImageView)findViewById(R.id.c0_price_desc_select);
        mPriceDesc.setClickable(true);
        mPriceDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_order = ENUM_SEARCH_ORDER.price_desc;
                selectedSearchOrder(search_order);
                hideFilter();
                mDataModel.fetPreService(mServiceType.id, search_order);
            }
        });

        mPriceAsc = (TextView)findViewById(R.id.c0_price_asc);
        mPriceAscSelected = (ImageView)findViewById(R.id.c0_price_asc_select) ;
        mPriceAsc.setClickable(true);
        mPriceAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_order = ENUM_SEARCH_ORDER.price_asc;
                selectedSearchOrder(search_order);
                hideFilter();
                mDataModel.fetPreService(mServiceType.id, search_order);
            }
        });

        mRankDesc = (TextView)findViewById(R.id.c0_rank_desc);
        mRankDescSelected = (ImageView)findViewById(R.id.c0_rank_desc_select);
        mRankDesc.setClickable(true);
        mRankDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_order = ENUM_SEARCH_ORDER.rank_desc;
                selectedSearchOrder(search_order);
                hideFilter();
                mDataModel.fetPreService(mServiceType.id, search_order);
            }
        });

        mLocationAsc = (TextView)findViewById(R.id.location_asc);
        mLocationAsc.setClickable(true);
        mLocationAscSelected = (ImageView)findViewById(R.id.c0_location_asc_select);
        mLocationAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_order = ENUM_SEARCH_ORDER.location_asc;
                selectedSearchOrder(search_order);
                hideFilter();
                mDataModel.fetPreService(mServiceType.id, search_order);
            }
        });

        selectedSearchOrder(search_order);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
    	mListView.stopRefresh();
    	mListView.stopLoadMore();
        if(url.endsWith(ApiInterface.USER_LIST))
        {
            if (null != jo)
            {
                userlistResponse response = new userlistResponse();
                response.fromJson(jo);

                if (null == mListWithServiceAdapter)
                {
                    mListWithServiceAdapter = new C0_ServiceListAdapter(this, mDataModel.dataList);
                    mListView.setAdapter(mListWithServiceAdapter);
                    footView = new View(this);
                    footView.setEnabled(true);
                    footView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ImageUtil.Dp2Px(this,60)));
                    mListView.addFooterView(footView);
                }
                else
                {
                    mListWithServiceAdapter.notifyDataSetChanged();
                }
                mListView.stopLoadMore();
                if (0 == response.more)
                {
                    mListView.setPullLoadEnable(false);
                }

                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                int listHeight = mListView.getHeight();
                int screenHeight = getResources().getDisplayMetrics().heightPixels - ImageUtil.Dp2Px(this,50) - statusBarHeight;

                if (listHeight >= screenHeight)
                {
                    footView.setVisibility(View.VISIBLE);
                }
                else
                {
                    footView.setVisibility(View.GONE);
                    mListView.removeFooterView(footView);
                }
            }


        }

    }

    @Override
    public void onRefresh(int id) {
        mDataModel.fetPreService(mServiceType.id, search_order);
    }

    @Override
    public void onLoadMore(int id) {
        mDataModel.fetNextService(mServiceType.id, search_order);
    }
}
