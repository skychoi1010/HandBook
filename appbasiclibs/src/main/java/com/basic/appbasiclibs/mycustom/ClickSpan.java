package com.basic.appbasiclibs.mycustom;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class ClickSpan extends ClickableSpan {

    private OnClickListener mListener;

    private boolean isUnderline = false;

    int text_color=Color.BLUE;

    public ClickSpan(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View widget) {
       if (mListener != null) mListener.onClick();
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setTextColor(int color)
    {
        text_color=color;
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        //super.updateDrawState(ds);
        ds.setUnderlineText(isUnderline);
        ds.bgColor = (Color.parseColor("#FAFAFA"));
        //ds.setARGB(255,255,255,255);
        //ds.setColor(Color.parseColor("#014E76"));
        ds.setColor(text_color);
    }
}