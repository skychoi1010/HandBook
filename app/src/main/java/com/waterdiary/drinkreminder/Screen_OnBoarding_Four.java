package com.waterdiary.drinkreminder;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;
import com.waterdiary.drinkreminder.custom.InputFilterWeightRange;
import com.waterdiary.drinkreminder.utils.HeightWeightHelper;
import com.waterdiary.drinkreminder.utils.URLFactory;


public class Screen_OnBoarding_Four extends MasterBaseFragment
{
	View item_view;

	AppCompatTextView lbl_goal,lbl_unit,lbl_set_goal_manually;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.screen_onboarding_four, container, false);

		FindViewById();
		Body();

		return item_view;
	}

	public String getData(String str)
	{
		return  str.replace(",",".");
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			if(ph.getBoolean(URLFactory.SET_MANUALLY_GOAL))
			{
				//calculate_goal();
				URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE);
				ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);

				//ah.customAlert("call manually");

				//lbl_goal.setText(getData("" + URLFactory.DAILY_WATER_VALUE));
				lbl_goal.setText(getData("" + (int)URLFactory.DAILY_WATER_VALUE));

				if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
				{
					lbl_unit.setText("ml");
				}
				else
				{
					lbl_unit.setText("fl oz");
				}
			}
			else
			{
				calculate_goal();
			}
		}
		else{
			//no
		}

	}

	private void FindViewById()
	{
		lbl_set_goal_manually=item_view.findViewById(R.id.lbl_set_goal_manually);
		lbl_goal=item_view.findViewById(R.id.lbl_goal);
		lbl_unit=item_view.findViewById(R.id.lbl_unit);
	}

	private void Body()
	{
		if(ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)) {
//			calculate_goal();
			URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE);
			ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);

			//ah.customAlert("call manually");

			lbl_goal.setText(getData("" + URLFactory.DAILY_WATER_VALUE));

			if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
			{
				lbl_unit.setText("ml");
			}
			else
			{
				lbl_unit.setText("fl oz");
			}
		}
		else
		{
			calculate_goal();
		}

		lbl_set_goal_manually.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showSetManuallyGoalDialog();
			}
		});
	}

	public void calculate_goal()
	{
		String tmp_weight=""+ph.getString(URLFactory.PERSON_WEIGHT);

		boolean isFemale=ph.getBoolean(URLFactory.USER_GENDER);
		boolean isActive=ph.getBoolean(URLFactory.IS_ACTIVE);
		boolean isPregnant=ph.getBoolean(URLFactory.IS_PREGNANT);
		boolean isBreastfeeding=ph.getBoolean(URLFactory.IS_BREATFEEDING);
		int weatherIdx=ph.getInt(URLFactory.WEATHER_CONSITIONS);

		if(!sh.check_blank_data(tmp_weight))
		{
			double tot_drink=0;
			double tmp_kg = 0;
			if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
			{
				tmp_kg = Double.parseDouble("" + tmp_weight);

			}
			else
			{
				tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
			}

			Log.d("tot_drink 1 ",""+tot_drink);

			if(isFemale)
				tot_drink=isActive?tmp_kg*URLFactory.ACTIVE_FEMALE_WATER:tmp_kg*URLFactory.FEMALE_WATER;
			else
				tot_drink=isActive?tmp_kg*URLFactory.ACTIVE_MALE_WATER:tmp_kg*URLFactory.MALE_WATER;

			Log.d("tot_drink 2 ",""+tot_drink);

			if(weatherIdx==1)
				tot_drink*=URLFactory.WEATHER_CLOUDY;
			else if(weatherIdx==2)
				tot_drink*=URLFactory.WEATHER_RAINY;
			else if(weatherIdx==3)
				tot_drink*=URLFactory.WEATHER_SNOW;
			else
				tot_drink*=URLFactory.WEATHER_SUNNY;

			Log.d("tot_drink 3 ",""+tot_drink);

			if(isPregnant && isFemale)
			{
				tot_drink+=URLFactory.PREGNANT_WATER;
			}

			Log.d("tot_drink 4 ",""+tot_drink);

			if(isBreastfeeding && isFemale)
			{
				tot_drink+=URLFactory.BREASTFEEDING_WATER;
			}

			Log.d("tot_drink 5 ",""+tot_drink);

			if(tot_drink<900)
				tot_drink=900;

			if(tot_drink>8000)
				tot_drink=8000;

			Log.d("tot_drink 6 ",""+tot_drink);

			double tot_drink_fl_oz = HeightWeightHelper.mlToOzConverter(tot_drink);

			if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
			{
				//lbl_goal.setText(getData("" + tmp_ml));
				lbl_unit.setText("ml");
				URLFactory.DAILY_WATER_VALUE = (float) tot_drink;
			}
			else
			{
				//lbl_goal.setText(getData("" + URLFactory.decimalFormat.format(tmp_oz)));
				lbl_unit.setText("fl oz");
				URLFactory.DAILY_WATER_VALUE = (float) tot_drink_fl_oz;

			}

			URLFactory.DAILY_WATER_VALUE=Math.round(URLFactory.DAILY_WATER_VALUE);
			lbl_goal.setText(getData("" + (int)URLFactory.DAILY_WATER_VALUE));

			ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
		}
	}

	public void calculate_goal_old()
	{
		String tmp_weight=""+ph.getString(URLFactory.PERSON_WEIGHT);
		if(!sh.check_blank_data(tmp_weight))
		{
			double tmp_lbs = 0;
			if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
			{
				tmp_lbs = HeightWeightHelper.kgToLbConverter(Double.parseDouble(tmp_weight));
			}
			else
			{
				tmp_lbs = Double.parseDouble("" + tmp_weight);
			}

			double tmp_oz = tmp_lbs * 0.5;

			double tmp_ml = HeightWeightHelper.ozToMlConverter(tmp_oz);

			//double rounded_up = 100 * Math.ceil(tmp_ml / 100);

			if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
			{
				//lbl_goal.setText(getData("" + tmp_ml));
				lbl_unit.setText("ml");
				URLFactory.DAILY_WATER_VALUE = (float) tmp_ml;
			}
			else
			{
				//lbl_goal.setText(getData("" + URLFactory.decimalFormat.format(tmp_oz)));
				lbl_unit.setText("fl oz");
				URLFactory.DAILY_WATER_VALUE = (float) tmp_oz;

			}

			URLFactory.DAILY_WATER_VALUE=Math.round(URLFactory.DAILY_WATER_VALUE);
			lbl_goal.setText(getData("" + (int)URLFactory.DAILY_WATER_VALUE));

			ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
		}
	}




    boolean isExecute=true,isExecuteSeekbar=true;

	public void showSetManuallyGoalDialog()
	{
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		final View view = LayoutInflater.from(act).inflate(R.layout.dialog_set_manually_goal, null, false);


		final AppCompatEditText lbl_goal2=view.findViewById(R.id.lbl_goal);
		AppCompatTextView lbl_unit2=view.findViewById(R.id.lbl_unit);
		RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
		RelativeLayout btn_save=view.findViewById(R.id.btn_save);
		final SeekBar seekbarGoal=view.findViewById(R.id.seekbarGoal);


		/*if(ph.getBoolean(URLFactory.SET_MANUALLY_GOAL))
			lbl_goal2.setText( getData(URLFactory.decimalFormat.format(ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE))));
		else
			lbl_goal2.setText( getData(URLFactory.decimalFormat.format(ph.getFloat(URLFactory.DAILY_WATER))));*/

		if(ph.getBoolean(URLFactory.SET_MANUALLY_GOAL))
			lbl_goal2.setText( getData(""+(int)(ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE))));
		else
			lbl_goal2.setText( getData(""+(int)(ph.getFloat(URLFactory.DAILY_WATER))));

		lbl_unit2.setText(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");



		if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				seekbarGoal.setMin(900);
			}
			seekbarGoal.setMax(8000);
			lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(0,8000),new InputFilter.LengthFilter(4)});
			//lbl_goal2.setMaxL
		}
		else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				seekbarGoal.setMin(30);
			}
			seekbarGoal.setMax(270);
			lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(0,270),new InputFilter.LengthFilter(3)});
		}

		int f= ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)?(int) ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE):(int) ph.getFloat(URLFactory.DAILY_WATER);
		seekbarGoal.setProgress(f);

        lbl_goal2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isExecuteSeekbar=false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try
                {
                    if(!sh.check_blank_data(lbl_goal2.getText().toString().trim()) && isExecute)
                    {
                        int data = Integer.parseInt(lbl_goal2.getText().toString().trim());
                        seekbarGoal.setProgress(data);
                    }

                }
                catch (Exception e){}

                isExecuteSeekbar=true;
            }
        });

		seekbarGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBars, int progress, boolean fromUser) {
                if(isExecuteSeekbar)
                {
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
					{
						if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
						{
							progress=progress<900?900:progress;
						}
						else
						{
							progress=progress<30?30:progress;
						}
						seekbarGoal.setProgress(progress);
					}

                    lbl_goal2.setText("" + progress);
                }


			}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isExecute=false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isExecute=true;
            }
		});




		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{

				if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && Float.parseFloat(lbl_goal2.getText().toString().trim())>=900)
				{
					URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
					ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
					//lbl_goal.setText(getData(URLFactory.decimalFormat.format(URLFactory.DAILY_WATER_VALUE)));
					lbl_goal.setText(getData("" + (int) URLFactory.DAILY_WATER_VALUE));
					ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
					ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
					dialog.dismiss();
				}
				else
				{
					if(!ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && Float.parseFloat(lbl_goal2.getText().toString().trim())>=30)
					{
						URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
						ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
						//lbl_goal.setText(getData(URLFactory.decimalFormat.format(URLFactory.DAILY_WATER_VALUE)));
						lbl_goal.setText(getData("" + (int) URLFactory.DAILY_WATER_VALUE));
						ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
						ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
						dialog.dismiss();
					}
					else {
						ah.customAlert(sh.get_string(R.string.str_set_daily_goal_validation));
					}
				}
			}
		});

		dialog.setContentView(view);

		dialog.show();
	}
}