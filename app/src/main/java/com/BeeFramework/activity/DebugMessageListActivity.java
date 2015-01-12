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

package com.BeeFramework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.BeeFramework.adapter.DebugListAdapter;
import com.BeeFramework.model.DebugMessageModel;
import com.insthub.O2OMobile.R;

public class DebugMessageListActivity extends BaseActivity{

	private TextView title;
    private ListView messageListView;
    private ListView stompListView;

    private TextView messageButton;
    private TextView stompButton;

    DebugListAdapter debugAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_message_list);
        
        title = (TextView) findViewById(R.id.navigationbar_title);
        title.setText("协议log");
        
        messageListView = (ListView)findViewById(R.id.debugMessageList);
        stompListView = (ListView)findViewById(R.id.stompList);

        messageButton = (TextView)findViewById(R.id.message_log);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageListView.setVisibility(View.VISIBLE);
                stompListView.setVisibility(View.GONE);
            }
        });
        debugAdapter = new DebugListAdapter(this);
        messageListView.setAdapter(debugAdapter);
        messageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				int size = DebugMessageModel.messageList.size();
				
				Intent intent = new Intent(DebugMessageListActivity.this, DebugDetailActivity.class);
				intent.putExtra("position", size-1-arg2);
				startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        debugAdapter.notifyDataSetChanged();
    }
}
