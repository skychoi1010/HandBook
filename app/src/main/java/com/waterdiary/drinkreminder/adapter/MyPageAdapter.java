package com.waterdiary.drinkreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.waterdiary.drinkreminder.R;

public class MyPageAdapter extends PagerAdapter {

    Context mContext;
    int resId = 0;

    public MyPageAdapter(Context context) {
        mContext = context;
    }

    public Object instantiateItem(ViewGroup collection, int position)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (position)
        {
            case 0:
                resId = R.layout.screen_battery_optimize_one;
                break;
            case 1:
                resId = R.layout.screen_battery_optimize_two;
                break;
        }
        ViewGroup layout = (ViewGroup) inflater.inflate(resId, collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}