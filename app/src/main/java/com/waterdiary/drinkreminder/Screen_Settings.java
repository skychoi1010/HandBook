package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.utils.URLFactory;

public class Screen_Settings extends MasterBaseActivity
{
	LinearLayout right_icon_block,left_icon_block;
	AppCompatTextView lbl_toolbar_title;

	LinearLayout backup_restore_block,weight_block;

	SwitchCompat switch_notification;
	SwitchCompat switch_sound;

	AppCompatTextView lbl_restore_and_backup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_settings);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.str_green_card));
		}

		FindViewById();
		Body();
	}

	private void FindViewById()
	{
		right_icon_block=findViewById(R.id.right_icon_block);
		left_icon_block=findViewById(R.id.left_icon_block);
		lbl_toolbar_title=findViewById(R.id.lbl_toolbar_title);

		backup_restore_block=findViewById(R.id.backup_restore_block);
		//weight_block=findViewById(R.id.weight_block);

		switch_notification=findViewById(R.id.switch_notification);
		switch_sound=findViewById(R.id.switch_sound);

		lbl_restore_and_backup=findViewById(R.id.lbl_restore_and_backup);
		lbl_restore_and_backup.setText(sh.get_string(R.string.str_backup_and_restore));
	}

	private void Body()
	{
		lbl_toolbar_title.setText(sh.get_string(R.string.str_settings));
		left_icon_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		right_icon_block.setVisibility(View.GONE);

		backup_restore_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				intent = new Intent(act, Screen_Backup_Restore.class);
				startActivity(intent);
			}
		});

		/*weight_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				intent=new Intent(act, Screen_Profile.class);
				startActivity(intent);
			}
		});*/

		switch_notification.setChecked(ph.getBoolean(URLFactory.DISABLE_NOTIFICATION));

		switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ph.savePreferences(URLFactory.DISABLE_NOTIFICATION,isChecked);
			}
		});


		switch_sound.setChecked(ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER));

		switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ph.savePreferences(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER,isChecked);
			}
		});
	}
}