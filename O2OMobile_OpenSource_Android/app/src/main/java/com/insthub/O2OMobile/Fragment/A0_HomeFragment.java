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

package com.insthub.O2OMobile.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.external.eventbus.EventBus;
import com.external.viewpagerindicator.CirclePageIndicator;
import com.insthub.O2OMobile.Activity.C1_PublishOrderActivity;
import com.insthub.O2OMobile.Activity.SlidingActivity;
import com.insthub.O2OMobile.Adapter.Bee_PageAdapter;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Utils.LocationManager;
import com.insthub.O2OMobile.View.A0_RequestListView;
import com.insthub.O2OMobile.View.A0_ServiceView;

import java.util.ArrayList;

public class A0_HomeFragment extends Fragment {
	
	private ArrayList<View> mViewList;
	private View view;
	private ImageView mMenu;
	private TextView mTitle;
	private CirclePageIndicator mIndicator;
	private ImageView mSearch;
	private ImageView mFilter;
	private ViewPager mImagePager;
	
	private A0_ServiceView mA0ServiceView;
	private A0_RequestListView mHomeDemand;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mViewList = new ArrayList<View>();
		
		LocationManager locationManager = new LocationManager(getActivity());
        locationManager.refreshLocation();
		
		view = inflater.inflate(R.layout.a0_home, null);
		mMenu = (ImageView) view.findViewById(R.id.home_menu);
		mTitle = (TextView) view.findViewById(R.id.home_title);
		mImagePager = (ViewPager) view.findViewById(R.id.home_pager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.home_indicator);
		mSearch = (ImageView) view.findViewById(R.id.home_search);
		mFilter = (ImageView) view.findViewById(R.id.home_filter);
		
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		mA0ServiceView = (A0_ServiceView) mInflater.inflate(R.layout.a0_service, null);
		mViewList.add(mA0ServiceView);
		mHomeDemand = (A0_RequestListView) mInflater.inflate(R.layout.a0_request_list, null);
		mViewList.add(mHomeDemand);
		
		mImagePager.setAdapter(new Bee_PageAdapter(mViewList));
		mImagePager.setCurrentItem(0);
		mIndicator.setViewPager(mImagePager, 0);
		mIndicator.requestLayout();
		
		mImagePager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 == 0) {
                    mTitle.setText(getString(R.string.need_help));
                    ((SlidingActivity) getActivity()).isShowLeft(true);
                    mSearch.setVisibility(View.VISIBLE);
                    mFilter.setVisibility(View.GONE);
                } else {
                    mTitle.setText(getString(R.string.request_around));
                    ((SlidingActivity) getActivity()).isShowLeft(false);
                    mSearch.setVisibility(View.GONE);
                    mFilter.setVisibility(View.VISIBLE);
                }
                mIndicator.setCurrentItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
		
		if (!EventBus.getDefault().isregister(this)) {
            EventBus.getDefault().register(this);
        }
		
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
		
		mMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((SlidingActivity) getActivity()).showLeft();
            }
        });
		
		mSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), C1_PublishOrderActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
		
		mFilter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeDemand.showView();
            }
        });
	}
	
	public void onEvent(Object event) {
		Message message = (Message) event;
		if (message.what == MessageConstant.SHOW_SEARCH_VIEW) {
			mFilter.setImageResource(R.drawable.b2_close);
		} else if (message.what == MessageConstant.HIDE_SEARCH_VIEW) {
			mFilter.setImageResource(R.drawable.b1_icon_filter);
		} 
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if (EventBus.getDefault().isregister(this)) {
			EventBus.getDefault().unregister(this);
		}
		super.onDestroyView();
	}
}

