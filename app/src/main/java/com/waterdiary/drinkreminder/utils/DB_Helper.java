package com.waterdiary.drinkreminder.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import com.basic.appbasiclibs.utils.Database_Helper;

import java.util.HashMap;

public class DB_Helper
{
    Context mContext;
    Activity act;
    Database_Helper dh;

    public DB_Helper(Context mContext,Activity act)
    {
        this.mContext=mContext;
        this.act=act;
        dh=new Database_Helper(mContext,act);
        CREATE_DB_TABLE();
    }

    public void CREATE_DB_TABLE()
    {
        HashMap<String,String> enum_fields=new HashMap<>();
        enum_fields.put("id","INTEGER PRIMARY KEY AUTOINCREMENT");
        enum_fields.put("ContainerID","INTEGER DEFAULT 0");
        enum_fields.put("ContainerValue","TEXT");
        enum_fields.put("ContainerValueOZ","TEXT");
        enum_fields.put("ContainerMeasure","TEXT");
        enum_fields.put("IsOpen","TEXT");
        enum_fields.put("IsCustom","INTEGER DEFAULT 0");
        dh.CREATE_TABLE("tbl_container_details",enum_fields);
        enum_fields.clear();

        add_container_data();

        HashMap<String,String> item_fields=new HashMap<>();
        item_fields.put("id","INTEGER PRIMARY KEY AUTOINCREMENT");
        item_fields.put("ContainerValue","TEXT");
        item_fields.put("ContainerValueOZ","TEXT");
        item_fields.put("ContainerMeasure","TEXT");
        item_fields.put("DrinkDate","TEXT");
        item_fields.put("DrinkTime","TEXT");
        item_fields.put("DrinkDateTime","TEXT");
        item_fields.put("TodayGoal","TEXT");
        item_fields.put("TodayGoalOZ","TEXT");
        dh.CREATE_TABLE("tbl_drink_details",item_fields);
        item_fields.clear();



        HashMap<String,String> alarm_fields=new HashMap<>();
        alarm_fields.put("id","INTEGER PRIMARY KEY AUTOINCREMENT");
        alarm_fields.put("AlarmTime","TEXT");
        alarm_fields.put("AlarmId","TEXT");
        alarm_fields.put("AlarmType","TEXT");
        alarm_fields.put("AlarmInterval","TEXT");

        alarm_fields.put("IsOff","INTEGER DEFAULT 0");
        alarm_fields.put("Sunday","INTEGER DEFAULT 1");
        alarm_fields.put("Monday","INTEGER DEFAULT 1");
        alarm_fields.put("Tuesday","INTEGER DEFAULT 1");
        alarm_fields.put("Wednesday","INTEGER DEFAULT 1");
        alarm_fields.put("Thursday","INTEGER DEFAULT 1");
        alarm_fields.put("Friday","INTEGER DEFAULT 1");
        alarm_fields.put("Saturday","INTEGER DEFAULT 1");
        alarm_fields.put("SundayAlarmId","TEXT");
        alarm_fields.put("MondayAlarmId","TEXT");
        alarm_fields.put("TuesdayAlarmId","TEXT");
        alarm_fields.put("WednesdayAlarmId","TEXT");
        alarm_fields.put("ThursdayAlarmId","TEXT");
        alarm_fields.put("FridayAlarmId","TEXT");
        alarm_fields.put("SaturdayAlarmId","TEXT");

        dh.CREATE_TABLE("tbl_alarm_details",alarm_fields);
        alarm_fields.clear();

        HashMap<String,String> alarm_sub_fields=new HashMap<>();
        alarm_sub_fields.put("id","INTEGER PRIMARY KEY AUTOINCREMENT");
        alarm_sub_fields.put("AlarmTime","TEXT");
        alarm_sub_fields.put("AlarmId","TEXT");
        alarm_sub_fields.put("SuperId","TEXT");
        dh.CREATE_TABLE("tbl_alarm_sub_details",alarm_sub_fields);
        alarm_sub_fields.clear();
    }

    public void add_container_data()
    {
            if(dh.TOTAL_ROW("tbl_container_details")>0)
                return;

            Integer[] cval={50,100,150,200,250,300,500,600,700,800,900,1000};
            Integer[] cval2={2,3,5,7,8,10,17,20,24,27,30,34};
            Integer[] iop={1,1,1,1,1,1,1,1,0,0,0,0};

            for(int k=0;k<cval.length;k++)
            {
                ContentValues initialValues = new ContentValues();

                initialValues.put("ContainerID", ""+(k+1));
                initialValues.put("ContainerValue", ""+cval[k]);
                initialValues.put("ContainerValueOZ", ""+cval2[k]);
                initialValues.put("IsOpen", ""+iop[k]);

                dh.INSERT("tbl_container_details", initialValues);
            }
    }

    public void CLEAR_DB()
    {
        /*dh.REMOVE("tbl_location_price_list_details");
        */
    }
}
