package com.basic.appbasiclibs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.core.content.res.ResourcesCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.basic.appbasiclibs.R;
import com.basic.appbasiclibs.mycustom.ClickSpan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility_Function
{
	Context mContext;
	Activity act;
	String_Helper sh;

	//email validation pattern
	public static final Pattern emailPattern = Pattern.compile
			(
					"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
							"\\@" +
							"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
							"(" +
							"\\." +
							"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
							")+"
			);

	//email GST NO pattern
	public static final Pattern gstPattern = Pattern.compile
			(
					"\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z\\d]{1}[Z]{1}[a-zA-Z\\d]{1}"
			);


	public Utility_Function(Context mContext, Activity act)
	{
		this.mContext=mContext;
		this.act=act;
		sh=new String_Helper(mContext,act);
	}

	public void permission_StrictMode()
	{
		if (Build.VERSION.SDK_INT > 9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	public void permission_VMStrictMode()
	{
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
	}

	public void hideKeyboard() {
		// Check if no view has focus:

		View view = act.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	public void clickify_read_more_click(final TextView txt,final View read_more,final String str)
	{
		read_more.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//txt.setText(str+" Read Less");
				//txt.setText(Html.fromHtml("<html><body>" + str.toString().trim() + " Read Less " + "</body></html>"));

				String des=Html.fromHtml(str.toString().trim() + " "+sh.get_string(R.string.str_read_less)).toString();
				txt.setText(Html.fromHtml(des));

				txt.setMaxLines(Integer.MAX_VALUE);//your TextView
				txt.setSingleLine(false);
				txt.setEllipsize(null);
				read_more.setVisibility(View.GONE);

				clickify_read_less_click(txt,read_more,str);
			}
		});
	}

	public void clickify_read_less_click(final TextView txt,final View read_more,final String str)
	{
		clickify(txt, ""+sh.get_string(R.string.str_read_less), new ClickSpan.OnClickListener()
		{
			@Override
			public void onClick()
			{
				try
				{
					txt.setMaxLines(1);//your TextView
					txt.setSingleLine(true);

					String des= Html.fromHtml(str.toString().trim()).toString();
					txt.setText(Html.fromHtml(des));

					read_more.setVisibility(View.VISIBLE);
					txt.setEllipsize(TextUtils.TruncateAt.END);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void clickify(TextView view, final String clickableText,final ClickSpan.OnClickListener listener)
	{
		CharSequence text = view.getText();
		String string = text.toString();
		ClickSpan span = new ClickSpan(listener);

		int start = string.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1) return;

		if (text instanceof Spannable) {
			((Spannable)text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(s);
		}

		MovementMethod m = view.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}


	public void share_wish(String str_subject, String str_link)
	{
		Intent_Helper ih=new Intent_Helper(mContext,act);
		ih.CustomShare(str_subject,str_link);
	}

	public boolean isValidEmail(String email)
	{
		boolean isValidEmail = false;
		CharSequence inputStr = email;
		Matcher matcher = emailPattern.matcher(inputStr);
		if (matcher.matches())
		{
			isValidEmail = true;
		}

		return isValidEmail;
	}

	public boolean isValidGSTNO(String no)
	{
		boolean isValidEmail = false;
		CharSequence inputStr = no;
		Matcher matcher = gstPattern.matcher(inputStr);
		if (matcher.matches())
		{
			isValidEmail = true;
		}

		return isValidEmail;
	}

	public void generateNoteOnSD(String sFileName, String sBody)
	{
		try
		{
			File root = new File(Environment.getExternalStorageDirectory(), "Notes");
			if (!root.exists())
			{
				root.mkdirs();
			}
			File gpxfile = new File(root, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void loadbanner()
	{
		/*AdView mAdView = (AdView) act.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);*/
	}

	public void setLabelFont(String font_name, TextView lbl_set)
	{
		Typeface lbl_tf = Typeface.createFromAsset(mContext.getAssets(),font_name);
		lbl_set.setTypeface(lbl_tf);
	}

	public boolean isTablet()
	{
		return (mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public String getIMEI()
	{
		TelephonyManager mngr = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
		@SuppressLint("MissingPermission")
        String imei = mngr.getDeviceId();
		return imei;
	}

	public void check_Enable_GPS()
	{
		final LocationManager manager = (LocationManager)act.getSystemService(Context.LOCATION_SERVICE );

		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
		{
			if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
				CheckEnableGPS();
			else
				turnGPSOn();
		}
	}

	public void CheckEnableGPS()
	{
		final AlertDialog.Builder builder =  new AlertDialog.Builder(act);
		final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
		builder.setCancelable(false);
		builder.setMessage(act.getResources().getString(R.string.open_gps_dialog))
				.setPositiveButton(act.getResources().getString(R.string.dialog_ok_button),
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface d, int id)
							{
								d.cancel();
								act.startActivity(new Intent(action));
							}
						})
				.setNegativeButton(act.getResources().getString(R.string.dialog_cancel_button),
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface d, int id)
							{
								d.cancel();
							}
						});
		builder.create().show();
	}

	public void turnGPSOn()
	{
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		act.sendBroadcast(intent);
	}

	public void turnGPSOff()
	{
		//Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		//intent.putExtra("enabled", false);
		//act.sendBroadcast(intent);
	}

	public boolean chkinternet()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressLint("MissingPermission")
		NetworkInfo i = connectivityManager.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;

		return true;
	}

	public double get_screen_size()
	{
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int dens = dm.densityDpi;
		double wi = (double)width / (double)dens;
		double hi = (double)height / (double)dens;
		double x = Math.pow(wi, 2);
		double y = Math.pow(hi, 2);
		double screenInches = Math.sqrt(x+y);
		String screenInformation= String.format("%.2f", screenInches);
		return Double.parseDouble(""+screenInformation);
	}

	public String GetCountryZipCode()
    {
		String CountryID="";
		String CountryZipCode="";

		TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		//getNetworkCountryIso
		CountryID= manager.getSimCountryIso().toUpperCase();
		String[] rl=mContext.getResources().getStringArray(R.array.CountryCodes);
		for(int i=0;i<rl.length;i++){
			String[] g=rl[i].split(",");
			if(g[1].trim().equals(CountryID.trim())){
				CountryZipCode=g[0];
				break;
			}
		}
		return "+"+CountryZipCode;
	}

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        String imgUrl = "http://img.youtube.com/vi/"+extractVideoIdFromUrl(videoUrl) + "/0.jpg";
        return imgUrl;
    }

    public static String extractVideoIdFromUrl(String url)
    {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);

        for(String regex : Constant.videoIdRegex) {
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

            if(matcher.find()){
                return matcher.group(1);
            }
        }

        return null;
    }
    private static String youTubeLinkWithoutProtocolAndDomain(String url) {
        Pattern compiledPattern = Pattern.compile(Constant.youTubeUrlRegEx);
        Matcher matcher = compiledPattern.matcher(url);

        if(matcher.find()){
            return url.replace(matcher.group(), "");
        }
        return url;
    }
}