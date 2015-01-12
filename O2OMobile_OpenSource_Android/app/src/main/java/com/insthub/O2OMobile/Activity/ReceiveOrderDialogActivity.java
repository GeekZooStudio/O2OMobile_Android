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
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.insthub.O2OMobile.Protocol.ENUM_MESSAGE_TYPE;
import com.insthub.O2OMobile.Protocol.PUSH_MESSAGE;
import com.insthub.O2OMobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveOrderDialogActivity extends Activity{

    public Dialog mDialog;
    public TextView dialog_message;
    public TextView positive;
    public TextView negative;

    public static final String CUSTOM_CONTENT ="CustomContent";

    String show_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        show_message = getIntent().getStringExtra(CUSTOM_CONTENT);

        dialog_message = (TextView) findViewById(R.id.dialog_message);



        positive = (TextView) findViewById(R.id.yes);
        negative = (TextView) findViewById(R.id.no);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(show_message);

            final PUSH_MESSAGE message1 = new PUSH_MESSAGE();
            message1.fromJson(jsonObject);
            dialog_message.setText(message1.content + getString(R.string.look_up_or_not));

        positive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    if (message1.type == ENUM_MESSAGE_TYPE.ORDER.value())
                    {


                        Intent it = new Intent(ReceiveOrderDialogActivity.this, D1_OrderActivity.class);
                        it.putExtra(D1_OrderActivity.ORDER_ID, message1.order_id);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ReceiveOrderDialogActivity.this.startActivity(it);
                        (ReceiveOrderDialogActivity.this).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }



                finish();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
