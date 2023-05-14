package com.example.project_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailInformationActivity extends AppCompatActivity {

    TextView txtNama, txtPrice, txtDesc, txtAmount;
    String picture;
    ImageView logo;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_information);

        txtNama = (TextView) findViewById(R.id.txtNama);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        logo = (ImageView) findViewById(R.id.imgView);
        txtAmount = (TextView) findViewById(R.id.txtAmount);

        txtNama.setText(getIntent().getStringExtra("nama-produk"));
        txtDesc.setText(getIntent().getStringExtra("desc-produk"));
        txtPrice.setText("Rp." + getIntent().getStringExtra("harga-produk"));
        picture = getIntent().getStringExtra("pict-produk");

        Picasso.Builder builder = new Picasso.Builder(this);

        builder.build().load(picture)
                .placeholder((R.drawable.ic_launcher_foreground))
                .error(R.drawable.ic_launcher_foreground)
                .into(logo);
    }

    public void operator(View view) {
        int viewID = view.getId();
        switch (viewID){
            case R.id.btnPlus:
                amount++;
                txtAmount.setText(String.valueOf(amount));
                break;
            case R.id.btnMinus:
                amount = amount > 1 ? --amount : 1;
                txtAmount.setText(String.valueOf(amount));
                break;
            default:
                break;
        }
    }
}