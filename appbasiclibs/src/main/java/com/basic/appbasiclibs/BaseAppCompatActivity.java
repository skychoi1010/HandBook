package com.basic.appbasiclibs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.basic.appbasiclibs.utils.Alert_Helper;
import com.basic.appbasiclibs.utils.Bitmap_Helper;
import com.basic.appbasiclibs.utils.Database_Helper;
import com.basic.appbasiclibs.utils.Date_Helper;
import com.basic.appbasiclibs.utils.Intent_Helper;
import com.basic.appbasiclibs.utils.Json_Helper;
import com.basic.appbasiclibs.utils.Map_Helper;
import com.basic.appbasiclibs.utils.Preferences_Helper;
import com.basic.appbasiclibs.utils.String_Helper;
import com.basic.appbasiclibs.utils.Utility_Function;
import com.basic.appbasiclibs.utils.Zip_Helper;

public class BaseAppCompatActivity extends AppCompatActivity
{
    public Context mContext;
    public Activity act;

    public Intent intent;

    public Utility_Function uf;
    public Alert_Helper ah;
    public Json_Helper jh;
    public Bitmap_Helper bh;
    public Date_Helper dth;
    public Database_Helper dh;
    public Map_Helper mah;
    public String_Helper sh;
    public Preferences_Helper ph;
    public Zip_Helper zh;
    public Intent_Helper ih;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mContext=BaseAppCompatActivity.this;
        act=BaseAppCompatActivity.this;

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(act));

        uf=new Utility_Function(mContext,act);
        ah=new Alert_Helper(mContext);
        jh=new Json_Helper(mContext);
        bh=new Bitmap_Helper(mContext);
        dth=new Date_Helper();
        dh=new Database_Helper(mContext,act);
        ih=new Intent_Helper(mContext,act);
        mah=new Map_Helper();
        sh=new String_Helper(mContext,act);
        ph=new Preferences_Helper(mContext,act);
        zh=new Zip_Helper(mContext);

        uf.permission_StrictMode();
    }
}
