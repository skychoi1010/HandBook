package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;

public class Screen_OnBoarding_new extends MasterBaseFragment {
    View item_view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        item_view=inflater.inflate(R.layout.screen_onboarding_new, container, false);

        FindViewById();
        Body();

        return item_view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            //ah.customAlert("call");
            //actionView();
        }
        else{
            //no
        }

    }

    private void FindViewById()
    {

    }

    private void Body()
    {

    }
}
