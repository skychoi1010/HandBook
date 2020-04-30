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

import com.waterdiary.drinkreminder.R;
import com.waterdiary.drinkreminder.model.IntervalModel;

import java.util.List;

@SuppressLint("NewApi")
public class IntervalAdapter extends RecyclerView.Adapter<IntervalAdapter.ViewHolder>
{
	Context mContext;
	private final List<IntervalModel> intervals;
	CallBack callBack;

	public IntervalAdapter(Context c, List<IntervalModel> intervals, CallBack callBack)
	{
		mContext = c;
		this.intervals=intervals;
		this.callBack=callBack;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_item_sound, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		holder.lbl_sound_name.setText(intervals.get(position).getName());

		if(intervals.get(position).isSelected()) {
			holder.img_selected.setVisibility(View.VISIBLE);
			holder.item_block.getBackground().setTint(mContext.getResources().getColor(R.color.colorPrimary));
		}
		else {
			holder.img_selected.setVisibility(View.INVISIBLE);
			holder.item_block.getBackground().setTint(mContext.getResources().getColor(R.color.white));
		}

		holder.item_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callBack.onClickSelect(intervals.get(position),position);
			}
		});
	}


	@Override
	public int getItemCount() {
		return intervals.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		ImageView img_selected;
		LinearLayout item_block;
		TextView lbl_sound_name;

		public ViewHolder(View itemView) {
			super(itemView);
			item_block=itemView.findViewById(R.id.item_block);
			img_selected=itemView.findViewById(R.id.img_selected);
			lbl_sound_name=itemView.findViewById(R.id.lbl_sound_name);
		}
	}

	public interface CallBack
	{
		void onClickSelect(IntervalModel time, int position);
	}

}

