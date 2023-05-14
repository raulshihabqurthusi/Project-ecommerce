package com.example.project_ecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ecommerce.ProductActivity;
import com.example.project_ecommerce.R;
import com.example.project_ecommerce.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context mcontext;
    private String[] categoryName;
    private int[] categoryPicture;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Activity activity;

    public CategoryAdapter( Context mcontext,Activity activity , String[] categoryName, int[] categoryPicture) {
        this.mcontext = mcontext;
        this.activity = activity;
        this.categoryName = categoryName;
        this.categoryPicture = categoryPicture;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.col_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.courseTV.setText(categoryName[position]);
        holder.courseIV.setImageResource(categoryPicture[position]);
    }

    @Override
    public int getItemCount() {
        return categoryName.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView courseTV;
        private ImageView courseIV;
        private LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, ProductActivity.class);
                    intent.putExtra("category", categoryName[getLayoutPosition()]);
                    mcontext.startActivity(intent);
                }
            });

        }

    }
}
