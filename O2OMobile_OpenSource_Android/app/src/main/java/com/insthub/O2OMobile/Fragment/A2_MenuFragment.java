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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.BeeFramework.Utils.Utils;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.RoundedWebImageView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.Activity.F0_ProfileActivity;
import com.insthub.O2OMobile.Activity.SlidingActivity;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.MessageUnreadCountModel;
import com.insthub.O2OMobile.Model.UserBalanceModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.USER;
import com.insthub.O2OMobile.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class A2_MenuFragment extends Fragment implements OnClickListener, BusinessResponse {
	private A0_HomeFragment mA0HomeFragment;

    private E0_PublishedOrdersFragment mMyOrderListFragment;
    private H0_MessageFragment mH0MessageFragment;
    private F4_RefferalFragment mG2RefferalShareFragment;
    private E2_ReceivedOrdersFragment mReceivedOrderListFragment;
	
	private View mHeaderView;
	private ListView mListView;
	
	private RoundedWebImageView mAvatar;
	private TextView mUserName;
	private TextView mIncome;
	private LinearLayout mHome;
	private TextView mHomeText;
	private ImageView mHomeArrow;
	private LinearLayout mIssuance;
	private TextView mIssuanceText;
	private ImageView mIssuanceArrow;
	private LinearLayout mOrderReceiving;
	private TextView mOrderReceivingText;
	private ImageView mOrderReceivingArrow;
	private LinearLayout mMessage;
	private TextView mMessageText;
	private TextView mMessageNum;
	private ImageView mMessageArrow;
	private LinearLayout mInvite;
	private TextView mInviteText;
	private ImageView mInviteArrow;
    private SharedPreferences mShared;
	private UserBalanceModel mUserBalance;
	private MessageUnreadCountModel mMessageUnreadCountModel;
    private UserBalanceModel mUserBalanceModel;
    private USER mUser;
    protected ImageLoader publicImageLoader = ImageLoader.getInstance();
    
    public int publucLastSelectedMenu = -1;
    public static int publicMessageUnreadCount = 0;
    private ImageView mHomeIco;
    private ImageView mIssureIcon;
    private ImageView mReceiveIcon;
    private ImageView mMessageIcon;
    private ImageView mInviteIcon;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a2_menu, null);
        mHeaderView = inflater.inflate(R.layout.a2_menu_header, null);
        mListView = (ListView) view.findViewById(R.id.menu_listview);

        mAvatar = (RoundedWebImageView) mHeaderView.findViewById(R.id.left_avatar);
        mUserName = (TextView) mHeaderView.findViewById(R.id.left_userName);
        mIncome = (TextView) mHeaderView.findViewById(R.id.left_income);
        mHome = (LinearLayout) mHeaderView.findViewById(R.id.left_home);
        mHomeText = (TextView) mHeaderView.findViewById(R.id.left_home_text);
        mHomeArrow = (ImageView) mHeaderView.findViewById(R.id.left_home_arrow);
        mIssuance = (LinearLayout) mHeaderView.findViewById(R.id.left_issuance);
        mIssuanceText = (TextView) mHeaderView.findViewById(R.id.left_issuance_text);
        mIssuanceArrow = (ImageView) mHeaderView.findViewById(R.id.left_issuance_arrow);
        mOrderReceiving = (LinearLayout) mHeaderView.findViewById(R.id.left_order_receiving);
        mOrderReceivingText = (TextView) mHeaderView.findViewById(R.id.left_order_receiving_text);
        mOrderReceivingArrow = (ImageView) mHeaderView.findViewById(R.id.left_order_receiving_arrow);
        mMessage = (LinearLayout) mHeaderView.findViewById(R.id.left_message);
        mMessageText = (TextView) mHeaderView.findViewById(R.id.left_message_text);
        mMessageNum = (TextView) mHeaderView.findViewById(R.id.left_message_num);
        mMessageArrow = (ImageView) mHeaderView.findViewById(R.id.left_message_arrow);
        mInvite = (LinearLayout) mHeaderView.findViewById(R.id.left_invite);
        mInviteText = (TextView) mHeaderView.findViewById(R.id.left_invite_text);
        mInviteArrow = (ImageView) mHeaderView.findViewById(R.id.left_invite_arrow);
        mHomeIco= (ImageView) mHeaderView.findViewById(R.id.home);
        mIssureIcon= (ImageView) mHeaderView.findViewById(R.id.issure);
        mReceiveIcon= (ImageView) mHeaderView.findViewById(R.id.receive);
        mMessageIcon= (ImageView) mHeaderView.findViewById(R.id.message);
        mInviteIcon= (ImageView) mHeaderView.findViewById(R.id.invite);
        mListView.addHeaderView(mHeaderView);
        mListView.setAdapter(null);
        view.setOnClickListener(this);
        mUserBalance = new UserBalanceModel(getActivity());
        mUserBalance.addResponseListener(this);

		mUserBalance.get();
		mMessageUnreadCountModel = new MessageUnreadCountModel(getActivity());
		mMessageUnreadCountModel.addResponseListener(this);


        mUserBalanceModel = new UserBalanceModel(getActivity());
        mUserBalanceModel.addResponseListener(this);
		
		if (!EventBus.getDefault().isregister(this)) {
            EventBus.getDefault().register(this);
        }
		
		return view;
    }
   
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		publucLastSelectedMenu = mHome.getId();
		mHome.setOnClickListener(this);
		mIssuance.setOnClickListener(this);
		mOrderReceiving.setOnClickListener(this);
		mMessage.setOnClickListener(this);
		mInvite.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.left_home:
			changeTextColor(mHome);
			if(publucLastSelectedMenu == R.id.left_home) {
				if (getActivity() instanceof SlidingActivity) {
	                SlidingActivity slidingActivity = (SlidingActivity) getActivity();
	                slidingActivity.showLeft();
	            }
			} else {
				mA0HomeFragment = new A0_HomeFragment();
	            switchFragment((Fragment) mA0HomeFragment, true);
			}
			break;
		case R.id.left_issuance:
			changeTextColor(mIssuance);
			if(publucLastSelectedMenu == R.id.left_issuance) {
				if (getActivity() instanceof SlidingActivity) {
	                SlidingActivity slidingActivity = (SlidingActivity) getActivity();
	                slidingActivity.showLeft();
	            }
			} else {
				mMyOrderListFragment = new E0_PublishedOrdersFragment();
	            switchFragment((Fragment) mMyOrderListFragment, true);
			}
			break;
		case R.id.left_order_receiving:
			changeTextColor(mOrderReceiving);
			if(publucLastSelectedMenu == R.id.left_order_receiving) {
				if (getActivity() instanceof SlidingActivity) {
	                SlidingActivity slidingActivity = (SlidingActivity) getActivity();
	                slidingActivity.showLeft();
	            }
			} else {
                mReceivedOrderListFragment = new E2_ReceivedOrdersFragment();
	            switchFragment((Fragment) mReceivedOrderListFragment, true);
			}
			break;
		case R.id.left_message:
			changeTextColor(mMessage);
			if(publucLastSelectedMenu == R.id.left_message) {
				if (getActivity() instanceof SlidingActivity) {
	                SlidingActivity slidingActivity = (SlidingActivity) getActivity();
	                slidingActivity.showLeft();
	            }
			} else {
				mH0MessageFragment = new H0_MessageFragment();
	            switchFragment((Fragment) mH0MessageFragment, true);
			}
			break;
		case R.id.left_invite:
			changeTextColor(mInvite);
			if(publucLastSelectedMenu == R.id.left_invite)
            {
				if (getActivity() instanceof SlidingActivity)
                {
	                SlidingActivity slidingActivity = (SlidingActivity) getActivity();
	                slidingActivity.showLeft();
	            }
			}
            else
            {
                mG2RefferalShareFragment = new F4_RefferalFragment();
                switchFragment((Fragment) mG2RefferalShareFragment, true);


            }
			break;
        case R.id.left_avatar:
            Intent intent_profile=new Intent(getActivity(), F0_ProfileActivity.class);
            intent_profile.putExtra(F0_ProfileActivity.USER_ID, mUser.id);
            startActivity(intent_profile);
            getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            break;
		}
		publucLastSelectedMenu = v.getId();
	}
	
	private void changeTextColor(View view) {
		if(view == mHome) {
            mHomeIco.setImageResource(R.drawable.a3_ico_home_selected);
            mIssureIcon.setImageResource(R.drawable.a3_ico_issue);
            mReceiveIcon.setImageResource(R.drawable.a3_ico_receive);
            mInviteIcon.setImageResource(R.drawable.a3_ico_friends);
            mMessageIcon.setImageResource(R.drawable.a3_ico_message);


			mHomeText.setTextColor(Color.parseColor("#B3EF69"));
			mIssuanceText.setTextColor(Color.parseColor("#FAFCFD"));
			mOrderReceivingText.setTextColor(Color.parseColor("#FAFCFD"));
			mMessageText.setTextColor(Color.parseColor("#FAFCFD"));
			mInviteText.setTextColor(Color.parseColor("#FAFCFD"));
			
			mHomeArrow.setImageResource(R.drawable.ico_right_green);
			mIssuanceArrow.setImageResource(R.drawable.ico_right_grey);
			mOrderReceivingArrow.setImageResource(R.drawable.ico_right_grey);
			mMessageArrow.setImageResource(R.drawable.ico_right_grey);
			mInviteArrow.setImageResource(R.drawable.ico_right_grey);
		} else if(view == mIssuance) {
            mHomeIco.setImageResource(R.drawable.a3_ico_home);
            mIssureIcon.setImageResource(R.drawable.a3_ico_issue_selected);
            mReceiveIcon.setImageResource(R.drawable.a3_ico_receive);
            mInviteIcon.setImageResource(R.drawable.a3_ico_friends);
            mMessageIcon.setImageResource(R.drawable.a3_ico_message);

            mHomeText.setTextColor(Color.parseColor("#FAFCFD"));
            mIssuanceText.setTextColor(Color.parseColor("#B3EF69"));
            mOrderReceivingText.setTextColor(Color.parseColor("#FAFCFD"));
            mMessageText.setTextColor(Color.parseColor("#FAFCFD"));
            mInviteText.setTextColor(Color.parseColor("#FAFCFD"));

            mHomeArrow.setImageResource(R.drawable.ico_right_grey);
            mIssuanceArrow.setImageResource(R.drawable.ico_right_green);
            mOrderReceivingArrow.setImageResource(R.drawable.ico_right_grey);
            mMessageArrow.setImageResource(R.drawable.ico_right_grey);
            mInviteArrow.setImageResource(R.drawable.ico_right_grey);
		}  else if(view == mOrderReceiving) {
            mHomeIco.setImageResource(R.drawable.a3_ico_home);
            mIssureIcon.setImageResource(R.drawable.a3_ico_issue);
            mReceiveIcon.setImageResource(R.drawable.a3_ico_receive_selected);
            mInviteIcon.setImageResource(R.drawable.a3_ico_friends);
            mMessageIcon.setImageResource(R.drawable.a3_ico_message);

            mHomeText.setTextColor(Color.parseColor("#FAFCFD"));
            mIssuanceText.setTextColor(Color.parseColor("#FAFCFD"));
            mOrderReceivingText.setTextColor(Color.parseColor("#B3EF69"));
            mMessageText.setTextColor(Color.parseColor("#FAFCFD"));
            mInviteText.setTextColor(Color.parseColor("#FAFCFD"));

            mHomeArrow.setImageResource(R.drawable.ico_right_green);
            mIssuanceArrow.setImageResource(R.drawable.ico_right_green);
            mOrderReceivingArrow.setImageResource(R.drawable.ico_right_green);
            mMessageArrow.setImageResource(R.drawable.ico_right_grey);
            mInviteArrow.setImageResource(R.drawable.ico_right_grey);
		} else if(view == mMessage) {
            mHomeIco.setImageResource(R.drawable.a3_ico_home);
            mIssureIcon.setImageResource(R.drawable.a3_ico_issue);
            mReceiveIcon.setImageResource(R.drawable.a3_ico_receive);
            mInviteIcon.setImageResource(R.drawable.a3_ico_friends);
            mMessageIcon.setImageResource(R.drawable.a3_ico_message_selected);

            mHomeText.setTextColor(Color.parseColor("#FAFCFD"));
            mIssuanceText.setTextColor(Color.parseColor("#FAFCFD"));
            mOrderReceivingText.setTextColor(Color.parseColor("#FAFCFD"));
            mMessageText.setTextColor(Color.parseColor("#B3EF69"));
            mInviteText.setTextColor(Color.parseColor("#FAFCFD"));

            mHomeArrow.setImageResource(R.drawable.ico_right_grey);
            mIssuanceArrow.setImageResource(R.drawable.ico_right_grey);
            mOrderReceivingArrow.setImageResource(R.drawable.ico_right_grey);
            mMessageArrow.setImageResource(R.drawable.ico_right_green);
            mInviteArrow.setImageResource(R.drawable.ico_right_grey);
		} else if(view == mInvite) {
            mHomeIco.setImageResource(R.drawable.a3_ico_home);
            mIssureIcon.setImageResource(R.drawable.a3_ico_issue);
            mReceiveIcon.setImageResource(R.drawable.a3_ico_receive);
            mInviteIcon.setImageResource(R.drawable.a3_ico_friends_selected);
            mMessageIcon.setImageResource(R.drawable.a3_ico_message);

            mHomeText.setTextColor(Color.parseColor("#FAFCFD"));
            mIssuanceText.setTextColor(Color.parseColor("#FAFCFD"));
            mOrderReceivingText.setTextColor(Color.parseColor("#FAFCFD"));
            mMessageText.setTextColor(Color.parseColor("#FAFCFD"));
            mInviteText.setTextColor(Color.parseColor("#B3EF69"));

            mHomeArrow.setImageResource(R.drawable.ico_right_grey);
            mIssuanceArrow.setImageResource(R.drawable.ico_right_grey);
            mOrderReceivingArrow.setImageResource(R.drawable.ico_right_grey);
            mMessageArrow.setImageResource(R.drawable.ico_right_grey);
            mInviteArrow.setImageResource(R.drawable.ico_right_green);
		}
	}

    public void switchFragment(Fragment fragment, boolean showLeft) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof SlidingActivity) {
        	SlidingActivity slidingActivity = (SlidingActivity) getActivity();
            slidingActivity.switchContent(fragment);
            if (showLeft) {
            	slidingActivity.isShowLeft(true);
                slidingActivity.showLeft();
            }
        }
    }

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// TODO Auto-generated method stub
		if(url.endsWith(ApiInterface.USER_BALANCE)) {
            //int balanceInt = Float.valueOf(userBalance.balance).intValue();
			String balance = Utils.formatBalance(mUserBalance.publicBalance);
			mIncome.setText(getString(R.string.balance) + balance + getString(R.string.yuan));
		} else if(url.endsWith(ApiInterface.MESSAGE_UNREAD_COUNT)) {
			publicMessageUnreadCount = mMessageUnreadCountModel.publicUnreadCount;
			setMessageUnreadCount();
		}
        else if (url.endsWith(ApiInterface.USER_PROFILE))
        {
            String userStr = mShared.getString("user", "");
            try
            {
                if (userStr != null)
                {
                    JSONObject userJson = new JSONObject(userStr);
                    mUser = new USER();
                    mUser.fromJson(userJson);
                    publicImageLoader.displayImage(mUser.avatar.thumb, mAvatar, O2OMobile.options_head);
                    mUserName.setText(Utils.replaceBlank(mUser.nickname));

                }
            }
            catch (Exception e)
            {

            }
        }
	}

	private void setMessageUnreadCount() {
		if(publicMessageUnreadCount <= 0) {
			mMessageNum.setVisibility(View.GONE);
		} else {
			mMessageNum.setVisibility(View.VISIBLE);
			if(publicMessageUnreadCount < 100) {
				mMessageNum.setText(publicMessageUnreadCount + "");
			} else {
				mMessageNum.setText("99+");
			}
		}
	}
	
    public void onEvent(Object event) {
    	Message message = (Message)event;
    	if(message.what == MessageConstant.MESSAGE_READ) {
    		publicMessageUnreadCount = publicMessageUnreadCount - 1;
    		setMessageUnreadCount();
        }
        else if (message.what == MessageConstant.APPLY_FREEMAN)
        {
            mUserBalanceModel.getProfile(mUser.id);
            changeTextColor(mHome);
            switchFragment(mA0HomeFragment,true);
        }
        else if (message.what == MessageConstant.SIGN_IN_SUCCESS)
        {
            mMessageUnreadCountModel.getMessageUnreadCount();
        }
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
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	mUserBalance.get();
    	mMessageUnreadCountModel.getMessageUnreadCount();
    	
    	mShared = getActivity().getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        String userStr = mShared.getString("user", "");
		try {
			if (userStr != null) {
				JSONObject userJson = new JSONObject(userStr);
				mUser = new USER();
				mUser.fromJson(userJson);
				publicImageLoader.displayImage(mUser.avatar.thumb, mAvatar, O2OMobile.options_head);
				mUserName.setText(Utils.replaceBlank(mUser.nickname));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
	
}
