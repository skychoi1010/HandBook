package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;
import com.waterdiary.drinkreminder.utils.URLFactory;


public class Screen_OnBoarding_Seven extends MasterBaseFragment
{
	View item_view;

	LinearLayout block_for_female;

	RelativeLayout active_block,pregnant_block,breastfeeding_block;
	ImageView img_active,img_pregnant,img_breastfeeding;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.screen_onboarding_seven, container, false);

		FindViewById();
		Body();

		return item_view;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
			//ah.customAlert("call");
			//actionView();

			if(ph.getBoolean(URLFactory.IS_PREGNANT))
			{
				pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
				img_pregnant.setImageResource(R.drawable.pregnant_selected);
			}
			else
			{
				pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
				img_pregnant.setImageResource(R.drawable.pregnant);
			}

			if(ph.getBoolean(URLFactory.IS_BREATFEEDING))
			{
				breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
				img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected);
			}
			else
			{
				breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
				img_breastfeeding.setImageResource(R.drawable.breastfeeding);
			}



			if(ph.getBoolean(URLFactory.USER_GENDER)) // female
			{
				//block_for_female.setVisibility(View.VISIBLE);

				pregnant_block.setFocusableInTouchMode(true);
				pregnant_block.setClickable(true);
				pregnant_block.setFocusable(true);
				pregnant_block.setAlpha(1);

				for (int i = 0; i < pregnant_block.getChildCount(); i++) {
					View child = pregnant_block.getChildAt(i);
					child.setEnabled(true);
				}

				breastfeeding_block.setFocusableInTouchMode(true);
				breastfeeding_block.setClickable(true);
				breastfeeding_block.setFocusable(true);
				breastfeeding_block.setAlpha(1);

				for (int i = 0; i < breastfeeding_block.getChildCount(); i++) {
					View child = breastfeeding_block.getChildAt(i);
					child.setEnabled(true);
				}
			}
			else
			{
				//block_for_female.setVisibility(View.GONE);

				pregnant_block.setFocusableInTouchMode(false);
				pregnant_block.setClickable(false);
				pregnant_block.setFocusable(false);
				pregnant_block.setAlpha(0.50f);

				for (int i = 0; i < pregnant_block.getChildCount(); i++) {
					View child = pregnant_block.getChildAt(i);
					child.setEnabled(false);
				}

				breastfeeding_block.setFocusableInTouchMode(false);
				breastfeeding_block.setClickable(false);
				breastfeeding_block.setFocusable(false);
				breastfeeding_block.setAlpha(0.50f);

				for (int i = 0; i < breastfeeding_block.getChildCount(); i++) {
					View child = breastfeeding_block.getChildAt(i);
					child.setEnabled(false);
				}
			}
		}
		else{
			//no
		}

	}

	private void FindViewById()
	{
		block_for_female=item_view.findViewById(R.id.block_for_female);

		active_block=item_view.findViewById(R.id.active_block);
		pregnant_block=item_view.findViewById(R.id.pregnant_block);
		breastfeeding_block=item_view.findViewById(R.id.breastfeeding_block);

		img_active=item_view.findViewById(R.id.img_active);
		img_pregnant=item_view.findViewById(R.id.img_pregnant);
		img_breastfeeding=item_view.findViewById(R.id.img_breastfeeding);

	}

	private void Body()
	{
		setActive();
		setBreastfeeding();
		setPregnant();

		active_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(ph.getBoolean(URLFactory.IS_ACTIVE))
					ph.savePreferences(URLFactory.IS_ACTIVE,false);
				else
					ph.savePreferences(URLFactory.IS_ACTIVE,true);

				setActive();
			}
		});

		pregnant_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(ph.getBoolean(URLFactory.IS_PREGNANT))
					ph.savePreferences(URLFactory.IS_PREGNANT,false);
				else
					ph.savePreferences(URLFactory.IS_PREGNANT,true);

				setPregnant();
			}
		});

		breastfeeding_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(ph.getBoolean(URLFactory.IS_BREATFEEDING))
					ph.savePreferences(URLFactory.IS_BREATFEEDING,false);
				else
					ph.savePreferences(URLFactory.IS_BREATFEEDING,true);

				setBreastfeeding();
			}
		});
	}

	public void setActive()
	{
		ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);

		if(ph.getBoolean(URLFactory.IS_ACTIVE))
		{
			active_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_active.setImageResource(R.drawable.active_selected);
		}
		else
		{
			active_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_active.setImageResource(R.drawable.active);
		}
	}

	public void setPregnant()
	{
		ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);

		if(ph.getBoolean(URLFactory.IS_PREGNANT))
		{
			pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_pregnant.setImageResource(R.drawable.pregnant_selected);
		}
		else
		{
			pregnant_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_pregnant.setImageResource(R.drawable.pregnant);
		}
	}

	public void setBreastfeeding()
	{
		ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);

		if(ph.getBoolean(URLFactory.IS_BREATFEEDING))
		{
			breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_breastfeeding.setImageResource(R.drawable.breastfeeding_selected);
		}
		else
		{
			breastfeeding_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_breastfeeding.setImageResource(R.drawable.breastfeeding);
		}
	}
}