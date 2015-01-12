package com.insthub.O2OMobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.O2OMobile.Activity.B0_SigninActivity;
import com.insthub.O2OMobile.Activity.LeadActivity;
import com.insthub.O2OMobile.O2OMobileAppConst;
import com.insthub.O2OMobile.R;

public class LeadAdapter extends PagerAdapter {

    private int[] imageBg;
    private LayoutInflater mInflater;
    private Context mContext;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public LeadAdapter(Context context, int[] imageBg) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.imageBg = imageBg;
        shared =context.getSharedPreferences(O2OMobileAppConst.USERINFO, 0);
        editor = shared.edit();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageBg.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = mInflater.inflate(R.layout.lead_item, null);
        ImageView image = (ImageView) imageLayout.findViewById(R.id.lead_item);
        TextView start = (TextView) imageLayout.findViewById(R.id.start_expexperience);
        image.setBackgroundResource(imageBg[position]);

        ((ViewPager) view).addView(imageLayout, 0);
        if (imageBg.length - 1 == position) {
            start.setVisibility(View.VISIBLE);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, B0_SigninActivity.class);
                    (mContext).startActivity(intent);
                    ((LeadActivity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    editor.putBoolean("isFirstRunLead", false);
                    editor.commit();
                    ((LeadActivity) mContext).finish();
                }
            });
        }

        return imageLayout;
    }
}

