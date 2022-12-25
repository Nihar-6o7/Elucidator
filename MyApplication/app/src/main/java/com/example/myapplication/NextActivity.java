package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        String NAME = getIntent().getStringExtra("NAME");
        String BRANCH = getIntent().getStringExtra("BRANCH");
        String ROLE = getIntent().getStringExtra("ROLE");
        String EMAIL = getIntent().getStringExtra("EMAIL");
        String PH = getIntent().getStringExtra("PH");
        String USN = getIntent().getStringExtra("USN");
    }
}