package com.waterdiary.drinkreminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.model.AlarmModel;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>
{
	Context mContext;
	private final ArrayList<AlarmModel> alarmList;
	CallBack callBack;

	public AlarmAdapter(Context c, ArrayList<AlarmModel> alarmList, CallBack callBack)
	{
		mContext = c;
		this.alarmList=alarmList;
		this.callBack=callBack;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_item_alarm, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		holder.lbl_time.setText(alarmList.get(position).getDrinkTime());

		if(alarmList.get(position).getAlarmType().equalsIgnoreCase("R"))
		{
			holder.lbl_time.setText(holder.lbl_time.getText()+"\n"+mContext.getResources().getString(R.string.str_every)+" "+alarmList.get(position).getAlarmInterval()+" "+mContext.getResources().getString(R.string.str_minutes).toLowerCase());
		}

		holder.item_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callBack.onClickSelect(alarmList.get(position),position);
			}
		});

		holder.img_remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showMenu(view,alarmList.get(position),position);
			}
		});

		holder.switch_reminder.setChecked(alarmList.get(position).getIsOff()!=1);

		holder.switch_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				callBack.onClickSwitch(alarmList.get(position),position,isChecked);
			}
		});


		holder.chk_sunday.setChecked(alarmList.get(position).getSunday()==1);
		holder.chk_monday.setChecked(alarmList.get(position).getMonday()==1);
		holder.chk_tuesday.setChecked(alarmList.get(position).getTuesday()==1);
		holder.chk_wednesday.setChecked(alarmList.get(position).getWednesday()==1);
		holder.chk_thursday.setChecked(alarmList.get(position).getThursday()==1);
		holder.chk_friday.setChecked(alarmList.get(position).getFriday()==1);
		holder.chk_saturday.setChecked(alarmList.get(position).getSaturday()==1);

		holder.chk_sunday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,0,holder.chk_sunday.isChecked());
			}
		});

		holder.chk_monday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,1,holder.chk_monday.isChecked());
			}
		});

		holder.chk_tuesday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,2,holder.chk_tuesday.isChecked());
			}
		});

		holder.chk_wednesday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,3,holder.chk_wednesday.isChecked());
			}
		});

		holder.chk_thursday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,4,holder.chk_thursday.isChecked());
			}
		});

		holder.chk_friday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,5,holder.chk_friday.isChecked());
			}
		});

		holder.chk_saturday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickWeek(alarmList.get(position),position,6,holder.chk_saturday.isChecked());
			}
		});
	}



	public void showMenu(View v,final AlarmModel alarmModel,final int position) {
		PopupMenu popup = new PopupMenu(mContext, v);
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem)
			{
				switch (menuItem.getItemId()) {
					case R.id.edit_item:
						callBack.onClickEdit(alarmModel,position);
						return true;
					case R.id.delete_item:
						callBack.onClickRemove(alarmModel,position);
						return true;
					default:
						return false;
				}
			}
		});
		popup.inflate(R.menu.manual_reminder_menu);
		popup.show();
	}


	@Override
	public int getItemCount() {
		return alarmList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		ImageView img_remove;
		LinearLayout item_block;
		TextView lbl_time;
		SwitchCompat switch_reminder;

		CheckBox chk_sunday,chk_monday,chk_tuesday,chk_wednesday,chk_thursday,chk_friday,chk_saturday;

		public ViewHolder(View itemView) {
			super(itemView);
			item_block=itemView.findViewById(R.id.item_block);
			img_remove=itemView.findViewById(R.id.img_remove);
			lbl_time=itemView.findViewById(R.id.lbl_time);
			switch_reminder=itemView.findViewById(R.id.switch_reminder);

			chk_sunday=itemView.findViewById(R.id.chk_sunday);
			chk_monday=itemView.findViewById(R.id.chk_monday);
			chk_tuesday=itemView.findViewById(R.id.chk_tuesday);
			chk_wednesday=itemView.findViewById(R.id.chk_wednesday);
			chk_thursday=itemView.findViewById(R.id.chk_thursday);
			chk_friday=itemView.findViewById(R.id.chk_friday);
			chk_saturday=itemView.findViewById(R.id.chk_saturday);
		}
	}

	public interface CallBack
	{
		void onClickSelect(AlarmModel time, int position);
		void onClickRemove(AlarmModel time, int position);
		void onClickEdit(AlarmModel time, int position);
		void onClickSwitch(AlarmModel time, int position,boolean isOn);
		void onClickWeek(AlarmModel time, int position,int week_pos,boolean isOn);
	}

}

