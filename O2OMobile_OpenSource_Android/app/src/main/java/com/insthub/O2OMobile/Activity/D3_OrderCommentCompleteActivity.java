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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.BeeFramework.activity.BaseActivity;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.wxapi.WXEntryActivity;

public class D3_OrderCommentCompleteActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTitle;
    private ImageView mBack;
    private Button mBtnBack;
    private LinearLayout mShareComment;
    private int mOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d3_order_comment_complete);
        mOrderId =getIntent().getIntExtra("order_id",0);
        mTitle = (TextView) findViewById(R.id.top_view_title);
        mTitle.setText(getString(R.string.evaluate_complete));
        mBack = (ImageView) findViewById(R.id.top_view_back);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mShareComment = (LinearLayout) findViewById(R.id.share_comment);
        mBack.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mShareComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.share_comment:
                Intent intent =new Intent(this, WXEntryActivity.class);
                intent.putExtra(WXEntryActivity.IS_COMMENT,true);
                intent.putExtra("order_id", mOrderId);
                startActivity(intent);
                overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
                break;
        }
    }
}
