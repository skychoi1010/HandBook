package com.waterdiary.drinkreminder;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.widget.RelativeLayout;

import com.basic.appbasiclibs.AppClose;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.model.Container;
import com.waterdiary.drinkreminder.mywidgets.NewAppWidget;
import com.waterdiary.drinkreminder.utils.HeightWeightHelper;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class Screen_Select_Bottle extends MasterBaseActivity
{
    ArrayList<Container> containerArrayList=new ArrayList<>();

    int selected_pos=0;

    //======================

    float drink_water=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_select_bottle);
        getWindow().setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

        NotificationManager notificationManager =(NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        if(ph.getFloat(URLFactory.DAILY_WATER)==0)
        {
            URLFactory.DAILY_WATER_VALUE=2500;
        }
        else
        {
            URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);
        }

        if(sh.check_blank_data(""+ph.getString(URLFactory.WATER_UNIT)))
        {
            URLFactory.WATER_UNIT_VALUE="ml";
        }
        else
        {
            URLFactory.WATER_UNIT_VALUE=ph.getString(URLFactory.WATER_UNIT);
        }

        FindViewById();
        Body();
    }

    private void FindViewById()
    {

    }

    public void saveDefaultContainer()
    {
        if(!ph.getBoolean(URLFactory.HIDE_WELCOME_SCREEN))
        {
            ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT,true);
            ph.savePreferences(URLFactory.PERSON_WEIGHT,"80");
            ph.savePreferences(URLFactory.USER_NAME,"");
            intent = new Intent(act, Screen_OnBoarding.class);
            startActivity(intent);
            finish();
        }
        else
        {

            boolean addRecord=true;

            String str="";
            if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                str=sh.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1","8000 ml");
            else
                str=sh.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1","270 fl oz");

            ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_drink_details","DrinkDate ='"+dth.getCurrentDate(URLFactory.DATE_FORMAT)+"'");

            drink_water=0;
            for(int k=0;k<arr_data.size();k++)
            {
                if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                    drink_water+=Double.parseDouble(""+arr_data.get(k).get("ContainerValue"));
                else
                    drink_water+=Double.parseDouble(""+arr_data.get(k).get("ContainerValueOZ"));
            }

            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")
                    && drink_water>8000)
            {
                ah.customAlert(str);
                addRecord=false;
            }
            else if (!(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                    && drink_water>270)
            {
                ah.customAlert(str);
                addRecord=false;

            }


            float count_drink_after_add_current_water=drink_water;

            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                count_drink_after_add_current_water+=Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValue());
            else if (!(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")))
                count_drink_after_add_current_water+=Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValueOZ());

            if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")
                    && count_drink_after_add_current_water>8000)
            {
                if(drink_water>=8000)
                    ah.customAlert(str);
                else if(URLFactory.DAILY_WATER_VALUE<(8000-Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValue())))
                    ah.customAlert(str);
            }
            else if (!(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                    && count_drink_after_add_current_water>270)
            {
                if(drink_water>=270)
                    ah.customAlert(str);
                else if(URLFactory.DAILY_WATER_VALUE<(270-Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValueOZ())))
                    ah.customAlert(str);
            }

            if(drink_water==8000 && URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                addRecord=false;
            }
            else if(drink_water==270 && !URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
            {
                addRecord=false;
            }

            if(addRecord) {

                ContentValues initialValues = new ContentValues();

                initialValues.put("ContainerValue", "" + containerArrayList.get(selected_pos).getContainerValue());
                initialValues.put("ContainerValueOZ", "" + containerArrayList.get(selected_pos).getContainerValueOZ());
                initialValues.put("ContainerMeasure", "" + ph.getString(URLFactory.WATER_UNIT));
                initialValues.put("DrinkDate", "" + dth.getCurrentDate("dd-MM-yyyy"));
                initialValues.put("DrinkTime", "" + dth.getCurrentTime(true));
                initialValues.put("DrinkDateTime", "" + dth.getCurrentDate("dd-MM-yyyy HH:mm:ss"));

                if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
                    initialValues.put("TodayGoal", "" + URLFactory.DAILY_WATER_VALUE);
                    initialValues.put("TodayGoalOZ", "" + HeightWeightHelper.mlToOzConverter(URLFactory.DAILY_WATER_VALUE));
                } else {
                    initialValues.put("TodayGoal", "" + HeightWeightHelper.ozToMlConverter(URLFactory.DAILY_WATER_VALUE));
                    initialValues.put("TodayGoalOZ", "" + URLFactory.DAILY_WATER_VALUE);
                }

                dh.INSERT("tbl_drink_details", initialValues);
            }


            //==============================


            Intent intent = new Intent(act, NewAppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(act).getAppWidgetIds(new ComponentName(act, NewAppWidget.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            act.sendBroadcast(intent);

            AppClose.exitApplication(mContext);
        }
    }

    private void Body()
    {

        String selected_container_id="1";

        if(ph.getInt(URLFactory.SELECTED_CONTAINER)==0)
            selected_container_id="1";
        else
            selected_container_id=""+ph.getInt(URLFactory.SELECTED_CONTAINER);

        ArrayList<HashMap<String, String>> arr_container = dh.getdata("tbl_container_details","IsCustom",1);

        for(int k=0;k<arr_container.size();k++)
        {
            Container container=new Container();
            container.setContainerId(arr_container.get(k).get("ContainerID"));
            container.setContainerValue(arr_container.get(k).get("ContainerValue"));
            container.setContainerValueOZ(arr_container.get(k).get("ContainerValueOZ"));
            container.isOpen(arr_container.get(k).get("IsOpen").equalsIgnoreCase("1")?true:false);
            //container.isSelected(false);
            container.isSelected(selected_container_id.equalsIgnoreCase(arr_container.get(k).get("ContainerID"))?true:false);
            if(container.isSelected())
                selected_pos=k;//+1
            containerArrayList.add(container);
        }

        saveDefaultContainer();
    }
}