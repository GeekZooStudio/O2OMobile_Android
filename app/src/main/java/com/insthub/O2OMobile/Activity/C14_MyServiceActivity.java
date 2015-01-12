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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.Utils.ImageUtil;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Adapter.C14_MyServiceAdapter;
import com.insthub.O2OMobile.Model.UserBalanceModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.MY_SERVICE;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

public class C14_MyServiceActivity extends BaseActivity implements BusinessResponse, View.OnClickListener ,IXListViewListener {
    private TextView mTitle;
    private ImageView mBack;
    private UserBalanceModel mUserBalanceModel;
    private XListView mMyServiceListView;
    private C14_MyServiceAdapter mF4ServiceListAdapter;
    private TextView mEmptyView;
    private ImageView mE0EmptyView;
    private static  final int REQUEST_MODIFY_SERVICE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.c14_my_service);
        mTitle = (TextView) findViewById(R.id.top_view_title);
        mTitle.setText(getString(R.string.my_service));
        mBack = (ImageView) findViewById(R.id.top_view_back);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mBack.setOnClickListener(this);
        mE0EmptyView = (ImageView) findViewById(R.id.e0_empty_view);
        mE0EmptyView.setOnClickListener(this);
        mMyServiceListView = (XListView) findViewById(R.id.my_service_listView);
        mMyServiceListView.setXListViewListener(this, 0);
        mMyServiceListView.setPullLoadEnable(true);
        mMyServiceListView.setRefreshTime();
        mUserBalanceModel = new UserBalanceModel(this);
        mUserBalanceModel.addResponseListener(this);
        mUserBalanceModel.getServiceList(SESSION.getInstance().uid);
        mMyServiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MY_SERVICE my_service = mUserBalanceModel.publicMyServiceList.get(position - 1);
                Intent intent = new Intent(C14_MyServiceActivity.this, C15_EditPriceActivity.class);
                intent.putExtra(C15_EditPriceActivity.SERVICE, my_service);
                startActivityForResult(intent, REQUEST_MODIFY_SERVICE);

            }
        });

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if(url.endsWith(ApiInterface.MYSERVICE_LIST)){
            mMyServiceListView.stopRefresh();
            mMyServiceListView.stopLoadMore();
            if(jo==null){
                mE0EmptyView.setVisibility(View.VISIBLE);
            }else{
                mE0EmptyView.setVisibility(View.GONE);
                setAdapterCont();
                if(mUserBalanceModel.publicMore == 0) {
                    mMyServiceListView.setPullLoadEnable(false);
                } else {
                    mMyServiceListView.setPullLoadEnable(true);
                }
                if (mUserBalanceModel.publicMyServiceList.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mMyServiceListView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    mMyServiceListView.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    private void setAdapterCont() {
        if(mF4ServiceListAdapter == null) {
            mF4ServiceListAdapter = new C14_MyServiceAdapter(this, mUserBalanceModel.publicMyServiceList);
            mMyServiceListView.setAdapter(mF4ServiceListAdapter);
        } else {
            mF4ServiceListAdapter.publicList = mUserBalanceModel.publicMyServiceList;
            mF4ServiceListAdapter.notifyDataSetChanged();
        }
        if(mUserBalanceModel.publicMore==1){
            View foot=new View(this);
            foot.setLayoutParams(new AbsListView.LayoutParams(mMyServiceListView.getMeasuredWidth(), ImageUtil.Dp2Px(this, 48)));
            mMyServiceListView.addFooterView(foot);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
//Todo
            case R.id.e0_empty_view:
                mUserBalanceModel.getServiceList(SESSION.getInstance().uid);
                break;

        }
    }

    @Override
    public void onRefresh(int id) {
        mUserBalanceModel.getServiceList(SESSION.getInstance().uid);
    }

    @Override
    public void onLoadMore(int id) {
        mUserBalanceModel.getServiceListMore(SESSION.getInstance().uid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MODIFY_SERVICE)
        {
            if (data != null)
            {
                mUserBalanceModel.getServiceList(SESSION.getInstance().uid);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}