package com.example.project_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddStaffActivity extends AppCompatActivity {

    EditText inputNameStaff, inputEmailStaff, inputPasswordStaff;
    Button buttonAddStaff;
    FirebaseFirestore db;
    FirebaseAuth auth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        inputNameStaff = (EditText) findViewById(R.id.inputNameStaff);
        inputEmailStaff = (EditText) findViewById(R.id.emailStaff);
        inputPasswordStaff = (EditText) findViewById(R.id.passwordStaff);
        buttonAddStaff = (Button) findViewById(R.id.buttonAddStaff);
        progressbar = findViewById(R.id.progressBarStaff);

        buttonAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStaffAccount();
            }
        });
    }

    private void addStaffAccount() {
        String name, email, password;
        name = inputNameStaff.getText().toString();
        email = inputEmailStaff.getText().toString();
        password = inputPasswordStaff.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Tolong isi semua field", Toast.LENGTH_LONG).show();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressbar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Add staff successful!", Toast.LENGTH_LONG).show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", name);
                    data.put("email", email);
                    data.put("as", "staff");

                    addDataStaff(data);

                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addDataStaff(Map<String, Object> data){
        db.collection("user")
                .document(auth.getUid())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}