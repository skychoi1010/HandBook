package com.waterdiary.drinkreminder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.waterdiary.drinkreminder.worker.handbook_hospitaldata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class hospadapter extends ArrayAdapter<handbook_hospitaldata> {
    private static final String TAG = "hospadapter";
    private Context nContext;
    int nResource;

    public hospadapter(@NonNull Context context, int resource, @NonNull ArrayList<handbook_hospitaldata> objects) {
        super(context, resource, objects);
        nContext = context;
        nResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View converView, @NonNull ViewGroup parent) {
        String address= Objects.requireNonNull(getItem(position)).address;
        String doctor= Objects.requireNonNull(getItem(position)).doctor;
        String img_url= Objects.requireNonNull(getItem(position)).img_url;
        String lat = Objects.requireNonNull(getItem(position)).lat;
        String lon = Objects.requireNonNull(getItem(position)).lon;
        String name = Objects.requireNonNull(getItem(position)).name;
        handbook_hospitaldata hosp = new handbook_hospitaldata(address,doctor,img_url,lat,lon,name);

        LayoutInflater inflater = LayoutInflater.from(nContext);
        converView=inflater.inflate(nResource,parent,false);
        ImageView img = (ImageView) converView.findViewById(R.id.hosp_img);
        TextView add = (TextView) converView.findViewById(R.id.hosp_add);
        TextView doc = (TextView) converView.findViewById(R.id.doc_name);
        TextView hos = (TextView) converView.findViewById(R.id.hosp_name);
        InputStream URLcontent = null;
        try {
            URLcontent = (InputStream) new URL(img_url).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable image = Drawable.createFromStream(URLcontent, img_url);
        img.setImageDrawable(image);
        add.setText(address);
        doc.setText(doctor);
        hos.setText(name);
        return converView;
    }
}
