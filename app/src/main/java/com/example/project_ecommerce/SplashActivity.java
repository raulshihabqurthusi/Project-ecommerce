package com.example.project_ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 1500;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth = FirebaseAuth.getInstance();
        SharedPreferences mPrefs = getSharedPreferences("lastIntent",0);
        String lastIntent = mPrefs.getString("intent", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth.getCurrentUser() != null) {
                    if (lastIntent.equals("admin")) {
                        Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else if(lastIntent.equals("staff")){
                        Intent intent = new Intent(SplashActivity.this, StaffActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(lastIntent.equals("user")){
                        Intent intent = new Intent(SplashActivity.this, UserActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}