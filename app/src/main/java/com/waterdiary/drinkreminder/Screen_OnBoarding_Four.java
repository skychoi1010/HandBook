package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;


public class Screen_OnBoarding_Four extends MasterBaseFragment
{
	View item_view;

	AppCompatTextView lbl_goal,lbl_unit,lbl_set_goal_manually;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.screen_onboarding_four, container, false);

		FindViewById();
		//Body();

		return item_view;
	}

	private void FindViewById()
	{
		lbl_set_goal_manually=item_view.findViewById(R.id.lbl_set_goal_manually);
		lbl_goal=item_view.findViewById(R.id.lbl_goal);
		lbl_unit=item_view.findViewById(R.id.lbl_unit);
	}

	/*

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
/*
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
	}*/
}