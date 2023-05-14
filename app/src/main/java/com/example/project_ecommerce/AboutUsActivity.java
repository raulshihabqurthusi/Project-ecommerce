package com.example.project_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void whatsapp(View view) {
        int viewID = view.getId();
        String number = null;
        switch (viewID){
            case R.id.wa_tessa:
                number = "6288902844473";
                break;
            case R.id.wa_alpha:
                number = "6281322810220";
                break;
            case R.id.wa_raul:
                number = "62895610441428";
                break;
            default:
                break;
        }
        String url = "https://api.whatsapp.com/send?phone="+number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}