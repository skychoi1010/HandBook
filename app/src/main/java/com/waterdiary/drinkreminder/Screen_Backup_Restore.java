package com.waterdiary.drinkreminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.waterdiary.drinkreminder.adapter.FileAdapter;
import com.waterdiary.drinkreminder.model.BackUpFileModel;
import com.waterdiary.drinkreminder.receiver.MyAlarmManager;
import com.waterdiary.drinkreminder.utils.FileUtils2;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.model.backuprestore.AlarmDetails;
import com.waterdiary.drinkreminder.model.backuprestore.AlarmSubDetails;
import com.waterdiary.drinkreminder.model.backuprestore.BackupRestore;
import com.waterdiary.drinkreminder.model.backuprestore.ContainerDetails;
import com.waterdiary.drinkreminder.model.backuprestore.DrinkDetails;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.waterdiary.drinkreminder.R.id;

public class Screen_Backup_Restore extends MasterBaseActivity
{
    LinearLayout right_icon_block,left_icon_block;
    AppCompatTextView lbl_toolbar_title;

    LinearLayout backup_block,restore_block,clear_block;

    private static final int ALL_PERMISSION = 3;
    private static final int SELECT_FILE = 1;

    boolean executeBackup=true;

    List<BackUpFileModel> lst_backup_file=new ArrayList<>();
    FileAdapter fileAdapter;


    SwitchCompat switch_auto_backup;

    LinearLayout auto_backup_option_block;
    RadioButton rdo_daily,rdo_weekly,rdo_monthly;

    Calendar auto_backup_time=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_backup_restore);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.str_green_card));
        }

        FindViewById();
        Body();
    }

    private void FindViewById()
    {
        right_icon_block=findViewById(id.right_icon_block);
        left_icon_block=findViewById(id.left_icon_block);
        lbl_toolbar_title=findViewById(id.lbl_toolbar_title);

        backup_block=findViewById(R.id.backup_block);
        restore_block=findViewById(R.id.restore_block);
        clear_block=findViewById(R.id.clear_block);

        switch_auto_backup=findViewById(R.id.switch_auto_backup);

        auto_backup_option_block=findViewById(R.id.auto_backup_option_block);

        rdo_daily=findViewById(R.id.rdo_daily);
        rdo_weekly=findViewById(R.id.rdo_weekly);
        rdo_monthly=findViewById(R.id.rdo_monthly);
    }

    public void setTime()
    {
        auto_backup_time.set(Calendar.AM_PM, 0);
        auto_backup_time.set(Calendar.HOUR_OF_DAY,1);
        auto_backup_time.set(Calendar.MINUTE,0);
        auto_backup_time.set(Calendar.SECOND,0);
        auto_backup_time.set(Calendar.MILLISECOND,0);
    }

    private void Body()
    {
        lbl_toolbar_title.setText(sh.capitalize(sh.get_string(R.string.str_backup_and_restore)));
        left_icon_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        right_icon_block.setVisibility(View.GONE);

        switch_auto_backup.setChecked(ph.getBoolean(URLFactory.AUTO_BACK_UP));

        auto_backup_option_block.setVisibility(ph.getBoolean(URLFactory.AUTO_BACK_UP)?View.VISIBLE: View.GONE);

        switch_auto_backup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                auto_backup_option_block.setVisibility(isChecked?View.VISIBLE: View.GONE);
                ph.savePreferences(URLFactory.AUTO_BACK_UP,isChecked);

                if(isChecked)
                {
                    setTime();

                    int _id = (int) System.currentTimeMillis();

                    ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,_id);

                    if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==0)
                    {
                        MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,0);
                    }
                    else if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==1)
                    {
                        MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,1);
                    }
                    else if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==2)
                    {
                        MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,2);
                    }
                }
                else
                {
                    MyAlarmManager.cancelRecurringAlarm(mContext,ph.getInt(URLFactory.AUTO_BACK_UP_ID));
                }
            }
        });

        rdo_daily.setChecked(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==0);
        rdo_weekly.setChecked(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==1);
        rdo_monthly.setChecked(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==2);

        setTime();

        rdo_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAlarmManager.cancelRecurringAlarm(mContext,ph.getInt(URLFactory.AUTO_BACK_UP_ID));

                setTime();
                ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE,0);
                int _id = (int) System.currentTimeMillis();
                ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,_id);
                MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,0);
            }
        });

        rdo_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAlarmManager.cancelRecurringAlarm(mContext,ph.getInt(URLFactory.AUTO_BACK_UP_ID));

                setTime();
                ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE,1);
                int _id = (int) System.currentTimeMillis();
                ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,_id);
                MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time, _id,1);
            }
        });

        rdo_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAlarmManager.cancelRecurringAlarm(mContext,ph.getInt(URLFactory.AUTO_BACK_UP_ID));

                setTime();
                ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE,2);
                int _id = (int) System.currentTimeMillis();
                ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,_id);
                MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time, _id,2);
            }
        });




        backup_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                executeBackup=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkStoragePermissions();
                } else {
                    //backup_data();
                    new backupFromDBData().execute();
                }
            }
        });

        restore_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeBackup=false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkStoragePermissions();
                } else {
                    restore_data();
                }
            }
        });

        clear_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=  new  AlertDialog.Builder(act)
                        .setMessage(sh.get_string(R.string.str_clear_all_data_confirmation_messge))
                        .setPositiveButton(sh.get_string(R.string.str_yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();

                                        String path = Environment.getExternalStorageDirectory().toString()
                                                +"/"+URLFactory.APP_DIRECTORY_NAME;
                                        Log.d("Files", "Path: " + path);
                                        File directory = new File(path);
                                        File[] files = directory.listFiles();

                                        for(int k=0;k<files.length;k++)
                                        {
                                            if(files[k].exists())
                                            {
                                                files[k].delete();
                                            }
                                        }
                                    }
                                }
                        )
                        .setNegativeButton(sh.get_string(R.string.str_no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );

                dialog.show();
            }
        });
    }

    public void checkStoragePermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALL_PERMISSION);
            }
            else
            {
                if(executeBackup)
                    //backup_data();
                    new backupFromDBData().execute();
                else
                    restore_data();
            }
        }
    }

    public void backup_data()
    {
        ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_container_details");

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

        arr_data = dh.getdata("tbl_drink_details");

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

        arr_data = dh.getdata("tbl_alarm_details");

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

            ArrayList<HashMap<String,String>> arr_data2 = dh.getdata("tbl_alarm_sub_details","SuperId="+arr_data.get(k).get("id"));

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

        backupRestore.isActive(ph.getBoolean(URLFactory.IS_ACTIVE));
        backupRestore.isBreastfeeding(ph.getBoolean(URLFactory.IS_BREATFEEDING));
        backupRestore.isPregnant(ph.getBoolean(URLFactory.IS_PREGNANT));
        backupRestore.setWeatherConditions(ph.getInt(URLFactory.WEATHER_CONSITIONS));



        String jsondata=new Gson().toJson(backupRestore);
        JsonParser jsonParser1 = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser1.parse(jsondata);

        store_response(jsonObject.toString());
    }


    private class backupFromDBData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ah.Show_Custom_Progress_Dialog();
        }

        protected String doInBackground(String... str)
        {
            try
            {
                backup_data();
            }
            catch (Exception e){
                Log.d("Response : ",""+e.getMessage());
            }
            return "";
        }

        protected void onPostExecute(String result)
        {
            ah.Close_Custom_Progress_Dialog();

            ah.customAlert(sh.get_string(R.string.str_backup_msg));
        }
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

    public void restore_data()
    {
        //selectFile();
        load_backup_file();

        if(lst_backup_file.size()>0)
        {
            openBackUpFilePicker();
        }
        else
        {
            ah.customAlert(sh.get_string(R.string.str_no_backup_found));
        }
    }

    public void load_backup_file()
    {
        lst_backup_file.clear();

        String path = Environment.getExternalStorageDirectory().toString()+"/"+URLFactory.APP_DIRECTORY_NAME;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();

        List<File> lst_file=new ArrayList<>();

        if (files != null && files.length > 1) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return (int) ((object1.lastModified() > object2.lastModified()) ? object1.lastModified() : object2.lastModified());
                    //return (object1.lastModified() > object2.lastModified()) ? 1 : 0;
                }
            });
        }

        for (int i = 0; i < files.length; i++)
        {
            if(files[i].getName().endsWith(".txt"))
                lst_file.add(files[i]);
        }


        Collections.reverse(lst_file);

        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < lst_file.size(); i++)
        {
            Log.d("Files", "FileName:" + lst_file.get(i).getName() + " @@@ "+lst_file.get(i).lastModified());

            BackUpFileModel backUpFileModel=new BackUpFileModel();
            backUpFileModel.setName(lst_file.get(i).getName());
            backUpFileModel.setPath(lst_file.get(i).getPath());
            backUpFileModel.setLastmodify(lst_file.get(i).lastModified());
            backUpFileModel.isSelected(i==0?true:false);
            lst_backup_file.add(backUpFileModel);
        }


    }

    public void openBackUpFilePicker()
    {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final View view = LayoutInflater.from(act).inflate(R.layout.dialog_backup_pick, null, false);


        RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_save=view.findViewById(R.id.btn_save);
        AppCompatTextView btn_text=view.findViewById(R.id.btn_text);

        btn_text.setText(sh.get_string(R.string.str_restore));


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog2=  new  AlertDialog.Builder(act)
                        .setTitle(sh.get_string(R.string.str_restore_all_data_confirmation_messge))
                        .setPositiveButton(sh.get_string(R.string.str_yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog2, int whichButton) {
                                        dialog2.dismiss();

                                        int pos=-1;
                                        for(int k=0;k<lst_backup_file.size();k++)
                                        {
                                            if(lst_backup_file.get(k).isSelected())
                                                pos=k;
                                        }

                                        dialog.dismiss();

                                        if(pos>=0)
                                        {
                                            new restoreFromBackupData(lst_backup_file.get(pos).getPath()).execute();
                                        }
                                    }
                                }
                        )
                        .setNegativeButton(sh.get_string(R.string.str_no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog2, int whichButton) {
                                        dialog2.dismiss();
                                    }
                                }
                        );

                dialog2.show();


            }
        });


        RecyclerView soundRecyclerView=view.findViewById(R.id.soundRecyclerView);

        /*ViewGroup.LayoutParams params=soundRecyclerView.getLayoutParams();
        params.height=300;
        soundRecyclerView.setLayoutParams(params);*/

        fileAdapter = new FileAdapter(act, lst_backup_file, new FileAdapter.CallBack() {
            @Override
            public void onClickSelect(BackUpFileModel backUpFileModel, int position)
            {
                for(int k=0;k<lst_backup_file.size();k++)
                {
                    lst_backup_file.get(k).isSelected(false);
                }

                lst_backup_file.get(position).isSelected(true);
                fileAdapter.notifyDataSetChanged();
            }
        });

        soundRecyclerView.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));

        soundRecyclerView.setAdapter(fileAdapter);

        dialog.setContentView(view);

        dialog.show();
    }

    private class restoreFromBackupData extends AsyncTask<String, String, String>
    {
        String path;

        public restoreFromBackupData(String path)
        {
            this.path=path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ah.Show_Custom_Progress_Dialog();
        }

        protected String doInBackground(String... str)
        {
            try
            {
                restoreAppData(path);
            }
            catch (Exception e){
                Log.d("Response : ",""+e.getMessage());
            }
            return "";
        }

        protected void onPostExecute(String result)
        {
            ah.Close_Custom_Progress_Dialog();

            ah.customAlert(sh.get_string(R.string.str_restore_msg));
            refreshApp();
        }
    }

    public void restoreAppData(String path)
    {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            //ah.customAlert(""+e.getMessage());
        }

        String decrypted = text.toString();

        //==============================================

        ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details");

        for(int k=0;k<arr_data.size();k++)
        {
            if (arr_data.get(k).get("AlarmType").equalsIgnoreCase("M"))
            {
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("SundayAlarmId")));
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("MondayAlarmId")));
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("TuesdayAlarmId")));
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("WednesdayAlarmId")));
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("ThursdayAlarmId")));
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("FridayAlarmId")));
                MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("SaturdayAlarmId")));
            }
            else {
                ArrayList<HashMap<String, String>> arr_data2 = dh.getdata("tbl_alarm_sub_details",
                        "SuperId=" + arr_data.get(k).get("id"));

                for (int j = 0; j < arr_data2.size(); j++) {
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data2.get(j).get("AlarmId")));
                }
            }
        }

        //==============================================



        BackupRestore backupRestore=new Gson().fromJson(decrypted,BackupRestore.class);

        dh.REMOVE("tbl_container_details");
        for(int k=0;k<backupRestore.getContainerDetails().size();k++)
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("ContainerID", ""+ backupRestore.getContainerDetails().get(k).getContainerID());
            initialValues.put("ContainerValue", ""+ backupRestore.getContainerDetails().get(k).getContainerValue());
            initialValues.put("ContainerValueOZ", ""+ backupRestore.getContainerDetails().get(k).getContainerValueOZ());
            initialValues.put("ContainerMeasure", ""+ backupRestore.getContainerDetails().get(k).getContainerMeasure());
            initialValues.put("IsOpen", ""+ backupRestore.getContainerDetails().get(k).getIsOpen());
            initialValues.put("IsCustom", ""+ backupRestore.getContainerDetails().get(k).getIsCustom());
            dh.INSERT("tbl_container_details", initialValues);
        }

        dh.REMOVE("tbl_drink_details");
        for(int k=0;k<backupRestore.getDrinkDetails().size();k++)
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("ContainerValue", ""+ backupRestore.getDrinkDetails().get(k).getContainerValue());
            initialValues.put("ContainerValueOZ", ""+ backupRestore.getDrinkDetails().get(k).getContainerValueOZ());
            initialValues.put("ContainerMeasure", ""+ backupRestore.getDrinkDetails().get(k).getContainerMeasure());
            initialValues.put("DrinkDate", ""+ backupRestore.getDrinkDetails().get(k).getDrinkDate());
            initialValues.put("DrinkTime", ""+ backupRestore.getDrinkDetails().get(k).getDrinkTime());
            initialValues.put("DrinkDateTime", ""+ backupRestore.getDrinkDetails().get(k).getDrinkDateTime());
            initialValues.put("TodayGoal", ""+ backupRestore.getDrinkDetails().get(k).getTodayGoal());
            initialValues.put("TodayGoalOZ", ""+ backupRestore.getDrinkDetails().get(k).getTodayGoalOZ());
            dh.INSERT("tbl_drink_details", initialValues);
        }

        dh.REMOVE("tbl_alarm_details");
        dh.REMOVE("tbl_alarm_sub_details");

        for(int k=0;k<backupRestore.getAlarmDetails().size();k++)
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("AlarmTime", ""+ backupRestore.getAlarmDetails().get(k).getAlarmTime());
            initialValues.put("AlarmId", ""+ backupRestore.getAlarmDetails().get(k).getAlarmId());
            initialValues.put("AlarmType", ""+ backupRestore.getAlarmDetails().get(k).getAlarmType());
            initialValues.put("AlarmInterval", ""+ backupRestore.getAlarmDetails().get(k).getAlarmInterval());

            initialValues.put("SundayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmSundayId());
            initialValues.put("MondayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmMondayId());
            initialValues.put("TuesdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmTuesdayId());
            initialValues.put("WednesdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmWednesdayId());
            initialValues.put("ThursdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmThursdayId());
            initialValues.put("FridayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmFridayId());
            initialValues.put("SaturdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmSaturdayId());

            initialValues.put("IsOff", backupRestore.getAlarmDetails().get(k).getIsOff());
            initialValues.put("Sunday", backupRestore.getAlarmDetails().get(k).getSunday());
            initialValues.put("Monday", backupRestore.getAlarmDetails().get(k).getMonday());
            initialValues.put("Tuesday", backupRestore.getAlarmDetails().get(k).getTuesday());
            initialValues.put("Wednesday", backupRestore.getAlarmDetails().get(k).getWednesday());
            initialValues.put("Thursday", backupRestore.getAlarmDetails().get(k).getThursday());
            initialValues.put("Friday", backupRestore.getAlarmDetails().get(k).getFriday());
            initialValues.put("Saturday", backupRestore.getAlarmDetails().get(k).getSaturday());

            dh.INSERT("tbl_alarm_details", initialValues);

            if (backupRestore.getAlarmDetails().get(k).getAlarmType().equalsIgnoreCase("M")
                    && backupRestore.isManualReminderActive())
            {
                int hourOfDay = Integer.parseInt("" +
                        dth.FormateDateFromString("hh:mm a", "HH",
                                backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));
                int minute = Integer.parseInt("" +
                        dth.FormateDateFromString("hh:mm a", "mm",
                                backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));

                if (backupRestore.getAlarmDetails().get(k).getSunday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.SUNDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSundayId()));

                if (backupRestore.getAlarmDetails().get(k).getMonday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.MONDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmMondayId()));

                if (backupRestore.getAlarmDetails().get(k).getTuesday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.TUESDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmTuesdayId()));

                if (backupRestore.getAlarmDetails().get(k).getWednesday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.WEDNESDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmWednesdayId()));

                if (backupRestore.getAlarmDetails().get(k).getThursday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.THURSDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmThursdayId()));

                if (backupRestore.getAlarmDetails().get(k).getFriday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.FRIDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmFridayId()));

                if (backupRestore.getAlarmDetails().get(k).getSaturday() == 1)
                    MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.SATURDAY, hourOfDay, minute
                            , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSaturdayId()));
            }

            String getLastId=dh.GET_LAST_ID("tbl_alarm_details");

            List<AlarmSubDetails> alarmSubDetailsList=backupRestore.getAlarmDetails().get(k).getAlarmSubDetails();

            for(int j=0;j<alarmSubDetailsList.size();j++)
            {
                ContentValues initialValues2 = new ContentValues();
                initialValues2.put("AlarmTime", "" + alarmSubDetailsList.get(j).getAlarmTime());
                initialValues2.put("AlarmId", "" + alarmSubDetailsList.get(j).getAlarmId());
                initialValues2.put("SuperId", "" + getLastId);
                dh.INSERT("tbl_alarm_sub_details", initialValues2);

                if(!backupRestore.isManualReminderActive()) {

                    int hourOfDay = Integer.parseInt("" +
                            dth.FormateDateFromString("hh:mm a", "HH",
                                    alarmSubDetailsList.get(j).getAlarmTime().trim()));
                    int minute = Integer.parseInt("" +
                            dth.FormateDateFromString("hh:mm a", "mm",
                                    alarmSubDetailsList.get(j).getAlarmTime().trim()));

                    Calendar updateTime = Calendar.getInstance();
                    updateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    updateTime.set(Calendar.MINUTE, minute);
                    updateTime.set(Calendar.SECOND,0);

                    MyAlarmManager.scheduleAutoRecurringAlarm(mContext, updateTime, Integer.parseInt("" + alarmSubDetailsList.get(j).getAlarmId()));

                    //setRecurringAlarm(mContext, hourOfDay, minute, Integer.parseInt("" + alarmSubDetailsList.get(j).getAlarmId()));
                }
            }
        }


        ph.savePreferences(URLFactory.DAILY_WATER,backupRestore.getTotalDrink());
        URLFactory.DAILY_WATER_VALUE=backupRestore.getTotalDrink();

        ph.savePreferences(URLFactory.PERSON_WEIGHT,backupRestore.getTotalWeight());
        ph.savePreferences(URLFactory.PERSON_HEIGHT,backupRestore.getTotalHeight());

        ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT,backupRestore.isKgUnit());
        ph.savePreferences(URLFactory.PERSON_HEIGHT_UNIT,backupRestore.isCMUnit());

        ph.savePreferences(URLFactory.WATER_UNIT,backupRestore.isMlUnit()?"ml":"fl oz");
        URLFactory.WATER_UNIT_VALUE=backupRestore.isMlUnit()?"ml":"fl oz";

        ph.savePreferences(URLFactory.REMINDER_OPTION,backupRestore.getReminderOption());
        ph.savePreferences(URLFactory.REMINDER_SOUND,backupRestore.getReminderSound());
        ph.savePreferences(URLFactory.DISABLE_NOTIFICATION,backupRestore.isDisableNotifiction());
        ph.savePreferences(URLFactory.IS_MANUAL_REMINDER,backupRestore.isManualReminderActive());
        ph.savePreferences(URLFactory.REMINDER_VIBRATE,backupRestore.isReminderVibrate());

        ph.savePreferences(URLFactory.USER_NAME,backupRestore.getUserName());
        ph.savePreferences(URLFactory.USER_GENDER,backupRestore.getUserGender());

        ph.savePreferences(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER,backupRestore.isDisableSound());


        ph.savePreferences(URLFactory.AUTO_BACK_UP,backupRestore.isAutoBackup());
        ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE,backupRestore.getAutoBackupType());
        ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,backupRestore.getAutoBackupId());


        MyAlarmManager.cancelRecurringAlarm(mContext,ph.getInt(URLFactory.AUTO_BACK_UP_ID));

        if(ph.getBoolean(URLFactory.AUTO_BACK_UP))
        {
            setTime();

            int _id = (int) System.currentTimeMillis();

            ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,_id);

            if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==0)
            {
                MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,0);
            }
            else if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==1)
            {
                MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,1);
            }
            else if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==2)
            {
                MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,2);
            }
        }


        ph.savePreferences(URLFactory.IS_ACTIVE,backupRestore.isActive());
        ph.savePreferences(URLFactory.IS_BREATFEEDING,backupRestore.isBreastfeeding());
        ph.savePreferences(URLFactory.IS_PREGNANT,backupRestore.isPregnant());
        ph.savePreferences(URLFactory.WEATHER_CONSITIONS,backupRestore.getWeatherConditions());



        //ah.customAlert(sh.get_string(R.string.str_restore_msg));

        //refreshApp();
    }

    public void selectFile() {
        startActivityForResult(getFileChooserIntent(),SELECT_FILE);
    }

    private Intent getFileChooserIntent() {
        //String[] mimeTypes = {"image/*", "application/pdf"};
        String[] mimeTypes = {"text/plain"};


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            String path = FileUtils2.getPath(act, data.getData());

            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append("\n");

                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                //ah.customAlert(""+e.getMessage());
            }

            String decrypted = text.toString();



            //==============================================

            ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details");

            for(int k=0;k<arr_data.size();k++)
            {
                if (arr_data.get(k).get("AlarmType").equalsIgnoreCase("M"))
                {
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("SundayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("MondayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("TuesdayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("WednesdayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("ThursdayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("FridayAlarmId")));
                    MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data.get(k).get("SaturdayAlarmId")));
                }
                else {
                    ArrayList<HashMap<String, String>> arr_data2 = dh.getdata("tbl_alarm_sub_details",
                            "SuperId=" + arr_data.get(k).get("id"));

                    for (int j = 0; j < arr_data2.size(); j++) {
                        MyAlarmManager.cancelRecurringAlarm(mContext,Integer.parseInt(arr_data2.get(j).get("AlarmId")));
                    }
                }
            }

            //==============================================



            BackupRestore backupRestore=new Gson().fromJson(decrypted,BackupRestore.class);

            dh.REMOVE("tbl_container_details");
            for(int k=0;k<backupRestore.getContainerDetails().size();k++)
            {
                ContentValues initialValues = new ContentValues();
                initialValues.put("ContainerID", ""+ backupRestore.getContainerDetails().get(k).getContainerID());
                initialValues.put("ContainerValue", ""+ backupRestore.getContainerDetails().get(k).getContainerValue());
                initialValues.put("ContainerValueOZ", ""+ backupRestore.getContainerDetails().get(k).getContainerValueOZ());
                initialValues.put("ContainerMeasure", ""+ backupRestore.getContainerDetails().get(k).getContainerMeasure());
                initialValues.put("IsOpen", ""+ backupRestore.getContainerDetails().get(k).getIsOpen());
                initialValues.put("IsCustom", ""+ backupRestore.getContainerDetails().get(k).getIsCustom());
                dh.INSERT("tbl_container_details", initialValues);
            }

            dh.REMOVE("tbl_drink_details");
            for(int k=0;k<backupRestore.getDrinkDetails().size();k++)
            {
                ContentValues initialValues = new ContentValues();
                initialValues.put("ContainerValue", ""+ backupRestore.getDrinkDetails().get(k).getContainerValue());
                initialValues.put("ContainerValueOZ", ""+ backupRestore.getDrinkDetails().get(k).getContainerValueOZ());
                initialValues.put("ContainerMeasure", ""+ backupRestore.getDrinkDetails().get(k).getContainerMeasure());
                initialValues.put("DrinkDate", ""+ backupRestore.getDrinkDetails().get(k).getDrinkDate());
                initialValues.put("DrinkTime", ""+ backupRestore.getDrinkDetails().get(k).getDrinkTime());
                initialValues.put("DrinkDateTime", ""+ backupRestore.getDrinkDetails().get(k).getDrinkDateTime());
                initialValues.put("TodayGoal", ""+ backupRestore.getDrinkDetails().get(k).getTodayGoal());
                initialValues.put("TodayGoalOZ", ""+ backupRestore.getDrinkDetails().get(k).getTodayGoalOZ());
                dh.INSERT("tbl_drink_details", initialValues);
            }

            dh.REMOVE("tbl_alarm_details");
            dh.REMOVE("tbl_alarm_sub_details");

            for(int k=0;k<backupRestore.getAlarmDetails().size();k++)
            {
                ContentValues initialValues = new ContentValues();
                initialValues.put("AlarmTime", ""+ backupRestore.getAlarmDetails().get(k).getAlarmTime());
                initialValues.put("AlarmId", ""+ backupRestore.getAlarmDetails().get(k).getAlarmId());
                initialValues.put("AlarmType", ""+ backupRestore.getAlarmDetails().get(k).getAlarmType());
                initialValues.put("AlarmInterval", ""+ backupRestore.getAlarmDetails().get(k).getAlarmInterval());

                initialValues.put("SundayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmSundayId());
                initialValues.put("MondayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmMondayId());
                initialValues.put("TuesdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmTuesdayId());
                initialValues.put("WednesdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmWednesdayId());
                initialValues.put("ThursdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmThursdayId());
                initialValues.put("FridayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmFridayId());
                initialValues.put("SaturdayAlarmId", backupRestore.getAlarmDetails().get(k).getAlarmSaturdayId());

                initialValues.put("IsOff", backupRestore.getAlarmDetails().get(k).getIsOff());
                initialValues.put("Sunday", backupRestore.getAlarmDetails().get(k).getSunday());
                initialValues.put("Monday", backupRestore.getAlarmDetails().get(k).getMonday());
                initialValues.put("Tuesday", backupRestore.getAlarmDetails().get(k).getTuesday());
                initialValues.put("Wednesday", backupRestore.getAlarmDetails().get(k).getWednesday());
                initialValues.put("Thursday", backupRestore.getAlarmDetails().get(k).getThursday());
                initialValues.put("Friday", backupRestore.getAlarmDetails().get(k).getFriday());
                initialValues.put("Saturday", backupRestore.getAlarmDetails().get(k).getSaturday());

                dh.INSERT("tbl_alarm_details", initialValues);

                if (backupRestore.getAlarmDetails().get(k).getAlarmType().equalsIgnoreCase("M")
                        && backupRestore.isManualReminderActive())
                {
                    int hourOfDay = Integer.parseInt("" +
                            dth.FormateDateFromString("hh:mm a", "HH",
                                    backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));
                    int minute = Integer.parseInt("" +
                            dth.FormateDateFromString("hh:mm a", "mm",
                                    backupRestore.getAlarmDetails().get(k).getAlarmTime().trim()));

                    if (backupRestore.getAlarmDetails().get(k).getSunday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.SUNDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSundayId()));

                    if (backupRestore.getAlarmDetails().get(k).getMonday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.MONDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmMondayId()));

                    if (backupRestore.getAlarmDetails().get(k).getTuesday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.TUESDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmTuesdayId()));

                    if (backupRestore.getAlarmDetails().get(k).getWednesday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.WEDNESDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmWednesdayId()));

                    if (backupRestore.getAlarmDetails().get(k).getThursday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.THURSDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmThursdayId()));

                    if (backupRestore.getAlarmDetails().get(k).getFriday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.FRIDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmFridayId()));

                    if (backupRestore.getAlarmDetails().get(k).getSaturday() == 1)
                        MyAlarmManager.scheduleManualRecurringAlarm(mContext, Calendar.SATURDAY, hourOfDay, minute
                                , Integer.parseInt("" + backupRestore.getAlarmDetails().get(k).getAlarmSaturdayId()));
                }

                String getLastId=dh.GET_LAST_ID("tbl_alarm_details");

                List<AlarmSubDetails> alarmSubDetailsList=backupRestore.getAlarmDetails().get(k).getAlarmSubDetails();

                for(int j=0;j<alarmSubDetailsList.size();j++)
                {
                    ContentValues initialValues2 = new ContentValues();
                    initialValues2.put("AlarmTime", "" + alarmSubDetailsList.get(j).getAlarmTime());
                    initialValues2.put("AlarmId", "" + alarmSubDetailsList.get(j).getAlarmId());
                    initialValues2.put("SuperId", "" + getLastId);
                    dh.INSERT("tbl_alarm_sub_details", initialValues2);

                    if(!backupRestore.isManualReminderActive()) {

                        int hourOfDay = Integer.parseInt("" +
                                dth.FormateDateFromString("hh:mm a", "HH",
                                        alarmSubDetailsList.get(j).getAlarmTime().trim()));
                        int minute = Integer.parseInt("" +
                                dth.FormateDateFromString("hh:mm a", "mm",
                                        alarmSubDetailsList.get(j).getAlarmTime().trim()));

                        Calendar updateTime = Calendar.getInstance();
                        updateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        updateTime.set(Calendar.MINUTE, minute);
                        updateTime.set(Calendar.SECOND,0);

                        MyAlarmManager.scheduleAutoRecurringAlarm(mContext, updateTime, Integer.parseInt("" + alarmSubDetailsList.get(j).getAlarmId()));

                        //setRecurringAlarm(mContext, hourOfDay, minute, Integer.parseInt("" + alarmSubDetailsList.get(j).getAlarmId()));
                    }
                }
            }


            ph.savePreferences(URLFactory.DAILY_WATER,backupRestore.getTotalDrink());
            URLFactory.DAILY_WATER_VALUE=backupRestore.getTotalDrink();

            ph.savePreferences(URLFactory.PERSON_WEIGHT,backupRestore.getTotalWeight());
            ph.savePreferences(URLFactory.PERSON_HEIGHT,backupRestore.getTotalHeight());

            ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT,backupRestore.isKgUnit());
            ph.savePreferences(URLFactory.PERSON_HEIGHT_UNIT,backupRestore.isCMUnit());

            ph.savePreferences(URLFactory.WATER_UNIT,backupRestore.isMlUnit()?"ml":"fl oz");
            URLFactory.WATER_UNIT_VALUE=backupRestore.isMlUnit()?"ml":"fl oz";

            ph.savePreferences(URLFactory.REMINDER_OPTION,backupRestore.getReminderOption());
            ph.savePreferences(URLFactory.REMINDER_SOUND,backupRestore.getReminderSound());
            ph.savePreferences(URLFactory.DISABLE_NOTIFICATION,backupRestore.isDisableNotifiction());
            ph.savePreferences(URLFactory.IS_MANUAL_REMINDER,backupRestore.isManualReminderActive());
            ph.savePreferences(URLFactory.REMINDER_VIBRATE,backupRestore.isReminderVibrate());

            ph.savePreferences(URLFactory.USER_NAME,backupRestore.getUserName());
            ph.savePreferences(URLFactory.USER_GENDER,backupRestore.getUserGender());


            ph.savePreferences(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER,backupRestore.isDisableSound());


            ph.savePreferences(URLFactory.AUTO_BACK_UP,backupRestore.isAutoBackup());
            ph.savePreferences(URLFactory.AUTO_BACK_UP_TYPE,backupRestore.getAutoBackupType());
            ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,backupRestore.getAutoBackupId());

            MyAlarmManager.cancelRecurringAlarm(mContext,ph.getInt(URLFactory.AUTO_BACK_UP_ID));

            if(ph.getBoolean(URLFactory.AUTO_BACK_UP))
            {
                setTime();

                int _id = (int) System.currentTimeMillis();

                ph.savePreferences(URLFactory.AUTO_BACK_UP_ID,_id);

                if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==0)
                {
                    MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,0);
                }
                else if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==1)
                {
                    MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,1);
                }
                else if(ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)==2)
                {
                    MyAlarmManager.scheduleAutoBackupAlarm(mContext,auto_backup_time,_id,2);
                }
            }

            ph.savePreferences(URLFactory.IS_ACTIVE,backupRestore.isActive());
            ph.savePreferences(URLFactory.IS_BREATFEEDING,backupRestore.isBreastfeeding());
            ph.savePreferences(URLFactory.IS_PREGNANT,backupRestore.isPregnant());
            ph.savePreferences(URLFactory.WEATHER_CONSITIONS,backupRestore.getWeatherConditions());


            ah.customAlert(sh.get_string(R.string.str_restore_msg));

            refreshApp();
        }
    }

    public void refreshApp()
    {
        intent=new Intent(act,Screen_Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition( 0, 0);
        startActivity(intent);
        finish();
        overridePendingTransition( 0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(executeBackup)
                        //backup_data();
                        new backupFromDBData().execute();
                    else
                        restore_data();
                    return;
                } else {

                }
                break;
        }
    }
}