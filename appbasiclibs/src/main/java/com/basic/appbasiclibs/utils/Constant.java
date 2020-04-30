package com.basic.appbasiclibs.utils;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;

import com.basic.appbasiclibs.BaseActivity;

import java.util.List;

public class Constant extends BaseActivity
{
	public static final boolean DEVELOPER_MODE = true;

	// DATABASE
	public static SQLiteDatabase SDB;
	public static final String DATABASE_NAME="3star.db";

	// custom share
	public static String share_purchase_title="Share To";
	public static List<ResolveInfo> launchables;
	public static PackageManager pm;
	public static List<ResolveInfo> launchables_sel;

	public static final String general_share_title="Share";

	//FOR INTENT
	public static final int PICK_CONTACT = 1000;

	public static final String no_internet_message="No Internet Connection!!!";

	public static final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
	public static final String[] videoIdRegex = { "\\?vi?=([^&]*)","watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};
}