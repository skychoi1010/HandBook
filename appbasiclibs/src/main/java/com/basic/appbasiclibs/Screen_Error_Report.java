package com.basic.appbasiclibs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.appbasiclibs.utils.ExceptionHandler;

public class Screen_Error_Report extends BaseActivity
{
	TextView error;
	Button btn_cancel_error,btn_send_error;
	Context mContext;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.screen_error_report);

		mContext=Screen_Error_Report.this;

		error = (TextView) findViewById(R.id.error);
		btn_cancel_error=findViewById(R.id.btn_cancel_error);
		btn_send_error=findViewById(R.id.btn_send_error);

		error.setText(getIntent().getStringExtra("error"));

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
		Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		btn_send_error.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{
					Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + ""));
					intent.putExtra(Intent.EXTRA_SUBJECT, "Android Bug Report");
					intent.putExtra(Intent.EXTRA_TEXT, ""+error.getText().toString());
					startActivity(intent);
				}
				catch (Exception ex)
				{
					Toast.makeText(Screen_Error_Report.this, ex.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});

		btn_cancel_error.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AppClose.exitApplication(mContext);
            }
        });
	}
}