package com.waterdiary.drinkreminder;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.waterdiary.drinkreminder.worker.coup_u;
import com.waterdiary.drinkreminder.worker.coup_user;
import com.waterdiary.drinkreminder.worker.coupon_class;

import java.util.ArrayList;
import java.util.Objects;

public class coup_adapter extends ArrayAdapter<coup_u> {
   // private static final String TAG = "NewsListAdapter";
    private Context mContext;
    int mResource;

    public coup_adapter(@NonNull Context context, int resource, @NonNull ArrayList<coup_u> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String path= Objects.requireNonNull(getItem(position)).path; //getcontent();
        String date= Objects.requireNonNull(getItem(position)).date; //getread();
        String isUsed= Objects.requireNonNull(getItem(position)).isUsed; //gettitle();
        String img =Objects.requireNonNull(getItem(position)).img;
        coup_u coupon = new coup_u(date,isUsed,path,img);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResource,parent,false);
        ImageView image =(ImageView)convertView.findViewById(R.id.coupon_id);
        TextView hos = (TextView) convertView.findViewById(R.id.date);
        //TextView ho = (TextView) convertView.findViewById(R.id.img);
        getImage(path,image,img);
        hos.setText(date);
        //ho.setText(path);
        return convertView;
    }
    public void getImage(String Path, final ImageView image,String img){
        final FirebaseStorage storage  = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(img).child(Path+".jpg");
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
