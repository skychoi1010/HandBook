package com.waterdiary.drinkreminder.receiver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.basic.appbasiclibs.utils.Constant;
import com.basic.appbasiclibs.utils.Date_Helper;
import com.basic.appbasiclibs.utils.Preferences_Helper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waterdiary.drinkreminder.model.backuprestore.AlarmDetails;
import com.waterdiary.drinkreminder.model.backuprestore.AlarmSubDetails;
import com.waterdiary.drinkreminder.model.backuprestore.BackupRestore;
import com.waterdiary.drinkreminder.model.backuprestore.ContainerDetails;
import com.waterdiary.drinkreminder.model.backuprestore.DrinkDetails;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BackupHelper {

    private Context mContext;

    Date_Helper dth=new Date_Helper();
    Preferences_Helper ph;

    BackupHelper(Context context) {
        mContext = context;
        ph=new Preferences_Helper(mContext);
    }

    void createAutoBackSetup()
    {
        if(!ph.getBoolean(URLFactory.AUTO_BACK_UP))
            return;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStoragePermissions();
        } else {
            backup_data();
        }

    }

    public void checkStoragePermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                backup_data();
            }
        }
    }

    public void backup_data()
    {
        ArrayList<HashMap<String,String>> arr_data = getdata("tbl_container_details");

        BackupRestore backupRestore=new BackupRestore();

        List<ContainerDetails> containerDetailsList=new ArrayList<>();

        for(int k=0;k<arr_data.size();k++)
        {
            ContainerDetails containerDetails=new ContainerDetails();
            containerDetails.setContainerID(arr_data.get(k).get("ContainerID"));
            containerDetails.setContainerMeasure(arr_data.get(k).get("ContainerMeasure"));
            containerDetails.setContainerValue(arr_data.get(k).get("ContainerValue"));
            containerDetails.setContainerValueOZ(arr_data.get(k).get("ContainerValueOZ"));
            containerDetails.setIsOpen(arr_data.get(k).get("IsOpen"));
            containerDetails.setId(arr_data.get(k).get("id"));
            containerDetails.setIsCustom(arr_data.get(k).get("IsCustom"));
            containerDetailsList.add(containerDetails);
        }

        arr_data = getdata("tbl_drink_details");

        List<DrinkDetails> drinkDetailsList=new ArrayList<>();

        for(int k=0;k<arr_data.size();k++)
        {
            DrinkDetails drinkDetails=new DrinkDetails();
            drinkDetails.setDrinkDateTime(arr_data.get(k).get("DrinkDateTime"));
            drinkDetails.setDrinkDate(arr_data.get(k).get("DrinkDate"));
            drinkDetails.setDrinkTime(arr_data.get(k).get("DrinkTime"));
            drinkDetails.setContainerMeasure(arr_data.get(k).get("ContainerMeasure"));
            drinkDetails.setContainerValue(arr_data.get(k).get("ContainerValue"));
            drinkDetails.setContainerValueOZ(arr_data.get(k).get("ContainerValueOZ"));
            drinkDetails.setId(arr_data.get(k).get("id"));
            drinkDetails.setTodayGoal(arr_data.get(k).get("TodayGoal"));
            drinkDetails.setTodayGoalOZ(arr_data.get(k).get("TodayGoalOZ"));
            drinkDetailsList.add(drinkDetails);
        }

        arr_data = getdata("tbl_alarm_details");

        List<AlarmDetails> alarmDetailsList=new ArrayList<>();

        for(int k=0;k<arr_data.size();k++)
        {
            AlarmDetails alarmDetails=new AlarmDetails();
            alarmDetails.setAlarmId(arr_data.get(k).get("AlarmId"));
            alarmDetails.setAlarmInterval(arr_data.get(k).get("AlarmInterval"));
            alarmDetails.setAlarmTime(arr_data.get(k).get("AlarmTime"));
            alarmDetails.setAlarmType(arr_data.get(k).get("AlarmType"));
            alarmDetails.setId(arr_data.get(k).get("id"));

            alarmDetails.setAlarmSundayId(arr_data.get(k).get("SundayAlarmId"));
            alarmDetails.setAlarmMondayId(arr_data.get(k).get("MondayAlarmId"));
            alarmDetails.setAlarmTuesdayId(arr_data.get(k).get("TuesdayAlarmId"));
            alarmDetails.setAlarmWednesdayId(arr_data.get(k).get("WednesdayAlarmId"));
            alarmDetails.setAlarmThursdayId(arr_data.get(k).get("ThursdayAlarmId"));
            alarmDetails.setAlarmFridayId(arr_data.get(k).get("FridayAlarmId"));
            alarmDetails.setAlarmSaturdayId(arr_data.get(k).get("SaturdayAlarmId"));

            alarmDetails.setIsOff(Integer.parseInt(arr_data.get(k).get("IsOff")));
            alarmDetails.setSunday(Integer.parseInt(arr_data.get(k).get("Sunday")));
            alarmDetails.setMonday(Integer.parseInt(arr_data.get(k).get("Monday")));
            alarmDetails.setTuesday(Integer.parseInt(arr_data.get(k).get("Tuesday")));
            alarmDetails.setWednesday(Integer.parseInt(arr_data.get(k).get("Wednesday")));
            alarmDetails.setThursday(Integer.parseInt(arr_data.get(k).get("Thursday")));
            alarmDetails.setFriday(Integer.parseInt(arr_data.get(k).get("Friday")));
            alarmDetails.setSaturday(Integer.parseInt(arr_data.get(k).get("Saturday")));


            List<AlarmSubDetails> alarmSubDetailsList=new ArrayList<>();

            ArrayList<HashMap<String,String>> arr_data2 = getdata("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));

            Log.d("arr_data2 : ",""+arr_data2.size());

            for(int j=0;j<arr_data2.size();j++)
            {
                AlarmSubDetails alarmSubDetails=new AlarmSubDetails();
                alarmSubDetails.setAlarmId(arr_data2.get(j).get("AlarmId"));
                alarmSubDetails.setAlarmTime(arr_data2.get(j).get("AlarmTime"));
                alarmSubDetails.setId(arr_data2.get(j).get("id"));
                alarmSubDetails.setSuperId(arr_data2.get(j).get("SuperId"));
                alarmSubDetailsList.add(alarmSubDetails);
            }

            alarmDetails.setAlarmSubDetails(alarmSubDetailsList);

            alarmDetailsList.add(alarmDetails);
        }


        backupRestore.setContainerDetails(containerDetailsList);
        backupRestore.setDrinkDetails(drinkDetailsList);
        backupRestore.setAlarmDetails(alarmDetailsList);

        backupRestore.setTotalDrink(ph.getFloat(URLFactory.DAILY_WATER));

        backupRestore.setTotalWeight(ph.getString(URLFactory.PERSON_WEIGHT));
        backupRestore.setTotalHeight(ph.getString(URLFactory.PERSON_HEIGHT));

        backupRestore.isCMUnit(ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT));
        backupRestore.isKgUnit(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT));
        //backupRestore.isMlUnit(ph.getString(URLFactory.WATER_UNIT).equalsIgnoreCase("ml")?true:false);
        backupRestore.isMlUnit(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT));


        backupRestore.setReminderOption(ph.getInt(URLFactory.REMINDER_OPTION));
        backupRestore.setReminderSound(ph.getInt(URLFactory.REMINDER_SOUND));
        backupRestore.isDisableNotifiction(ph.getBoolean(URLFactory.DISABLE_NOTIFICATION));
        backupRestore.isManualReminderActive(ph.getBoolean(URLFactory.IS_MANUAL_REMINDER));
        backupRestore.isReminderVibrate(ph.getBoolean(URLFactory.REMINDER_VIBRATE));

        backupRestore.setUserName(ph.getString(URLFactory.USER_NAME));
        backupRestore.setUserGender(ph.getBoolean(URLFactory.USER_GENDER));


        backupRestore.isDisableSound(ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER));


        backupRestore.isAutoBackup(ph.getBoolean(URLFactory.AUTO_BACK_UP));
        backupRestore.setAutoBackupType(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE));
        backupRestore.setAutoBackupID(ph.getInt(URLFactory.AUTO_BACK_UP_ID));


        String jsondata=new Gson().toJson(backupRestore);
        JsonParser jsonParser1 = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser1.parse(jsondata);

        store_response(jsonObject.toString());
    }

    public void store_response(String plainBody)
    {
        File f = new File(Environment.getExternalStorageDirectory() + "/"+URLFactory.APP_DIRECTORY_NAME+"/");

        if(!f.exists())
            f.mkdir();

        if (f.exists())
        {
            String dt=dth.getCurrentDate("dd-MMM-yyyy hh:mm:ss a");

            String full_file_name=Environment.getExternalStorageDirectory() + "/"+URLFactory.APP_DIRECTORY_NAME+"/Backup_"
                    + dt + ".txt";

            File file = new File(full_file_name);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
                //writer.append(cipherBody);
                writer.append(plainBody);
                writer.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<HashMap<String, String>> getdata(String table_name)
    {
        ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

        String query = "SELECT * FROM "+table_name;

        Cursor c = Constant.SDB.rawQuery(query, null);

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

    public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con)
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