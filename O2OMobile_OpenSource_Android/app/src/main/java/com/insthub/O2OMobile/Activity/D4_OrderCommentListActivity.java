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
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BeeFramework.Utils.TimeUtil;
import com.BeeFramework.Utils.Utils;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.view.RoundedWebImageView;
import com.insthub.O2OMobile.O2OMobile;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.nostra13.universalimageloader.core.ImageLoader;

public class D4_OrderCommentListActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mOrderSNTextView;
    private TextView mServiceTypeTextView;
    private TextView mOrderLocationTextView;
    private TextView mOrderPriceTextView;
    private TextView mTransactionTextView;
    private LinearLayout mEmployerView;
    private RoundedWebImageView mEmployerAvatar;
    private TextView mEmployerName;
    private ImageView mEmployerStarOne;
    private ImageView mEmployerStarTwo;
    private ImageView mEmployerStarThree;
    private ImageView mEmployerStarFour;
    private ImageView mEmployerStarFive;
    private TextView mEmployerTime;
    private TextView mEmployerContent;
    private LinearLayout mEmployeeView;
    private RoundedWebImageView employeeAvatar;
    private TextView mEmployee_name;
    private ImageView mEmployeeStarOne;
    private ImageView mEmployeeStarTwo;
    private ImageView mEmployeeStarThree;
    private ImageView mEmployeeStarFour;
    private ImageView mEmployeeStarFive;
    private TextView mEmployeeTime;
    private TextView mEmployeeContent;
    
    private ORDER_INFO mOrderInfo;
    protected ImageLoader mImageLoader = ImageLoader.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d4_order_comment_list);
		
		mOrderInfo = (ORDER_INFO)getIntent().getSerializableExtra(O2OMobileAppConst.ORDERINFO);
		
		mBack = (ImageView) 			findViewById(R.id.top_view_back);
		mTitle = (TextView) 			findViewById(R.id.top_view_title);
        mOrderSNTextView = (TextView)			findViewById(R.id.d17_order_sn);
        mServiceTypeTextView = (TextView)			findViewById(R.id.d17_order_service_type);
        mOrderLocationTextView = (TextView)			findViewById(R.id.d17_order_location);
        mOrderPriceTextView = (TextView)			findViewById(R.id.d17_order_price);
        mTransactionTextView = (TextView)			findViewById(R.id.d17_order_transaction_price);
		
        mEmployerView = (LinearLayout) 		findViewById(R.id.d17_employer_view);
        mEmployerAvatar = (RoundedWebImageView) findViewById(R.id.d17_employer_avatar);
        mEmployerName = (TextView) 			findViewById(R.id.d17_employer_name);
        mEmployerStarOne = (ImageView) 			findViewById(R.id.d17_employer_star_one);
        mEmployerStarTwo = (ImageView) 			findViewById(R.id.d17_employer_star_two);
        mEmployerStarThree = (ImageView) 			findViewById(R.id.d17_employer_star_three);
        mEmployerStarFour = (ImageView) 			findViewById(R.id.d17_employer_star_four);
        mEmployerStarFive = (ImageView) 			findViewById(R.id.d17_employer_star_five);
        mEmployerTime = (TextView) 			findViewById(R.id.d17_employer_time);
        mEmployerContent = (TextView) 			findViewById(R.id.d17_employer_content);
        
        mEmployeeView = (LinearLayout) 		findViewById(R.id.d17_employee_view);
        employeeAvatar = (RoundedWebImageView) findViewById(R.id.d17_employee_avatar);
        mEmployee_name = (TextView) 			findViewById(R.id.d17_employee_name);
        mEmployeeStarOne = (ImageView) 			findViewById(R.id.d17_employee_star_one);
        mEmployeeStarTwo = (ImageView) 			findViewById(R.id.d17_employee_star_two);
        mEmployeeStarThree = (ImageView) 			findViewById(R.id.d17_employee_star_three);
        mEmployeeStarFour = (ImageView) 			findViewById(R.id.d17_employee_star_four);
        mEmployeeStarFive = (ImageView) 			findViewById(R.id.d17_employee_star_five);
        mEmployeeTime = (TextView) 			findViewById(R.id.d17_employee_time);
        mEmployeeContent = (TextView) 			findViewById(R.id.d17_employee_content);
        
		mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
		mTitle.setText(getString(R.string.order_evaluate));
		
		if (null != mOrderInfo) {
            mOrderSNTextView.setText(mOrderInfo.order_sn);
            mServiceTypeTextView.setText(mOrderInfo.service_type.title);
            mOrderLocationTextView.setText(mOrderInfo.location.name);
            mOrderPriceTextView.setText(Utils.formatBalance(mOrderInfo.offer_price) + "元");
            mTransactionTextView.setText(Utils.formatBalance(mOrderInfo.transaction_price) + "元");
        }
		
		// 雇主
		if(mOrderInfo.employer_comment != null && mOrderInfo.employer_comment.id != 0) {
			mEmployerView.setVisibility(View.VISIBLE);
			
			if(mOrderInfo.employer_comment.user != null && mOrderInfo.employer_comment.user.avatar != null
					&& mOrderInfo.employer_comment.user.avatar.thumb != null
					&& !mOrderInfo.employer_comment.user.avatar.thumb.equals("")) {
				mImageLoader.displayImage(mOrderInfo.employer_comment.user.avatar.thumb, mEmployerAvatar, O2OMobile.options_head);
			}
			if(mOrderInfo.employer_comment.user != null && mOrderInfo.employer_comment.user.nickname != null) {
				mEmployerName.setText(mOrderInfo.employer_comment.user.nickname);
			}
			if(mOrderInfo.employer_comment.created_at != null) {
				mEmployerTime.setText(TimeUtil.timeAgo(mOrderInfo.employer_comment.created_at));
			}
			if(mOrderInfo.employer_comment.content.text != null) {
				mEmployerContent.setText(mOrderInfo.employer_comment.content.text);
			}
			setEmployerStar(mOrderInfo.employer_comment.rank);
		}
		
		// 雇员
		if(mOrderInfo.employee_comment != null && mOrderInfo.employee_comment.id != 0) {
			mEmployeeView.setVisibility(View.VISIBLE);
			
			if(mOrderInfo.employee_comment.user != null && mOrderInfo.employee_comment.user.avatar != null
					&& mOrderInfo.employee_comment.user.avatar.thumb != null
					&& !mOrderInfo.employee_comment.user.avatar.thumb.equals("")) {
				mImageLoader.displayImage(mOrderInfo.employee_comment.user.avatar.thumb, employeeAvatar, O2OMobile.options_head);
			}
			if(mOrderInfo.employee_comment.user != null && mOrderInfo.employee_comment.user.nickname != null) {
				mEmployee_name.setText(mOrderInfo.employee_comment.user.nickname);
			}
			if(mOrderInfo.employee_comment.created_at != null) {
				mEmployeeTime.setText(TimeUtil.timeAgo(mOrderInfo.employee_comment.created_at));
			}
			if(mOrderInfo.employee_comment.content.text != null) {
				mEmployeeContent.setText(mOrderInfo.employee_comment.content.text);
			}
			setEmployeeStar(mOrderInfo.employee_comment.rank);
		}
		
	}
	
	private void setEmployerStar(int star) {
		if (star > 0){
			mEmployerStarOne.setImageResource(R.drawable.b7_star_on);
        }
		if(star > 1) {
			mEmployerStarTwo.setImageResource(R.drawable.b7_star_on);
		}
		if(star > 2) {
			mEmployerStarThree.setImageResource(R.drawable.b7_star_on);
		}
		if(star > 3) {
			mEmployerStarFour.setImageResource(R.drawable.b7_star_on);
		}
		if(star > 4) {
			mEmployerStarFive.setImageResource(R.drawable.b7_star_on);
		}
	}
	
	private void setEmployeeStar(int star) {
		if (star > 0){
			mEmployeeStarOne.setImageResource(R.drawable.b7_star_on);
        }
		if(star > 1) {
			mEmployeeStarTwo.setImageResource(R.drawable.b7_star_on);
		}
		if(star > 2) {
			mEmployeeStarThree.setImageResource(R.drawable.b7_star_on);
		}
		if(star > 3) {
			mEmployeeStarFour.setImageResource(R.drawable.b7_star_on);
		}
		if(star > 4) {
			mEmployeeStarFive.setImageResource(R.drawable.b7_star_on);
		}
	}
	
}
