package com.basic.appbasiclibs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database_Helper
{
	Utility_Function uf;
	Alert_Helper ah;
	String_Helper sh;

	Context mContext;
	Activity act;

	@SuppressLint("WrongConstant")
	public Database_Helper(Context mContext, Activity act)
	{
		this.mContext=mContext;
		this.act=act;

		uf=new Utility_Function(mContext,act);
		uf.permission_StrictMode();

		ah=new Alert_Helper(mContext);
		sh=new String_Helper(mContext,act);

		Constant.SDB=mContext.openOrCreateDatabase(Constant.DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY,null);


	}

	public void CREATE_TABLE(String table_name, HashMap<String,String> fields)
	{
		String query="CREATE TABLE IF NOT EXISTS "+table_name+"(";

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);

			query+=key+" "+value+",";
		}

		query=query.substring(0,query.length()-1);

		query=query+");";

		System.out.println("CREAT QUERY : "+ query);

		FIRE(query);
	}

	public void INSERT(String table_name, HashMap<String,String> fields)
	{
		/*String query="INSERT INTO "+table_name+" ( ";
		String column_list="",column_value_list="";

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);

			column_list+="\""+key+"\",";
			column_value_list+="\""+value+"\",";
		}

		column_list=column_list.substring(0,column_list.length()-1);
		column_value_list=column_value_list.substring(0,column_value_list.length()-1);

		query+=column_list;

		query+=") VALUES(";

		query+=column_value_list;

		query+=")";

		System.out.println("QUERY : "+ query);

		FIRE(query);*/

		ContentValues initialValues = new ContentValues();

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);
			initialValues.put(key, value);
		}

		Constant.SDB.insert(table_name, null, initialValues);
	}

	public void INSERT(String table_name, ContentValues fields )
	{
		Constant.SDB.insert(table_name, null, fields);
	}

	public void UPDATE(String table_name, HashMap<String,String> fields, String where_con)
	{
		/*String query="UPDATE "+table_name+" SET ";

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);

			query+=key+"=\""+value+"\",";
		}

		query=query.substring(0,query.length()-1);

		query+=" WHERE "+where_con;

		System.out.println("UPDATE QUERY : "+ query);

		FIRE(query);*/

		ContentValues initialValues = new ContentValues();

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);
			initialValues.put(key, value);
		}

		Constant.SDB.update(table_name,  initialValues,where_con,null);
	}

	public void UPDATE(String table_name, ContentValues fields, String where_con )
	{
		Constant.SDB.update(table_name,fields,where_con,null);
	}

	public void GET_LOGIN_USER_DETAILS()
	{
		try
		{
			//Constant.user_id="";

			Cursor cur=Constant.SDB.rawQuery("select * from tbl_user_login",null);
			while(cur.moveToNext())
			{
				//Constant.user_id=cur.getString(cur.getColumnIndex("user_id"));
			}
		}
		catch(Exception e){}
	}

	public ArrayList<HashMap<String, String>> getdataquery(String query)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		Cursor c = Constant.SDB.rawQuery(query, null);

		System.out.println("SELECT QUERY : "+ query);

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public ArrayList<HashMap<String, String>> getdata(String table_name)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		String query = "SELECT * FROM "+table_name;

		Cursor c = Constant.SDB.rawQuery(query, null);

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		String query = "SELECT * FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" where "+where_con;

		Cursor c = Constant.SDB.rawQuery(query, null);

		System.out.println("SELECT QUERY : "+ query);

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public ArrayList<HashMap<String, String>> getdata(String table_name, String order_field, int order_by)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		String query = "SELECT * FROM "+table_name;

		if(!sh.check_blank_data(order_field))
		{
			if (order_by == 0)
				query+=" ORDER BY "+order_field+" ASC";
			else
				query+=" ORDER BY "+order_field+" DESC";
		}

		Cursor c = Constant.SDB.rawQuery(query, null);

		System.out.println("DESC QUERY:"+query);

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public ArrayList<HashMap<String, String>> getdata(String table_name, String where_con, String order_field, int order_by)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		String query = "SELECT * FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" WHERE "+where_con;

		if(!sh.check_blank_data(order_field))
		{
			if (order_by == 0)
				query+=" ORDER BY "+order_field+" ASC";
			else
				query+=" ORDER BY "+order_field+" DESC";
		}

		Cursor c = Constant.SDB.rawQuery(query, null);

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public ArrayList<HashMap<String, String>> getdata(String field_name, String table_name, String where_con)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		String query = "SELECT "+field_name+" FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" where "+where_con;

		System.out.print("JOIN QUERY:"+query);
		//ah.Show_Alert_Dialog("JOIN QUERY:"+query);


		Cursor c = Constant.SDB.rawQuery(query, null);


		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public ArrayList<HashMap<String, String>> getdata(String field_name, String table_name, String where_con, String order_field, int order_by)
	{
		ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String,String>>();

		String query = "SELECT "+field_name+" FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" where "+where_con;

		if(!sh.check_blank_data(order_field))
		{
			if (order_by == 0)
				query+=" ORDER BY "+order_field+" ASC";
			else
				query+=" ORDER BY "+order_field+" DESC";
		}

		System.out.println("HISTORY JOIN QUERY:"+query);

		Cursor c = Constant.SDB.rawQuery(query, null);

		if(c.moveToFirst())
		{
			do
			{
				HashMap<String, String> map = new HashMap<String, String>();
				for(int i=0; i<c.getColumnCount();i++)
				{
					map.put(c.getColumnName(i), c.getString(i));
				}

				maplist.add(map);
			}
			while (c.moveToNext());
		}

		return maplist;
	}

	public void REMOVE(String table_name)
	{
		String query = "DELETE FROM "+table_name;
		FIRE(query);
	}

	public void REMOVE(String table_name, String where_con)
	{
		String query = "DELETE FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" WHERE "+where_con;

		FIRE(query);
	}

	public int TOTAL_ROW(String table_name)
	{
		String query = "SELECT * FROM "+table_name;
		Cursor c = Constant.SDB.rawQuery(query, null);

		return c.getCount();
	}

	public int TOTAL_ROW(String table_name, String where_con)
	{
		String query = "SELECT * FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" WHERE "+where_con;

		Cursor c = Constant.SDB.rawQuery(query, null);

		int count=c.getCount();

		if(c != null)
			c.close();

		return count;
	}

	public boolean IS_EXISTS(String table_name, String where_con)
	{
		String query = "SELECT * FROM "+table_name;

		if(!sh.check_blank_data(where_con))
			query+=" WHERE "+where_con;

		Cursor c = Constant.SDB.rawQuery(query, null);

		if(c.getCount()>0)
			return true;

		return false;
	}

	public String GET_LAST_ID(String table_name)
	{
		String query = "SELECT id FROM "+table_name;

		Cursor c = Constant.SDB.rawQuery(query, null);

		if(c.moveToLast())
			return ""+c.getString(0);

		return "0";
	}

	public long NO_OF_AFFECTED_ROWS()
	{
		long affectedRowCount=0;

		Cursor cursor = Constant.SDB.rawQuery("SELECT changes() AS affected_row_count", null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
			affectedRowCount = cursor.getLong(cursor.getColumnIndex("affected_row_count"));

		return affectedRowCount;
	}

	public void FIRE(String query)
	{
		Constant.SDB.execSQL(query);
	}

	public String MD5(String md5)
	{
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i)
			{
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		}
		catch (java.security.NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean exportDB()
	{
		boolean result=false;
		File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Databackup");

		if(!sd.exists()) {
			sd.mkdirs();
		}
		//File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Databackup");
		File data = Environment.getDataDirectory();
		FileChannel source=null;
		FileChannel destination=null;
		String currentDBPath = "/data/"+ "com.appname.appnamebasic" +"/databases/"+ Constant.DATABASE_NAME;
		String backupDBPath = Constant.DATABASE_NAME;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			result=true;
			//Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
		} catch(Exception e)
		{
			result=false;
			e.printStackTrace();
		}
		return  result;
	}
}
