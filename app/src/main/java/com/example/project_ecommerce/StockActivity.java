package com.example.project_ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project_ecommerce.adapter.ItemAdapter;
import com.example.project_ecommerce.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockActivity extends AppCompatActivity {
    
    FloatingActionButton fab;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<Item> list = new ArrayList<>();
    ItemAdapter itemAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_stock);
        db = FirebaseFirestore.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Get data...");
        itemAdapter = new ItemAdapter(this, list);
        showCustomDialog();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(itemAdapter);

    }

    public void showCustomDialog(){
        itemAdapter.setDialog(new ItemAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final EditText inputQuantity = new EditText(StockActivity.this);
                inputQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputQuantity.setText(list.get(pos).getQuantity());
                inputQuantity.setSelection(list.get(pos).getQuantity().length());

                AlertDialog.Builder dialog = new AlertDialog.Builder(StockActivity.this);

                dialog.setTitle("Update Quantity").setMessage("Input new quantity").setView(inputQuantity).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateData(list.get(pos).getDocId(), inputQuantity.getText().toString());
                        Toast.makeText(StockActivity.this, "Quantity Updated "+list.get(pos).getDocId(), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    public void getData(){
        progressDialog.show();
        db.collection("item").orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Item item = new Item(document.getString("id"), document.getString("name"), document.getString("quantity"), document.getString("picture"), document.getString("category"), document.getString("filter"), document.getString("price"), document.getString("description"));
                                item.setDocId(document.getId());
                                list.add(item);
                            }
                            itemAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getApplicationContext(), "Data gagal di ambil!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void updateData(String docId, String quantity){
        Map<String, Object> item = new HashMap<>();
        item.put("quantity", quantity);
        db.collection("item")
                .document(docId)
                .update(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialogMessage("Update Changes", "Wait for updating");
                        getData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    public void dialogMessage(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
    }








}