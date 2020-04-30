package com.waterdiary.drinkreminder.mywidgets;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import com.basic.appbasiclibs.utils.Constant;
import com.basic.appbasiclibs.utils.Date_Helper;
import com.basic.appbasiclibs.utils.Preferences_Helper;
import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.Screen_Select_Bottle;
import com.waterdiary.drinkreminder.Screen_Splash;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static Context mContext;
    static float drink_water=0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        mContext=context;
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, get_today_report());

        views.setInt(R.id.circularProgressbar,"setMax", (int) URLFactory.DAILY_WATER_VALUE);
        views.setInt(R.id.circularProgressbar,"setProgress", (int) drink_water);

        Intent launchMain = new Intent(context, Screen_Splash.class);
        launchMain.putExtra("from_widget",true);
        launchMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingMainIntent);


        Intent launchMain2 = new Intent(context, Screen_Select_Bottle.class);
        launchMain2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingMainIntent2 = PendingIntent.getActivity(context, 0, launchMain2, 0);
        views.setOnClickPendingIntent(R.id.add_water, pendingMainIntent2);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @SuppressLint("WrongConstant")
    public static String get_today_report()
    {
        Constant.SDB=mContext.openOrCreateDatabase(Constant.DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null);

        Date_Helper dth=new Date_Helper();
        Preferences_Helper ph=new Preferences_Helper(mContext);

        ArrayList<HashMap<String, String>> arr_data=getdata("tbl_drink_details","DrinkDate ='"+dth.getCurrentDate("dd-MM-yyyy")+"'");

        if(ph.getFloat(URLFactory.DAILY_WATER)==0)
        {
            URLFactory.DAILY_WATER_VALUE=2500;
        }
        else
        {
            URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);
        }

        if(check_blank_data(""+ph.getString(URLFactory.WATER_UNIT)))
        {
            URLFactory.WATER_UNIT_VALUE="ML";
        }
        else
        {
            URLFactory.WATER_UNIT_VALUE=ph.getString(URLFactory.WATER_UNIT);
        }

        drink_water=0;
        for(int k=0;k<arr_data.size();k++)
        {
            if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
                drink_water+=Double.parseDouble(arr_data.get(k).get("ContainerValue"));
            else
                drink_water+=Double.parseDouble(arr_data.get(k).get("ContainerValueOZ"));
        }

        /*if(drink_water==0)
        {
            return mContext.getResources().getString(R.string.str_have_u_had_any_water_yet);
        }*/



        //Log.d("Today Completed: ",""+drink_water+" "+URLFactory.WATER_UNIT_VALUE+" Total: "+URLFactory.DAILY_WATER_VALUE+" "+URLFactory.WATER_UNIT_VALUE);

        return ""+(int)(drink_water)+"/"+(int)URLFactory.DAILY_WATER_VALUE+" "+URLFactory.WATER_UNIT_VALUE;

        //return "Today Completed: "+drink_water+"/"+URLFactory.DAILY_WATER_VALUE+" "+URLFactory.WATER_UNIT_VALUE;
    }

    public static boolean check_blank_data(String data)
    {
        if(data.equals("") || data.isEmpty() || data.length()==0 || data.equals("null") || data==null)
            return true;

        return false;
    }

    public static ArrayList<HashMap<String, String>> getdata(String table_name, String where_con)
    {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

        String query = "SELECT * FROM "+table_name;

        query+=" where "+where_con;

        Cursor c = Constant.SDB.rawQuery(query, null);

        System.out.println("SELECT QUERY : "+ query);

        if(c.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<c.getColumnCount();i++)
                {
                    map.put(c.getColumnName(i), c.getString(i));
                }

                maplist.add(map);
            }
            while (c.moveToNext());
        }

        return maplist;
    }
}

