package com.basic.appbasiclibs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences_Helper
{
    Context mContext;
    Activity act;

    SharedPreferences sharedPreferences;


    public Preferences_Helper(Context mContext, Activity act)
    {
        this.mContext=mContext;
        this.act=act;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public Preferences_Helper(Context mContext)
    {
        this.mContext=mContext;
        this.act=null;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void savePreferences(String key, boolean value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreferences(String key, String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void savePreferences(String key, float value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void savePreferences(String key, long value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean getBoolean(String name){
        return sharedPreferences.getBoolean(name, false);
    }

    public String getString(String name) {
        return sharedPreferences.getString(name, "");
    }

    public int getInt(String name) {
        return sharedPreferences.getInt(name, 0);
    }

    public float getFloat(String name) {
        return sharedPreferences.getFloat(name, 0f);
    }

    public long getLong(String name) {
        return sharedPreferences.getLong(name, 0);
    }


    /*
    public void loadSavedPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //Constant.UserLabel=sharedPreferences.getString(Constant.perfUserLabel, "");
    }*/
}
