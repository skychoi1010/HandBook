package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;
import com.waterdiary.drinkreminder.utils.URLFactory;


public class Screen_OnBoarding_Eight extends MasterBaseFragment
{
	View item_view;

	RelativeLayout sunny_block,cloudy_block,rainy_block,snow_block;
	ImageView img_sunny,img_cloudy,img_rainy,img_snow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.screen_onboarding_eight, container, false);

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
		}
		else{
			//no
		}

	}

	private void FindViewById()
	{
		sunny_block=item_view.findViewById(R.id.sunny_block);
		cloudy_block=item_view.findViewById(R.id.cloudy_block);
		rainy_block=item_view.findViewById(R.id.rainy_block);
		snow_block=item_view.findViewById(R.id.snow_block);

		img_sunny=item_view.findViewById(R.id.img_sunny);
		img_cloudy=item_view.findViewById(R.id.img_cloudy);
		img_rainy=item_view.findViewById(R.id.img_rainy);
		img_snow=item_view.findViewById(R.id.img_snow);
	}

	private void Body()
	{
		setWeather(ph.getInt(URLFactory.WEATHER_CONSITIONS));

		sunny_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(0);
			}
		});

		cloudy_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(1);
			}
		});

		rainy_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(2);
			}
		});

		snow_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setWeather(3);
			}
		});
	}

	public void setWeather(int idx)
	{
		ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);

		ph.savePreferences(URLFactory.WEATHER_CONSITIONS,idx);

		sunny_block.setBackground(idx==0?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_sunny.setImageResource(idx==0?R.drawable.sunny_selected:R.drawable.sunny);

		cloudy_block.setBackground(idx==1?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_cloudy.setImageResource(idx==1?R.drawable.cloudy_selected:R.drawable.cloudy);

		rainy_block.setBackground(idx==2?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_rainy.setImageResource(idx==2?R.drawable.rainy_selected:R.drawable.rainy);

		snow_block.setBackground(idx==3?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
				:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		img_snow.setImageResource(idx==3?R.drawable.snow_selected:R.drawable.snow);

		/*if(idx==1)
		{
			sunny_block.setBackground(idx==0?mContext.getResources().getDrawable(R.drawable.rdo_gender_select)
					:mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_sunny.setImageResource(idx==0?R.drawable.ic_female_selected:R.drawable.ic_female_normal);

			cloudy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_cloudy.setImageResource(R.drawable.ic_female_selected);

			rainy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_rainy.setImageResource(R.drawable.ic_female_normal);

			snow_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_snow.setImageResource(R.drawable.ic_female_normal);
		}
		else if(idx==2)
		{
			sunny_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_sunny.setImageResource(R.drawable.ic_female_selected);

			cloudy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_cloudy.setImageResource(R.drawable.ic_female_normal);

			rainy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_rainy.setImageResource(R.drawable.ic_female_normal);

			snow_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_snow.setImageResource(R.drawable.ic_female_normal);
		}
		else if(idx==3)
		{
			sunny_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_sunny.setImageResource(R.drawable.ic_female_selected);

			cloudy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_cloudy.setImageResource(R.drawable.ic_female_normal);

			rainy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_rainy.setImageResource(R.drawable.ic_female_normal);

			snow_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
		}
		else
		{
			sunny_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_sunny.setImageResource(R.drawable.ic_female_selected);

			cloudy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_cloudy.setImageResource(R.drawable.ic_female_normal);

			rainy_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_rainy.setImageResource(R.drawable.ic_female_normal);

			snow_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_snow.setImageResource(R.drawable.ic_female_normal);
		}*/
	}
}