package com.example.project_ecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project_ecommerce.DetailInformationActivity;
import com.example.project_ecommerce.ProductActivity;
import com.example.project_ecommerce.R;
import com.example.project_ecommerce.StockActivity;
import com.example.project_ecommerce.UserActivity;
import com.example.project_ecommerce.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private Context context;
    private List<Item> list;
    private Dialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface Dialog{
        void onClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public ItemAdapter(Context context, List<Item> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context instanceof ProductActivity){
            View itemView = LayoutInflater.from(context).inflate(R.layout.row_item_user, parent, false);
            return new MyViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(context instanceof ProductActivity){
                holder.userItemName.setText(list.get(position).getName());
                holder.userItemPrice.setText("Rp." + list.get(position).getPrice());
                holder.userItemStock.setText("Stock: " +list.get(position).getQuantity());

                holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, DetailInformationActivity.class);
                        i.putExtra("nama-produk", list.get(position).getName());
                        i.putExtra("harga-produk", list.get(position).getPrice());
                        i.putExtra("desc-produk", list.get(position).getDescription());
                        i.putExtra("pict-produk", list.get(position).getPicture());
                        view.getContext().startActivity(i);
                    }
                });

                Glide.with(context)
                        .load(list.get(position).getPicture())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progressBarUser.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBarUser.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .error(R.drawable.ic_baseline_error_24)
                        .into(holder.userItemPicture);
            }else{
                holder.idItem.setText(list.get(position).getId());
                holder.nameItem.setText(list.get(position).getName());
                holder.quantityItem.setText(list.get(position).getQuantity());

                Glide.with(context)
                        .load(list.get(position).getPicture())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .error(R.drawable.ic_baseline_error_24)
                        .into(holder.pictureItem);
            }
        }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idItem, nameItem, quantityItem;
        ImageView pictureItem;
        ProgressBar progressBar;
        Button updateStockItem, deleteItem;

        TextView userItemName, userItemPrice, userItemStock;
        ShapeableImageView userItemPicture;
        ProgressBar progressBarUser;

        CardView btnInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idItem = (TextView) itemView.findViewById(R.id.itemId);
            nameItem = (TextView) itemView.findViewById(R.id.itemName);
            quantityItem = (TextView) itemView.findViewById(R.id.itemQuantity);
            pictureItem = (ImageView) itemView.findViewById(R.id.itemImage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            updateStockItem = (Button) itemView.findViewById(R.id.updateStockItem);
            deleteItem = (Button) itemView.findViewById(R.id.deleteItem);

            userItemName = (TextView) itemView.findViewById(R.id.userItemName);
            userItemPrice = (TextView) itemView.findViewById(R.id.userItemPrice);
            userItemStock = (TextView) itemView.findViewById(R.id.userItemStock);
            userItemPicture = (ShapeableImageView) itemView.findViewById(R.id.userItemPict);
            progressBarUser = (ProgressBar) itemView.findViewById(R.id.progressBarUser);

            btnInfo = itemView.findViewById(R.id.cardView);



            if(context instanceof StockActivity){
                updateStockItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog!=null){
                            dialog.onClick(getLayoutPosition());
                        }
                    }
                });

                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        db.collection("item")
                                .document(list.get(getLayoutPosition()).getDocId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Success delete item", Toast.LENGTH_SHORT).show();
                                        ((StockActivity) context).getData();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }


        }
    }
}
