package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class searchById extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_left);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFF2EEEB")));

        Intent intent = getIntent();
        String name=intent.getStringExtra("Name");
        String branch=intent.getStringExtra("Branch");
        String usn=intent.getStringExtra("USN");
        String email=intent.getStringExtra("Email");
        String phone=intent.getStringExtra("Phone");
        String role=intent.getStringExtra("Role");
        String img=intent.getStringExtra("Image");
        String h = "Hidden";

        ImageView s_img = findViewById(R.id.img);
        Glide.with((s_img).getContext()).load(intent.getStringExtra("Image")).placeholder(R.drawable.default_profile).circleCrop().into(s_img);


        TextView s_name = findViewById(R.id.name);
        s_name.setText(name);

        TextView s_branch = findViewById(R.id.branch);
        s_branch.setText(branch);

        TextView s_usn = findViewById(R.id.usn);
        s_usn.setText(usn);

        TextView s_email = findViewById(R.id.email);
        if(Access.skipped){
            s_email.setText(h);
        }
        else{
            s_email.setText(email);
        }

        TextView s_no = findViewById(R.id.phone);
        if(Access.skipped){
            s_no.setText(h);
        }
        else{
            s_no.setText(phone);
        }

        TextView s_role = findViewById(R.id.role);
        s_role.setText(role);

        Button share = (Button) findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Name: "+name+"\nEmail ID: "+email+"\nPhone Number: "+phone;
                String shareSub = "Details of FLC member with ID "+usn;
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
    }

   @Override
   public boolean onSupportNavigateUp(){
       onBackPressed();
       return true;
   }
}
