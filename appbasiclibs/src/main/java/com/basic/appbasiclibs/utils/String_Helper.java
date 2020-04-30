package com.basic.appbasiclibs.utils;

import android.app.Activity;
import android.content.Context;

import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class String_Helper
{
    Context mContext;
    Activity act;

    public String_Helper(Context mContext, Activity act)
    {
        this.mContext=mContext;
        this.act=act;
    }

    public StringBody getMultiplePartParam(String str)
    {
        StringBody stringBody=null;
        try
        {
            stringBody = new StringBody(str, Charset.forName(HTTP.UTF_8));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return stringBody;
    }

    public boolean check_blank_data(String data)
    {
        if(data.equals("") || data.isEmpty() || data.length()==0 || data.equals("null") || data==null)
            return true;

        return false;
    }

    public String getSqlValue(String input)
    {
        return input.replace("'","''");
    }

    public String getSqlValue_reverse(String input)
    {
        return input.replace("\\", "");
    }

    public String getHtmlData(String data)
    {
        String head = "<head><style>@font-face {font-family: 'verdana';src: url('file:///android_asset/Roboto-Bold.ttf');}body {font-family: 'verdana';}</style></head>";
        String htmlData= "<html>"+head+"<body style=\"text-align:justify\">"+data+"</body></html>" ;
        return htmlData;
    }

    public String getHtmlDataNormal(String data)
    {
        String head = "<head><style>@font-face {font-family: 'verdana';src: url('file:///android_asset/Roboto-Regular.ttf');}body {font-family: 'verdana';}</style></head>";
        String htmlData= "<html>"+head+"<body style=\"text-align:justify\">"+data+"</body></html>" ;
        return htmlData;
    }

    public String getCommaSeparatedString(ArrayList<String> arr)
    {
        String lst_data="";

        for(int k=0;k<arr.size();k++)
            lst_data+=arr.get(k)+",";

        if(!check_blank_data(lst_data))
            lst_data=lst_data.substring(0,lst_data.length()-1);

        return lst_data;
    }

    public String getCommaSeparatedString(ArrayList<String> arr, String sign)
    {
        String lst_data="";

        for(int k=0;k<arr.size();k++)
            lst_data+=arr.get(k)+sign;

        if(!check_blank_data(lst_data))
            lst_data=lst_data.substring(0,lst_data.length()-1);

        return lst_data;
    }

    public ArrayList<String> getArrayListFromCommaSeparatedString(String comma_string)
    {
        ArrayList<String> arr=new ArrayList<>();

        String[] str_arr=comma_string.split(",");

        for(int k=0;k<str_arr.length;k++)
            arr.add(str_arr[k]);

        return arr;
    }

    public ArrayList<String> getArrayListFromCommaSeparatedString(String comma_string, String sign)
    {
        ArrayList<String> arr=new ArrayList<>();

        String[] str_arr=comma_string.split(sign);

        for(int k=0;k<str_arr.length;k++)
            arr.add(str_arr[k]);

        return arr;
    }

    public String[] getArrayFromCommaSeparatedString(String comma_string)
    {
        String[] str_arr=comma_string.split(",");

        return str_arr;
    }

    public String[] getArrayFromCommaSeparatedString(String comma_string, String sign)
    {
        String[] str_arr=comma_string.split(sign);

        return str_arr;
    }

    public String get_2_point(String no)
    {
        if(no.length()==1)
            no="0"+no;
        return no;
    }

    public String get_2_digit_year(String no)
    {
        return no.substring(2);
    }

    public String firstLetterCaps(String data)
    {
        String firstLetter = data.substring(0,1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public String get_string(int id)
    {
        return mContext.getResources().getString(id);
    }

    public String[] get_array(int id)
    {
        return mContext.getResources().getStringArray(id);
    }

    public ArrayList<String> get_arraylist(int id)
    {
        ArrayList<String> arr=new ArrayList<String>( Arrays.asList(mContext.getResources().getStringArray(id)));
        return arr;
    }

    public String get_user_address(String street, String city, String state, String zipcode, String country)
    {
        String final_address=street;

        if(!(city.equals("") || city.isEmpty()))
            final_address+=" , "+city;

        if(!(state.equals("") || state.isEmpty()))
            final_address+=" , "+state;

        if(!(zipcode.equals("") || zipcode.isEmpty()))
            final_address+=" - "+zipcode;

        if(!(country.equals("") || country.isEmpty()))
            final_address+=" , "+country;

        return final_address;
    }
}
