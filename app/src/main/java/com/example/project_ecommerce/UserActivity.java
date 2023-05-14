package com.example.project_ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project_ecommerce.adapter.CategoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {
    FloatingActionButton buttonUserLogout;
    FirebaseAuth auth;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        auth = FirebaseAuth.getInstance();
        buttonUserLogout = (FloatingActionButton) findViewById(R.id.buttonUserLogout);

        buttonUserLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(UserActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(UserActivity.this,LoginActivity.class));
            }
        });

        recyclerView=findViewById(R.id.rvColCategory);

        String[] categoryName = getResources().getStringArray(R.array.dropdownCategory);;
        int[] categoryPicture = {R.drawable.ic_cloth, R.drawable.ic_elec, R.drawable.ic_peralatan_rumah_tangga, R.drawable.ic_others};

        CategoryAdapter adapter=new CategoryAdapter(this, UserActivity.this, categoryName, categoryPicture);

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

}