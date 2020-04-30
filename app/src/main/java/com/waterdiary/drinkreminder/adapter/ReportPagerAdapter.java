package com.waterdiary.drinkreminder.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.Screen_Month_Report;
import com.waterdiary.drinkreminder.Screen_Week_Report;
import com.waterdiary.drinkreminder.Screen_Year_Report;

public class ReportPagerAdapter extends FragmentStatePagerAdapter
{

    Screen_Week_Report tab1Fragment;
    Screen_Month_Report tab2Fragment;
    Screen_Year_Report tab3Fragment;

    Context mContext;

    public ReportPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        tab1Fragment = new Screen_Week_Report();
        tab2Fragment = new Screen_Month_Report();
        tab3Fragment = new Screen_Year_Report();
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            return tab1Fragment;
        }
        else if (position == 1)
        {
            return tab2Fragment;
        }
        else
        {
            return tab3Fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = mContext.getResources().getString(R.string.str_week);
        }
        else if (position == 1)
        {
            title = mContext.getResources().getString(R.string.str_month);
        }
        else if (position == 2)
        {
            title = mContext.getResources().getString(R.string.str_year);
        }
        return title;
    }
}
