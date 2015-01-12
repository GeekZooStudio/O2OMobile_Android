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
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.model.BusinessResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.eventbus.EventBus;
import com.insthub.O2OMobile.Activity.SlidingActivity;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.MessageConstant;
import com.insthub.O2OMobile.Model.UserModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;
import com.insthub.O2OMobile.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class F4_RefferalFragment extends Fragment implements BusinessResponse{
     private ImageView mHomeMenu;
     private Button mShare;
     private UserModel mUserModel;
     private TextView mInviteCode;
     private SharedPreferences mShared;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View mainView = inflater.inflate(R.layout.f4_refferal,null);
            mainView.setOnClickListener(null);
            mHomeMenu = (ImageView) mainView.findViewById(R.id.home_menu);
            mShare = (Button) mainView.findViewById(R.id.btn_share);
            mInviteCode = (TextView) mainView.findViewById(R.id.tv_invite_code);
            mUserModel =new UserModel(getActivity());
            mUserModel.addResponseListener(this);
            mShared = getActivity().getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
            String invitecode= mShared.getString("invitecode_"+SESSION.getInstance().uid,"");
            if("".equals(invitecode)){
                mUserModel.getInviteCode();
            }else{
                mInviteCode.setText(invitecode);
            }
            if (!EventBus.getDefault().isregister(this)) {
                EventBus.getDefault().register(this);
            }

            return mainView;

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
        public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
            if(url.endsWith(ApiInterface.USER_INVITE_CODE)){
                mInviteCode.setText(mUserModel.invite_code);
            }

        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHomeMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((SlidingActivity) getActivity()).showLeft();
            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                intent.putExtra(WXEntryActivity.IS_COMMENT, false);
                intent.putExtra(WXEntryActivity.INVITECODE, mInviteCode.getText());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        });
    }
    public void onEvent(Object event) {
        Message message = (Message) event;
        if (message.what == MessageConstant.SIGN_IN_SUCCESS) {
            mUserModel.getInviteCode();
        }
    }
}
