package com.example.project_ecommerce;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
    Spinner dropdownFilter;
    Activity activity;
    Context context;
    String[] filterPakaian = {"Pria", "Wanita"};
    String[] filterElektronik = {"Laptop", "Kipas Angin", "TV", "Mouse", "Keyboard"};
    String[] filterPeralatanRumah = {"Kompor", "Piring", "Sapu"};
    String[] others = {"others"};

    CustomOnItemSelectedListener(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        dropdownFilter = (Spinner) activity.findViewById(R.id.dropdownFilter);

        if(i == 0){
            setData(filterPakaian);
        }else if(i == 1){
            setData(filterElektronik);
        }else if(i == 2){
            setData(filterPeralatanRumah);
        }else{
            setData(others);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setData(String[] filter){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, filter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownFilter.setAdapter(dataAdapter);
    }
}
