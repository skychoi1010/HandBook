package com.waterdiary.drinkreminder.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import com.basic.appbasiclibs.BaseAppCompatActivity;
import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.utils.DB_Helper;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.Locale;

public class MasterBaseAppCompatActivity extends BaseAppCompatActivity
{
    DB_Helper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbh=new DB_Helper(MasterBaseAppCompatActivity.this,MasterBaseAppCompatActivity.this);
    }

    public static int getThemeColor(Context ctx)
    {
        /*if(true)
            return ctx.getResources().getColor(R.color.colorPrimaryDark2);*/

        return ctx.getResources().getColor(R.color.colorPrimaryDark);
    }

    public static int[] getThemeColorArray(Context ctx)
    {
        int[] colors = {Color.parseColor("#001455da"),Color.parseColor("#FF1455da")};

        /*if(true)
            colors = new int[]{Color.parseColor("#0034a2a9"), Color.parseColor("#FF34a2a9")};*/

        return colors;

    }
}
