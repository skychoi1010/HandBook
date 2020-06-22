package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;
import com.waterdiary.drinkreminder.base.MasterBaseFragment;
import com.waterdiary.drinkreminder.utils.URLFactory;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Screen_OnBoarding_Six extends MasterBaseFragment
{
	View item_view;

	AppCompatTextView txt_wakeup_time,txt_bed_time;
	RadioButton rdo_15,rdo_30,rdo_45,rdo_60;

	int from_hour=8,from_minute=0,to_hour=22,to_minute=0;

	AppCompatTextView lbl_message;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.screen_onboarding_six, container, false);

		FindViewById();
		Body();

		setCount();

		return item_view;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			//ah.customAlert("call");
			//actionView();
			setCount();
		}
		else{
			//no
		}

		//ah.Show_Alert_Dialog(""+dth.getCurrentTime(true));

	}

	/*public void openTimePicker(final AppCompatTextView appCompatTextView)
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance(Locale.US);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(act,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        appCompatTextView.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }*/


	private void FindViewById()
	{
		txt_wakeup_time=item_view.findViewById(R.id.txt_wakeup_time);
		txt_bed_time=item_view.findViewById(R.id.txt_bed_time);

		rdo_15=item_view.findViewById(R.id.rdo_15);
		rdo_30=item_view.findViewById(R.id.rdo_20);
		rdo_45=item_view.findViewById(R.id.rdo_30);
		rdo_60=item_view.findViewById(R.id.rdo_35);

		lbl_message=item_view.findViewById(R.id.lbl_message);
	}

	private void Body()
	{
		/*rdo_15.setText("15 "+sh.get_string(R.string.str_min));
		rdo_30.setText("30 "+sh.get_string(R.string.str_min));
		rdo_45.setText("45 "+sh.get_string(R.string.str_min));
		rdo_60.setText("1 "+sh.get_string(R.string.str_hour));

		txt_wakeup_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAutoTimePicker(txt_wakeup_time,true);
				//openTimePicker(txt_wakeup_time);
			}
		});

		txt_bed_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAutoTimePicker(txt_bed_time,false);
				//openTimePicker(txt_bed_time);
			}
		});

		rdo_15.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});

		rdo_30.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});

		rdo_45.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});

		rdo_60.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCount();
			}
		});*/
	}

	public void openAutoTimePicker(final AppCompatTextView appCompatTextView, final boolean isFrom)
	{
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
			{
				String formatedDate = "";
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
				SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a", Locale.US);
				Date dt;
				String time = "";

				//ah.Show_Alert_Dialog(""+hourOfDay+":"+minute+"  @@@  "+isFrom);

				try
				{
					if(isFrom)
					{
						from_hour=hourOfDay;
						from_minute=minute;
					}
					else
					{
						to_hour=hourOfDay;
						to_minute=minute;
					}

					//if(isValidDate())
					//{
					//Log.d("openAutoTimePicker : ", "" + from_hour + "  @@@@  " + from_minute);
					//Log.d("openAutoTimePicker : ", "" + to_hour + "  @@@@  " + to_minute);

					time = "" + hourOfDay + ":" + minute + ":" + "00";
					dt = sdf.parse(time);
					formatedDate = sdfs.format(dt);
					appCompatTextView.setText("" + formatedDate);
					/*}
					else
					{
						if(isFrom)
						{
							from_hour=8;
							from_minute=0;
							appCompatTextView.setText("08:00 AM");
						}
						else
						{
							to_hour=22;
							to_minute=0;
							appCompatTextView.setText("10:00 PM");
						}
						ah.customAlert(sh.get_string(R.string.str_from_to_invalid_validation));
					}*/

					setCount();
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
			}
		};

		Calendar now = Calendar.getInstance();

		if(isFrom) {
			now.set(Calendar.HOUR_OF_DAY, from_hour);
			now.set(Calendar.MINUTE, from_minute);
		}
		else {
			now.set(Calendar.HOUR_OF_DAY, to_hour);
			now.set(Calendar.MINUTE, to_minute);
		}
		TimePickerDialog tpd;
		if (!DateFormat.is24HourFormat(act)) {
			//12 hrs format
			tpd = TimePickerDialog.newInstance(
					onTimeSetListener,
					now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), false);

			tpd.setSelectableTimes(generateTimepoints(23.50, 30));

			tpd.setMaxTime(23, 30, 00);
		} else {
			//24 hrs format
			tpd = TimePickerDialog.newInstance(
					onTimeSetListener,
					now.get(Calendar.HOUR_OF_DAY),
					now.get(Calendar.MINUTE), true);

			tpd.setSelectableTimes(generateTimepoints(23.50, 30));

			tpd.setMaxTime(23, 30, 00);
		}


		tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
		tpd.show(act.getFragmentManager(), "Datepickerdialog");
		tpd.setAccentColor(MasterBaseAppCompatActivity.getThemeColor(mContext));
	}

	public static Timepoint[] generateTimepoints(double maxHour, int minutesInterval) {

		int lastValue = (int) (maxHour * 60);

		List<Timepoint> timepoints = new ArrayList<>();

		for (int minute = 0; minute <= lastValue; minute += minutesInterval) {
			int currentHour = minute / 60;
			int currentMinute = minute - (currentHour > 0 ? (currentHour * 60) : 0);
			if (currentHour == 24)
				continue;
			timepoints.add(new Timepoint(currentHour, currentMinute));
		}
		return timepoints.toArray(new Timepoint[timepoints.size()]);
	}

	public int getDifference()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = simpleDateFormat.parse(txt_wakeup_time.getText().toString().trim());
			date2 = simpleDateFormat.parse(txt_bed_time.getText().toString().trim());

			long difference = date2.getTime() - date1.getTime();
			int days = (int) (difference / (1000*60*60*24));
			int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
			int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

			return min;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return  0;

	}



	public boolean isValidDate()
	{
		int interval=rdo_15.isChecked()?15:rdo_30.isChecked()?30:rdo_45.isChecked()?45:60;

		if(interval<=getDifference())
			return true;

		/*Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, from_hour);
		startTime.set(Calendar.MINUTE, from_minute);
		startTime.set(Calendar.SECOND,0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, to_hour);
		endTime.set(Calendar.MINUTE, to_minute);
		endTime.set(Calendar.SECOND,0);
		if(startTime.getTimeInMillis()+100<endTime.getTimeInMillis())
		{
			return true;
		}*/

		return false;
	}

	public boolean isNextDayEnd()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = simpleDateFormat.parse(txt_wakeup_time.getText().toString().trim());
			date2 = simpleDateFormat.parse(txt_bed_time.getText().toString().trim());

			if(date1.getTime()>date2.getTime())
				return true;
			else
				return false;
		}
		catch (Exception e){}

		return false;
	}

	public void setCount()
	{
		/*Date date1 = parseDate(txt_bed_time.getText().toString());
		Date date2 = parseDate(txt_wakeup_time.getText().toString());
		long mills = date1.getTime() - date2.getTime();
		int hours = (int) (mills/(1000 * 60 * 60));
		int mins = (int) ((mills/(1000*60)) % 60);*/

		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY, from_hour);
		startTime.set(Calendar.MINUTE, from_minute);
		startTime.set(Calendar.SECOND,0);

		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, to_hour);
		endTime.set(Calendar.MINUTE, to_minute);
		endTime.set(Calendar.SECOND,0);

		// @@@@@
		if(isNextDayEnd())
			endTime.add(Calendar.DATE,1);

		long mills = endTime.getTimeInMillis() - startTime.getTimeInMillis();

		int hours = (int) (mills/(1000 * 60 * 60));
		int mins = (int) ((mills/(1000*60)) % 60);
		float total_minutes=(hours*60)+mins;

		int interval=rdo_15.isChecked()?15:rdo_30.isChecked()?30:rdo_45.isChecked()?45:60;

		int consume=0; // @@@@@
		if(total_minutes>0)
			consume= Math.round(URLFactory.DAILY_WATER_VALUE/(total_minutes/interval));

		String unit=ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz";

		lbl_message.setText(sh.get_string(R.string.str_goal_consume).replace("$1",""+consume+" "+unit).replace("$2",""+(int)URLFactory.DAILY_WATER_VALUE+" "+unit));

		ph.savePreferences(URLFactory.WAKE_UP_TIME,txt_wakeup_time.getText().toString().trim());
		ph.savePreferences(URLFactory.WAKE_UP_TIME_HOUR,from_hour);
		ph.savePreferences(URLFactory.WAKE_UP_TIME_MINUTE,from_minute);

		ph.savePreferences(URLFactory.BED_TIME,txt_bed_time.getText().toString().trim());
		ph.savePreferences(URLFactory.BED_TIME_HOUR,to_hour);
		ph.savePreferences(URLFactory.BED_TIME_MINUTE,to_minute);

		ph.savePreferences(URLFactory.INTERVAL,interval);

		if(consume>(int)URLFactory.DAILY_WATER_VALUE)
			ph.savePreferences(URLFactory.IGNORE_NEXT_STEP,true);
		else if(consume==0)
			ph.savePreferences(URLFactory.IGNORE_NEXT_STEP,true);
		else
			ph.savePreferences(URLFactory.IGNORE_NEXT_STEP,false);

		//dth.getCurrentDate();
	}

	private Date parseDate(String date) {
        String inputFormat = "hh:mm aa";

        //SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat);
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        //SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.getDefault());
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
}