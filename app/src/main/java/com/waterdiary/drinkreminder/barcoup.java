package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class barcoup extends MasterBaseActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coup_store);
        Intent intent = getIntent();
        String bar = intent.getStringExtra("bar");
        ImageView v=(ImageView)findViewById(R.id.bimg);
        final String img=intent.getStringExtra("img");
        getImage(bar,v,img);
        Log.d("flower", "getView");

    }

    public void getImage(String Path, final ImageView image, String img){
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
