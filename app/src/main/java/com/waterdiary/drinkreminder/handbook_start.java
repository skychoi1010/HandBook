package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.os.Bundle;
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
import com.waterdiary.drinkreminder.base.MasterBaseAppCompatActivity;

public class handbook_start extends MasterBaseAppCompatActivity {
    private FirebaseAuth mAuth;
    AppCompatEditText email;
    AppCompatEditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(handbook_start.this, Screen_Dashboard.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_start);
        AppCompatTextView login = findViewById(R.id.login);
        AppCompatTextView createAccount = findViewById(R.id.createAccount);
        AppCompatTextView skip = findViewById(R.id.skip);
        email = findViewById(R.id.email_login);
        pwd = findViewById(R.id.pwd_login);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_str = email.getText().toString().trim();
                final String pwd_str = pwd.getText().toString().trim();
                final Intent intent = getIntent();

                mAuth.signInWithEmailAndPassword(email_str, pwd_str)
                        .addOnCompleteListener(handbook_start.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String username;
                                if (!task.isSuccessful()) {
                                    Toast.makeText(handbook_start.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if(intent.getExtras() != null){
                                        username = intent.getExtras().getString("name");
                                    }
                                    else if(mAuth.getCurrentUser().getDisplayName() != null) {
                                        username = mAuth.getCurrentUser().getDisplayName();
                                    }
                                    else{
                                        username = "Anonymous";
                                    }
                                    setUsername(username);
                                    Intent intent2 = new Intent(getApplicationContext(), Screen_Dashboard.class);
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
                    Intent intent = new Intent(handbook_start.this, Screen_Dashboard.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(handbook_start.this, "login error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
