package com.waterdiary.drinkreminder;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.waterdiary.drinkreminder.base.MasterBaseFragment;
import com.waterdiary.drinkreminder.utils.URLFactory;


public class Screen_OnBoarding_Two extends MasterBaseFragment
{
	View item_view;

	//RadioButton rdo_male,rdo_female;

	/*RelativeLayout male_block,female_block;
	ImageView img_male,img_female;
	AppCompatTextView lbl_male,lbl_female;*/

	//boolean isMaleGender=true;

	AppCompatEditText txt_user_name;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		item_view=inflater.inflate(R.layout.handbook_login_create, container, false);

		//FindViewById();
		//Body();

		return item_view;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser)
		{
		}
		else{
			//no
		}

	}

	private void FindViewById()
	{
		//rdo_male=item_view.findViewById(R.id.rdo_male);
		//rdo_female=item_view.findViewById(R.id.rdo_female);

		/*male_block=item_view.findViewById(R.id.male_block);
		img_male=item_view.findViewById(R.id.img_male);
		lbl_male=item_view.findViewById(R.id.lbl_male);

		female_block=item_view.findViewById(R.id.female_block);
		img_female=item_view.findViewById(R.id.img_female);
		lbl_female=item_view.findViewById(R.id.lbl_female);*/

		txt_user_name=item_view.findViewById(R.id.txt_user_name);
	}

	private void Body()
	{
		/*male_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setGender(true);
			}
		});

		female_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setGender(false);
			}
		});*/

		txt_user_name.setText(ph.getString(URLFactory.USER_NAME));
		//setGender(!ph.getBoolean(URLFactory.USER_GENDER));

		txt_user_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				ph.savePreferences(URLFactory.USER_NAME,txt_user_name.getText().toString().trim());
			}
		});

		/*rdo_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b)
					rdo_male.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_select_text_color));
				else
					rdo_male.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_regular_text_color));
			}
		});

		rdo_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b)
					rdo_female.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_select_text_color));
				else
					rdo_female.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_regular_text_color));
			}
		});*/
	}

	/*public void setGender(boolean isMale)
	{
		ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);

		if(isMale)
		{
			isMaleGender=true;

			ph.savePreferences(URLFactory.USER_GENDER,false);

			ph.savePreferences(URLFactory.IS_PREGNANT,false);
			ph.savePreferences(URLFactory.IS_BREATFEEDING,false);

			male_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_male.setImageResource(R.drawable.ic_male_selected);
			//lbl_male.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_select_text_color));

			female_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_female.setImageResource(R.drawable.ic_female_normal);
			//lbl_female.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_regular_text_color));
		}
		else
		{
			isMaleGender=false;

			ph.savePreferences(URLFactory.USER_GENDER,true);



			male_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_regular));
			img_male.setImageResource(R.drawable.ic_male_normal);
			//lbl_male.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_regular_text_color));

			female_block.setBackground(mContext.getResources().getDrawable(R.drawable.rdo_gender_select));
			img_female.setImageResource(R.drawable.ic_female_selected);
			//lbl_female.setTextColor(mContext.getResources().getColor(R.color.rdo_gender_select_text_color));
		}
	}*/

}