package com.waterdiary.drinkreminder.base;

import android.os.Bundle;

import com.basic.appbasiclibs.BaseActivity;

import com.waterdiary.drinkreminder.utils.DB_Helper;

public class MasterBaseActivity extends BaseActivity
{
    public DB_Helper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbh=new DB_Helper(MasterBaseActivity.this,MasterBaseActivity.this);
    }
}