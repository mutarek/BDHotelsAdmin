package com.techtrickbd.admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techtrickbd.admin.MainActivity;
import com.techtrickbd.admin.R;
import com.techtrickbd.admin.statics.UserValue;

public class LoginActivity extends AppCompatActivity {
    EditText email,pass;
    private FirebaseAuth firebaseApp = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_et);
        pass = findViewById(R.id.password_et);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            // No user is signed in
        }
    }

    public void login(View view) {
        String semail = email.getText().toString();
        String spass = pass.getText().toString();
        firebaseApp.signInWithEmailAndPassword(semail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    UserValue.uid =firebaseApp.getCurrentUser().getUid();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
    }

}
