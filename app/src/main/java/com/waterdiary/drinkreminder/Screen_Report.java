package com.waterdiary.drinkreminder;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.waterdiary.drinkreminder.adapter.ReportPagerAdapter;
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;
import com.waterdiary.drinkreminder.custom.NonSwipeableViewPager;


public class Screen_Report extends MasterBaseAppCompatActivity
{
	LinearLayout right_icon_block,left_icon_block;
	AppCompatTextView lbl_toolbar_title;

	TabLayout tabs;
	NonSwipeableViewPager viewPager;
	ReportPagerAdapter reportPagerAdapter;

	RadioButton rdo_week,rdo_month,rdo_year;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_report);

		FindViewById();
		Body();
	}

	private void FindViewById()
	{
		right_icon_block=findViewById(R.id.right_icon_block);
		left_icon_block=findViewById(R.id.left_icon_block);
		lbl_toolbar_title=findViewById(R.id.lbl_toolbar_title);

		tabs=findViewById(R.id.tabs);
		viewPager=findViewById(R.id.viewPager);

		rdo_week=findViewById(R.id.rdo_week);
		rdo_month=findViewById(R.id.rdo_month);
		rdo_year=findViewById(R.id.rdo_year);

		rdo_week.setText(sh.firstLetterCaps(sh.get_string(R.string.str_week)));
		rdo_month.setText(sh.firstLetterCaps(sh.get_string(R.string.str_month)));
		rdo_year.setText(sh.firstLetterCaps(sh.get_string(R.string.str_year)));

		rdo_week.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});

		rdo_month.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});

		rdo_year.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(2);
			}
		});
	}

	private void Body()
	{
		lbl_toolbar_title.setText("Report");
		left_icon_block.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		right_icon_block.setVisibility(View.GONE);

		/*tabs.addTab(tabs.newTab().setText(sh.get_string(R.string.str_week)),true);
		tabs.addTab(tabs.newTab().setText(sh.get_string(R.string.str_month)),false);
		tabs.addTab(tabs.newTab().setText(sh.get_string(R.string.str_year)),false);*/

		tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});

		reportPagerAdapter=new ReportPagerAdapter(getSupportFragmentManager(),mContext);
		viewPager.setAdapter(reportPagerAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) { }
		});

		viewPager.setOffscreenPageLimit(5);

		tabs.setupWithViewPager(viewPager);
	}
}