package com.example.project_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StaffActivity extends AppCompatActivity {
    Button buttonLogoutStaff;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        buttonLogoutStaff = (Button) findViewById(R.id.buttonStaffLogout);
        auth = FirebaseAuth.getInstance();

        buttonLogoutStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
                startActivity(new Intent(StaffActivity.this, LoginActivity.class));
            }
        });
    }
}