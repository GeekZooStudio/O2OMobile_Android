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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.BeeFramework.BeeFrameworkApp;
import com.BeeFramework.model.BeeQuery;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.R;
import com.insthub.O2OMobile.SESSION;

public class SplashActivity extends Activity {
    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View startView = View.inflate(this, R.layout.splash, null);
        setContentView(startView);
        
        if(BeeQuery.environment() == BeeQuery.ENVIROMENT_DEVELOPMENT || BeeQuery.environment() == BeeQuery.ENVIROMENT_MOCKSERVER)
        {
            BeeFrameworkApp.getInstance().showBug(this);
        }
        
        mShared =getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        mEditor = mShared.edit();
        //渐变
        AlphaAnimation aa = new AlphaAnimation(1f, 1.0f);
        aa.setDuration(2500);
        startView.setAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                redirectto();
            }
        }
        );
    }

    private void redirectto() {
        boolean isFirstRunLead = mShared.getBoolean("isFirstRunLead", true);
        if (isFirstRunLead) {
            Intent intent = new Intent(this, LeadActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        } else {
            if (mShared.getBoolean("isLogin", false)) {
                Intent intent = new Intent(this, SlidingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            } else {
                mEditor.putBoolean("isLogin", false);
                mEditor.putString("user", "");
                mEditor.putInt("uid", 0);
                mEditor.putString("sid", "");
                mEditor.commit();
                SESSION.getInstance().uid = mShared.getInt("uid", 0);
                SESSION.getInstance().sid = mShared.getString("sid", "");
                Intent intent = new Intent(this, B0_SigninActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        }

    }
}
