package com.waterdiary.drinkreminder.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

//import com.waterdiary.drinkreminder.Screen_OnBoarding_Eight;
import com.waterdiary.drinkreminder.Screen_OnBoarding_Five;
import com.waterdiary.drinkreminder.Screen_OnBoarding_Four;
//import com.waterdiary.drinkreminder.Screen_OnBoarding_Seven;
import com.waterdiary.drinkreminder.Screen_OnBoarding_Six;
import com.waterdiary.drinkreminder.Screen_OnBoarding_Three;
import com.waterdiary.drinkreminder.Screen_OnBoarding_Two;
import com.waterdiary.drinkreminder.Screen_OnBoarding_new;

public class OnBoardingPagerAdapter extends FragmentStatePagerAdapter
{
    //Screen_OnBoarding_Two tab2Fragment;
    Screen_OnBoarding_Three tab3Fragment;
    Screen_OnBoarding_Four tab4Fragment;
    Screen_OnBoarding_Five tab5Fragment;
    Screen_OnBoarding_Six tab6Fragment;
    Screen_OnBoarding_new tab0Fragment;
    //Screen_OnBoarding_Seven tab7Fragment;
    //Screen_OnBoarding_Eight tab8Fragment;

    Context mContext;

    public OnBoardingPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        //tab2Fragment = new Screen_OnBoarding_Two();
        tab0Fragment = new Screen_OnBoarding_new();
        tab3Fragment = new Screen_OnBoarding_Three();
        tab4Fragment = new Screen_OnBoarding_Four();
        tab5Fragment = new Screen_OnBoarding_Five();
        tab6Fragment = new Screen_OnBoarding_Six();
        //tab7Fragment = new Screen_OnBoarding_Seven();
        //tab8Fragment = new Screen_OnBoarding_Eight();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1)
        {
            return tab4Fragment;
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
            return tab3Fragment;
        }
        else if (position == 3)
        {
            return tab6Fragment;
        }
        else if (position == 4)
        {
            return tab5Fragment;
        }
        else
        {
            return tab0Fragment;
        }
        /*else
        {
            return tab2Fragment;
        }*/
    }

    @Override
    public int getCount() {
        return 5;
    }
}