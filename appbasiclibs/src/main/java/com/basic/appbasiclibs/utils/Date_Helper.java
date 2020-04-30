package com.basic.appbasiclibs.utils;


import android.util.Log;

import com.basic.appbasiclibs.BaseActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Date_Helper extends BaseActivity
{
    public Long getMillisecondFromDate(String givenDateString, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long timeInMilliseconds=0;
        try
        {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public long getMillisecond()
    {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.AM_PM,0);
        return cal.getTimeInMillis();
    }

    public long getCurrentGMTMillisecond()
    {
        Calendar current_cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return current_cal.getTimeInMillis();
    }

    public long getMillisecond(int year,int month,int day)
    {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.AM_PM,0);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year,int month,int day,int hour,int minute,int format)
    {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.HOUR,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.AM_PM,format);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year,int month,int day,int hour,int minute,String format)
    {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.HOUR,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);
        if(format.toUpperCase().equals("PM"))
            cal.set(Calendar.AM_PM,1);
        else
            cal.set(Calendar.AM_PM,0);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year,int month,int day,int hour,int minute)
    {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);
        return cal.getTimeInMillis();
    }

    public long getCurrentMillisecond()
    {
        Calendar cal= Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    public String getTimeWithAP(String time)
    {
        String fformat;
        String[] separated = time.split(":");
        int fhour= Integer.parseInt(""+separated[0]);
        int fmin= Integer.parseInt(""+separated[1]);
        if (fhour == 0)
        {
            fhour += 12;
            fformat = "AM";
        }
        else if (fhour == 12)
        {
            fformat = "PM";
        }
        else if (fhour > 12)
        {
            fhour -= 12;
            fformat = "PM";
        }
        else
        {
            fformat = "AM";
        }

        return sh.get_2_point(""+fhour)+":"+sh.get_2_point(""+fmin)+" "+fformat;
    }

    public String getDaySuffix(final int n)
    {
        if(n < 1 || n > 31)
            return "Invalid date";
        if (n >= 11 && n <= 13)
            return "th";

        switch (n % 10)
        {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    public String getFullMonth(String dateInString, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formated="";
        try
        {
            Date date = sdf.parse(dateInString);
            formated = new SimpleDateFormat("MMMM").format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return formated;
    }

    public String getShortMonth(String dateInString, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formated="";
        try
        {
            Date date = sdf.parse(dateInString);
            formated = new SimpleDateFormat("MMM").format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return formated;
    }

    public String getMonth(String dateInString, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formated="";
        try
        {
            Date date = sdf.parse(dateInString);
            formated = new SimpleDateFormat("MM").format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return formated;
    }

    public String getDay(String dateInString, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formated="";
        try
        {
            Date date = sdf.parse(dateInString);
            formated = new SimpleDateFormat("dd").format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return formated;
    }

    public String getYear(String dateInString, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formated="";
        try
        {
            Date date = sdf.parse(dateInString);
            formated = new SimpleDateFormat("yyyy").format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return formated;
    }

    public String getDayswithPrefix(String dateInString, String format)
    {
        /*SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar c = Calendar.getInstance();
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString+getDaySuffix(Integer.parseInt(dateInString));*/
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formated="0";
        try
        {
            Date date = sdf.parse(dateInString);
            formated = new SimpleDateFormat("dd").format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return formated+getDaySuffix(Integer.parseInt(formated));
    }

    public String getCurrentDateTime(boolean is24TimeFormat)
    {
        SimpleDateFormat dateFormat;
        if(is24TimeFormat)
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        else
            dateFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm a", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }

    public String set_format_date(int year, int month, int day, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formatedDate = sdf.format(new Date(year, month, day));
        return formatedDate;
    }

    public String getCurrentDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getFormatDate(String format)
    {
        //SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getCurrentDate(String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getCurrentTime(boolean is24TimeFormat)
    {
        SimpleDateFormat dateFormat;
        if(is24TimeFormat)
            dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        else
            dateFormat = new SimpleDateFormat("KK:mm a", Locale.US);

        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String FormateDateFromString(String inputFormat, String outputFormat, String inputDate)
    {
        Date parsed = null;
        String outputDate = "";

        /*SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());*/

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat,Locale.US);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat,Locale.US);

        try
        {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        }
        catch (Exception e) {}

        return outputDate;
    }

    public long DayDifferent(String str_date1, String str_date2)
    {
		/*String inputString1 = "23 01 1997";
		String inputString2 = "27 04 1997";*/
        long diff=0;
        long days_diff=0;
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try
        {
            Date date1 = myFormat.parse(str_date1);
            Date date2 = myFormat.parse(str_date2);
            diff = date2.getTime() - date1.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            days_diff= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return days_diff;
    }

    public long DayDifferent(String str_date1, String str_date2, String format)
    {
		/*String inputString1 = "23 01 1997";
		String inputString2 = "27 04 1997";*/
        long diff=0;
        long days_diff=0;
        SimpleDateFormat myFormat = new SimpleDateFormat(format, Locale.getDefault());
        try
        {
            Date date1 = myFormat.parse(str_date1);
            Date date2 = myFormat.parse(str_date2);
            diff = date2.getTime() - date1.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            days_diff= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return days_diff;
    }

    public String getDaysAgo(String date)
    {
        String dateString = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date convertedDate = new Date();
        Date serverDate = new Date();

        try
        {
            convertedDate = dateFormat.parse(dateString);
            Calendar c = Calendar.getInstance();
            String formattedDate = dateFormat.format(c.getTime());
            serverDate = dateFormat.parse(formattedDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //long days1 = (convertedDate.getTime() - serverDate.getTime());
        long days1 = (serverDate.getTime() - convertedDate.getTime());

        final long seconds = days1/1000;
        final long minutes = seconds/60;
        final long hours = minutes/60;
        final long days = hours/24;
        final long months = days/31;
        final long years = days/365;

        System.out.println("serverDate:"+serverDate.getTime());
        System.out.println("convertedDate:"+convertedDate.getTime());
        System.out.println("days1:"+days1);
        System.out.println("seconds:"+seconds);
        System.out.println("minutes:"+minutes);
        System.out.println("hours:"+hours);
        System.out.println("days:"+days);
        System.out.println("months:"+months);
        System.out.println("years:"+years);

        if (seconds < 86400) // 24  60 60 ( less than 1 day )
        {
            return "today";
        }
        else if (seconds < 172800) // 48  60  60 ( less than 2 day )
        {
            return "yesterday";
        }
        else if (seconds < 2592000) // 30  24  60 * 60 ( less than 1 month )
        {
            return days + " days ago";
        }
        else if (seconds < 31104000) // 12  30  24  60  60
        {
            return months <= 1 ? "one month ago" : months + " months ago";
        }
        else
        {
            return years <= 1 ? "one year ago" : years + " years ago";
        }
    }

    public boolean check_current_time_between_2date(String start_date, String end_date)
    {
        try
        {
            Date mToday = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            String curTime = sdf.format(mToday);

            Date start = sdf.parse(start_date);
            Date end = sdf.parse(end_date);
            Date userDate = sdf.parse(curTime);

            if(end.before(start))
            {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(mCal.getTimeInMillis());
            }

            Log.d("curTime", userDate.toString());
            Log.d("start", start.toString());
            Log.d("end", end.toString());

            if (userDate.after(start) && userDate.before(end))
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean check_specific_time_between_2date(String start_date, String end_date, String my_date)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            Date start = sdf.parse(start_date);
            Date end = sdf.parse(end_date);
            Date userDate = sdf.parse(my_date);

            if(end.before(start))
            {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(mCal.getTimeInMillis());
            }

            Log.d("curTime", userDate.toString());
            Log.d("start", start.toString());
            Log.d("end", end.toString());

            if(userDate==start)
                return true;
            else if (userDate.after(start) && userDate.before(end))
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public String getGMTDate(String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar cal= Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        return formatter.format(cal.getTime());
    }

    public int get_total_days_of_month(int month,int year)
    {
        int day=31;

        if (month == 4 || month == 6 || month == 9 || month == 11)
            return 30;
        else if (month == 2)
        {
            if(year%4==0)
                return 29;
            else
                return 28;
        }
        return day;
    }

    public boolean different_time(String current_time,String time)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date CurrentTime = null;
        Date Time = null;

        try
        {
            CurrentTime = simpleDateFormat.parse(current_time);
            Time = simpleDateFormat.parse(time);
        }
        catch(ParseException e)
        {
            //Some thing if its not working
        }

        long difference = Time.getTime() - CurrentTime.getTime();

        if(difference>=0)
            return true;

        return false;
    }

    public boolean different_time(String current_time,String time,String format)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date CurrentTime =  null;
        Date Time = null;

        try
        {
            CurrentTime = simpleDateFormat.parse(current_time);
            Time = simpleDateFormat.parse(time);
        }
        catch(ParseException e)
        {
            //Some thing if its not working
        }

        long difference = Time.getTime() - CurrentTime.getTime();

        if(difference>=0)
            return true;

        return false;
    }

    public String get_2_point(String no)
    {
        if(no.length()==1)
            no="0"+no;
        return no;
    }

    public String getTimeHour(String time)
    {
        String[] separated = time.split(":");
        int fhour= Integer.parseInt(""+separated[0]);
        return get_2_point(""+fhour);
    }

    public String getTimeMin(String time)
    {
        String[] separated = time.split(":");
        int fmin= Integer.parseInt(""+separated[1]);
        return get_2_point(""+fmin);
    }

    public String getTimeFormat(String time)
    {
        String fformat;
        String[] separated = time.split(":");
        int fhour= Integer.parseInt(""+separated[0]);
        if (fhour == 0)
        {
            fformat = "AM";
        }
        else if (fhour == 12)
        {
            fformat = "PM";
        }
        else if (fhour > 12)
        {
            fformat = "PM";
        }
        else
        {
            fformat = "AM";
        }
        return ""+fformat;
    }

    public String getCurrentTimeSecond(boolean is24TimeFormat)
    {
        SimpleDateFormat dateFormat;
        if(is24TimeFormat)
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        else
            dateFormat = new SimpleDateFormat("KK:mm:ss a", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getLastDateOfMonth(int month, int year,String format)
    {
        Calendar calendar = Calendar.getInstance();
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat(format);//"MM/dd/yyyy"
        return DATE_FORMAT.format(date);
    }
}