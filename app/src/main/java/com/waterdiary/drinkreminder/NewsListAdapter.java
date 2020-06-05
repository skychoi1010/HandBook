package com.waterdiary.drinkreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.waterdiary.drinkreminder.worker.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsListAdapter extends ArrayAdapter<News> {
private static final String TAG = "NewsListAdapter";
private Context mContext;
int mResource;

    public NewsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String content= Objects.requireNonNull(getItem(position)).content; //getcontent();
        String read= Objects.requireNonNull(getItem(position)).read; //getread();
        String title= Objects.requireNonNull(getItem(position)).title; //gettitle();
        String date = Objects.requireNonNull(getItem(position)).date; //getdate();
        String link = Objects.requireNonNull(getItem(position)).link; //getlink();

        News news = new News(title,content,date,read,link);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);
        TextView tvtitle = (TextView) convertView.findViewById(R.id.newstitle);
        TextView tvsubtitile = (TextView) convertView.findViewById(R.id.newsubtitle);
        TextView reada = (TextView) convertView.findViewById(R.id.read);
        TextView links = (TextView) convertView.findViewById(R.id.link);
        tvtitle.setText(title);
        tvsubtitile.setText(content);
        reada.setText(read);
        links.setText(link);
        return convertView;
    }
}
