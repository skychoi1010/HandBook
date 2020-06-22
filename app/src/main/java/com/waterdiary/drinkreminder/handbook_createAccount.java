package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_createAccount extends MasterBaseActivity {
    FirebaseAuth mAuth;
    AppCompatEditText email;
    AppCompatEditText password;
    AppCompatEditText name;
    AppCompatEditText cpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_create_account);

        AppCompatTextView createAccount = findViewById(R.id.createacc);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.txt_name);
        cpassword = findViewById(R.id.cpwd);
        mAuth = FirebaseAuth.getInstance();

        cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd_str = password.getText().toString();
                String cpwd_str = cpassword.getText().toString();
                if(pwd_str.equals(cpwd_str)){
                    password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.green_color), PorterDuff.Mode.SRC_ATOP);
                    cpassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.green_color), PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    cpassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.error_header), PorterDuff.Mode.SRC_ATOP);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_str = email.getText().toString();
                final String pwd_str = password.getText().toString();
                final String name_str = name.getText().toString();
/*
                if(email_str.length() == 0){
                    Toast.makeText(handbook_createAccount.this, "Enter email", Toast.LENGTH_SHORT).show();
                }

                if(pwd_str.length() == 0){
                    Toast.makeText(handbook_createAccount.this, "Enter password", Toast.LENGTH_SHORT).show();
                }

                if(cpwd_str.length() == 0){
                    Toast.makeText(handbook_createAccount.this, "Confirm password", Toast.LENGTH_SHORT).show();
                }

                if(!pwd_str.equals(cpwd_str)){
                    Toast.makeText(handbook_createAccount.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
*/
                mAuth.createUserWithEmailAndPassword(email_str, pwd_str)
                        .addOnCompleteListener(handbook_createAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    //Toast.makeText(handbook_start.this, "username"+ username, Toast.LENGTH_SHORT).show();
                                    if (user != null) {
                                        // User is signed in
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name_str)
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //successful
                                                        }
                                                    }
                                                });
                                        user.updateEmail(email_str)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //success
                                                        }
                                                    }
                                                });
                                        String userId = user.getUid();
                                        //Example you need save a Store in
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference userdata = database.getReference("UserData");
                                        UserData userdataStorage = new UserData(email_str, name_str, pwd_str, "vibration", 20, 0, null);
                                        userdata.child(userId).setValue(userdataStorage);
                                        Intent intent = new Intent(handbook_createAccount.this, handbook_start.class);
                                        intent.putExtra("email", email_str);
                                        intent.putExtra("pwd", pwd_str);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // No user is signed in
                                        Toast.makeText(handbook_createAccount.this, "no user signed in", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(handbook_createAccount.this, "email already exists", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });


            }
        });

    }

    public void setUsername(String username) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(handbook_start.this, "username"+ username, Toast.LENGTH_SHORT).show();
        if (user != null) {
            // User is signed in
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //successful
                                //Toast.makeText(handbook_start.this, "displayname"+ user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // No user is signed in
        }
    }

    public class UserData {
        public String email;
        public String name;
        public String password;
        public String noti_type;
        public int interval;
        public int coins;
        public String coupons;

        public UserData(){};

        public UserData(String email, String name, String password, String noti_type, int interval, int coins, String coupons) {
            this.email = email;
            this.name = name;
            this.password = password;
            this.noti_type = noti_type;
            this.interval = interval;
            this.coins = coins;
            this.coupons = coupons;
        }
    }
}
