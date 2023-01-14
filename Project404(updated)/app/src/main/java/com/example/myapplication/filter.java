package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class filter extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<Datum> data= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.toolbar);

        actionBar.setHomeAsUpIndicator(R.drawable.arrow_left);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#eee0d3")));
        //actionBar.setTitle("Search Results:");

        Intent intent = getIntent();
        Bundle extra=intent.getExtras();
        if (extra!=null) {
            data = extra.getParcelableArrayList("list");
        }

        mAdapt adapt = new mAdapt(data, getApplicationContext());
        recview = (RecyclerView) findViewById(R.id.recView);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapt);

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}