package com.waterdiary.drinkreminder;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;
import com.waterdiary.drinkreminder.custom.DigitsInputFilter;
import com.waterdiary.drinkreminder.custom.InputFilterRange;
import com.waterdiary.drinkreminder.custom.InputFilterWeightRange;
import com.waterdiary.drinkreminder.mywidgets.NewAppWidget;
import com.waterdiary.drinkreminder.utils.FileUtils;
import com.waterdiary.drinkreminder.utils.HeightWeightHelper;
import com.waterdiary.drinkreminder.utils.URLFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Screen_Profile extends MasterBaseAppCompatActivity
{
    LinearLayout right_icon_block,left_icon_block;
    AppCompatTextView lbl_toolbar_title;

    //=================

    ImageView img_user;
    AppCompatTextView txt_user_name,txt_gender,txt_height,txt_weight,txt_goal;
    AppCompatTextView lbl_gender,lbl_height,lbl_weight,lbl_goal;

    AppCompatTextView lbl_active,lbl_pregnant,lbl_breastfeeding;
    SwitchCompat switch_active,switch_pregnant,switch_breastfeeding;


    PopupWindow mDropdown=null;

    boolean isExecute=true,isExecuteSeekbar=true;

    List<String> weight_kg_lst=new ArrayList<>();
    List<String> weight_lb_lst=new ArrayList<>();

    List<String> height_cm_lst=new ArrayList<>();
    List<String> height_feet_lst=new ArrayList<>();

    RadioButton rdo_cm,rdo_feet;
    RadioButton rdo_kg,rdo_lb;

    List<Double> height_feet_elements=new ArrayList<>();


    LinearLayout gender_block,height_block,weight_block,goal_block;

    LinearLayout other_factors;


    LinearLayout weather_block;
    AppCompatTextView txt_weather;
    AppCompatTextView lbl_weather;

    PopupWindow mDropdownWeather=null;

    View active_line;
    AppCompatTextView lbl_other_factor;

    LinearLayout edit_user_name_block;

    RelativeLayout change_profile;


    private static final int STORAGE_PERMISSION = 3;
    private static final int PICK_Camera_IMAGE = 2;
    private static final int SELECT_FILE1 = 1;
    Uri imageUri,selectedImage;
    private String selectedImagePath;

    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_profile);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.water_color));
        }

        URLFactory.DAILY_WATER_VALUE=ph.getFloat(URLFactory.DAILY_WATER);

        FindViewById();
        Header();
        Body();

        init_WeightKG();
        init_WeightLB();
        init_HeightCM();
        init_HeightFeet();

        loadHeightData();
    }

    public void loadHeightData()
    {
        height_feet_elements.clear();

        height_feet_elements.add(2.0);
        height_feet_elements.add(2.1);
        height_feet_elements.add(2.2);
        height_feet_elements.add(2.3);
        height_feet_elements.add(2.4);
        height_feet_elements.add(2.5);
        height_feet_elements.add(2.6);
        height_feet_elements.add(2.7);
        height_feet_elements.add(2.8);
        height_feet_elements.add(2.9);
        height_feet_elements.add(2.10);
        height_feet_elements.add(2.11);
        height_feet_elements.add(3.0);
        height_feet_elements.add(3.1);
        height_feet_elements.add(3.2);
        height_feet_elements.add(3.3);
        height_feet_elements.add(3.4);
        height_feet_elements.add(3.5);
        height_feet_elements.add(3.6);
        height_feet_elements.add(3.7);
        height_feet_elements.add(3.8);
        height_feet_elements.add(3.9);
        height_feet_elements.add(3.10);
        height_feet_elements.add(3.11);
        height_feet_elements.add(4.0);
        height_feet_elements.add(4.1);
        height_feet_elements.add(4.2);
        height_feet_elements.add(4.3);
        height_feet_elements.add(4.4);
        height_feet_elements.add(4.5);
        height_feet_elements.add(4.6);
        height_feet_elements.add(4.7);
        height_feet_elements.add(4.8);
        height_feet_elements.add(4.9);
        height_feet_elements.add(4.10);
        height_feet_elements.add(4.11);
        height_feet_elements.add(5.0);
        height_feet_elements.add(5.1);
        height_feet_elements.add(5.2);
        height_feet_elements.add(5.3);
        height_feet_elements.add(5.4);
        height_feet_elements.add(5.5);
        height_feet_elements.add(5.6);
        height_feet_elements.add(5.7);
        height_feet_elements.add(5.8);
        height_feet_elements.add(5.9);
        height_feet_elements.add(5.10);
        height_feet_elements.add(5.11);
        height_feet_elements.add(6.0);
        height_feet_elements.add(6.1);
        height_feet_elements.add(6.2);
        height_feet_elements.add(6.3);
        height_feet_elements.add(6.4);
        height_feet_elements.add(6.5);
        height_feet_elements.add(6.6);
        height_feet_elements.add(6.7);
        height_feet_elements.add(6.8);
        height_feet_elements.add(6.9);
        height_feet_elements.add(6.10);
        height_feet_elements.add(6.11);
        height_feet_elements.add(7.0);
        height_feet_elements.add(7.1);
        height_feet_elements.add(7.2);
        height_feet_elements.add(7.3);
        height_feet_elements.add(7.4);
        height_feet_elements.add(7.5);
        height_feet_elements.add(7.6);
        height_feet_elements.add(7.7);
        height_feet_elements.add(7.8);
        height_feet_elements.add(7.9);
        height_feet_elements.add(7.10);
        height_feet_elements.add(7.11);
        height_feet_elements.add(8.0);
    }

    public  void FindViewById()
    {
        right_icon_block=findViewById(R.id.right_icon_block);
        left_icon_block=findViewById(R.id.left_icon_block);
        lbl_toolbar_title=findViewById(R.id.lbl_toolbar_title);

        txt_weight=findViewById(R.id.txt_weight);

        img_user=findViewById(R.id.img_user);

        txt_user_name=findViewById(R.id.txt_user_name);

        edit_user_name_block=findViewById(R.id.edit_user_name_block);

        change_profile=findViewById(R.id.change_profile);

        //===================

        gender_block=findViewById(R.id.gender_block);
        height_block=findViewById(R.id.height_block);
        weight_block=findViewById(R.id.weight_block);
        goal_block=findViewById(R.id.goal_block);

        active_line=findViewById(R.id.active_line);

        lbl_active=findViewById(R.id.lbl_active);
        lbl_pregnant=findViewById(R.id.lbl_pregnant);
        lbl_breastfeeding=findViewById(R.id.lbl_breastfeeding);
        switch_active=findViewById(R.id.switch_active);
        switch_pregnant=findViewById(R.id.switch_pregnant);
        switch_breastfeeding=findViewById(R.id.switch_breastfeeding);

        other_factors=findViewById(R.id.other_factors);

        lbl_other_factor=findViewById(R.id.lbl_other_factor);

        //===================

        weather_block=findViewById(R.id.weather_block);
        txt_weather=findViewById(R.id.txt_weather);
        lbl_weather=findViewById(R.id.lbl_weather);








        lbl_gender=findViewById(R.id.lbl_gender);
        txt_gender=findViewById(R.id.txt_gender);

        lbl_height=findViewById(R.id.lbl_height);
        txt_height=findViewById(R.id.txt_height);

        lbl_weight=findViewById(R.id.lbl_weight);
        txt_weight=findViewById(R.id.txt_weight);

        txt_goal=findViewById(R.id.txt_goal);
        lbl_goal=findViewById(R.id.lbl_goal);

        convertUpperCase(lbl_gender);
        convertUpperCase(lbl_weight);
        convertUpperCase(lbl_height);
        convertUpperCase(lbl_goal);
        convertUpperCase(lbl_active);
        convertUpperCase(lbl_pregnant);
        convertUpperCase(lbl_breastfeeding);
        convertUpperCase(lbl_weather);
        convertUpperCase(lbl_other_factor);

        //ah.Show_Alert_Dialog(""+ph.getBoolean(URLFactory.USER_GENDER));

        txt_user_name.setText(ph.getString(URLFactory.USER_NAME));
        txt_gender.setText(ph.getBoolean(URLFactory.USER_GENDER)?sh.get_string(R.string.str_female):sh.get_string(R.string.str_male));

        //img_user.setImageResource(ph.getBoolean(URLFactory.USER_GENDER)?R.drawable.ic_female_normal:R.drawable.ic_male_normal);

        loadPhoto();


        //ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT)
        String str=ph.getString(URLFactory.PERSON_HEIGHT) + " " +
                (ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT)?"cm":"feet");
        txt_height.setText(str);

        String str2=(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?URLFactory.decimalFormat2.format(Double.parseDouble(ph.getString(URLFactory.PERSON_WEIGHT)))+" kg":ph.getString(URLFactory.PERSON_WEIGHT)+" lb");
        txt_weight.setText(str2);

        String str3=getData("" + (int)URLFactory.DAILY_WATER_VALUE) +" "
                +(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");
        txt_goal.setText(str3);
    }

    public void loadPhoto()
    {
        if(sh.check_blank_data(ph.getString(URLFactory.USER_PHOTO))) {
            Glide.with(act).load(ph.getBoolean(URLFactory.USER_GENDER) ? R.drawable.female_white
                    : R.drawable.male_white).apply(RequestOptions.circleCropTransform())
                    .into(img_user);
        }
        else
        {
            boolean ex=false;

            try
            {
                File f=new File(ph.getString(URLFactory.USER_PHOTO));
                if(f.exists())
                    ex=true;
            }
            catch (Exception e){}

            if(ex) {
                Glide.with(act).load(ph.getString(URLFactory.USER_PHOTO)).apply(RequestOptions.circleCropTransform())
                        .into(img_user);
            }
            else
            {
                Glide.with(act).load(ph.getBoolean(URLFactory.USER_GENDER) ? R.drawable.female_white
                        : R.drawable.male_white).apply(RequestOptions.circleCropTransform())
                        .into(img_user);
            }
        }
    }

    public String getData(String str)
    {
        return  str.replace(",",".");
    }

    public void convertUpperCase(AppCompatTextView appCompatTextView)
    {
        appCompatTextView.setText(appCompatTextView.getText().toString().toUpperCase());
    }

    public void Header()
    {
        lbl_toolbar_title.setText(sh.get_string(R.string.str_my_profile));

        left_icon_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        right_icon_block.setVisibility(View.GONE);
    }

    public void Body()
    {
        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkStoragePermissions();
                } else {
                    openPicker();
                }
            }
        });

        gender_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showGenderMenu(v);
                initiatePopupWindow(v);
            }
        });

        edit_user_name_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNameDialog();
            }
        });


        goal_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetManuallyGoalDialog();
            }
        });

        height_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHeightDialog();
            }
        });

        weight_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeightDialog();
            }
        });

        switch_active.setChecked(ph.getBoolean(URLFactory.IS_ACTIVE));

        switch_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ph.savePreferences(URLFactory.IS_ACTIVE,isChecked);

                String tmp_weight=""+ph.getString(URLFactory.PERSON_WEIGHT);
                boolean isFemale=ph.getBoolean(URLFactory.USER_GENDER);
                float min=ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?900:30;
                float max=ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?8000:270;
                int weatherIdx=ph.getInt(URLFactory.WEATHER_CONSITIONS);

                Log.d("maxmaxmaxmax : ",""+max+" @@@ "+min+"  @@@  "+tmp_weight);

                double tmp_kg = 0;
                if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
                {
                    tmp_kg = Double.parseDouble("" + tmp_weight);
                }
                else
                {
                    tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
                }

                Log.d("maxmaxmaxmax : ",""+tmp_kg);

                double diff=0;

                if(isFemale)
                    diff=tmp_kg*URLFactory.DEACTIVE_FEMALE_WATER;
                else
                    diff=tmp_kg*URLFactory.DEACTIVE_MALE_WATER;

                Log.d("maxmaxmaxmax DIFF : ",""+diff);

                if(weatherIdx==1)
                    diff*=URLFactory.WEATHER_CLOUDY;
                else if(weatherIdx==2)
                    diff*=URLFactory.WEATHER_RAINY;
                else if(weatherIdx==3)
                    diff*=URLFactory.WEATHER_SNOW;
                else
                    diff*=URLFactory.WEATHER_SUNNY;


                Log.d("maxmaxmaxmax : ",""+diff+" @@@ "+URLFactory.DAILY_WATER_VALUE);

                if(isChecked)
                {
                    if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
                    {
                        URLFactory.DAILY_WATER_VALUE+=diff;
                    }
                    else
                    {
                        URLFactory.DAILY_WATER_VALUE+=HeightWeightHelper.mlToOzConverter(diff);
                    }

                    if(URLFactory.DAILY_WATER_VALUE>max)
                        URLFactory.DAILY_WATER_VALUE=max;
                }
                else
                {
                    if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
                    {
                        URLFactory.DAILY_WATER_VALUE-=diff;
                    }
                    else
                    {
                        URLFactory.DAILY_WATER_VALUE-=HeightWeightHelper.mlToOzConverter(diff);
                    }

                    if(URLFactory.DAILY_WATER_VALUE>max)
                        URLFactory.DAILY_WATER_VALUE=max;
                }

                URLFactory.DAILY_WATER_VALUE=Math.round(URLFactory.DAILY_WATER_VALUE);

                String str=getData("" + (int)URLFactory.DAILY_WATER_VALUE)+" "+
                        (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");

                txt_goal.setText(str);

                ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);

            }
        });

        switch_breastfeeding.setChecked(ph.getBoolean(URLFactory.IS_BREATFEEDING));

        switch_breastfeeding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ph.savePreferences(URLFactory.IS_BREATFEEDING,isChecked);

                setSwitchData(isChecked,URLFactory.BREASTFEEDING_WATER);
            }
        });

        switch_pregnant.setChecked(ph.getBoolean(URLFactory.IS_PREGNANT));

        switch_pregnant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ph.savePreferences(URLFactory.IS_PREGNANT,isChecked);

                setSwitchData(isChecked,URLFactory.PREGNANT_WATER);
            }
        });


        other_factors.setVisibility(ph.getBoolean(URLFactory.USER_GENDER)?View.VISIBLE:View.GONE);

        String str="";
        if(ph.getInt(URLFactory.WEATHER_CONSITIONS)==1)
            str=sh.get_string(R.string.cloudy);
        else if(ph.getInt(URLFactory.WEATHER_CONSITIONS)==2)
            str=sh.get_string(R.string.rainy);
        else if(ph.getInt(URLFactory.WEATHER_CONSITIONS)==3)
            str=sh.get_string(R.string.snow);
        else
            str=sh.get_string(R.string.sunny);
        txt_weather.setText(str);

        weather_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateWeatherPopupWindow(switch_active);
            }
        });



        calculateActiveValue();
    }

    public void openPicker()
    {


        bottomSheetDialog=new BottomSheetDialog(act);

        LayoutInflater layoutInflater=LayoutInflater.from(act);
        View view = layoutInflater.inflate(R.layout.bottom_sheet_pick_image, null, false);

        AppCompatTextView btnGallery=view.findViewById(R.id.btnGallery);
        AppCompatTextView btnCamera=view.findViewById(R.id.btnCamera);
        AppCompatTextView btnCancel=view.findViewById(R.id.btnCancel);
        AppCompatTextView btnRemove=view.findViewById(R.id.btnRemove);

        View btnRemoveLine=view.findViewById(R.id.btnRemoveLine);

        if(sh.check_blank_data(ph.getString(URLFactory.USER_PHOTO))) {
            btnRemove.setVisibility(View.GONE);
            btnRemoveLine.setVisibility(View.GONE);
        }
        else {
            btnRemove.setVisibility(View.VISIBLE);
            btnRemoveLine.setVisibility(View.VISIBLE);
        }

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //if(isclicked) {
                selectImage();
                //isclicked=false;
                //}
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //if(isclicked) {
                captureImage();
                //isclicked=false;
                //}
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder dialog=  new  AlertDialog.Builder(act)
                        .setMessage(sh.get_string(R.string.str_remove_photo_confirmation_message))
                        .setPositiveButton(sh.get_string(R.string.str_yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();

                                        ph.savePreferences(URLFactory.USER_PHOTO,"");
                                        loadPhoto();
                                    }
                                }
                        )
                        .setNegativeButton(sh.get_string(R.string.str_no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();

                                    }
                                }
                        );

                dialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });




        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
    }

    public void setSwitchData(boolean isChecked,double water)
    {
        double diff=0;
        float min=ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?900:30;
        float max=ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?8000:270;

        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
            diff=water;
        else
            diff=HeightWeightHelper.mlToOzConverter(water);


        if(isChecked)
        {
            URLFactory.DAILY_WATER_VALUE+=diff;

            if(URLFactory.DAILY_WATER_VALUE>max)
                URLFactory.DAILY_WATER_VALUE=max;
        }
        else
        {
            URLFactory.DAILY_WATER_VALUE-=diff;

            if(URLFactory.DAILY_WATER_VALUE<min)
                URLFactory.DAILY_WATER_VALUE=min;
        }


        URLFactory.DAILY_WATER_VALUE=Math.round(URLFactory.DAILY_WATER_VALUE);

        String str=getData("" + (int)URLFactory.DAILY_WATER_VALUE)+" "+
                (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");

        txt_goal.setText(str);

        ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);

        calculateActiveValue();
    }


    public void showGenderMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                switch (menuItem.getItemId()) {
                    case R.id.male_item:
                        // do your code

                        ph.savePreferences(URLFactory.USER_GENDER,false);
                        //img_user.setImageResource(R.drawable.ic_male_normal);

                        loadPhoto();


                        return true;
                    case R.id.female_item:
                        // do your code

                        ph.savePreferences(URLFactory.USER_GENDER,true);
                        //img_user.setImageResource(R.drawable.ic_female_normal);

                        loadPhoto();

                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.gender_menu);
        popup.show();
    }

    private PopupWindow initiatePopupWindow(View v) {

        try {

            LayoutInflater mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.row_item_gender, null);

            //If you want to add any listeners to your textviews, these are two //textviews.
            final TextView lbl_male = layout.findViewById(R.id.lbl_male);

            lbl_male.setText(sh.get_string(R.string.str_male));


            final TextView lbl_female = layout.findViewById(R.id.lbl_female);

            lbl_female.setText(sh.get_string(R.string.str_female));

            lbl_male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ph.savePreferences(URLFactory.USER_GENDER,false);
                    //img_user.setImageResource(R.drawable.ic_male_normal);
                    loadPhoto();
                    mDropdown.dismiss();
                    txt_gender.setText(sh.get_string(R.string.str_male));
                    other_factors.setVisibility(View.GONE);
                    switch_breastfeeding.setChecked(false);
                    switch_pregnant.setChecked(false);

                    calculate_goal();
                }
            });

            lbl_female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ph.savePreferences(URLFactory.USER_GENDER,true);
                    //img_user.setImageResource(R.drawable.ic_female_normal);
                    loadPhoto();
                    mDropdown.dismiss();
                    txt_gender.setText(sh.get_string(R.string.str_female));
                    other_factors.setVisibility(View.VISIBLE);

                    calculate_goal();
                }
            });


            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout,FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);
            //Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(v, 5, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }

    private PopupWindow initiateWeatherPopupWindow(View v) {

        try {

            LayoutInflater mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.row_item_weather, null);

            //If you want to add any listeners to your textviews, these are two //textviews.
            final TextView lbl_sunny = layout.findViewById(R.id.lbl_sunny);
            lbl_sunny.setText(sh.get_string(R.string.sunny));

            final TextView lbl_cloudy = layout.findViewById(R.id.lbl_cloudy);
            lbl_cloudy.setText(sh.get_string(R.string.cloudy));

            final TextView lbl_rainy = layout.findViewById(R.id.lbl_rainy);
            lbl_rainy.setText(sh.get_string(R.string.rainy));

            final TextView lbl_snow = layout.findViewById(R.id.lbl_snow);
            lbl_snow.setText(sh.get_string(R.string.snow));

            lbl_sunny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ph.savePreferences(URLFactory.WEATHER_CONSITIONS,0);
                    mDropdownWeather.dismiss();
                    txt_weather.setText(sh.get_string(R.string.sunny));

                    calculate_goal();
                }
            });

            lbl_cloudy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ph.savePreferences(URLFactory.WEATHER_CONSITIONS,1);
                    mDropdownWeather.dismiss();
                    txt_weather.setText(sh.get_string(R.string.cloudy));

                    calculate_goal();
                }
            });

            lbl_rainy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ph.savePreferences(URLFactory.WEATHER_CONSITIONS,2);
                    mDropdownWeather.dismiss();
                    txt_weather.setText(sh.get_string(R.string.rainy));

                    calculate_goal();
                }
            });

            lbl_snow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ph.savePreferences(URLFactory.WEATHER_CONSITIONS,3);
                    mDropdownWeather.dismiss();
                    txt_weather.setText(sh.get_string(R.string.snow));

                    calculate_goal();
                }
            });


            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdownWeather = new PopupWindow(layout,FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);
            //Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            //mDropdown.setBackgroundDrawable(background);

            mDropdownWeather.showAsDropDown(v, 5, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdownWeather;

    }

    public void openNameDialog()
    {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final View view = LayoutInflater.from(act).inflate(R.layout.dialog_user_name, null, false);

        RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_add=view.findViewById(R.id.btn_add);
        ImageView img_cancel=view.findViewById(R.id.img_cancel);

        final AppCompatEditText txt_name=view.findViewById(R.id.txt_pass);

        txt_name.requestFocus();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        txt_name.setText(ph.getString(URLFactory.USER_NAME));
        txt_name.setSelection(txt_name.getText().toString().trim().length());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sh.check_blank_data(txt_name.getText().toString().trim())) {
                    ah.customAlert(sh.get_string(R.string.str_your_name_validation));

                }
                else if(txt_name.getText().toString().trim().length()<3) {
                    ah.customAlert(sh.get_string(R.string.str_valid_name_validation));
                }
                else
                {
                    ph.savePreferences(URLFactory.USER_NAME,txt_name.getText().toString().trim());

                    txt_user_name.setText(ph.getString(URLFactory.USER_NAME));

                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(view);

        dialog.show();
    }


    public void showSetManuallyGoalDialog()
    {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final View view = LayoutInflater.from(act).inflate(R.layout.dialog_set_manually_goal, null, false);


        final AppCompatEditText lbl_goal2=view.findViewById(R.id.lbl_goal);
        AppCompatTextView lbl_unit2=view.findViewById(R.id.lbl_unit);
        RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_save=view.findViewById(R.id.btn_save);
        final SeekBar seekbarGoal=view.findViewById(R.id.seekbarGoal);


		/*if(ph.getBoolean(URLFactory.SET_MANUALLY_GOAL))
			lbl_goal2.setText( getData(URLFactory.decimalFormat.format(ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE))));
		else
			lbl_goal2.setText( getData(URLFactory.decimalFormat.format(ph.getFloat(URLFactory.DAILY_WATER))));*/

        if(ph.getBoolean(URLFactory.SET_MANUALLY_GOAL))
            lbl_goal2.setText( getData(""+(int)(ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE))));
        else
            lbl_goal2.setText( getData(""+(int)(ph.getFloat(URLFactory.DAILY_WATER))));

        lbl_unit2.setText(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");



        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbarGoal.setMin(900);
            }
            seekbarGoal.setMax(8000);
            lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(0,8000),new InputFilter.LengthFilter(4)});
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekbarGoal.setMin(30);
            }
            seekbarGoal.setMax(270);
            lbl_goal2.setFilters(new InputFilter[]{new InputFilterWeightRange(0,270),new InputFilter.LengthFilter(3)});
        }

        int f= ph.getBoolean(URLFactory.SET_MANUALLY_GOAL)?(int) ph.getFloat(URLFactory.SET_MANUALLY_GOAL_VALUE):(int) ph.getFloat(URLFactory.DAILY_WATER);
        seekbarGoal.setProgress(f);

        lbl_goal2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isExecuteSeekbar=false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try
                {
                    if(!sh.check_blank_data(lbl_goal2.getText().toString().trim()) && isExecute)
                    {
                        int data = Integer.parseInt(lbl_goal2.getText().toString().trim());
                        seekbarGoal.setProgress(data);
                    }

                }
                catch (Exception e){}

                isExecuteSeekbar=true;
            }
        });

        seekbarGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBars, int progress, boolean fromUser) {
                if(isExecuteSeekbar)
                {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                    {
                        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
                        {
                            progress=progress<900?900:progress;
                        }
                        else
                        {
                            progress=progress<30?30:progress;
                        }
                        seekbarGoal.setProgress(progress);
                    }

                    lbl_goal2.setText("" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isExecute=false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isExecute=true;
            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String unit=ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz";

                if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && Float.parseFloat(lbl_goal2.getText().toString().trim())>=900)
                {
                    URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
                    ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
                    txt_goal.setText(getData("" + (int) URLFactory.DAILY_WATER_VALUE)+" "+unit);
                    ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
                    ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
                    dialog.dismiss();

                    refreshWidget();
                }
                else
                {
                    if(!ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT) && Float.parseFloat(lbl_goal2.getText().toString().trim())>=30)
                    {
                        URLFactory.DAILY_WATER_VALUE = Float.parseFloat(lbl_goal2.getText().toString().trim());
                        ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
                        txt_goal.setText(getData("" + (int) URLFactory.DAILY_WATER_VALUE)+" "+unit);
                        ph.savePreferences(URLFactory.SET_MANUALLY_GOAL, true);
                        ph.savePreferences(URLFactory.SET_MANUALLY_GOAL_VALUE, URLFactory.DAILY_WATER_VALUE);
                        dialog.dismiss();

                        refreshWidget();
                    }
                    else {
                        ah.customAlert(sh.get_string(R.string.str_set_daily_goal_validation));
                    }
                }
            }
        });

        dialog.setContentView(view);

        dialog.show();
    }


    public void openHeightDialog()
    {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final View view = LayoutInflater.from(act).inflate(R.layout.dialog_height, null, false);

        RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_add=view.findViewById(R.id.btn_add);
        ImageView img_cancel=view.findViewById(R.id.img_cancel);
        final AppCompatEditText txt_name=view.findViewById(R.id.txt_pass);


        rdo_cm=view.findViewById(R.id.rdo_cm);
        rdo_feet=view.findViewById(R.id.rdo_feet);

        txt_name.requestFocus();




        rdo_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sh.check_blank_data(txt_name.getText().toString()))
                {
                    int final_height_cm=61;

                    try
                    {
                        String tmp_height=getData(txt_name.getText().toString().trim());

                        int d= (int) (Float.parseFloat(txt_name.getText().toString().trim()));

                        Log.d("after_decimal", "" + tmp_height.indexOf("."));

                        if(tmp_height.indexOf(".")>0)
                        {
                            String after_decimal = tmp_height.substring(tmp_height.indexOf(".") + 1);

                            if (!sh.check_blank_data(after_decimal))
                            {
                                int after_decimal_int = Integer.parseInt(after_decimal);

                                double final_height = (d * 12) + after_decimal_int;

                                final_height_cm = (int) Math.round(final_height * 2.54);

								/*ah.Show_Alert_Dialog(d + " @@@  " + tmp_height + " @@@  "
										+ after_decimal + " @@@  " + final_height + "  @@@@ "
										+ final_height_cm);*/

                            }
                            else
                            {
                                final_height_cm = (int) Math.round(d * 12 * 2.54);

                                //ah.Show_Alert_Dialog(""+final_height_cm);
                            }
                        }
                        else
                        {
                            final_height_cm = (int) Math.round(d * 12 * 2.54);

                            //ah.Show_Alert_Dialog(""+final_height_cm);
                        }
                    }
                    catch (Exception e){}






					rdo_feet.setClickable(true);
                    rdo_cm.setClickable(false);
                    txt_name.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,240)});
                    txt_name.setText(getData(""+final_height_cm));
                    txt_name.setSelection(txt_name.length());


                    //saveData();
                }
                else
                {
                    rdo_feet.setChecked(true);
                    rdo_cm.setChecked(false);
                }

            }
        });

        rdo_feet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sh.check_blank_data(txt_name.getText().toString()))
                {
                    String final_height_feet="5.0";

                    try
                    {
                        int d= (int) (Float.parseFloat(txt_name.getText().toString().trim()));

                        int tmp_height_inch = (int) Math.round(d / 2.54);

                        int first=tmp_height_inch/12;
                        int second=tmp_height_inch%12;

                        //ah.Show_Alert_Dialog(""+final_height_feet+" @@@  "+first+" @@@ "+second);

                        final_height_feet=first+"."+second;
                    }
                    catch (Exception e){}

                    rdo_feet.setClickable(false);
                    rdo_cm.setClickable(true);
                    txt_name.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
                    txt_name.setText(getData(final_height_feet));
                    txt_name.setSelection(txt_name.length());

                    //saveData();
                }
                else
                {
                    rdo_feet.setChecked(false);
                    rdo_cm.setChecked(true);
                }
            }
        });

        if(ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT)) {
            rdo_cm.setChecked(true);
            rdo_cm.setClickable(false);
            rdo_feet.setClickable(true);
        }
        else {
            rdo_feet.setChecked(true);
            rdo_cm.setClickable(true);
            rdo_feet.setClickable(false);
        }

        if(!sh.check_blank_data(ph.getString(URLFactory.PERSON_HEIGHT))) {
            if(rdo_cm.isChecked())
            {
                txt_name.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,240)});
                txt_name.setText(getData(ph.getString(URLFactory.PERSON_HEIGHT)));
            }
            else {
                txt_name.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
                txt_name.setText(getData(ph.getString(URLFactory.PERSON_HEIGHT)));
            }
        }
        else
        {
            if(rdo_cm.isChecked())
            {
                txt_name.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,240)});
                txt_name.setText("150");
            }
            else {
                txt_name.setFilters(new InputFilter[]{new InputFilterRange(0.00,height_feet_elements)});
                txt_name.setText("5.0");
            }
        }



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        txt_name.setSelection(txt_name.getText().toString().length());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sh.check_blank_data(txt_name.getText().toString().trim())) {
                    ah.customAlert(sh.get_string(R.string.str_height_validation));
                }
                else
                {
                    String str=txt_name.getText().toString().trim();

                    if(rdo_feet.isChecked())
                    {
                        if(!str.contains(".11") && !str.contains(".10"))
                        str=URLFactory.decimalFormat2.format(Double.parseDouble(str));
                    }

                    str+=" "+(rdo_feet.isChecked()?"feet":"cm");

                    txt_height.setText(str);

                    saveData(txt_name);

                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(view);

        dialog.show();
    }


    public void openWeightDialog()
    {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.drawable_background_tra);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        final View view = LayoutInflater.from(act).inflate(R.layout.dialog_weight, null, false);

        RelativeLayout btn_cancel=view.findViewById(R.id.btn_cancel);
        RelativeLayout btn_add=view.findViewById(R.id.btn_add);
        ImageView img_cancel=view.findViewById(R.id.img_cancel);
        final AppCompatEditText txt_name=view.findViewById(R.id.txt_pass);


        rdo_kg=view.findViewById(R.id.rdo_kg);
        rdo_lb=view.findViewById(R.id.rdo_lb);

        txt_name.requestFocus();



        rdo_kg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(!sh.check_blank_data(txt_name.getText().toString()))
                {
                    double weight_in_lb=Double.parseDouble(txt_name.getText().toString());

                    double weight_in_kg=0.0;

                    if(weight_in_lb>0)
                        weight_in_kg=Math.round(HeightWeightHelper.lbToKgConverter(weight_in_lb));

                    int tmp= (int) weight_in_kg;

                    txt_name.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});
                    txt_name.setText(getData(""+URLFactory.decimalFormat2.format(tmp)));
                    rdo_kg.setClickable(false);
                    rdo_lb.setClickable(true);
                }

                //saveWeightData(txt_name);
            }
        });

        rdo_lb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!sh.check_blank_data(txt_name.getText().toString()))
                {
                    double weight_in_kg = Double.parseDouble(txt_name.getText().toString());

                    double weight_in_lb=0.0;

                    if(weight_in_kg>0)
                        weight_in_lb = Math.round(HeightWeightHelper.kgToLbConverter(weight_in_kg));

                    int tmp= (int) weight_in_lb;

                    txt_name.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,287)});
                    txt_name.setText(getData(""+tmp));
                    rdo_kg.setClickable(true);
                    rdo_lb.setClickable(false);
                }

                //saveWeightData(txt_name);
            }
        });

        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            rdo_kg.setChecked(true);
            rdo_kg.setClickable(false);
            rdo_lb.setClickable(true);
        }
        else {
            rdo_lb.setChecked(true);
            rdo_kg.setClickable(true);
            rdo_lb.setClickable(false);
        }

        if(!sh.check_blank_data(ph.getString(URLFactory.PERSON_WEIGHT))) {
            if(rdo_kg.isChecked()) {

                //ah.customAlert("if if");

                /*int sel_pos=0;
                for(int k=0;k<weight_kg_lst.size();k++)
                {
                    if(Float.parseFloat(weight_kg_lst.get(k))==Float.parseFloat(""+getData(ph.getString(URLFactory.PERSON_WEIGHT))))
                        sel_pos=k;
                }

                pickerKG.setSelectedItem(sel_pos);*/

                txt_name.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});
                txt_name.setText(getData(ph.getString(URLFactory.PERSON_WEIGHT)));
            }
            else
            {
                //ah.customAlert("if else");
                txt_name.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,287)});
                txt_name.setText(getData(ph.getString(URLFactory.PERSON_WEIGHT)));
            }

        }
        else
        {
            if(rdo_kg.isChecked()) {
                //ah.customAlert("else if");
                txt_name.setFilters(new InputFilter[]{new InputFilterWeightRange(0,130)});
                txt_name.setText("80.0");
            }
            else
            {
                //ah.customAlert("else else");
                txt_name.setFilters(new InputFilter[] {new DigitsInputFilter(3,0,287)});
                txt_name.setText("176");
            }
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        txt_name.setSelection(txt_name.getText().toString().length());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sh.check_blank_data(txt_name.getText().toString().trim())) {
                    ah.customAlert(sh.get_string(R.string.str_weight_validation));
                }
                else
                {
                    String str=txt_name.getText().toString().trim();

                    if(rdo_kg.isChecked())
                    {
                        str=URLFactory.decimalFormat2.format(Double.parseDouble(str));
                    }


                    str+=" "+(rdo_kg.isChecked()?"kg":"lb");

                    txt_weight.setText(str);

                    saveWeightData(txt_name);

                    calculate_goal();

                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(view);

        dialog.show();
    }

    public void calculate_goal_old()
    {
        String tmp_weight=""+ph.getString(URLFactory.PERSON_WEIGHT);
        if(!sh.check_blank_data(tmp_weight))
        {
            double tmp_lbs = 0;
            if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
            {
                tmp_lbs = HeightWeightHelper.kgToLbConverter(Double.parseDouble(tmp_weight));
            }
            else
            {
                tmp_lbs = Double.parseDouble("" + tmp_weight);
            }

            double tmp_oz = tmp_lbs * 0.5;

            double tmp_ml = HeightWeightHelper.ozToMlConverter(tmp_oz);

            //double rounded_up = 100 * Math.ceil(tmp_ml / 100);

            if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
            {
                URLFactory.DAILY_WATER_VALUE = (float) tmp_ml;
            }
            else
            {
                URLFactory.DAILY_WATER_VALUE = (float) tmp_oz;
            }

            URLFactory.DAILY_WATER_VALUE=Math.round(URLFactory.DAILY_WATER_VALUE);

            String str=getData("" + (int)URLFactory.DAILY_WATER_VALUE)+" "+
                    (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");

            txt_goal.setText(str);

            ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);
        }
    }


    public void calculate_goal()
    {
        String tmp_weight=""+ph.getString(URLFactory.PERSON_WEIGHT);

        boolean isFemale=ph.getBoolean(URLFactory.USER_GENDER);
        boolean isActive=ph.getBoolean(URLFactory.IS_ACTIVE);
        boolean isPregnant=ph.getBoolean(URLFactory.IS_PREGNANT);
        boolean isBreastfeeding=ph.getBoolean(URLFactory.IS_BREATFEEDING);
        int weatherIdx=ph.getInt(URLFactory.WEATHER_CONSITIONS);

        if(!sh.check_blank_data(tmp_weight))
        {
            double tot_drink=0;
            double tmp_kg = 0;
            if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
            {
                tmp_kg = Double.parseDouble("" + tmp_weight);

            }
            else
            {
                tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
            }

            Log.d("tot_drink 1 ",""+tot_drink);

            if(isFemale)
                tot_drink=isActive?tmp_kg*URLFactory.ACTIVE_FEMALE_WATER:tmp_kg*URLFactory.FEMALE_WATER;
            else
                tot_drink=isActive?tmp_kg*URLFactory.ACTIVE_MALE_WATER:tmp_kg*URLFactory.MALE_WATER;

            Log.d("tot_drink 2 ",""+tot_drink);

            if(weatherIdx==1)
                tot_drink*=URLFactory.WEATHER_CLOUDY;
            else if(weatherIdx==2)
                tot_drink*=URLFactory.WEATHER_RAINY;
            else if(weatherIdx==3)
                tot_drink*=URLFactory.WEATHER_SNOW;
            else
                tot_drink*=URLFactory.WEATHER_SUNNY;

            Log.d("tot_drink 3 ",""+tot_drink);

            if(isPregnant && isFemale)
            {
                tot_drink+=URLFactory.PREGNANT_WATER;
            }

            Log.d("tot_drink 4 ",""+tot_drink);

            if(isBreastfeeding && isFemale)
            {
                tot_drink+=URLFactory.BREASTFEEDING_WATER;
            }

            Log.d("tot_drink 5 ",""+tot_drink);

            if(tot_drink<900)
                tot_drink=900;

            if(tot_drink>8000)
                tot_drink=8000;

            Log.d("tot_drink 6 ",""+tot_drink);

            double tot_drink_fl_oz = HeightWeightHelper.mlToOzConverter(tot_drink);

            if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
            {
                //lbl_unit.setText("ml");
                URLFactory.DAILY_WATER_VALUE = (float) tot_drink;
            }
            else
            {
                //lbl_unit.setText("fl oz");
                URLFactory.DAILY_WATER_VALUE = (float) tot_drink_fl_oz;

            }

            URLFactory.DAILY_WATER_VALUE=Math.round(URLFactory.DAILY_WATER_VALUE);
            //txt_goal.setText(getData("" + (int)URLFactory.DAILY_WATER_VALUE));

            String str=getData("" + (int)URLFactory.DAILY_WATER_VALUE)+" "+
                    (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)?"ml":"fl oz");

            txt_goal.setText(str);

            ph.savePreferences(URLFactory.DAILY_WATER, URLFactory.DAILY_WATER_VALUE);

            refreshWidget();

            calculateActiveValue();





        }




    }


    public void calculateActiveValue()
    {
        String pstr="";

        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            pstr=(int)URLFactory.PREGNANT_WATER+" ml";
        }
        else
        {
            pstr=(int)Math.round(HeightWeightHelper.mlToOzConverter(URLFactory.PREGNANT_WATER))+" fl oz";
        }

        lbl_pregnant.setText(sh.get_string(R.string.pregnant));
        convertUpperCase(lbl_pregnant);
        lbl_pregnant.setText(lbl_pregnant.getText().toString()+" (+"+pstr+")");

        //====================================

        String bstr="";

        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            bstr=(int)URLFactory.BREASTFEEDING_WATER+" ml";
        }
        else
        {
            bstr=(int)Math.round(HeightWeightHelper.mlToOzConverter(URLFactory.BREASTFEEDING_WATER))+" fl oz";
        }

        lbl_breastfeeding.setText(sh.get_string(R.string.breastfeeding));
        convertUpperCase(lbl_breastfeeding);
        lbl_breastfeeding.setText(lbl_breastfeeding.getText().toString()+" (+"+bstr+")");

        //====================================

        String tmp_weight=""+ph.getString(URLFactory.PERSON_WEIGHT);
        boolean isFemale=ph.getBoolean(URLFactory.USER_GENDER);
        int weatherIdx=ph.getInt(URLFactory.WEATHER_CONSITIONS);

        double tmp_kg = 0;
        if (ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
        {
            tmp_kg = Double.parseDouble("" + tmp_weight);
        }
        else
        {
            tmp_kg = HeightWeightHelper.lbToKgConverter(Double.parseDouble(tmp_weight));
        }

        //====================

        double diff=0;

        if(isFemale)
            diff=tmp_kg*URLFactory.DEACTIVE_FEMALE_WATER;
        else
            diff=tmp_kg*URLFactory.DEACTIVE_MALE_WATER;


        //====================

        if(weatherIdx==1)
            diff*=URLFactory.WEATHER_CLOUDY;
        else if(weatherIdx==2)
            diff*=URLFactory.WEATHER_RAINY;
        else if(weatherIdx==3)
            diff*=URLFactory.WEATHER_SNOW;
        else
            diff*=URLFactory.WEATHER_SUNNY;


        //====================


        bstr="";

        if(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT)) {
            bstr=(int)Math.round(diff)+" ml";
        }
        else
        {
            bstr=(int)Math.round(HeightWeightHelper.mlToOzConverter(diff))+" fl oz";
        }

        lbl_active.setText(sh.get_string(R.string.active));
        convertUpperCase(lbl_active);
        lbl_active.setText(lbl_active.getText().toString()+" (+"+bstr+")");
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void saveData(AppCompatEditText txt_name)
    {
        Log.d("saveData",""+txt_name.getText().toString().trim());

        ph.savePreferences(URLFactory.PERSON_HEIGHT,""+txt_name.getText().toString().trim());
        ph.savePreferences(URLFactory.PERSON_HEIGHT_UNIT,rdo_cm.isChecked());

        ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);


    }

    public void saveWeightData(AppCompatEditText txt_name)
    {
        Log.d("saveWeightData",""+rdo_kg.isChecked()+" @@@ "+txt_name.getText().toString().trim());

        ph.savePreferences(URLFactory.PERSON_WEIGHT,""+txt_name.getText().toString().trim());
        ph.savePreferences(URLFactory.PERSON_WEIGHT_UNIT,rdo_kg.isChecked());

        ph.savePreferences(URLFactory.WATER_UNIT, rdo_kg.isChecked()?"ml":"fl oz");

        ph.savePreferences(URLFactory.SET_MANUALLY_GOAL,false);

        URLFactory.WATER_UNIT_VALUE=rdo_kg.isChecked()?"ml":"fl oz";

        refreshWidget();
    }

    public void refreshWidget()
    {
        Intent intent = new Intent(act, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(act).getAppWidgetIds(new ComponentName(act, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        act.sendBroadcast(intent);
    }


    //===============


    public void init_WeightKG()
    {
        weight_kg_lst.clear();
        float f=30.0f;
        weight_kg_lst.add(""+f);
        for(int k=0;k<200;k++)
        {
            f+=0.5;
            weight_kg_lst.add(""+f);
        }

        final CharSequence[] st=new CharSequence[weight_kg_lst.size()];
        for(int k=0;k<weight_kg_lst.size();k++)
        {
            st[k]=""+weight_kg_lst.get(k);
        }
    }

    public void init_WeightLB()
    {
        weight_lb_lst.clear();
        for(int k=66;k<288;k++)
        {
            weight_lb_lst.add(""+k);
        }

        final CharSequence[] st=new CharSequence[weight_lb_lst.size()];
        for(int k=0;k<weight_lb_lst.size();k++)
        {
            st[k]=""+weight_lb_lst.get(k);
        }
    }

    //===============

    public void init_HeightCM()
    {
        height_cm_lst.clear();
        for(int k=60;k<241;k++)
        {
            height_cm_lst.add(""+k);
        }

        final CharSequence[] st=new CharSequence[height_cm_lst.size()];
        for(int k=0;k<height_cm_lst.size();k++)
        {
            st[k]=""+height_cm_lst.get(k);
        }
    }

    public void init_HeightFeet()
    {
        height_feet_lst.clear();
        height_feet_lst.add("2.0");
        height_feet_lst.add("2.1");
        height_feet_lst.add("2.2");
        height_feet_lst.add("2.3");
        height_feet_lst.add("2.4");
        height_feet_lst.add("2.5");
        height_feet_lst.add("2.6");
        height_feet_lst.add("2.7");
        height_feet_lst.add("2.8");
        height_feet_lst.add("2.9");
        height_feet_lst.add("2.10");
        height_feet_lst.add("2.11");
        height_feet_lst.add("3.0");
        height_feet_lst.add("3.1");
        height_feet_lst.add("3.2");
        height_feet_lst.add("3.3");
        height_feet_lst.add("3.4");
        height_feet_lst.add("3.5");
        height_feet_lst.add("3.6");
        height_feet_lst.add("3.7");
        height_feet_lst.add("3.8");
        height_feet_lst.add("3.9");
        height_feet_lst.add("3.10");
        height_feet_lst.add("3.11");
        height_feet_lst.add("4.0");
        height_feet_lst.add("4.1");
        height_feet_lst.add("4.2");
        height_feet_lst.add("4.3");
        height_feet_lst.add("4.4");
        height_feet_lst.add("4.5");
        height_feet_lst.add("4.6");
        height_feet_lst.add("4.7");
        height_feet_lst.add("4.8");
        height_feet_lst.add("4.9");
        height_feet_lst.add("4.10");
        height_feet_lst.add("4.11");
        height_feet_lst.add("5.0");
        height_feet_lst.add("5.1");
        height_feet_lst.add("5.2");
        height_feet_lst.add("5.3");
        height_feet_lst.add("5.4");
        height_feet_lst.add("5.5");
        height_feet_lst.add("5.6");
        height_feet_lst.add("5.7");
        height_feet_lst.add("5.8");
        height_feet_lst.add("5.9");
        height_feet_lst.add("5.10");
        height_feet_lst.add("5.11");
        height_feet_lst.add("6.0");
        height_feet_lst.add("6.1");
        height_feet_lst.add("6.2");
        height_feet_lst.add("6.3");
        height_feet_lst.add("6.4");
        height_feet_lst.add("6.5");
        height_feet_lst.add("6.6");
        height_feet_lst.add("6.7");
        height_feet_lst.add("6.8");
        height_feet_lst.add("6.9");
        height_feet_lst.add("6.10");
        height_feet_lst.add("6.11");
        height_feet_lst.add("7.0");
        height_feet_lst.add("7.1");
        height_feet_lst.add("7.2");
        height_feet_lst.add("7.3");
        height_feet_lst.add("7.4");
        height_feet_lst.add("7.5");
        height_feet_lst.add("7.6");
        height_feet_lst.add("7.7");
        height_feet_lst.add("7.8");
        height_feet_lst.add("7.9");
        height_feet_lst.add("7.10");
        height_feet_lst.add("7.11");
        height_feet_lst.add("8.0");

        final CharSequence[] st=new CharSequence[height_feet_lst.size()];
        for(int k=0;k<height_feet_lst.size();k++)
        {
            st[k]=""+height_feet_lst.get(k);
        }
    }

    //===============================================

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE1);
    }

    public void captureImage()
    {
        getSaveImageUri();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                imageUri);

        startActivityForResult(cameraIntent, PICK_Camera_IMAGE);
    }

    public void getSaveImageUri() {

        try {
            File root = new File(Environment.getExternalStorageDirectory()
                    + "/"+URLFactory.APP_DIRECTORY_NAME+"/"+URLFactory.APP_PROFILE_DIRECTORY_NAME+"/");
            if (!root.exists()) {
                root.mkdirs();
            }
            //String imageName = "profile_image_" + System.currentTimeMillis() + ".png";
            String imageName = "profile_image.png";

            File sdImageMainDirectory = new File(root, imageName);


            final boolean isNoget = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
            if (isNoget) {
                imageUri = FileProvider.getUriForFile(act, act.getPackageName() + ".provider", sdImageMainDirectory);
                selectedImagePath = sdImageMainDirectory.getAbsolutePath();
            } else {
                imageUri = Uri.fromFile(sdImageMainDirectory);
                selectedImagePath = FileUtils.getPath(act,imageUri);
            }
        } catch (Exception e) {
            Log.d("Incident Photo " , "Error occurred. Please try again later."+e.getMessage());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkStoragePermissions()
    {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, STORAGE_PERMISSION);
        } else {
            //selectImage();
            openPicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // selectImage();
                    openPicker();
                    return;
                } else {

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case SELECT_FILE1:

                if (resultCode == RESULT_OK)
                {
                    if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        bottomSheetDialog.dismiss();
                    }
                    else
                    {
                        try
                        {
                            selectedImage = data.getData();

                            if (selectedImage != null)
                            {
                                /*Glide.with(act)
                                        .load(selectedImage)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(img_user);

                                String path = FileUtils.getPath(act, selectedImage);

                                ph.savePreferences(URLFactory.USER_PHOTO,path);*/

                                bottomSheetDialog.dismiss();

                                CropImage.activity(selectedImage).start(act);


                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK)
                {
                    String path = selectedImagePath;

                    /*Glide.with(act)
                            .load(path)
                            .apply(RequestOptions.circleCropTransform())
                            .into(img_user);

                    ph.savePreferences(URLFactory.USER_PHOTO,path);*/

                    bottomSheetDialog.dismiss();

                    CropImage.activity(imageUri).start(act);






                }
                break;

            case  CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();

                    String path = FileUtils.getPath(act, resultUri);

                    ph.savePreferences(URLFactory.USER_PHOTO,path);

                    Glide.with(act)
                            .load(resultUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(img_user);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;
        }
    }

}