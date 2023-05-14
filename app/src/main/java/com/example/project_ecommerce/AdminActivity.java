package com.example.project_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonAddStaff, buttonAddStock, buttonToLogout;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        buttonAddStaff = (Button) findViewById(R.id.buttonToStaff);
        buttonAddStock = (Button) findViewById(R.id.buttonToStock);
        buttonToLogout = (Button) findViewById(R.id.buttonToLogout);

        buttonAddStaff.setOnClickListener(this);
        buttonAddStock.setOnClickListener(this);
        buttonToLogout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonToStaff:
                Intent intentStaff = new Intent(AdminActivity.this, AddStaffActivity.class);
                startActivity(intentStaff);
                break;
            case R.id.buttonToStock:
                Intent intentStock = new Intent(AdminActivity.this, StockActivity.class);
                startActivity(intentStock);
                break;
            case R.id.buttonToLogout:
                auth.signOut();
                finish();
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                break;
        }
    }
}