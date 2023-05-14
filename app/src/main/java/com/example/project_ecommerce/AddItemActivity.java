package com.example.project_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project_ecommerce.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.DocumentTransform;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    private EditText inputIdItem, inputNameItem, inputQuantityItem, inputPictureItem, inputPriceItem, inputDescriptionItem;
    private Button buttonAddItem;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private List<String> list = new ArrayList<>();

    private Spinner dropdownCategory, dropdownFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        dialogMessage("Loading...", "Add Data Item");
        inputIdItem = (EditText) findViewById(R.id.inputIdItem);
        inputNameItem = (EditText) findViewById(R.id.inputItemName);
        inputQuantityItem = (EditText) findViewById(R.id.inputItemQuantity);
        inputPictureItem = (EditText) findViewById(R.id.inputItemPicture);
        inputPriceItem = (EditText) findViewById(R.id.inputItemPrice);
        inputDescriptionItem = (EditText) findViewById(R.id.inputItemDescription);
        buttonAddItem = (Button) findViewById(R.id.buttonAddItem);

        dropdownCategory = (Spinner) findViewById(R.id.dropdownMenu);
        dropdownFilter = (Spinner) findViewById(R.id.dropdownFilter);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputIdItem.getText().toString().isEmpty() || inputPriceItem.getText().toString().isEmpty() || inputNameItem.getText().toString().isEmpty() || inputQuantityItem.getText().toString().isEmpty() || inputPictureItem.getText().toString().isEmpty() || inputDescriptionItem.getText().toString().isEmpty()){
                    Toast.makeText(AddItemActivity.this, "Tolong isi semua field", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(String str: list){
                    if(str.equals(inputIdItem.getText().toString())){
                        Toast.makeText(AddItemActivity.this, "Maaf, ID sudah terdaftar", Toast.LENGTH_SHORT).show();
                        inputIdItem.setText("");
                        return;
                    }
                }

                saveData(inputIdItem.getText().toString(),dropdownCategory.getSelectedItem().toString().toLowerCase(Locale.ROOT), dropdownFilter.getSelectedItem().toString().toLowerCase(Locale.ROOT), inputNameItem.getText().toString(), inputQuantityItem.getText().toString(), inputPictureItem.getText().toString(), inputPriceItem.getText().toString(), inputDescriptionItem.getText().toString());
                
            }
        });

        dropdownCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener(this, AddItemActivity.this));

    }

    private void saveData(String id, String category, String filter, String name, String quantity, String picture, String price, String description){
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("category", category);
        item.put("filter", filter);
        item.put("name", name);
        item.put("quantity", quantity);
        item.put("picture", picture);
        item.put("date", FieldValue.serverTimestamp());
        item.put("price", price);
        item.put("description", description);

        progressDialog.show();
        db.collection("item")
            .add(item)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    progressDialog.dismiss();
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        db.collection("item").orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String id = document.getString("id");
                                list.add(id);
                            }
                            System.out.println(list);
                        }else{
                            Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void dialogMessage(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
    }

}