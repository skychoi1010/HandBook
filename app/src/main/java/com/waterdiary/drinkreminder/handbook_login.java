package com.waterdiary.drinkreminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.waterdiary.drinkreminder.base.MasterBaseActivity;

public class handbook_login extends MasterBaseActivity {
    FirebaseAuth mAuth;
    EditText email;
    EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handbook_login_create);
        Button login = findViewById(R.id.login);
        Button cancel = findViewById(R.id.cancel);
        email = findViewById(R.id.login_email);
        pwd = findViewById(R.id.login_pwd);
        mAuth = mAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_str = email.getText().toString().trim();
                String pwd_str = pwd.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email_str, pwd_str)
                        .addOnCompleteListener(handbook_login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(handbook_login.this, Screen_Dashboard.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(handbook_login.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), handbook_start.class);
                startActivity(intent);
            }
        });

    }
}
