package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;

public class handbook_start extends MasterBaseAppCompatActivity {
    private FirebaseAuth mAuth;
    AppCompatEditText email;
    AppCompatEditText pwd;
    UserData userdataStorage;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_start);
        AppCompatTextView login = findViewById(R.id.login);
        AppCompatTextView createAccount = findViewById(R.id.createAccount);
        AppCompatTextView skip = findViewById(R.id.skip);
        email = findViewById(R.id.email_login);
        pwd = findViewById(R.id.pwd_login);
        mAuth = FirebaseAuth.getInstance();
        if (user != null) {
            // User is signed in
            final Intent intent = getIntent();
            if(intent.getFlags() == Intent.FLAG_ACTIVITY_NO_HISTORY){
                intent.removeFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                email.setText(intent.getStringExtra("email"));
                pwd.setText(intent.getStringExtra("pwd"));
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email_str = email.getText().toString().trim();
                        String pwd_str = pwd.getText().toString().trim();

                        Toast.makeText(handbook_start.this, "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                        mAuth.signInWithEmailAndPassword(email_str, pwd_str)
                                .addOnCompleteListener(handbook_start.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        final String username;
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(handbook_start.this, "Authentication failed",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            username = mAuth.getCurrentUser().getDisplayName();
                                            setUsername(username);
                                            Intent intent2 = new Intent(handbook_start.this, Screen_Dashboard.class);
                                            startActivity(intent2);
                                        }
                                    }
                                });
                    }
                });
            }
            else {
                Intent i = new Intent(handbook_start.this, Screen_Dashboard.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        } else {
            // User is signed out
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_str = email.getText().toString().trim();
                String pwd_str = pwd.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(email_str, pwd_str)
                        .addOnCompleteListener(handbook_start.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String username;
                                if (!task.isSuccessful()) {
                                    Toast.makeText(handbook_start.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    username = mAuth.getCurrentUser().getDisplayName();
                                    setUsername(username);
                                    Intent intent2 = new Intent(handbook_start.this, Screen_Dashboard.class);
                                    startActivity(intent2);
                                }
                            }
                        });
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), handbook_createAccount.class);
                startActivity(intent1);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInAnonymously();
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
            Toast.makeText(handbook_start.this, "no user signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInAnonymously(){
        mAuth.signInAnonymously().addOnCompleteListener(handbook_start.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    setUsername("Anonymous");
                    FirebaseUser user = mAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    //Example you need save a Store in
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userdata = database.getReference("UserData");
                    userdataStorage = new UserData(null, "Anonymous", null, "vibration", 20, 0, null);
                    userdata.child(userId).setValue(userdataStorage);
                    Intent intent = new Intent(handbook_start.this, Screen_Dashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(handbook_start.this, "login error", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
