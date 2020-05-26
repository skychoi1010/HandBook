package com.waterdiary.drinkreminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.model.Container;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class ContainerAdapterNew extends RecyclerView.Adapter<ContainerAdapterNew.ViewHolder>
{
	Context mContext;
	private final ArrayList<Container> containerArrayList;
	private CallBack callBack;

	public ContainerAdapterNew(Context c, ArrayList<Container> containerArrayList, CallBack callBack)
	{
		mContext = c;
		this.containerArrayList=containerArrayList;
		this.callBack=callBack;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_item_container, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
	{
		try {

			if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {
				if (containerArrayList.get(position).getContainerValue().contains("."))
					holder.textView.setText(URLFactory.decimalFormat.format(Double.parseDouble(containerArrayList.get(position).getContainerValue())) + " " + URLFactory.WATER_UNIT_VALUE);
				else
					holder.textView.setText(containerArrayList.get(position).getContainerValue() + " " + URLFactory.WATER_UNIT_VALUE);
			} else {
				if (containerArrayList.get(position).getContainerValue().contains("."))
					holder.textView.setText(URLFactory.decimalFormat.format(Double.parseDouble(containerArrayList.get(position).getContainerValueOZ())) + " " + URLFactory.WATER_UNIT_VALUE);
				else
					holder.textView.setText(containerArrayList.get(position).getContainerValueOZ() + " " + URLFactory.WATER_UNIT_VALUE);
			}
		}
		catch (Exception e){}

		if(containerArrayList.get(position).isCustom())
			Glide.with(mContext).load(R.drawable.ic_custom_ml).into(holder.imageView);
		else
			Glide.with(mContext).load(getImage(position)).into(holder.imageView);

		holder.item_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callBack.onClickSelect(containerArrayList.get(position),position);
			}
		});

		if(containerArrayList.get(position).isSelected())
		{
			holder.img_selected.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.img_selected.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return containerArrayList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView textView;
		ImageView imageView;
		LinearLayout item_block;
		ImageView img_selected;

		public ViewHolder(View itemView) {
			super(itemView);
			textView =itemView.findViewById(R.id.container_name);
			imageView = itemView.findViewById(R.id.container_img);
			item_block=itemView.findViewById(R.id.item_block);
			img_selected = itemView.findViewById(R.id.img_selected);
		}
	}

	public interface CallBack
	{
		void onClickSelect(Container container, int position);
	}

	public Integer getImage(int pos)
	{
		Integer drawable=R.drawable.ic_menu_drink_water;
		/*
		if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")) {

			if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 50)
				drawable = R.drawable.ic_50_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 100)
				drawable = R.drawable.ic_100_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 150)
				drawable = R.drawable.ic_150_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 200)
				drawable = R.drawable.ic_200_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 250)
				drawable = R.drawable.ic_250_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 300)
				drawable = R.drawable.ic_300_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 500)
				drawable = R.drawable.ic_500_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 600)
				drawable = R.drawable.ic_600_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 700)
				drawable = R.drawable.ic_700_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 800)
				drawable = R.drawable.ic_800_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 900)
				drawable = R.drawable.ic_900_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValue()) == 1000)
				drawable = R.drawable.ic_1000_ml;
		}
		else
		{
			if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 2)
				drawable = R.drawable.ic_50_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 3)
				drawable = R.drawable.ic_100_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 5)
				drawable = R.drawable.ic_150_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 7)
				drawable = R.drawable.ic_200_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 8)
				drawable = R.drawable.ic_250_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 10)
				drawable = R.drawable.ic_300_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 17)
				drawable = R.drawable.ic_500_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 20)
				drawable = R.drawable.ic_600_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 24)
				drawable = R.drawable.ic_700_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 27)
				drawable = R.drawable.ic_800_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 30)
				drawable = R.drawable.ic_900_ml;
			else if (Double.parseDouble(containerArrayList.get(pos).getContainerValueOZ()) == 34)
				drawable = R.drawable.ic_1000_ml;
		}*/

		return drawable;
	}
}

