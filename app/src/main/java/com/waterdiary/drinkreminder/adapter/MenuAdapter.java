package com.waterdiary.drinkreminder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.model.Menu;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{
	Context mContext;
	private final ArrayList<Menu> menu_name;
	CallBack callBack;

	public MenuAdapter(Context c, ArrayList<Menu> menu_name,CallBack callBack)
	{
		mContext = c;
		this.menu_name=menu_name;
		this.callBack=callBack;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_item_menu, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		holder.textView.setText(menu_name.get(position).getMenuName());

		Glide.with(mContext).load(getImage(position)).into(holder.imageView);

		holder.item_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callBack.onClickSelect(menu_name.get(position),position);
			}
		});

		holder.selected_view.setVisibility(View.INVISIBLE);

		if(position==4 || position==7)
			holder.lbl_divider.setVisibility(View.VISIBLE);
		else
			holder.lbl_divider.setVisibility(View.GONE);
	}

	@Override
	public int getItemCount() {
		return menu_name.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		TextView textView;
		ImageView imageView;
		LinearLayout item_block;
		View selected_view;
		View lbl_divider;

		public ViewHolder(View itemView) {
			super(itemView);
			textView =itemView.findViewById(R.id.menu_name);
			imageView = itemView.findViewById(R.id.menu_img);
			item_block=itemView.findViewById(R.id.item_block);
			selected_view=itemView.findViewById(R.id.selected_view);
			lbl_divider=itemView.findViewById(R.id.lbl_divider);
		}
	}

	public interface CallBack
	{
		void onClickSelect(Menu menu, int position);
	}

	public Integer getImage(int pos)
	{
		Integer drawable=R.drawable.ic_menu_drink_water;

		switch (pos)
		{

			case 1:
				drawable=R.drawable.ic_menu_settings;
				break;

			case 2:
				drawable=R.drawable.ic_menu_rate;
				break;

			case 3:
				drawable=R.drawable.ic_menu_faq;
				break;

			case 4:
				drawable=R.drawable.ic_menu_achievements;
				break;

			case 5:
				drawable=R.drawable.ic_privacypolicy;
				break;

			case 6:
				drawable=R.drawable.ic_menu_share;
				break;

			default:
				drawable=R.drawable.ic_menu_drink_water;
				break;
		}

		return drawable;
	}
}

