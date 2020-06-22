package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;

public class Screen_OnBoarding_notisetting extends MasterBaseFragment {
    AppCompatTextView time;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.screen_onboarding_notisetting, container, false);

        time = (AppCompatTextView) view.findViewById(R.id.lbl_goal);
        radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(radioGroupButtonChangeListener2);

        return view;
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rdo_15){
                time.setText("15");
            }
            else if(i == R.id.rdo_20){
                time.setText("20");
            }
            else if(i == R.id.rdo_30){
                time.setText("30");
            }
            else if(i == R.id.rdo_35){
                time.setText("35");
            }
        }
    };
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rdo_push){
            }
            else if(i == R.id.rdo_vibrate){
            }
            else if(i == R.id.rdo_LED){
            }
        }
    };


}
