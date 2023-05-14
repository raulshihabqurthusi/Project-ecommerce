package com.example.project_ecommerce;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_ecommerce.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ImageView img;
    private TextView buttonToSignup;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> listData = new ArrayList<>();
    private List<String> listAs = new ArrayList<>();
    private TextView BtnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        img = findViewById(R.id.image);
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        buttonToSignup = findViewById(R.id.buttonToSignup);
        progressbar = findViewById(R.id.progressBar);
        BtnAbout = findViewById(R.id.btnAboutUs);


        BtnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, AboutUsActivity.class);
                startActivity(i);
            }
        });

        img.setVisibility(View.VISIBLE);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });

        buttonToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();

    }

    private void loginUserAccount() {
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Required field", Toast.LENGTH_LONG) .show();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                SharedPreferences mPrefs = getSharedPreferences("lastIntent", 0);
                                SharedPreferences.Editor editor = mPrefs.edit();


                                if (task.isSuccessful()) {
                                    progressbar.setVisibility(View.VISIBLE);
                                    for(int x = 0; x < listData.size(); x++){
                                        if(listData.get(x).equals(email) && listAs.get(x).equals("admin")){
                                            editor.putString("intent", "admin");
                                            editor.apply();
                                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else if(listData.get(x).equals(email) && listAs.get(x).equals("staff")){
                                            editor.putString("intent", "staff");
                                            editor.apply();
                                            Intent intent = new Intent(LoginActivity.this, StaffActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else if(listData.get(x).equals(email) && listAs.get(x).equals("user")){
                                            editor.putString("intent", "user");
                                            editor.apply();
                                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }

    private void getData(){
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listData.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String email = document.getString("email");
                                String as = document.getString("as");
                                listData.add(email);
                                listAs.add(as);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}