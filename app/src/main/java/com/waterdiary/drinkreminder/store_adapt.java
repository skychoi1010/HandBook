package com.waterdiary.drinkreminder;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.waterdiary.drinkreminder.worker.News;
import com.waterdiary.drinkreminder.worker.coupon_class;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class store_adapt extends ArrayAdapter<coupon_class> {
    private static final String TAG = "NewsListAdapter";
    private Context mContext;
    int mResource;

    public store_adapt(@NonNull Context context, int resource, @NonNull ArrayList<coupon_class> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String Path= Objects.requireNonNull(getItem(position)).path; //getcontent();
        String cost= Objects.requireNonNull(getItem(position)).cost; //getread();
        String isUsed= Objects.requireNonNull(getItem(position)).isUsed; //gettitle();

        coupon_class coupon = new coupon_class(cost,isUsed,Path);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);
        ImageView image =(ImageView)convertView.findViewById(R.id.coupon_id);
        getFlowerImage(Path, convertView,image);

        Log.d("flower"+Path, "getView");
        return convertView;
    }
    public void getFlowerImage(String Path, View convertView, final ImageView image){
        final FirebaseStorage storage  = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(Path);
        // Load the image using Glide

        pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(mContext.getApplicationContext())
                            .load(task.getResult())
                            .into(image);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(mContext.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}