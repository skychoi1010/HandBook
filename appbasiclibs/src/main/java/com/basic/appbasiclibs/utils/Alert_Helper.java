package com.basic.appbasiclibs.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.appbasiclibs.R;
import com.basic.appbasiclibs.mycustom.MaterialProgressBar;

public class Alert_Helper
{
    Context mContext;

    ProgressDialog pDialog;
    Dialog dialog1;

    public Alert_Helper(Context mContext)
    {
        this.mContext=mContext;
    }

    public void Show_Alert_Dialog(String msg)
    {
        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);

        ad.setTitle("Info");
        ad.setIcon(android.R.drawable.ic_dialog_info);
        ad.setPositiveButton("Close", null);
        ad.setMessage(msg);
        ad.show();
    }

    public void alert(String msg)
    {
        Toast.makeText(mContext,msg, Toast.LENGTH_LONG).show();
    }

    public void customAlert(String msg)
    {
        // Get the custom layout view.
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View toastView = layoutInflater.inflate(R.layout.activity_toast_custom_view, null);
        TextView customToastText=toastView.findViewById(R.id.customToastText);
        customToastText.setText(msg);

        // Initiate the Toast instance.
        Toast toast = new Toast(mContext);
        // Set custom view in toast.
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0,40);
        toast.show();
    }


    public void Show_Progress_Dialog(String msg)
    {
        pDialog = new ProgressDialog(mContext,0);
        pDialog.setMessage(msg);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void Close_Progress_Dialog()
    {
        if(pDialog!=null)
        pDialog.dismiss();
    }

    public void Show_Custom_Progress_Dialog()
    {
        dialog1 = new Dialog(mContext, R.style.AppDialogTheme);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.custom_progress_dialog);
        dialog1.setCancelable(false);

        int width = mContext.getResources().getDisplayMetrics().widthPixels-80;
        int height = mContext.getResources().getDisplayMetrics().heightPixels-100;

        dialog1.getWindow().setLayout(width, height);

        MaterialProgressBar progress_wheel;
        progress_wheel = dialog1.findViewById(R.id.progress_wheel);
        //progress_wheel.setBarColor(mContext.getResources().getColor(R.color.colorPrimary));
        progress_wheel.setBarColor(Color.parseColor("#3D6B70"));

        dialog1.show();
    }

    public void Close_Custom_Progress_Dialog()
    {
        dialog1.dismiss();
    }

    public boolean is_show_Custom_Progress_Dialog()
    {
        if(dialog1!=null)
            return dialog1.isShowing();

        return false;
    }

    public void Show_Error_Dialog(String msg)
    {
        if(Constant.DEVELOPER_MODE)
            Show_Alert_Dialog(msg);
    }
}
