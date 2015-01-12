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

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.O2OMobile.Adapter.FirstCategoryPopAdapter;
import com.insthub.O2OMobile.Adapter.SecondCategoryPopAdapter;
import com.insthub.O2OMobile.Adapter.ServiceTypePopAdapter;
import com.insthub.O2OMobile.Model.ServiceModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.SERVICE_CATEGORY;
import com.insthub.O2OMobile.Protocol.SERVICE_TYPE;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C17_ApplyFormActivity extends BaseActivity implements BusinessResponse, View.OnClickListener {
    private TextView mTitle;
    private ImageView mBack;
    private Button mApplyMoreService;
    private ServiceModel mServiceModel;
    private LinearLayout mServiceType;
    private TextView mServiceTypeTitle;
    private ArrayList<SERVICE_TYPE> mServiceTypeList;
    private PopupWindow mServiceListPopwindow;
    private View mServicePopwindowView;
    private ListView mServicePopwindowListView;
    private ServiceTypePopAdapter mServiceTypePopAdapter;
    private ImageView mServiceTypeArrow;
    private int mServiceTypeId;
    private int mFirstClassServiceCategory;
    private int mSecondVlassServiceCategory;

    private LinearLayout mFirstCategory;
    private TextView mFirstCategoryTitle;
    private ArrayList<SERVICE_CATEGORY> mFirstCategoryList;
    private PopupWindow mFirstCategoryListPopwindow;
    private View mFirstCategoryPopWindowView;
    private ListView mFirstCategoryPopWindowListView;
    private FirstCategoryPopAdapter mFirstCategoryPopAdapter;
    private ImageView mFirstCategoryArrow;

    private LinearLayout mSecondCategory;
    private TextView mSecondCategoryTitle;
    private ArrayList<SERVICE_CATEGORY> mSecondCategoryList;
    private PopupWindow mSecondCategoryListPopwindow;
    private View mSecondCategoryPopWindowView;
    private ListView mSecondCategoryPopWindowListView;
    private SecondCategoryPopAdapter mSecondCategoryPopAdapter;
    private ImageView mSecondCategoryArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c17_apply_form);
        mTitle = (TextView) findViewById(R.id.top_view_title);
        mTitle.setText(getString(R.string.apply_certificate));
        mBack = (ImageView) findViewById(R.id.top_view_back);
        mApplyMoreService = (Button) findViewById(R.id.apply_more_service);
        mServiceType = (LinearLayout) findViewById(R.id.service_type);
        mServiceTypeTitle = (TextView) findViewById(R.id.service_type_title);
        mServiceModel = new ServiceModel(this);
        mServiceModel.addResponseListener(this);
        mBack.setOnClickListener(this);
        mApplyMoreService.setOnClickListener(this);
        mServiceModel.getTypeList();
        mServiceType.setOnClickListener(this);
        mServiceTypeArrow = (ImageView) findViewById(R.id.service_type_arrow);

        mFirstCategory = (LinearLayout) findViewById(R.id.first_category);
        mFirstCategoryTitle = (TextView) findViewById(R.id.first_category_title);
        mFirstCategoryArrow = (ImageView) findViewById(R.id.first_category_arrow);
        mFirstCategory.setOnClickListener(this);

        mSecondCategory = (LinearLayout) findViewById(R.id.second_category);
        mSecondCategoryTitle = (TextView) findViewById(R.id.second_category_title);
        mSecondCategoryArrow = (ImageView) findViewById(R.id.second_category_arrow);
        mSecondCategory.setOnClickListener(this);


    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if (url.endsWith(ApiInterface.USER_APPLY_SERVICE)) {
            ToastView toast = new ToastView(C17_ApplyFormActivity.this, getString(R.string.apply_certificate_success));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            finish();
        } else if (url.endsWith(ApiInterface.SERVICETYPE_LIST)) {
            mServiceTypeList = mServiceModel.publicServiceTypeList;
            mServiceTypeTitle.setText(mServiceTypeList.get(0).title);
            mSecondCategoryTitle.setText("");
            mServiceModel.publicIsSecondCategory=false;
            mServiceTypeId = mServiceTypeList.get(0).id;
            mServiceModel.getCategoryList(mServiceTypeList.get(0).id);
        }else if (url.endsWith(ApiInterface.SERVICECATEGORY_LIST)) {
            if(mServiceModel.publicIsSecondCategory){
                mSecondCategoryList = mServiceModel.publicSecondCategories;
                if(mSecondCategoryList.size()==0){
                    mSecondCategoryTitle.setText(getString(R.string.none));
                    mSecondCategoryArrow.setVisibility(View.INVISIBLE);
                    mSecondVlassServiceCategory =0;
                    mSecondCategory.setClickable(false);
                }else{
                    mFirstCategoryArrow.setVisibility(View.VISIBLE);
                    mSecondCategoryArrow.setVisibility(View.VISIBLE);
                    mFirstCategory.setClickable(true);
                    mSecondCategory.setClickable(true);
                    mSecondCategoryTitle.setText(mSecondCategoryList.get(0).title);
                    mSecondVlassServiceCategory = mSecondCategoryList.get(0).id;
                }
            }else{
                mFirstCategoryList = mServiceModel.publicSecondCategories;
                if(mFirstCategoryList.size()==0){
                    mFirstCategoryTitle.setText(getString(R.string.none));
                    mSecondCategoryTitle.setText(getString(R.string.none));
                    mFirstCategoryArrow.setVisibility(View.INVISIBLE);
                    mSecondCategoryArrow.setVisibility(View.INVISIBLE);
                    mFirstCategory.setClickable(false);
                    mSecondCategory.setClickable(false);
                    mFirstClassServiceCategory =0;
                    mSecondVlassServiceCategory =0;
                }else{
                    mFirstCategoryTitle.setText(mFirstCategoryList.get(0).title);
                    mFirstClassServiceCategory = mFirstCategoryList.get(0).id;
                    mServiceModel.publicIsSecondCategory=true;
                    mServiceModel.getCategoryList(mFirstCategoryList.get(0).id);
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.service_type:
                showPopWindow(v);
                break;
            case R.id.first_category:
                showPopWindow(v);
                break;
            case R.id.second_category:
                showPopWindow(v);
                break;
            case R.id.apply_more_service:
                mServiceModel.applyMore(mServiceTypeId, mFirstClassServiceCategory, mSecondVlassServiceCategory);
                break;
        }
    }

    /**
     * 显示popwindow
     *
     * @param parent
     */
    private void showPopWindow(View parent) {
        int id = parent.getId();
        switch (id) {
            case R.id.service_type:
                if (mServiceListPopwindow == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    mServicePopwindowView = layoutInflater.inflate(R.layout.service_type_popwindow, null);
                    mServicePopwindowListView = (ListView) mServicePopwindowView.findViewById(R.id.service_type_pop_listview);
                    mServiceListPopwindow = new PopupWindow(mServicePopwindowView, parent.getWidth(), AbsoluteLayout.LayoutParams.WRAP_CONTENT, true);

                }
                if(mServiceTypePopAdapter == null)
                {
                    mServiceTypePopAdapter = new ServiceTypePopAdapter(this, mServiceModel.publicServiceTypeList);
                    mServicePopwindowListView.setAdapter(mServiceTypePopAdapter);
                }
                mServiceListPopwindow.setFocusable(true);
                // 设置允许在外点击消失
                mServiceListPopwindow.setOutsideTouchable(true);
                mServiceTypeArrow.setImageResource(R.drawable.b4_arrow_up);
                // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
                mServiceListPopwindow.setBackgroundDrawable(new BitmapDrawable());
                mServiceListPopwindow.showAsDropDown(parent);
                mServiceListPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mServiceTypeArrow.setImageResource(R.drawable.b3_arrow_down);
                    }
                });
                mServicePopwindowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SERVICE_TYPE serviceType = mServiceTypeList.get(position);
                        mServiceTypeId = serviceType.id;
                        mServiceTypeTitle.setText(serviceType.title);
                        mServiceModel.publicIsSecondCategory = false;
                        mServiceModel.getCategoryList(mServiceTypeId);
                        mServiceListPopwindow.dismiss();
                    }
                });
                break;
            case R.id.first_category:
                if (mFirstCategoryListPopwindow == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    mFirstCategoryPopWindowView = layoutInflater.inflate(R.layout.first_category_popwindow, null);
                    mFirstCategoryPopWindowListView = (ListView) mFirstCategoryPopWindowView.findViewById(R.id.first_category_pop_listview);
                    mFirstCategoryListPopwindow = new PopupWindow(mFirstCategoryPopWindowView, parent.getWidth(), AbsoluteLayout.LayoutParams.WRAP_CONTENT, true);

                }
                if(mFirstCategoryPopAdapter == null)
                {
                    mFirstCategoryPopAdapter = new FirstCategoryPopAdapter(this, mFirstCategoryList);
                    mFirstCategoryPopWindowListView.setAdapter(mFirstCategoryPopAdapter);
                }else{
                    mFirstCategoryPopAdapter.publicFirstCategoryList = mFirstCategoryList;
                    mFirstCategoryPopAdapter.notifyDataSetChanged();
                }
                mFirstCategoryListPopwindow.setFocusable(true);
                // 设置允许在外点击消失
                mFirstCategoryListPopwindow.setOutsideTouchable(true);
                mFirstCategoryArrow.setImageResource(R.drawable.b4_arrow_up);
                // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
                mFirstCategoryListPopwindow.setBackgroundDrawable(new BitmapDrawable());
                mFirstCategoryListPopwindow.showAsDropDown(parent);
                mFirstCategoryListPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mFirstCategoryArrow.setImageResource(R.drawable.b3_arrow_down);
                    }
                });
                mFirstCategoryPopWindowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SERVICE_CATEGORY service_category = mFirstCategoryList.get(position);
                        mFirstClassServiceCategory = service_category.id;
                        mFirstCategoryTitle.setText(service_category.title);
                        mServiceModel.publicIsSecondCategory = true;
                        mServiceModel.getCategoryList(mFirstClassServiceCategory);
                        mFirstCategoryListPopwindow.dismiss();
                    }
                });
                break;
            case R.id.second_category:
                if (mSecondCategoryListPopwindow == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    mSecondCategoryPopWindowView = layoutInflater.inflate(R.layout.second_category_popwindow, null);
                    mSecondCategoryPopWindowListView = (ListView) mSecondCategoryPopWindowView.findViewById(R.id.second_category_pop_listview);
                    mSecondCategoryListPopwindow = new PopupWindow(mSecondCategoryPopWindowView, parent.getWidth(), AbsoluteLayout.LayoutParams.WRAP_CONTENT, true);

                }
                if(mSecondCategoryPopAdapter == null)
                {
                    mSecondCategoryPopAdapter = new SecondCategoryPopAdapter(this, mSecondCategoryList);
                    mSecondCategoryPopWindowListView.setAdapter(mSecondCategoryPopAdapter);
                }else{
                    mSecondCategoryPopAdapter.publicSecondCategoryList= mSecondCategoryList;
                    mSecondCategoryPopAdapter.notifyDataSetChanged();
                }

                mSecondCategoryListPopwindow.setFocusable(true);
                // 设置允许在外点击消失
                mSecondCategoryListPopwindow.setOutsideTouchable(true);
                mSecondCategoryArrow.setImageResource(R.drawable.b4_arrow_up);
                // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
                mSecondCategoryListPopwindow.setBackgroundDrawable(new BitmapDrawable());
                mSecondCategoryListPopwindow.showAsDropDown(parent);
                mSecondCategoryListPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mSecondCategoryArrow.setImageResource(R.drawable.b3_arrow_down);
                    }
                });
                mSecondCategoryPopWindowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SERVICE_CATEGORY service_category = mSecondCategoryList.get(position);
                        mSecondVlassServiceCategory = service_category.id;
                        mSecondCategoryTitle.setText(service_category.title);
                        mServiceModel.publicIsSecondCategory = true;
                        mSecondCategoryListPopwindow.dismiss();
                    }
                });
                break;
        }


    }

}
