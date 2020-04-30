package com.waterdiary.drinkreminder.base;

import android.os.Bundle;

import com.basic.appbasiclibs.BaseFragmentActivity;
import com.waterdiary.drinkreminder.utils.DB_Helper;

public class MasterBaseFragmentActivity extends BaseFragmentActivity
{
    DB_Helper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbh=new DB_Helper(MasterBaseFragmentActivity.this,MasterBaseFragmentActivity.this);
    }
}