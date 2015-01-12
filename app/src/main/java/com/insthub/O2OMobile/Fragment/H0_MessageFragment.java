package com.insthub.O2OMobile.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.activity.WebViewActivity;
import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.external.maxwin.view.IXListViewListener;
import com.external.maxwin.view.XListView;
import com.insthub.O2OMobile.Activity.D1_OrderActivity;
import com.insthub.O2OMobile.Activity.MessageDetailActivity;
import com.insthub.O2OMobile.Activity.SlidingActivity;
import com.insthub.O2OMobile.Adapter.H0_MessageAdapter;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.MessageListModel;
import com.insthub.O2OMobile.Model.MessageUnreadCountModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ENUM_MESSAGE_TYPE;
import com.insthub.O2OMobile.Protocol.MESSAGE;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class H0_MessageFragment extends Fragment implements IXListViewListener, BusinessResponse {
	
	private View view;
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

    public static int msg = 0;
    public static int sys_msg = 1;
    private int mCurrentState = 0;
    private MessageUnreadCountModel mMessageUnreadCountModel;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.h0_message_list, null);
		mMenu = (ImageView) view.findViewById(R.id.message_menu);
        mUnreadMessageNum = (TextView) view.findViewById(R.id.unread_message_num);

		mMessagePerson = (TextView) view.findViewById(R.id.message_person);
		mMessageSystem = (TextView) view.findViewById(R.id.message_system);
		
		mListViewPerson = (XListView) view.findViewById(R.id.message_listview);
		mListViewPerson.setXListViewListener(this, 1);
		mListViewPerson.setPullLoadEnable(true);
		mListViewPerson.setRefreshTime();
		
		mListViewSystem = (XListView) view.findViewById(R.id.message_system_listview);
		mListViewSystem.setXListViewListener(this, 2);
		mListViewSystem.setPullLoadEnable(true);
		mListViewSystem.setRefreshTime();

        mMessageUnreadCountModel = new MessageUnreadCountModel(getActivity());
        mMessageUnreadCountModel.addResponseListener(this);

		mMessageListModel = new MessageListModel(getActivity());
		mMessageListModel.addResponseListener(this);

        mMessageListModel.loadCacheMsg();

        if(mMessageListModel.publicMessageList!=null&& mMessageListModel.publicMessageList.size()>0){
            mH0MessageAdapter = new H0_MessageAdapter(getActivity(), mMessageListModel.publicMessageList, true);
            mListViewPerson.setAdapter(mH0MessageAdapter);
            mListViewPerson.loadMoreHide();
        }

//		messageListModel.getMessageList();

		mListViewPerson.setOnItemClickListener(new OnItemClickListener() {

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
                        Intent intent = new Intent(getActivity(), D1_OrderActivity.class);
                        intent.putExtra(D1_OrderActivity.ORDER_ID, message.order_id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    } else if (!"".equals(message.url)) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra(WebViewActivity.WEBURL, message.url);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                }
            }
        });
		mListViewSystem.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                MESSAGE message = mMessageListModel.publicMessageSysList.get(position - 1);
                if (!"".equals(message.url)) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.WEBURL, message.url);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else {
                    Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                    intent.putExtra("message_content", message.content);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

            }
        });
		
		mMessagePerson.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCurrentState = msg;
                mListViewPerson.setVisibility(View.VISIBLE);
                mListViewSystem.setVisibility(View.GONE);
                mMessagePerson.setTextColor(Color.WHITE);
                mMessagePerson.setBackgroundResource(R.drawable.e0_nav_left_selected);
                mMessageSystem.setTextColor(getResources().getColor(R.color.select_item));
                mMessageSystem.setBackgroundResource(R.drawable.e0_nav_right_normal);
                if (mH0MessageAdapter == null) {
                    mMessageListModel.loadCacheMsg();
                    if (mMessageListModel.publicMessageList != null && mMessageListModel.publicMessageList.size() > 0) {
                        mH0MessageAdapter = new H0_MessageAdapter(getActivity(), mMessageListModel.publicMessageList, true);
                        mListViewPerson.setAdapter(mH0MessageAdapter);
                        mListViewPerson.loadMoreHide();
                    }

                    mMessageListModel.getList();
                }
            }
        });
		
		mMessageSystem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mCurrentState = sys_msg;
                mListViewSystem.setVisibility(View.VISIBLE);
                mListViewPerson.setVisibility(View.GONE);
                mMessagePerson.setTextColor(getResources().getColor(R.color.select_item));
                mMessagePerson.setBackgroundResource(R.drawable.e0_nav_left_normal);
                mMessageSystem.setTextColor(Color.WHITE);
                mMessageSystem.setBackgroundResource(R.drawable.e0_nav_right_selected);
                if (mMessageSystemAdapter == null) {

                    mMessageListModel.loadCacheSysMsg();
                    if (mMessageListModel.publicMessageSysList != null && mMessageListModel.publicMessageSysList.size() > 0) {
                        mMessageSystemAdapter = new H0_MessageAdapter(getActivity(), mMessageListModel.publicMessageSysList, false);
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
		return view;
	}

    @Override
    public void onResume() {
        mMessageUnreadCountModel.getMessageUnreadCount();
        if (mCurrentState == msg)
        {
            mMessageListModel.getList();
        }
        else
        {
            mMessageListModel.getSysList();
        }
        super.onResume();
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
	}
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        if (EventBus.getDefault().isregister(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		if(id == 1) {
			mMessageListModel.getList();
		} else {
			mMessageListModel.getSysList();
		}
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
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
			mH0MessageAdapter = new H0_MessageAdapter(getActivity(), mMessageListModel.publicMessageList, true);
			mListViewPerson.setAdapter(mH0MessageAdapter);
		} else {
			mH0MessageAdapter.publicList = mMessageListModel.publicMessageList;
			mH0MessageAdapter.notifyDataSetChanged();
		}
	}
	
	private void setSysAdapterCont() {
        mListViewSystem.loadMoreShow();
		if(mMessageSystemAdapter == null) {
			mMessageSystemAdapter = new H0_MessageAdapter(getActivity(), mMessageListModel.publicMessageSysList, false);
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
            if (mCurrentState == msg)
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

