package com.waterdiary.drinkreminder.base;

import android.content.Context;

import com.basic.appbasiclibs.BaseFragment;
import com.waterdiary.drinkreminder.utils.DB_Helper;

public class MasterBaseFragment extends BaseFragment
{
    DB_Helper dbh;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbh=new DB_Helper(getActivity(),getActivity());
    }
}