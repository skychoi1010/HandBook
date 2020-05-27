package com.waterdiary.drinkreminder;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.basic.appbasiclibs.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.waterdiary.drinkreminder.adapter.ContainerAdapterNew;
import com.waterdiary.drinkreminder.adapter.MenuAdapter;
import com.waterdiary.drinkreminder.adapter.MyPageAdapter;
import com.waterdiary.drinkreminder.adapter.SoundAdapter;
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;
import com.waterdiary.drinkreminder.custom.InputFilterWeightRange;
import com.waterdiary.drinkreminder.model.Container;
import com.waterdiary.drinkreminder.model.Menu;
import com.waterdiary.drinkreminder.model.NextReminderModel;
import com.waterdiary.drinkreminder.model.SoundModel;
import com.waterdiary.drinkreminder.mywidgets.NewAppWidget;
import com.waterdiary.drinkreminder.utils.HeightWeightHelper;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Screen_Dashboard extends MasterBaseAppCompatActivity
{
	// Header
	ImageView btn_menu;
	ImageView btn_alarm;
	ImageView img_pre,img_next;
	AppCompatTextView lbl_toolbar_title;

	DrawerLayout mDrawerLayout;
	RecyclerView mDrawerList;
	AppCompatTextView lbl_user_name;
	LinearLayout btn_rate_us,btn_contact_us;


	ArrayList<Menu> menu_name=new ArrayList<>();
	MenuAdapter menuAdapter;

	public static Calendar filter_cal;
	public static Calendar today_cal;
	public static Calendar yesterday_cal;

	List<SoundModel> lst_sounds=new ArrayList<>();
	SoundAdapter soundAdapter;


	//====================================================
	//====================================================
	//====================================================


	RelativeLayout add_water;

	ArrayList<Container> containerArrayList=new ArrayList<>();
	ContainerAdapterNew adapter;

	AppCompatTextView container_name;
	ImageView img_selected_container;
	RelativeLayout selected_container_block,open_history;

	float drink_water=0;
	float old_drink_water=0;
	int selected_pos=0;

	BottomSheetDialog bottomSheetDialog;

    //======================

	AppCompatTextView lbl_total_goal,lbl_total_drunk;

	//int current_progress=0;
	//int new_progress=0;
	Handler handler;
	Runnable runnable;

	AppCompatTextView lbl_next_reminder;
	LinearLayout next_reminder_block;


	Handler handlerReminder;
	Runnable runnableReminder;


	RelativeLayout content_frame;
	RelativeLayout content_frame_test;
	LottieAnimationView animationView;

	int max_bottle_height=870;
	int progress_bottle_height=0;

	int cp=0;
	int np=0;

	Ringtone ringtone;

	boolean btnclick=true;

	AdView mAdView;
	InterstitialAd mInterstitialAd;
	RewardedVideoAd rewardedVideoAd;
	boolean showWholeAd=false;


	LinearLayout banner_view;

	ImageView img_user;

	LinearLayout open_profile;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_dashboard);

		if (ph.getFloat(URLFactory.DAILY_WATER) == 0) {
			URLFactory.DAILY_WATER_VALUE = 2500;
		} else {
			URLFactory.DAILY_WATER_VALUE = ph.getFloat(URLFactory.DAILY_WATER);
		}

		if (sh.check_blank_data("" + ph.getString(URLFactory.WATER_UNIT))) {
			URLFactory.WATER_UNIT_VALUE = "ml";
		} else {
			URLFactory.WATER_UNIT_VALUE = ph.getString(URLFactory.WATER_UNIT);
		}

		FindViewById();

		//next_reminder_block.setVisibility(View.INVISIBLE);
		ringtone = RingtoneManager.getRingtone(mContext, Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.fill_water_sound));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			ringtone.setLooping(false);
		}

		try {

			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			String packageName = getApplicationContext().getPackageName();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (!pm.isIgnoringBatteryOptimizations(packageName)) {

					if(android.os.Build.MANUFACTURER.equalsIgnoreCase("OnePlus"))
					{
						/*Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
						startActivity(intent);*/
						showPermissionDialog();
					}
					else
					{
						Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
						intent.setData(Uri.parse("package:" + packageName));
						startActivity(intent);
					}
				}
				/*else
				{
					Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
					//intent.setData(Uri.parse("package:" + packageName));
					startActivity(intent);
				}*/
			}

			//showPermissionDialog();
		}
		catch (ActivityNotFoundException e) {
			e.printStackTrace();
			//ah.Show_Alert_Dialog(""+e.getMessage());
		}
	}

	public void showPermissionDialog()
	{
		try {

			final Dialog dialog = new Dialog(act);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
			dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


			final View view = LayoutInflater.from(act).inflate(R.layout.dialog_battery_optimization, null, false);

			DotsIndicator dots_indicator = view.findViewById(R.id.dots_indicator);
			ImageView img_cancel = view.findViewById(R.id.img_cancel);
			RelativeLayout btn_settings = view.findViewById(R.id.btn_settings);
			ViewPager viewPager = view.findViewById(R.id.viewPager);

			MyPageAdapter myPageAdapter=new MyPageAdapter(act);
			viewPager.setAdapter(myPageAdapter);

			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}

				@Override
				public void onPageSelected(int position) {
				}

				@Override
				public void onPageScrollStateChanged(int state) {
				}
			});

			viewPager.setOffscreenPageLimit(5);

			dots_indicator.setViewPager(viewPager);


			img_cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			btn_settings.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

					Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
					startActivity(intent);
				}
			});

			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
				}
			});

			dialog.setContentView(view);

			dialog.show();
		}
		catch (Exception e)
		{
			ah.Show_Alert_Dialog(""+e.getMessage());
		}
	}

	public void loadPhoto()
	{
		if(sh.check_blank_data(ph.getString(URLFactory.USER_PHOTO))) {
			Glide.with(act).load(ph.getBoolean(URLFactory.USER_GENDER) ? R.drawable.female_white
					: R.drawable.male_white).apply(RequestOptions.circleCropTransform())
					.into(img_user);
		}
		else
		{
			boolean ex=false;

			try
			{
				File f=new File(ph.getString(URLFactory.USER_PHOTO));
				if(f.exists())
					ex=true;
			}
			catch (Exception e){}

			if(ex) {
				Glide.with(act).load(ph.getString(URLFactory.USER_PHOTO)).apply(RequestOptions.circleCropTransform())
						.into(img_user);
			}
			else
			{
				Glide.with(act).load(ph.getBoolean(URLFactory.USER_GENDER) ? R.drawable.female_white
						: R.drawable.male_white).apply(RequestOptions.circleCropTransform())
						.into(img_user);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		//ah.customAlert(""+URLFactory.RELOAD_DASHBOARD);

		if(URLFactory.RELOAD_DASHBOARD)
		{
			init();
		}
		else
		{
			URLFactory.RELOAD_DASHBOARD=true;
		}
	}

	public void init()
	{
		initMenuScreen();

		FindViewById();

		Body();

		//loadAds();

		//getAllReminderData();

		//fetchNextReminder();

	}




	public void initMenuScreen()
	{
		filter_cal=Calendar.getInstance();
		today_cal=Calendar.getInstance();
		yesterday_cal=Calendar.getInstance();
		yesterday_cal.add(Calendar.DATE,-1);

		menuBody();

		lbl_toolbar_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				filter_cal.setTimeInMillis(today_cal.getTimeInMillis());
				lbl_toolbar_title.setText(sh.get_string(R.string.str_today));
				//setCustomDate(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT));
			}
		});

		lbl_user_name.setText(ph.getString(URLFactory.USER_NAME));
	}

	public void menuBody()
	{
		btn_menu=findViewById(R.id.btn_menu);
		btn_alarm=findViewById(R.id.btn_alarm);
		lbl_toolbar_title=findViewById(R.id.lbl_toolbar_title);
		img_pre=findViewById(R.id.img_pre);
		img_next=findViewById(R.id.img_next);

		img_user=findViewById(R.id.img_user);
		//open_profile=findViewById(R.id.open_profile);

		btn_rate_us=findViewById(R.id.btn_rate_us);
		btn_contact_us=findViewById(R.id.btn_contact_us);
		mDrawerLayout = findViewById(R.id.drawer_layout);
		mDrawerList = findViewById(R.id.left_drawer);
		lbl_user_name=findViewById(R.id.lbl_user_name);

		loadPhoto();

		lbl_toolbar_title.setText(sh.get_string(R.string.str_today));
		lbl_user_name.setText(ph.getString(URLFactory.USER_NAME));


		menu_name.clear();
		menu_name.add(new Menu(sh.get_string(R.string.str_home),true));
		//menu_name.add(new Menu(sh.get_string(R.string.str_drink_history),false));
		menu_name.add(new Menu(sh.get_string(R.string.str_notification_settings),false));
		menu_name.add(new Menu(sh.get_string(R.string.str_do_more_stretching),false));
		menu_name.add(new Menu(sh.get_string(R.string.str_health_tips),false));
		menu_name.add(new Menu(sh.get_string(R.string.str_store),false));
		//menu_name.add(new Menu(sh.get_string(R.string.str_faqs),false));
		menu_name.add(new Menu(sh.get_string(R.string.str_privacy_policy),false));
		menu_name.add(new Menu(sh.get_string(R.string.str_tell_a_friend),false));

		menuAdapter = new MenuAdapter(act, menu_name, new MenuAdapter.CallBack() {
			@Override
			public void onClickSelect(Menu menu, int position) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);

               if(position==1)
				{
					intent=new Intent(act,handbook_notifisettings.class);
					startActivity(intent);
				}
				else if(position==2)
				{
					intent=new Intent(act,handbook_stretching.class);
					startActivity(intent);
				}
				else if(position==3)
				{
					intent=new Intent(act,handbook_healthtips.class);
					startActivity(intent);
				}
				else if(position==4)
				{
					intent=new Intent(act,handbook_store.class);
					startActivity(intent);
				}
			   /*else if(position==5)
			   {
				   intent=new Intent(act,Screen_FAQ.class);
				   startActivity(intent);
			   }*/
				else if(position==5)
				{
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(URLFactory.PRIVACY_POLICY_ULR));
					startActivity(i);
				}
				else if(position==6)
				{
					String str=sh.get_string(R.string.app_share_txt).replace("#1",URLFactory.APP_SHARE_URL);

					ih.ShareText(getApplicationName(mContext),str);
				}
			}
		});

		btn_rate_us.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String appPackageName = getPackageName();
				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});

		btn_contact_us.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{
					Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "youremail@gmail.com"));
					intent.putExtra(Intent.EXTRA_SUBJECT, "");
					intent.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(intent);
				}
				catch (Exception ex){}
			}
		});

		mDrawerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

		mDrawerList.setAdapter(menuAdapter);

		/*open_profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
						mDrawerLayout.closeDrawer(Gravity.LEFT);
				}
				catch(Exception e){}

				startActivity(new Intent(act,Screen_Profile.class));


			}
		});*/

		btn_alarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showReminderDialog();
			}
		});

		btn_menu.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				try {
					if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
						mDrawerLayout.closeDrawer(Gravity.LEFT);
					else
						mDrawerLayout.openDrawer(Gravity.LEFT);
				}
				catch(Exception e){}
			}
		});


		img_pre.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				filter_cal.add(Calendar.DATE,-1);

				if(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT).equalsIgnoreCase(dth.getDate(yesterday_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)))
					lbl_toolbar_title.setText(sh.get_string(R.string.str_yesterday));
				else
					lbl_toolbar_title.setText(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT));

				//setCustomDate(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT));
			}
		});

		img_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				filter_cal.add(Calendar.DATE,1);

				if(filter_cal.getTimeInMillis()>today_cal.getTimeInMillis()) {
					filter_cal.add(Calendar.DATE, -1);
					return;
				}

				if(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT).equalsIgnoreCase(dth.getDate(today_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)))
					lbl_toolbar_title.setText(sh.get_string(R.string.str_today));
				else if(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT).equalsIgnoreCase(dth.getDate(yesterday_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)))
					lbl_toolbar_title.setText(sh.get_string(R.string.str_yesterday));
				else
					lbl_toolbar_title.setText(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT));

				//setCustomDate(dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT));

			}
		});

	}
/*
	public void loadAds()
	{
		// Initialize the Mobile Ads SDK
		MobileAds.initialize(act,getString(R.string.admob_app_id));


		mAdView = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
				//ah.customAlert("onAdLoaded");
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
				//ah.customAlert("onAdFailedToLoad "+errorCode);
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
			}

			@Override
			public void onAdClicked() {
				// Code to be executed when the user clicks on an ad.
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when the user is about to return
				// to the app after tapping on an ad.
			}
		});

		mInterstitialAd = new InterstitialAd(act);
        mInterstitialAd.setAdUnitId(sh.get_string(R.string.admob_interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //ah.customAlert("onAdFailedToLoad "+errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                //ah.customAlert("onAdClosed");
                execute_add_water();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

		rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(act);
		rewardedVideoAd.loadAd(sh.get_string(R.string.admob_rewarded_video_id),
				new AdRequest.Builder().build());

		rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewardedVideoAdLoaded() {
				//rewardedVideoAd.show();
			}

			@Override
			public void onRewardedVideoAdOpened() {

			}

			@Override
			public void onRewardedVideoStarted() {

			}

			@Override
			public void onRewardedVideoAdClosed() {
				if(showWholeAd)
				{
					execute_add_water();
					showWholeAd=false;
				}
				else
					btnclick=true;

				rewardedVideoAd.loadAd(sh.get_string(R.string.admob_rewarded_video_id),
						new AdRequest.Builder().build());
			}

			@Override
			public void onRewarded(RewardItem rewardItem) {
				showWholeAd=true;
			}

			@Override
			public void onRewardedVideoAdLeftApplication() {

			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {

			}
		});
    }
*/


/*
	public void getAllReminderData()
	{
		List<NextReminderModel> reminder_data=new ArrayList<>();

		ArrayList<HashMap<String,String>> arr_data = dh.getdata("tbl_alarm_details");

		for(int k=0;k<arr_data.size();k++)
		{
			if(arr_data.get(k).get("AlarmType").equalsIgnoreCase("R"))
			{
				if(!ph.getBoolean(URLFactory.IS_MANUAL_REMINDER))
				{
					ArrayList<HashMap<String, String>> arr_data2 = dh.getdata("tbl_alarm_sub_details", "SuperId='" + arr_data.get(k).get("id") + "'");
					for (int j = 0; j < arr_data2.size(); j++) {
						reminder_data.add(new NextReminderModel(getMillisecond(arr_data2.get(j).get("AlarmTime")), arr_data2.get(j).get("AlarmTime")));
					}
				}
			}
			else
			{
				if(ph.getBoolean(URLFactory.IS_MANUAL_REMINDER)) {
					if (arr_data.get(k).get("IsOff").equalsIgnoreCase("0")) {
						reminder_data.add(new NextReminderModel(getMillisecond(arr_data.get(k).get("AlarmTime")), arr_data.get(k).get("AlarmTime")));
					}
				}
			}
		}

		Date mDate = new Date();
		Collections.sort(reminder_data);
		int tmp_pos=0;
		for(int k=0;k<reminder_data.size();k++)
		{
			if(reminder_data.get(k).getMillesecond()>mDate.getTime())
			{
				tmp_pos=k;
				break;
			}
		}
*/

/*
		if(reminder_data.size()>0) {

			//Locale locale = Resources.getSystem().getConfiguration().locale;
			//ah.Show_Alert_Dialog(""+locale.getDisplayLanguage());

			lbl_next_reminder.setText(sh.get_string(R.string.str_next_reminder).replace("$1", reminder_data.get(tmp_pos).getTime()));
		}
		else
			next_reminder_block.setVisibility(View.INVISIBLE);*/
//	}
/*
	public long getMillisecond(String givenDateString)
	{
		long timeInMilliseconds=0;

		givenDateString=dth.getFormatDate("yyyy-MM-dd")+" "+givenDateString;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.US);
		try {
			Date mDate = sdf.parse(givenDateString);
			timeInMilliseconds = mDate.getTime();
			System.out.println("Date in milli :: " + timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
			ah.Show_Alert_Dialog(e.getMessage());
		}

		return timeInMilliseconds;
	}

	public void fetchNextReminder()
	{
		runnableReminder = new Runnable() {
			@Override
			public void run() {

				getAllReminderData();
				handlerReminder.postDelayed(runnableReminder, 1000);
			}
		};
		handlerReminder = new Handler();
		handlerReminder.postDelayed(runnableReminder, 1000);
	}
*/




	private void FindViewById()
	{
		animationView=findViewById(R.id.animationView);
		content_frame=findViewById(R.id.content_frame);
		content_frame_test=findViewById(R.id.content_frame_test);

		content_frame.getViewTreeObserver()
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						int w = content_frame.getWidth();
						int h = content_frame.getHeight();
						Log.v("getWidthHeight", w+"   -   "+h);
					}
				});

		content_frame_test.getViewTreeObserver()
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						int w = content_frame_test.getWidth();
						int h = content_frame_test.getHeight();
						Log.v("getWidthHeight test", w+"   -   "+h);
						max_bottle_height=h-30;
					}
				});

		lbl_next_reminder=findViewById(R.id.lbl_next_reminder);
		next_reminder_block=findViewById(R.id.next_reminder_block);

		add_water=findViewById(R.id.add_water);

		container_name=findViewById(R.id.container_name);
		img_selected_container=findViewById(R.id.img_selected_container);
		selected_container_block=findViewById(R.id.selected_container_block);
		open_history=findViewById(R.id.open_history);

		lbl_total_goal=findViewById(R.id.lbl_total_goal);
		lbl_total_drunk=findViewById(R.id.lbl_total_drunk);

		banner_view=findViewById(R.id.banner_view);

	}

	private void Body()
	{
		ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_drink_details","DrinkDate ='"+dth.getCurrentDate(URLFactory.DATE_FORMAT)+"'");
		old_drink_water=0;
		for(int k=0;k<arr_data.size();k++)
		{
			//ah.Show_Alert_Dialog(""+arr_data.get(k).get("ContainerValue")+"\n\n"+arr_data.get(k).get("ContainerValueOZ")+"\n\n"+Double.parseDouble(""+arr_data.get(k).get("ContainerValueOZ")));

			if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
				old_drink_water+=Double.parseDouble(""+arr_data.get(k).get("ContainerValue"));
			else
				old_drink_water+=Double.parseDouble(""+arr_data.get(k).get("ContainerValueOZ"));

			//ah.Show_Alert_Dialog(""+arr_data.get(k).get("ContainerValueOZ"));
		}

		//count_today_drink(false);

		selected_container_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//openChangeContainerPicker();
				intent=new Intent(act,Screen_Dashboard.class);
				startActivity(intent);
			}
		});

		open_history.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				intent=new Intent(act,Screen_Report.class);
				startActivity(intent);


			}
		});

		img_selected_container.setVisibility(View.INVISIBLE);
		add_water.setVisibility(View.INVISIBLE);
		next_reminder_block.setVisibility(View.INVISIBLE);
		lbl_next_reminder.setVisibility(View.INVISIBLE);
		LinearLayout goal = findViewById(R.id.goal);
		goal.setVisibility(View.INVISIBLE);
		LinearLayout consumed = findViewById(R.id.consumed);
		consumed.setVisibility(View.INVISIBLE);
		/*add_water.setOnClickListener(new View.OnClickListener()
        {
			@Override
			public void onClick(View view)
            {

				if(containerArrayList.size()>0)
				{
					if(!dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)
							.equalsIgnoreCase(dth.getDate(today_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)))
					{
						return;
					}

                    if(!btnclick)
                        return;

                    btnclick=false;

                    Random random=new Random();

                    if(random.nextBoolean())
                    {
                        URLFactory.RELOAD_DASHBOARD = false;
                        if(URLFactory.LOAD_VIDEO_ADS)
                        {
                            if(rewardedVideoAd.isLoaded()) {
                                URLFactory.LOAD_VIDEO_ADS = false;
                                rewardedVideoAd.show();
                                //interstitialAd.show();
                            }
                            else {
                                execute_add_water();
                                URLFactory.RELOAD_DASHBOARD=true;
                            }
                        }
                        else
                        {
                            if(mInterstitialAd.isLoaded())
                            {
                                URLFactory.LOAD_VIDEO_ADS = true;
                                mInterstitialAd.show();
                            }
                            else {
                                execute_add_water();
                                URLFactory.RELOAD_DASHBOARD=true;
                            }
                        }
                    }
                    else
                    {
                        execute_add_water();
                    }
				}
			}
		});*/
/*
		load_all_container();

		String unit=ph.getString(URLFactory.WATER_UNIT);

		if(unit.equalsIgnoreCase("ml"))
		{
			container_name.setText(""+containerArrayList.get(selected_pos).getContainerValue()+" "+unit);
			if(containerArrayList.get(selected_pos).isCustom())
				Glide.with(mContext).load(R.drawable.ic_custom_ml).into(img_selected_container);
			else
				Glide.with(mContext).load(getImage(containerArrayList.get(selected_pos).getContainerValue())).into(img_selected_container);
		}
		else {
			container_name.setText("" + containerArrayList.get(selected_pos).getContainerValueOZ() + " " + unit);
			if(containerArrayList.get(selected_pos).isCustom())
				Glide.with(mContext).load(R.drawable.ic_custom_ml).into(img_selected_container);
			else
				Glide.with(mContext).load(getImage(containerArrayList.get(selected_pos).getContainerValueOZ())).into(img_selected_container);
		}

		adapter = new ContainerAdapterNew(act, containerArrayList, new ContainerAdapterNew.CallBack() {
			@Override
			public void onClickSelect(Container menu, int position) {

				bottomSheetDialog.dismiss();

				selected_pos=position;

				ph.savePreferences(URLFactory.SELECTED_CONTAINER,Integer.parseInt(menu.getContainerId()));

				for(int k=0;k<containerArrayList.size();k++)
				{
					containerArrayList.get(k).isSelected(false);
				}

				containerArrayList.get(position).isSelected(true);

				adapter.notifyDataSetChanged();

                String unit=ph.getString(URLFactory.WATER_UNIT);

                if(unit.equalsIgnoreCase("ml")) {
					container_name.setText("" + menu.getContainerValue() + " " + unit);
					if(menu.isCustom())
						Glide.with(mContext).load(R.drawable.ic_custom_ml).into(img_selected_container);
					else
						Glide.with(mContext).load(getImage(menu.getContainerValue())).into(img_selected_container);
				}
                else {
					container_name.setText("" + menu.getContainerValueOZ() + " " + unit);
					if(menu.isCustom())
						Glide.with(mContext).load(R.drawable.ic_custom_ml).into(img_selected_container);
					else
						Glide.with(mContext).load(getImage(menu.getContainerValueOZ())).into(img_selected_container);
				}

				//container_name.setText(""+menu.getContainerValue()+" ml");
			}
        });
	}
*/

/*	public void execute_add_water()
	{
*/
		/*if(!btnclick)
			return;

		btnclick=false;*/
/*
		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")
				&& drink_water>8000)
		{
			showDailyMoreThanTargetDialog();
			btnclick=true;
			return;
		}
		else if (!(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
				&& drink_water>270)
		{
			//ah.Show_Alert_Dialog("above 270");
			showDailyMoreThanTargetDialog();
			btnclick=true;
			return;
		}

		float count_drink_after_add_current_water=drink_water;

		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
			count_drink_after_add_current_water+=Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValue());
		else if (!(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")))
			count_drink_after_add_current_water+=Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValueOZ());


		Log.d("above8000",""+URLFactory.WATER_UNIT_VALUE+" @@@  "+drink_water
				+" @@@ "+count_drink_after_add_current_water);


		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")
				&& count_drink_after_add_current_water>8000)
		{
			//ah.Show_Alert_Dialog("above 8000");
			if(drink_water>=8000)
				showDailyMoreThanTargetDialog();
			else if(URLFactory.DAILY_WATER_VALUE<(8000-Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValue())))
				showDailyMoreThanTargetDialog();
		}
		else if (!(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
				&& count_drink_after_add_current_water>270)
		{
			//ah.Show_Alert_Dialog("above 270");
			if(drink_water>=270)
				showDailyMoreThanTargetDialog();
			else if(URLFactory.DAILY_WATER_VALUE<(270-Float.parseFloat(""+containerArrayList.get(selected_pos).getContainerValueOZ())))
				showDailyMoreThanTargetDialog();
		}

		if(drink_water==8000 && URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
			btnclick=true;
			return;
		}
		else if(drink_water==270 && !URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
		{
			btnclick=true;
			return;
		}



		if(!ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER)) {
			//if(!ringtone.isPlaying())
			ringtone.stop();
			ringtone.play();
		}

		ContentValues initialValues = new ContentValues();

		initialValues.put("ContainerValue", "" + containerArrayList.get(selected_pos).getContainerValue());
		initialValues.put("ContainerValueOZ", "" + containerArrayList.get(selected_pos).getContainerValueOZ());
		//initialValues.put("ContainerMeasure", "ml");

*/
		/*initialValues.put("DrinkDate", "" + dth.getCurrentDate("dd-MM-yyyy"));
		initialValues.put("DrinkTime", "" + dth.getCurrentTime(true));
		initialValues.put("DrinkDateTime", "" + dth.getCurrentDate("dd-MM-yyyy HH:mm:ss"));*/
/*
		initialValues.put("DrinkDate", "" + dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT));
		initialValues.put("DrinkTime", "" + dth.getCurrentTime(true));
		initialValues.put("DrinkDateTime", "" + dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)
				+" "+dth.getCurrentDate("HH:mm:ss"));

		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
			initialValues.put("TodayGoal", "" + URLFactory.DAILY_WATER_VALUE);
			initialValues.put("TodayGoalOZ", "" + HeightWeightHelper.mlToOzConverter(URLFactory.DAILY_WATER_VALUE));
		} else {
			initialValues.put("TodayGoal", "" + HeightWeightHelper.ozToMlConverter(URLFactory.DAILY_WATER_VALUE));
			initialValues.put("TodayGoalOZ", "" + URLFactory.DAILY_WATER_VALUE);
		}

		dh.INSERT("tbl_drink_details", initialValues);



		count_today_drink(true);


		Intent intent = new Intent(act, NewAppWidget.class);
		intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
		// since it seems the onUpdate() is only fired on that:
		int[] ids = AppWidgetManager.getInstance(act).getAppWidgetIds(new ComponentName(act, NewAppWidget.class));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		act.sendBroadcast(intent);
	}
*/
/*	public void load_all_container()
	{
		containerArrayList.clear();

		ArrayList<HashMap<String, String>> arr_container = dh.getdata("tbl_container_details","IsCustom",1);

		String selected_container_id="1";

		if(ph.getInt(URLFactory.SELECTED_CONTAINER)==0)
			selected_container_id="1";
		else
			selected_container_id=""+ph.getInt(URLFactory.SELECTED_CONTAINER);

		//Container container2=new Container();
		//containerArrayList.add(container2);

		for(int k=0;k<arr_container.size();k++)
		{
			Container container=new Container();
			container.setContainerId(arr_container.get(k).get("ContainerID"));
			container.setContainerValue(arr_container.get(k).get("ContainerValue"));
			container.setContainerValueOZ(arr_container.get(k).get("ContainerValueOZ"));
			container.isOpen(arr_container.get(k).get("IsOpen").equalsIgnoreCase("1")?true:false);
			container.isSelected(selected_container_id.equalsIgnoreCase(arr_container.get(k).get("ContainerID"))?true:false);
			container.isCustom(arr_container.get(k).get("IsCustom").equalsIgnoreCase("1")?true:false);
			if(container.isSelected())
				selected_pos=k;//+1
			containerArrayList.add(container);
		}
	}

	public void count_today_drink(boolean isRegularAnimation)
	{
		//ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_drink_details","DrinkDate ='"+dth.getCurrentDate(URLFactory.DATE_FORMAT)+"'");
		ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_drink_details"
				,"DrinkDate ='"+dth.getDate(filter_cal.getTimeInMillis(),URLFactory.DATE_FORMAT)+"'");

		drink_water=0;
		for(int k=0;k<arr_data.size();k++)
		{
			if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
			    drink_water+=Double.parseDouble(""+arr_data.get(k).get("ContainerValue"));
		    else
                drink_water+=Double.parseDouble(""+arr_data.get(k).get("ContainerValueOZ"));
		}

		lbl_total_drunk.setText(getData(""+(int)(drink_water)+" "+URLFactory.WATER_UNIT_VALUE));
		lbl_total_goal.setText(getData(""+(int)(URLFactory.DAILY_WATER_VALUE)+" "+URLFactory.WATER_UNIT_VALUE));

		refresh_bottle(true,isRegularAnimation);
	}

	public String getData(String str)
	{
		return  str.replace(",",".");
	}
*/
/*	public void callDialog()
	{
		if(old_drink_water<URLFactory.DAILY_WATER_VALUE) {
			if (drink_water >= URLFactory.DAILY_WATER_VALUE) {
				//ah.customAlert(sh.get_string(R.string.str_daily_goal_reached));
				showDailyGoalReachedDialog();
			}
*/
			/*else
				calculateAllDrink();*/
		}
		
		/*else
			calculateAllDrink();*/
/*
		old_drink_water=drink_water;
	}
*/
/*	public void refresh_bottle(boolean isFromCurrentProgress,boolean isRegularAnimation)
	{
		final long animationDuration=isRegularAnimation?50:5;

		if(handler!=null && runnable!=null)
		handler.removeCallbacks(runnable);

		btnclick=false;

		cp=progress_bottle_height;
		np=Math.round((drink_water*max_bottle_height)/URLFactory.DAILY_WATER_VALUE);
*/
		/*if(np>max_bottle_height)
			np=max_bottle_height;*/

/*		if(cp<=np && isFromCurrentProgress)
		{
			animationView.setVisibility(View.VISIBLE);
			runnable = new Runnable() {
				@Override
				public void run() {

					if(cp>max_bottle_height) {
						btnclick = true;
						callDialog();
					}
					else if(cp<np) {
						cp+=6;
						content_frame.getLayoutParams().height = cp;
						content_frame.requestLayout();
						handler.postDelayed(runnable, animationDuration);
					}
					else
					{
						btnclick=true;
						callDialog();
					}
				}
			};
			handler = new Handler();
			handler.postDelayed(runnable, animationDuration);
		}
		else if(np==0)
		{
			animationView.setVisibility(View.GONE);
			content_frame.getLayoutParams().height = np;
			content_frame.requestLayout();
			btnclick=true;
			//ah.customAlert("-->else if");
			callDialog();
		}
		else
		{
			content_frame.getLayoutParams().height = 0;
			cp=0;
			animationView.setVisibility(View.VISIBLE);
			runnable = new Runnable() {
				@Override
				public void run() {

					if(cp>max_bottle_height) {
						btnclick = true;
						callDialog();
					}
					else if(cp<np) {
						cp+=6;
						content_frame.getLayoutParams().height = cp;
						content_frame.requestLayout();
						handler.postDelayed(runnable, animationDuration);
					}
					else
					{
						//ah.customAlert("-->else else");
						btnclick=true;
						callDialog();
					}

				}
			};
			handler = new Handler();
			handler.postDelayed(runnable, animationDuration);
		}

		progress_bottle_height=np;

		if(np>0)
			animationView.setVisibility(View.VISIBLE);
		else
			animationView.setVisibility(View.GONE);
	}
*/
/*	public void count_specific_day_drink(String custom_date)
	{
		ArrayList<HashMap<String, String>>  arr_dataO=dh.getdata("tbl_drink_details","DrinkDate ='"+custom_date+"'");
		old_drink_water=0;
		for(int k=0;k<arr_dataO.size();k++)
		{
			if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
				old_drink_water+=Double.parseDouble(""+arr_dataO.get(k).get("ContainerValue"));
			else
				old_drink_water+=Double.parseDouble(""+arr_dataO.get(k).get("ContainerValueOZ"));
		}

		ArrayList<HashMap<String, String>>  arr_data22=dh.getdata("tbl_drink_details","DrinkDate ='"+custom_date+"'","id",1);

		//double total_drink=URLFactory.DAILY_WATER_VALUE;
		double total_drink=0;

		if(arr_data22.size()>0)
		{
			if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
				total_drink=Double.parseDouble(arr_data22.get(0).get("TodayGoal"));
			else
				total_drink=Double.parseDouble(arr_data22.get(0).get("TodayGoalOZ"));
		}


		ArrayList<HashMap<String, String>>  arr_data=dh.getdata("tbl_drink_details","DrinkDate ='"+custom_date+"'");

		drink_water=0;
		for(int k=0;k<arr_data.size();k++)
		{
			if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
				drink_water+=Integer.parseInt(arr_data.get(k).get("ContainerValue"));
			else
				drink_water+=Integer.parseInt(arr_data.get(k).get("ContainerValueOZ"));

			//drink_water+=Integer.parseInt(arr_data.get(k).get("ContainerValue"));
		}

		//ah.Show_Alert_Dialog(""+total_drink);

		//lbl_total_goal.setText(""+total_drink+" "+URLFactory.WATER_UNIT_VALUE);



		if(custom_date.equalsIgnoreCase(dth.getCurrentDate(URLFactory.DATE_FORMAT)))
			URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);
		else if(total_drink>0)
			URLFactory.DAILY_WATER_VALUE=Float.parseFloat(""+total_drink);
		else
			URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);


		lbl_total_drunk.setText(getData(""+(int)(drink_water)+" "+URLFactory.WATER_UNIT_VALUE));
		lbl_total_goal.setText(getData(""+(int)(URLFactory.DAILY_WATER_VALUE)+" "+URLFactory.WATER_UNIT_VALUE));

		refresh_bottle(false,false);
	}
*/
	public Activity getActivity()
	{
		return act;
	}
/*
	public void setCustomDate(String date) {
		count_specific_day_drink(date);
	}

	//==========================


	public void openChangeContainerPicker()
	{
		bottomSheetDialog=new BottomSheetDialog(act);

		//bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener()
		{
			@Override
			public void onShow(DialogInterface dialog) {
				BottomSheetDialog d = (BottomSheetDialog) dialog;
				FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
				if (bottomSheet == null)
					return;
				BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
				bottomSheet.setBackground(null);
			}
		});

		LayoutInflater layoutInflater=LayoutInflater.from(act);
		View view = layoutInflater.inflate(R.layout.bottom_sheet_change_container, null, false);

		final RecyclerView containerRecyclerViewN=view.findViewById(R.id.containerRecyclerView);
		RelativeLayout add_custom_container=view.findViewById(R.id.add_custom_container);

		GridLayoutManager manager = new GridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false);
		containerRecyclerViewN.setLayoutManager(manager);
		containerRecyclerViewN.setAdapter(adapter);

		add_custom_container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bottomSheetDialog.dismiss();
				openCustomContainerPicker();
			}
		});

		bottomSheetDialog.setContentView(view);

		bottomSheetDialog.show();
	}


	public void openCustomContainerPicker()
	{
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.bottom_sheet_add_custom_container, null, false);

		RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
		RelativeLayout btn_add=view.findViewById(R.id.btn_add);
		ImageView img_cancel=view.findViewById(R.id.img_cancel);

		final AppCompatEditText txt_value=view.findViewById(R.id.txt_value);
		AppCompatTextView lbl_unit=view.findViewById(R.id.lbl_unit);

		lbl_unit.setText(sh.get_string(R.string.str_capacity).replace("$1",URLFactory.WATER_UNIT_VALUE));

		if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
			txt_value.setFilters(new InputFilter[]{new InputFilterWeightRange(1,8000)});
		else
			txt_value.setFilters(new InputFilter[]{new InputFilterWeightRange(1,270)});

		txt_value.requestFocus();

		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		img_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(sh.check_blank_data(txt_value.getText().toString().trim()))
				{
					ah.customAlert(sh.get_string(R.string.str_enter_value_validation));
				}
				else if(Integer.parseInt(txt_value.getText().toString().trim())==0)
				{
					ah.customAlert(sh.get_string(R.string.str_enter_value_validation));
				}
				else
				{
					double tml=0,tfloz=0;

					//String where_check_val="ContainerValue=";

					if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
					{
						tml=Float.parseFloat(txt_value.getText().toString().trim());
						tfloz=HeightWeightHelper.mlToOzConverter(tml);
						//where_check_val=txt_value.getText().toString().trim();
					}
					else
					{
						tfloz=Float.parseFloat(txt_value.getText().toString().trim());
						tml=HeightWeightHelper.ozToMlConverter(tfloz);
					}

					Log.d("HeightWeightHelper",""+tml+" @@@ "+tfloz);
*/
					/*ah.Show_Alert_Dialog(""+tml+" @@@ "+tfloz
							+"\n\n"+Math.round(tml)+" @@@ "+Math.round(tfloz));*/


					//dh.TOTAL_ROW("tbl_container_details","ContainerValue="+)

/*
					Cursor c = Constant.SDB.rawQuery("SELECT MAX(ContainerID) FROM tbl_container_details", null);
					int nextContainerID=0;

					try {
						if (c.getCount() > 0) {
							c.moveToNext();
							nextContainerID = Integer.parseInt(c.getString(0)) + 1;
							//ah.customAlert("--> " +nextContainerID);
						}
					}
					catch (Exception e){}

					ContentValues initialValues = new ContentValues();

					initialValues.put("ContainerID", ""+nextContainerID);
					initialValues.put("ContainerValue", ""+Math.round(tml));
					initialValues.put("ContainerValueOZ", ""+Math.round(tfloz));
					initialValues.put("IsOpen", "1");
					initialValues.put("IsCustom", "1");

					dh.INSERT("tbl_container_details", initialValues);

					load_all_container();

					ph.savePreferences(URLFactory.SELECTED_CONTAINER,nextContainerID);

					int tmp_pos=-1;

					for(int k=0;k<containerArrayList.size();k++)
					{
						try {
							if (nextContainerID == Integer.parseInt(containerArrayList.get(k).getContainerId())) {
								containerArrayList.get(k).isSelected(true);
								tmp_pos=k;
							}
							else
								containerArrayList.get(k).isSelected(false);
						}
						catch (Exception e){
							containerArrayList.get(k).isSelected(false);
						}
					}


					String unit=ph.getString(URLFactory.WATER_UNIT);

					if(tmp_pos>=0)
					{
						selected_pos=tmp_pos;

						Container menu = containerArrayList.get(tmp_pos);

						if (unit.equalsIgnoreCase("ml")) {
							container_name.setText("" + menu.getContainerValue() + " " + unit);
							if(menu.isCustom())
								Glide.with(mContext).load(R.drawable.ic_custom_ml).into(img_selected_container);
							else
								Glide.with(mContext).load(getImage(menu.getContainerValue())).into(img_selected_container);
						} else {
							container_name.setText("" + menu.getContainerValueOZ() + " " + unit);
							if(menu.isCustom())
								Glide.with(mContext).load(R.drawable.ic_custom_ml).into(img_selected_container);
							else
								Glide.with(mContext).load(getImage(menu.getContainerValueOZ())).into(img_selected_container);
						}
					}

					//containerArrayList.get(position).isSelected(true);

					adapter.notifyDataSetChanged();

					dialog.dismiss();


				}
			}
		});

		dialog.setContentView(view);

		dialog.show();
	}
*/
	//=============================
/*
	public Integer getImage(String val)
	{
		Integer drawable=R.drawable.ic_custom_ml;

		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {

			if (Double.parseDouble(val) == 50)
				drawable = R.drawable.ic_50_ml;
			else if (Double.parseDouble(val) == 100)
				drawable = R.drawable.ic_100_ml;
			else if (Double.parseDouble(val) == 150)
				drawable = R.drawable.ic_150_ml;
			else if (Double.parseDouble(val) == 200)
				drawable = R.drawable.ic_200_ml;
			else if (Double.parseDouble(val) == 250)
				drawable = R.drawable.ic_250_ml;
			else if (Double.parseDouble(val) == 300)
				drawable = R.drawable.ic_300_ml;
			else if (Double.parseDouble(val) == 500)
				drawable = R.drawable.ic_500_ml;
			else if (Double.parseDouble(val) == 600)
				drawable = R.drawable.ic_600_ml;
			else if (Double.parseDouble(val) == 700)
				drawable = R.drawable.ic_700_ml;
			else if (Double.parseDouble(val) == 800)
				drawable = R.drawable.ic_800_ml;
			else if (Double.parseDouble(val) == 900)
				drawable = R.drawable.ic_900_ml;
			else if (Double.parseDouble(val) == 1000)
				drawable = R.drawable.ic_1000_ml;
		}
		else
		{
			if (Double.parseDouble(val) == 2)
				drawable = R.drawable.ic_50_ml;
			else if (Double.parseDouble(val) == 3)
				drawable = R.drawable.ic_100_ml;
			else if (Double.parseDouble(val) == 5)
				drawable = R.drawable.ic_150_ml;
			else if (Double.parseDouble(val) == 7)
				drawable = R.drawable.ic_200_ml;
			else if (Double.parseDouble(val) == 8)
				drawable = R.drawable.ic_250_ml;
			else if (Double.parseDouble(val) == 10)
				drawable = R.drawable.ic_300_ml;
			else if (Double.parseDouble(val) == 17)
				drawable = R.drawable.ic_500_ml;
			else if (Double.parseDouble(val) == 20)
				drawable = R.drawable.ic_600_ml;
			else if (Double.parseDouble(val) == 24)
				drawable = R.drawable.ic_700_ml;
			else if (Double.parseDouble(val) == 27)
				drawable = R.drawable.ic_800_ml;
			else if (Double.parseDouble(val) == 30)
				drawable = R.drawable.ic_900_ml;
			else if (Double.parseDouble(val) == 34)
				drawable = R.drawable.ic_1000_ml;
		}

		return drawable;
	}
*/

	//=============================

	public void showDailyGoalReachedDialog()
	{
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.dialog_goal_reached, null, false);


		ImageView img_cancel=view.findViewById(R.id.img_cancel);
		RelativeLayout btn_share=view.findViewById(R.id.btn_share);

		img_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

				final String appPackageName = mContext.getPackageName();

				String share_text=sh.get_string(R.string.str_share_text)
						.replace("$1",""+(int)(drink_water)+" "+URLFactory.WATER_UNIT_VALUE);

				//share_text=share_text.replace("$2","@ https://play.google.com/store/apps/details?id=" + appPackageName);

				share_text=share_text.replace("$2","@ "+URLFactory.APP_SHARE_URL);

				ih.ShareText(getApplicationName(mContext),share_text);
			}
		});

		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
			}
		});

		dialog.setContentView(view);

		dialog.show();
	}

	public void showDailyMoreThanTargetDialog()
	{
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.dialog_goal_target_reached, null, false);


		AppCompatTextView lbl_desc=view.findViewById(R.id.lbl_desc);
		ImageView img_cancel=view.findViewById(R.id.img_cancel);
		RelativeLayout btn_share=view.findViewById(R.id.btn_share);
		ImageView img_bottle=view.findViewById(R.id.img_bottle);

		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
			img_bottle.setImageResource(R.drawable.ic_limit_ml);
		else
			img_bottle.setImageResource(R.drawable.ic_limit_oz);

		String desc=URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")?"8000 ml":"270 fl oz";

		lbl_desc.setText(sh.get_string(R.string.str_you_should_not_drink_more_then_target).replace("$1",desc));

		img_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);

		dialog.show();
	}





	//=======================================
	//======================================


	public void showReminderDialog()
	{
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.dialog_reminder, null, false);


		ImageView img_cancel=view.findViewById(R.id.img_cancel);
		RelativeLayout btn_save=view.findViewById(R.id.btn_save);

		final RelativeLayout off_block=view.findViewById(R.id.off_block);
		final RelativeLayout silent_block=view.findViewById(R.id.silent_block);
		final RelativeLayout auto_block=view.findViewById(R.id.auto_block);

		final ImageView img_off=view.findViewById(R.id.img_off);
		final ImageView img_silent=view.findViewById(R.id.img_silent);
		final ImageView img_auto=view.findViewById(R.id.img_auto);

		/*AppCompatTextView advance_settings=view.findViewById(R.id.advance_settings);

		advance_settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				startActivity(new Intent(act,Screen_Reminder.class));
			}
		});*/

		LinearLayout custom_sound_block=view.findViewById(R.id.custom_sound_block);

		custom_sound_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openSoundMenuPicker();
			}
		});


		SwitchCompat switch_vibrate=view.findViewById(R.id.switch_vibrate);

		switch_vibrate.setChecked(!ph.getBoolean(URLFactory.REMINDER_VIBRATE));

		switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ph.savePreferences(URLFactory.REMINDER_VIBRATE,!isChecked);
			}
		});

		if(ph.getInt(URLFactory.REMINDER_OPTION)==1)
		{
			off_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
			img_off.setImageResource(R.drawable.ic_off_selected);

			silent_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
			img_silent.setImageResource(R.drawable.ic_silent_normal);

			auto_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
			img_auto.setImageResource(R.drawable.ic_auto_normal);
		}
		else if(ph.getInt(URLFactory.REMINDER_OPTION)==2)
		{
			off_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
			img_off.setImageResource(R.drawable.ic_off_normal);

			silent_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
			img_silent.setImageResource(R.drawable.ic_silent_selected);

			auto_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
			img_auto.setImageResource(R.drawable.ic_auto_normal);
		}
		else
		{
			off_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
			img_off.setImageResource(R.drawable.ic_off_normal);

			silent_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
			img_silent.setImageResource(R.drawable.ic_silent_normal);

			auto_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
			img_auto.setImageResource(R.drawable.ic_auto_selected);
		}

		off_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				off_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
				img_off.setImageResource(R.drawable.ic_off_selected);

				silent_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
				img_silent.setImageResource(R.drawable.ic_silent_normal);

				auto_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
				img_auto.setImageResource(R.drawable.ic_auto_normal);

				ph.savePreferences(URLFactory.REMINDER_OPTION,1);
			}
		});

		silent_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				off_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
				img_off.setImageResource(R.drawable.ic_off_normal);

				silent_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
				img_silent.setImageResource(R.drawable.ic_silent_selected);

				auto_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
				img_auto.setImageResource(R.drawable.ic_auto_normal);

				ph.savePreferences(URLFactory.REMINDER_OPTION,2);
			}
		});

		auto_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				off_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
				img_off.setImageResource(R.drawable.ic_off_normal);

				silent_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_unselected));
				img_silent.setImageResource(R.drawable.ic_silent_normal);

				auto_block.setBackground(mContext.getResources().getDrawable(R.drawable.drawable_circle_selected));
				img_auto.setImageResource(R.drawable.ic_auto_selected);

				ph.savePreferences(URLFactory.REMINDER_OPTION,0);
			}
		});


		img_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);

		dialog.show();
	}

	public void openSoundMenuPicker()
	{
		loadSounds();

		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.dialog_sound_pick, null, false);


		RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
		RelativeLayout btn_save=view.findViewById(R.id.btn_save);


		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				for(int k=0;k<lst_sounds.size();k++)
				{
					if(lst_sounds.get(k).isSelected()) {
						ph.savePreferences(URLFactory.REMINDER_SOUND, k);
						break;
					}

				}

				//ph.savePreferences(URLFactory.REMINDER_SOUND,position);

				dialog.dismiss();
			}
		});


		RecyclerView soundRecyclerView=view.findViewById(R.id.soundRecyclerView);

		soundAdapter = new SoundAdapter(act, lst_sounds, new SoundAdapter.CallBack() {
			@Override
			public void onClickSelect(SoundModel time, int position)
			{
				for(int k=0;k<lst_sounds.size();k++)
				{
					lst_sounds.get(k).isSelected(false);
				}

				lst_sounds.get(position).isSelected(true);
				soundAdapter.notifyDataSetChanged();



				//if(position>0)
				playSound(position);
			}
		});

		soundRecyclerView.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));

		soundRecyclerView.setAdapter(soundAdapter);

		dialog.setContentView(view);

		dialog.show();
	}

	public void loadSounds()
	{
		lst_sounds.clear();

		lst_sounds.add(getSoundModel(0,"Default"));
		lst_sounds.add(getSoundModel(1,"Bell"));
		lst_sounds.add(getSoundModel(2,"Blop"));
		lst_sounds.add(getSoundModel(3,"Bong"));
		lst_sounds.add(getSoundModel(4,"Click"));
		lst_sounds.add(getSoundModel(5,"Echo droplet"));
		lst_sounds.add(getSoundModel(6,"Mario droplet"));
		lst_sounds.add(getSoundModel(7,"Ship bell"));
		lst_sounds.add(getSoundModel(8,"Simple droplet"));
		lst_sounds.add(getSoundModel(9,"Tiny droplet"));

	}

	public SoundModel getSoundModel(int index,String name)
	{
		SoundModel soundModel=new SoundModel();
		soundModel.setId(index);
		soundModel.setName(name);
		soundModel.isSelected(ph.getInt(URLFactory.REMINDER_SOUND)==index);

		return soundModel;
	}

	public void playSound(int idx)
	{
		MediaPlayer mp = null;

		//Ringtone r = RingtoneManager.getRingtone(mContext, getSound());
		//r.play();

		if(idx==0)
			mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
		else if(idx==1)
			mp = MediaPlayer.create(this, R.raw.bell);
		else if(idx==2)
			mp = MediaPlayer.create(this, R.raw.blop);
		else if(idx==3)
			mp = MediaPlayer.create(this, R.raw.bong);
		else if(idx==4)
			mp = MediaPlayer.create(this, R.raw.click);
		else if(idx==5)
			mp = MediaPlayer.create(this, R.raw.echo_droplet);
		else if(idx==6)
			mp = MediaPlayer.create(this, R.raw.mario_droplet);
		else if(idx==7)
			mp = MediaPlayer.create(this, R.raw.ship_bell);
		else if(idx==8)
			mp = MediaPlayer.create(this, R.raw.simple_droplet);
		else if(idx==9)
			mp = MediaPlayer.create(this, R.raw.tiny_droplet);

		mp.start();
	}

	public static String getApplicationName(Context context) {
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		int stringId = applicationInfo.labelRes;
		return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();

		try {
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			else
				finish();
		}
		catch(Exception e){}
	}
}
