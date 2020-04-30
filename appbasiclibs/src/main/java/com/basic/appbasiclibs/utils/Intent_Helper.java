package com.basic.appbasiclibs.utils;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.appbasiclibs.R;
import com.basic.appbasiclibs.adapter.CustomShareAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Intent_Helper
{
    Context mContext;
    Activity act;

    //share list
    Intent email = new Intent(Intent.ACTION_SEND);
    CustomShareAdapter app_adapter;
    ArrayList<String> appname=new ArrayList<String>();
    /*String lst_app_name[]={"Gmail","hike","Direct Message","Facebook","Hangouts","Messaging"
            ,"Messenger","Skype","WhatsApp","Create a Pin","LinkedIn","Instagram","Kik"};*/
    String lst_app_name[]={"Gmail","Facebook","Twitter","LinkedIn"};

    public Intent_Helper(Context mContext, Activity act)
    {
        this.mContext=mContext;
        this.act=act;
    }

    public void OpenContactList()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        act.startActivityForResult(intent, Constant.PICK_CONTACT);
    }

    public void OpenGalllery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*, video/*");
        act.startActivityForResult(intent, 101);
    }

    public void ShowPDF(File file)
    {
        PackageManager packageManager = mContext.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        //List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        mContext.startActivity(intent);
    }

    public void ShareText(String title,String subject)
    {
        try
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT,title);//
            share.putExtra(Intent.EXTRA_TEXT,subject);
            mContext.startActivity(Intent.createChooser(share, Constant.general_share_title));
        }
        catch(Exception e){}
    }

    public void ShareText()
    {
        try
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            //share.putExtra(Intent.EXTRA_SUBJECT, Constant.general_share_subject);//
            //share.putExtra(Intent.EXTRA_TEXT, Constant.general_share_text);
            //mContext.startActivity(Intent.createChooser(share, Constant.general_share_title));
        }
        catch(Exception e){}
    }

    public void ShareText_SpecificApp(String title,String subject)
    {
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                //Log.i("Package Name", packageName);

                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                        || packageName.contains("com.whatsapp") || packageName.contains("com.google.android.apps.plus")
                        || packageName.contains("com.google.android.talk") || packageName.contains("com.slack")
                        || packageName.contains("com.google.android.gm") || packageName.contains("com.facebook.orca")
                        || packageName.contains("com.yahoo.mobile") || packageName.contains("com.skype.raider")
                        || packageName.contains("com.android.mms")|| packageName.contains("com.linkedin.android")
                        || packageName.contains("com.google.android.apps.messaging")) {
                    Intent intent = new Intent();

                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, title);
                    intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Collections.sort(targetShareIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                mContext.startActivity(chooserIntent);
            } else {
                Toast.makeText(mContext, "No app to share.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void get_share_list()
    {
        Constant.pm=mContext.getPackageManager();
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{});
        email.putExtra(Intent.EXTRA_SUBJECT,"SUBJECT");
        email.putExtra(Intent.EXTRA_TEXT,"TEXT");
        email.setType("text/plain");
        Constant.launchables= Constant.pm.queryIntentActivities(email, 0);
        Constant.launchables_sel=new ArrayList<ResolveInfo>();
        try
        {
            for(int k1 = 0; k1< Constant.launchables.size(); k1++)
            {
                int position=k1;
                for(int k=0;k<lst_app_name.length;k++)
                {
                    if(Constant.launchables.get(position).loadLabel(Constant.pm).toString().toLowerCase().contains(lst_app_name[k].toLowerCase()))
                        Constant.launchables_sel.add(Constant.launchables.get(position));
                }
            }
            Collections.sort(Constant.launchables_sel,new ResolveInfo.DisplayNameComparator(Constant.pm));
            app_adapter=new CustomShareAdapter(mContext, Constant.pm, Constant.launchables_sel);
        }
        catch(Exception e)
        {
            //alert(ctx,"Error :"+e.getMessage());
        }
    }

    public  void CustomShare(final String str_subject, final String str_text)
    {
        get_share_list();
        final Dialog dialog = new Dialog(mContext, R.style.AppDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_share_dialog);
        dialog.setCancelable(true);

        int width = mContext.getResources().getDisplayMetrics().widthPixels-80;
        int height = mContext.getResources().getDisplayMetrics().heightPixels-100;

        dialog.getWindow().setLayout(width, height);

        TextView txt=(TextView) dialog.findViewById(R.id.textView1);
        txt.setText(Constant.share_purchase_title);

        ListView lv=(ListView)dialog.findViewById(R.id.listView1);

        lv.setAdapter(app_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                dialog.dismiss();

                ResolveInfo launchable=app_adapter.getItem(position);
                ActivityInfo activity=launchable.activityInfo;
                appname.clear();
                appname=app_adapter.getAppName();
                String apname=app_adapter.get_app_name(position);

                ComponentName name=new ComponentName(activity.applicationInfo.packageName,activity.name);
                email.addCategory(Intent.CATEGORY_LAUNCHER);
                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                email.setComponent(name);
                email.putExtra(Intent.EXTRA_SUBJECT,""+str_subject);
                email.putExtra(Intent.EXTRA_TEXT,""+str_text);

                dialog.dismiss();
                mContext.startActivity(email);
            }
        });
        dialog.show();
    }

    public void SEND_EMAIL_WITH_ATTACHMENT(String[] filePaths)
    {
        ArrayList<Uri> uris = new ArrayList<Uri>();

        Intent emailintent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailintent.setType("text/plain");

        final PackageManager pm = mContext.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailintent, 0);

        ResolveInfo best = null;

        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;

        if (best != null)
            emailintent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        emailintent.setType("message/rfc822");
        emailintent.putExtra(Intent.EXTRA_EMAIL,new String[] {""});
        emailintent.setType("application/pdf");
        //String[] filePaths = new String[] {Constant.LOCAL_DESKTOP_TEMPLATE_URL+"/"+desktop_name,Constant.LOCAL_MOBILE_TEMPLATE_URL+"/"+mobile_name};

        //String length_mul="";

        boolean is_empty=true;

        for (String file : filePaths)
        {
            File fileIn = new File(file);
            //length_mul+=fileIn.length()+"\n";
            if(fileIn.length()>0)
            {
                Uri u = Uri.fromFile(fileIn);
                uris.add(u);
                is_empty=false;
            }
        }

        if(!is_empty)
        {
            emailintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            mContext.startActivity(emailintent);
        }
    }

    private void setAlarm(Calendar targetCal, int unique_id)
    {
        Intent intent = new Intent(act.getBaseContext(), Intent_Helper.class);
        final int _id = unique_id;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(act.getBaseContext(), _id, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)act.getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    //facebook
    public void OpenFacebookPage(String fbpageid)
    {

        String facebookPageID = fbpageid;

        String facebookUrl = "https://www.facebook.com/" + facebookPageID;

        String facebookUrlScheme = "fb://page/" + facebookPageID;

        try
        {
            boolean isAppInstalled = appInstalledOrNot("com.facebook.katana");
            if(isAppInstalled)
            {
                int versionCode = mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

                if (versionCode >= 3002850)
                {
                    Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                else
                {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrlScheme)));
                }
            }
            else
            {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana")));
            }
        } catch (PackageManager.NameNotFoundException e) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }
}