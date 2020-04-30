package com.waterdiary.drinkreminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.model.History;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
{
	Context mContext;
	private final ArrayList<History> historyArrayList;
	CallBack callBack;

	public HistoryAdapter(Context c, ArrayList<History> historyArrayList, CallBack callBack)
	{
		mContext = c;
		this.historyArrayList=historyArrayList;
		this.callBack=callBack;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_item_history, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		holder.lbl_date.setText(historyArrayList.get(position).getDrinkDate());
		holder.lbl_total_day_water.setText(historyArrayList.get(position).getTotalML());

		String str=" "+historyArrayList.get(position).getContainerMeasure();

		if(URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml"))
			holder.container_name.setText(historyArrayList.get(position).getContainerValue()+str);
		else
			holder.container_name.setText(historyArrayList.get(position).getContainerValueOZ()+str);
		holder.lbl_time.setText(historyArrayList.get(position).getDrinkTime());

		if(position==0)
			holder.super_item_block.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
		else
			holder.super_item_block.setBackgroundColor(mContext.getResources().getColor(R.color.white));

		if(position!=0)
		{
			if(showHeader(position))
				holder.item_header_block.setVisibility(View.VISIBLE);
			else
				holder.item_header_block.setVisibility(View.GONE);
		}
		else
			holder.item_header_block.setVisibility(View.VISIBLE);

		holder.divider.setVisibility(View.VISIBLE);

		Glide.with(mContext).load(getImage(position)).into(holder.imageView);

		holder.item_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callBack.onClickSelect(historyArrayList.get(position),position);
			}
		});

		holder.btnRemoveRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClickRemove(historyArrayList.get(position),position);
			}
		});
	}

	public boolean showHeader(int position)
	{
		if(historyArrayList.get(position).getDrinkDate().equalsIgnoreCase(historyArrayList.get(position-1).getDrinkDate()))
			return false;
		else
			return  true;
	}

	@Override
	public int getItemCount() {
		return historyArrayList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView lbl_date,lbl_total_day_water;
		ImageView imageView;
		LinearLayout item_block,item_header_block;
		TextView container_name,lbl_time;
		View divider;
		RelativeLayout super_item_block;

		ImageView btnRemoveRow;

		public ViewHolder(View itemView) {
			super(itemView);
			lbl_date =itemView.findViewById(R.id.lbl_date);
			lbl_total_day_water = itemView.findViewById(R.id.lbl_total_day_water);
			item_block=itemView.findViewById(R.id.item_block);
			item_header_block=itemView.findViewById(R.id.item_header_block);
			imageView=itemView.findViewById(R.id.container_img);
			container_name=itemView.findViewById(R.id.container_name);
			lbl_time=itemView.findViewById(R.id.lbl_time);
			divider=itemView.findViewById(R.id.divider);
			super_item_block=itemView.findViewById(R.id.super_item_block);

			btnRemoveRow=itemView.findViewById(R.id.btnRemoveRow);
		}
	}

	public interface CallBack
	{
		void onClickSelect(History history, int position);
		void onClickRemove(History history, int position);
	}

	public Integer getImage(int pos)
	{
		Integer drawable=R.drawable.ic_custom_ml;

		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {

			String val=historyArrayList.get(pos).getContainerValue();

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
			String val=historyArrayList.get(pos).getContainerValueOZ();

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
}