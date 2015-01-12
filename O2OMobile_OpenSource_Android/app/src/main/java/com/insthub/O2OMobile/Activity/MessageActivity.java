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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.activity.WebViewActivity;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Adapter.H0_MessageAdapter;
import com.insthub.O2OMobile.Fragment.A2_MenuFragment;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.MessageListModel;
import com.insthub.O2OMobile.Model.MessageUnreadCountModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_MESSAGE_TYPE;
import com.insthub.O2OMobile.Protocol.MESSAGE;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageActivity extends BaseActivity implements IXListViewListener, BusinessResponse {

    private ImageView mMenu;
    private TextView mMessagePerson;
    private TextView mMessageSystem;
    private XListView mListViewPerson;
    private XListView mListViewSystem;
    private int mMessageId = -1;
    private MessageListModel mMessageListModel;
    private H0_MessageAdapter mH0MessageAdapter;
    private H0_MessageAdapter mMessageSystemAdapter;
    private TextView mUnreadMessageNum;
    private int mUnreadMessageCount;
    public static int MSG = 0;
    public static int SYS_MSG = 1;
    private int mCurrentState = 0;
    private MessageUnreadCountModel mMessageUnreadCountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h0_message_list);
        mMenu = (ImageView) findViewById(R.id.message_menu);
        mMenu.setImageResource(R.drawable.a2_back_button);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUnreadMessageNum = (TextView) findViewById(R.id.unread_message_num);

        mMessagePerson = (TextView) findViewById(R.id.message_person);
        mMessageSystem = (TextView) findViewById(R.id.message_system);

        mListViewPerson = (XListView) findViewById(R.id.message_listview);
        mListViewPerson.setXListViewListener(this, 1);
        mListViewPerson.setPullLoadEnable(true);
        mListViewPerson.setRefreshTime();

        mListViewSystem = (XListView) findViewById(R.id.message_system_listview);
        mListViewSystem.setXListViewListener(this, 2);
        mListViewSystem.setPullLoadEnable(true);
        mListViewSystem.setRefreshTime();

        mMessageUnreadCountModel = new MessageUnreadCountModel(this);
        mMessageUnreadCountModel.addResponseListener(this);

        mMessageListModel = new MessageListModel(this);
        mMessageListModel.addResponseListener(this);

        mMessageListModel.loadCacheMsg();

        if(mMessageListModel.publicMessageList!=null&& mMessageListModel.publicMessageList.size()>0){
            mH0MessageAdapter = new H0_MessageAdapter(this, mMessageListModel.publicMessageList, true);
            mListViewPerson.setAdapter(mH0MessageAdapter);
            mListViewPerson.loadMoreHide();
        }

        mListViewPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                MESSAGE message = mMessageListModel.publicMessageList.get(position - 1);
                mMessageId = message.id;
                if (message.is_readed == 0) {
                    mMessageListModel.read(mMessageId);
                }
                if (message.type == ENUM_MESSAGE_TYPE.ORDER.value()) {
                    if (message.order_id != 0) {
                        Intent intent = new Intent(MessageActivity.this, D1_OrderActivity.class);
                        intent.putExtra(D1_OrderActivity.ORDER_ID, message.order_id);
                        startActivity(intent);
                        MessageActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else if (!"".equals(message.url)) {
                        Intent intent = new Intent(MessageActivity.this, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.WEBURL, message.url);
                        startActivity(intent);
                        MessageActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                }
            }
        });
        mListViewSystem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                MESSAGE message = mMessageListModel.publicMessageSysList.get(position - 1);
                if (!"".equals(message.url)) {
                    Intent intent = new Intent(MessageActivity.this, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.WEBURL, message.url);
                    startActivity(intent);
                    MessageActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else {
                    Intent intent = new Intent(MessageActivity.this, MessageDetailActivity.class);
                    intent.putExtra("message_content", message.content);
                    startActivity(intent);
                    MessageActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

            }
        });

        mMessagePerson.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCurrentState = MSG;
                mListViewPerson.setVisibility(View.VISIBLE);
                mListViewSystem.setVisibility(View.GONE);
                mMessagePerson.setTextColor(Color.WHITE);
                mMessagePerson.setBackgroundResource(R.drawable.e0_nav_left_selected);
                mMessageSystem.setTextColor(getResources().getColor(R.color.select_item));
                mMessageSystem.setBackgroundResource(R.drawable.e0_nav_right_normal);
                if (mH0MessageAdapter == null) {
                    mMessageListModel.loadCacheMsg();
                    if (mMessageListModel.publicMessageList != null && mMessageListModel.publicMessageList.size() > 0) {
                        mH0MessageAdapter = new H0_MessageAdapter(MessageActivity.this, mMessageListModel.publicMessageList, true);
                        mListViewPerson.setAdapter(mH0MessageAdapter);
                        mListViewPerson.loadMoreHide();
                    }

                    mMessageListModel.getList();
                }
            }
        });

        mMessageSystem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCurrentState = SYS_MSG;
                mListViewSystem.setVisibility(View.VISIBLE);
                mListViewPerson.setVisibility(View.GONE);
                mMessagePerson.setTextColor(getResources().getColor(R.color.select_item));
                mMessagePerson.setBackgroundResource(R.drawable.e0_nav_left_normal);
                mMessageSystem.setTextColor(Color.WHITE);
                mMessageSystem.setBackgroundResource(R.drawable.e0_nav_right_selected);
                if (mMessageSystemAdapter == null) {

                    mMessageListModel.loadCacheSysMsg();
                    if (mMessageListModel.publicMessageSysList != null && mMessageListModel.publicMessageSysList.size() > 0) {
                        mMessageSystemAdapter = new H0_MessageAdapter(MessageActivity.this, mMessageListModel.publicMessageSysList, false);
                        mListViewSystem.setAdapter(mMessageSystemAdapter);
                        mListViewSystem.loadMoreHide();
                    }
                    mMessageListModel.getSysList();
                }
            }
        });
        if (!EventBus.getDefault().isregister(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onResume() {
        mMessageUnreadCountModel.getMessageUnreadCount();
        if (mCurrentState == MSG)
        {
            mMessageListModel.getList();
        }
        else
        {
            mMessageListModel.getSysList();
        }
        super.onResume();
    }

    protected void onDestroy() {
        if (EventBus.getDefault().isregister(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh(int id) {
        if(id == 1) {
            mMessageListModel.getList();
        } else {
            mMessageListModel.getSysList();
        }
    }

    @Override
    public void onLoadMore(int id) {
        if(id == 1) {
            mMessageListModel.getListMore();
        } else {
            mMessageListModel.getMessageSysListMore();
        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        // TODO Auto-generated method stub
        mListViewPerson.stopRefresh();
        mListViewPerson.stopLoadMore();
        mListViewSystem.stopRefresh();
        mListViewSystem.stopLoadMore();
        if(url.endsWith(ApiInterface.MESSAGE_LIST)) {
            setAdapterCont();
            if(mMessageListModel.publicMore == 0) {
                mListViewPerson.setPullLoadEnable(false);
            } else {
                mListViewPerson.setPullLoadEnable(true);
            }
        } else if(url.endsWith(ApiInterface.MESSAGE_READ)) {
            Message msg = new Message();
            msg.what = MessageConstant.MESSAGE_READ;
            EventBus.getDefault().post(msg);
            for(int i = 0; i < mMessageListModel.publicMessageList.size(); i++) {
                if(mMessageListModel.publicMessageList.get(i).id == mMessageId) {
                    mMessageListModel.publicMessageList.get(i).is_readed = 1;
                    break;
                }
            }
            mH0MessageAdapter.publicList = mMessageListModel.publicMessageList;
            mH0MessageAdapter.notifyDataSetChanged();
            setMessageUnreadCount();
        } else if(url.endsWith(ApiInterface.MESSAGE_SYSLIST)) {
            setSysAdapterCont();
            if(mMessageListModel.publicMoreSys == 0) {
                mListViewSystem.setPullLoadEnable(false);
            } else {
                mListViewSystem.setPullLoadEnable(true);
            }
        }else if(url.endsWith(ApiInterface.MESSAGE_UNREAD_COUNT)) {
            mUnreadMessageCount = mMessageUnreadCountModel.publicUnreadCount;
            setMessageUnreadCount();
        }
    }

    private void setAdapterCont() {
        mListViewPerson.loadMoreShow();
        if(mH0MessageAdapter == null) {
            mH0MessageAdapter = new H0_MessageAdapter(MessageActivity.this, mMessageListModel.publicMessageList, true);
            mListViewPerson.setAdapter(mH0MessageAdapter);
        } else {
            mH0MessageAdapter.publicList = mMessageListModel.publicMessageList;
            mH0MessageAdapter.notifyDataSetChanged();
        }
    }

    private void setSysAdapterCont() {
        mListViewSystem.loadMoreShow();
        if(mMessageSystemAdapter == null) {
            mMessageSystemAdapter = new H0_MessageAdapter(MessageActivity.this, mMessageListModel.publicMessageSysList, false);
            mListViewSystem.setAdapter(mMessageSystemAdapter);
        } else {
            mMessageSystemAdapter.publicList = mMessageListModel.publicMessageSysList;
            mMessageSystemAdapter.notifyDataSetChanged();
        }
    }
    private void setMessageUnreadCount() {
        if(mUnreadMessageCount <= 0) {
            mUnreadMessageNum.setVisibility(View.GONE);
        } else {
            mUnreadMessageNum.setVisibility(View.VISIBLE);
            if(mUnreadMessageCount < 100) {
                mUnreadMessageNum.setText(A2_MenuFragment.publicMessageUnreadCount + "");
            } else {
                mUnreadMessageNum.setText("99+");
            }
        }
    }
    public void onEvent(Object event) {
        Message message = (Message) event;
        if (message.what == MessageConstant.SIGN_IN_SUCCESS) {
            if (mCurrentState == MSG)
            {
                mMessageListModel.getList();
            }
            else
            {
                mMessageListModel.getSysList();
            }
        }
    }
}
