package com.waterdiary.drinkreminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.basic.appbasiclibs.utils.Constant;
import com.waterdiary.drinkreminder.adapter.HistoryAdapter;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;
import com.waterdiary.drinkreminder.model.History;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class Screen_History extends MasterBaseActivity
{
	LinearLayout right_icon_block,left_icon_block;
	AppCompatTextView lbl_toolbar_title;

	RecyclerView historyRecyclerView;
	ArrayList<History> histories=new ArrayList<>();
	HistoryAdapter adapter;
	AppCompatTextView lbl_no_record_found;

	NestedScrollView nestedScrollView;
	boolean isLoading = true;

	int perPage=20;
	int page=0;

	int beforeLoad=0,afterLoad=0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_history);

		FindViewById();
		Body();
	}

	private void FindViewById()
	{
		right_icon_block=findViewById(R.id.right_icon_block);
		left_icon_block=findViewById(R.id.left_icon_block);
		lbl_toolbar_title=findViewById(R.id.lbl_toolbar_title);

		historyRecyclerView=findViewById(R.id.historyRecyclerView);
		lbl_no_record_found=findViewById(R.id.lbl_no_record_found);
		nestedScrollView=findViewById(R.id.nestedScrollView);


	}

	private void Body()
	{
		lbl_toolbar_title.setText(sh.get_string(R.string.str_drink_history));
		left_icon_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		right_icon_block.setVisibility(View.GONE);

		historyRecyclerView.setNestedScrollingEnabled(false);

		adapter = new HistoryAdapter(act, histories, new HistoryAdapter.CallBack() {
			@Override
			public void onClickSelect(History history, int position) {

			}

			@Override
			public void onClickRemove(final History history, final int position) {
				AlertDialog.Builder dialog=  new  AlertDialog.Builder(act)
						.setMessage(sh.get_string(R.string.str_history_remove_confirm_message))
						.setPositiveButton(sh.get_string(R.string.str_yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {

										dh.REMOVE("tbl_drink_details","id="+history.getId());

										page=0;
										isLoading=true;
										histories.clear();
										load_history(false);

										/*histories.remove(position);*/
										adapter.notifyDataSetChanged();

										dialog.dismiss();
									}
								}
						)
						.setNegativeButton(sh.get_string(R.string.str_no),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								}
						);

				dialog.show();
			}
		});

		historyRecyclerView.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false));

		historyRecyclerView.setAdapter(adapter);

		load_history(false);

		nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
			@Override
			public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
				String TAG = "nested_sync";
				if (scrollY > oldScrollY) {
					Log.i(TAG, "Scroll DOWN");
				}
				if (scrollY < oldScrollY) {
					Log.i(TAG, "Scroll UP");
				}

				if (scrollY == 0) {
					Log.i(TAG, "TOP SCROLL");
				}

				if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
					Log.i(TAG, "BOTTOM SCROLL");

					if (isLoading)
					{
						isLoading = false;

						page++;

						//ah.Show_Custom_Progress_Dialog();

						load_history(true);
					}
				}
			}
		});
	}

	public void load_history(boolean closeLoader)
	{
		//ArrayList<HashMap<String, String>> arr_data=dh.getdata("tbl_drink_details","strftime('%Y-%m-%d %H:%M:%S', DrinkDateTime)",1);

        //strftime('%Y-%m-%d %H:%M:%S', DrinkDateTime) as mydt

		//datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || '-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8))

		/*Cursor c = Constant.SDB.rawQuery("SELECT DrinkDateTime FROM tbl_drink_details ORDER BY datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || '-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8))", null);

		//ah.customAlert("--> "+c.getCount());

		if(c.getCount()>0)
		{
			c.moveToNext();

			ah.customAlert("--> "+c.getString(0));
		}*/

		int start_idx=page*perPage;

		String query="SELECT * FROM tbl_drink_details ORDER BY datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || '-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8)) DESC limit "+start_idx+","+perPage;

		Cursor c = Constant.SDB.rawQuery(query, null);

		ArrayList<HashMap<String, String>> arr_data=new ArrayList<>();

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				arr_data.add(map);
			}
			while (c.moveToNext());
		}

		String mes_unit=URLFactory.WATER_UNIT_VALUE;

		for(int k=0;k<arr_data.size();k++)
		{
			History history=new History();
			history.setId(arr_data.get(k).get("id"));
			//history.setContainerMeasure(arr_data.get(k).get("ContainerMeasure"));

			history.setContainerMeasure(mes_unit);
			//history.setContainerValue(URLFactory.decimalFormat.format(Double.parseDouble(arr_data.get(k).get("ContainerValue")))+" "+mes_unit);
			//history.setContainerValueOZ(URLFactory.decimalFormat.format(Double.parseDouble(arr_data.get(k).get("ContainerValueOZ")))+" "+mes_unit);

			/*history.setContainerValue(""+(int)(Double.parseDouble(arr_data.get(k).get("ContainerValue")))+" "+mes_unit);
			history.setContainerValueOZ(""+(int)(Double.parseDouble(arr_data.get(k).get("ContainerValueOZ")))+" "+mes_unit);*/

			history.setContainerValue(""+(int)(Double.parseDouble(arr_data.get(k).get("ContainerValue"))));
			history.setContainerValueOZ(""+(int)(Double.parseDouble(arr_data.get(k).get("ContainerValueOZ"))));

			history.setDrinkDate(arr_data.get(k).get("DrinkDate"));
			history.setDrinkTime(dth.FormateDateFromString("HH:mm","hh:mm a",arr_data.get(k).get("DrinkTime")));

			ArrayList<HashMap<String, String>> arr_data2=dh.getdata("tbl_drink_details","DrinkDate ='"+arr_data.get(k).get("DrinkDate")+"'");

			float tot=0;

			for(int j=0;j<arr_data2.size();j++)
			{
				if(mes_unit.equalsIgnoreCase("ml"))
					tot+=Double.parseDouble(arr_data2.get(j).get("ContainerValue"));
				else
					tot+=Double.parseDouble(arr_data2.get(j).get("ContainerValueOZ"));
			}

			//history.setTotalML(URLFactory.decimalFormat.format(tot)+" "+mes_unit);

			history.setTotalML(""+(int)(tot)+" "+mes_unit);

			histories.add(history);
		}

		/*if(closeLoader)
			ah.Close_Custom_Progress_Dialog();*/

		afterLoad=histories.size();

		if(afterLoad==0)
			isLoading=false;
		else if(afterLoad>beforeLoad)
			isLoading=true;
		else
			isLoading=false;

		if(histories.size()>0)
			lbl_no_record_found.setVisibility(View.GONE);
		else
			lbl_no_record_found.setVisibility(View.VISIBLE);

		adapter.notifyDataSetChanged();
	}
}
