package com.waterdiary.drinkreminder.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.waterdiary.drinkreminder.Screen_OnBoarding_Five;
import com.waterdiary.drinkreminder.Screen_OnBoarding_Four;
import com.waterdiary.drinkreminder.Screen_OnBoarding_new;
import com.waterdiary.drinkreminder.Screen_OnBoarding_notisetting;

//import com.waterdiary.drinkreminder.Screen_OnBoarding_Eight;
//import com.waterdiary.drinkreminder.Screen_OnBoarding_Seven;

public class OnBoardingPagerAdapter extends FragmentStatePagerAdapter
{
    //Screen_OnBoarding_Two tab2Fragment;
    Screen_OnBoarding_Four tab1Fragment;
    Screen_OnBoarding_notisetting tab2Fragment;
    Screen_OnBoarding_Five tab3Fragment;
    Screen_OnBoarding_new tab0Fragment;
    //Screen_OnBoarding_Seven tab7Fragment;
    //Screen_OnBoarding_Eight tab8Fragment;

    Context mContext;

    public OnBoardingPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        //tab2Fragment = new Screen_OnBoarding_Two();
        tab0Fragment = new Screen_OnBoarding_new();
        tab1Fragment = new Screen_OnBoarding_Four();
        tab2Fragment = new Screen_OnBoarding_notisetting();
        tab3Fragment = new Screen_OnBoarding_Five();
        //tab7Fragment = new Screen_OnBoarding_Seven();
        //tab8Fragment = new Screen_OnBoarding_Eight();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1)
        {
            return tab1Fragment;
        }
        /*else if (position == 2)
        {
            return tab7Fragment;
        }*/
        /*else if (position == 2)
        {
            return tab8Fragment;
        }*/
        else if (position == 2)
        {
            return tab2Fragment;
        }
        else if (position == 3)
        {
            return tab3Fragment;
        }
        else
        {
            return tab0Fragment;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}