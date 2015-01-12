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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BeeFramework.Utils.Utils;
import com.BeeFramework.activity.BaseActivity;
import com.BeeFramework.model.BusinessResponse;
import com.BeeFramework.view.ToastView;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.Model.CommentModel;
import com.insthub.O2OMobile.Protocol.ApiInterface;
import com.insthub.O2OMobile.Protocol.ORDER_INFO;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class D3_OrderCommentActivity extends BaseActivity implements OnClickListener, BusinessResponse {

    private TextView mOrderSNTextView;
    private TextView mServiceTypeTextView;
    private TextView mOrderLocationTextView;
    private TextView mOrderPriceTextView;
    private TextView mTransactionTextView;
    private ImageView mStarOne;
    private ImageView mStarTwo;
    private ImageView mStarThree;
    private ImageView mStarFour;
    private ImageView mStarFive;
    private TextView mAddCommentTextView;
    private TextView mTitleTextView;
    private EditText mCommentContent;
    private ImageView mBack;
    private CommentModel mCommentModel;
    private int mCommentRank = 0;
    private ORDER_INFO mOrder_info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d3_order_comment);

        mOrder_info = (ORDER_INFO)getIntent().getSerializableExtra(O2OMobileAppConst.ORDERINFO);

        mOrderSNTextView = (TextView)findViewById(R.id.d12_order_sn);

        mServiceTypeTextView = (TextView)findViewById(R.id.d12_order_service_type);

        mOrderLocationTextView = (TextView)findViewById(R.id.d12_order_location);

        mOrderPriceTextView = (TextView)findViewById(R.id.d12_order_price);

        mTransactionTextView = (TextView)findViewById(R.id.d12_order_transaction_price);

        mBack = (ImageView) findViewById(R.id.top_view_back);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        mTitleTextView = (TextView)findViewById(R.id.top_view_title);
        mTitleTextView.setText(getString(R.string.send_evaluation));
        mAddCommentTextView = (TextView)findViewById(R.id.d12_publish_button);
        mAddCommentTextView.setOnClickListener(this);

        if (null != mOrder_info)
        {
            mOrderSNTextView.setText(mOrder_info.order_sn);
            mServiceTypeTextView.setText(mOrder_info.service_type.title);
            mOrderLocationTextView.setText(mOrder_info.location.name);
            mOrderPriceTextView.setText(Utils.formatBalance(mOrder_info.offer_price) + "元");
            mTransactionTextView.setText(Utils.formatBalance(mOrder_info.transaction_price) + "元");
        }

        mCommentContent = (EditText)findViewById(R.id.e12_comment_content);

        mStarOne = (ImageView)findViewById(R.id.d12_star_one);
        mStarOne.setOnClickListener(this);
        mStarTwo = (ImageView)findViewById(R.id.d12_star_two);
        mStarTwo.setOnClickListener(this);
        mStarThree = (ImageView)findViewById(R.id.d12_star_three);
        mStarThree.setOnClickListener(this);
        mStarFour = (ImageView)findViewById(R.id.d12_star_four);
        mStarFour.setOnClickListener(this);
        mStarFive = (ImageView)findViewById(R.id.d12_star_five);
        mStarFive.setOnClickListener(this);

        mCommentModel = new CommentModel(this);
        mCommentModel.addResponseListener(this);


    }

    @Override
    public void onClick(View v)
    {
       switch (v.getId())
       {
           case R.id.d12_star_one:
               selectRank(1);
               break;
           case R.id.d12_star_two:
               selectRank(2);
               break;
           case R.id.d12_star_three:
               selectRank(3);
               break;
           case R.id.d12_star_four:
               selectRank(4);
               break;
           case R.id.d12_star_five:
               selectRank(5);
               break;
           case R.id.d12_publish_button:
           {
               if (mCommentRank == 0)
               {
                   ToastView toast = new ToastView(this, getString(R.string.please_choose_servic_evaluation));
                   toast.setGravity(Gravity.CENTER, 0, 0);
                   toast.show();
               }
               else
               {
                   if (null != mOrder_info)
                   {
                       mCommentModel.comment(mCommentContent.getText().toString(), mCommentRank, mOrder_info.id);
                   }
                   else
                   {
                       mCommentModel.comment(mCommentContent.getText().toString(), mCommentRank, 1);
                   }

               }
           }
       }
    }

    private void selectRank(int rank)
    {
        mCommentRank = rank;
        mStarOne.setImageResource(R.drawable.b7_star_off);
        mStarTwo.setImageResource(R.drawable.b7_star_off);
        mStarThree.setImageResource(R.drawable.b7_star_off);
        mStarFour.setImageResource(R.drawable.b7_star_off);
        mStarFive.setImageResource(R.drawable.b7_star_off);
        if (rank > 0)
        {
            mStarOne.setImageResource(R.drawable.b7_star_on);
        }
        if (rank > 1)
        {
            mStarTwo.setImageResource(R.drawable.b7_star_on);
        }
        if (rank > 2)
        {
            mStarThree.setImageResource(R.drawable.b7_star_on);
        }
        if (rank > 3)
        {
            mStarFour.setImageResource(R.drawable.b7_star_on);
        }
        if (rank > 4)
        {
            mStarFive.setImageResource(R.drawable.b7_star_on);
        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if(url.endsWith(ApiInterface.COMMENT_SEND))
        {
            ToastView toast = new ToastView(this, getString(R.string.evaluate_success));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Intent intentResult = new Intent();
            intentResult.putExtra(O2OMobileAppConst.ORDERINFO, mCommentModel.publicOrder);
			setResult(Activity.RESULT_OK, intentResult);  
            finish();
            Intent intent = new Intent(this, D3_OrderCommentCompleteActivity.class);
            intent.putExtra("order_id", mOrder_info.id);
            startActivity(intent);
        }
    }
}
