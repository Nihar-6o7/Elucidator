package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,db;

    ArrayList<Datum> s_data= new ArrayList<Datum>();


    Context context=this;
    EditText s_usn;
    EditText s_name;
    EditText s_branch;
    EditText s_role;
    Button enter;
    TextView admin;
    TextView admin1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FLC-members");

        s_usn=findViewById(R.id.usn1);
        s_name=findViewById(R.id.name1);
        s_branch=findViewById(R.id.branch1);
        s_role=findViewById(R.id.role1);
        admin=findViewById(R.id.admin);
        admin1=findViewById(R.id.admin1);
        progressDialog = new ProgressDialog(this);
        enter=findViewById(R.id.enterbutton1);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usn=s_usn.getText().toString().trim();
                String name=s_name.getText().toString().trim().toLowerCase();
                String branch=s_branch.getText().toString().trim();
                String role=s_role.getText().toString().trim().toLowerCase();
                db = FirebaseDatabase.getInstance().getReference("FLC-members");


                if(usn.isEmpty() && name.isEmpty() && branch.isEmpty() && role.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill at least one of the given fields!", Toast.LENGTH_SHORT).show();
                }
                else {


                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int c = 0;
                            s_data.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Datum datum = snapshot.getValue(Datum.class);
                                if (usn.length() == 10) {
                                    if (datum.getUsn().equalsIgnoreCase(usn)) {
                                        c++;
                                        profile p = new profile();
                                        p.next(datum, getApplicationContext());
                                    }
                                }
                                else {
                                    if (datum.getName().toLowerCase().contains(name.toLowerCase()) && datum.getUsn().toLowerCase().contains(usn.toLowerCase()) && datum.getBranch().toLowerCase().contains(branch.toLowerCase()) && datum.getRole().toLowerCase().contains(role.toLowerCase())) {
                                        c++;
                                        s_data.add(datum);
                                    }
                                }
                            }

                            if (c!=0 && usn.length()!=10){
                                Intent intent=new Intent(getApplicationContext(), filter.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle bundle=new Bundle();
                                bundle.putParcelableArrayList("list",s_data);
                                intent.putExtras(bundle);
                                getApplicationContext().startActivity(intent);
                            }
                            else{
                                if (c==0) {
                                    if (usn.length() == 10) {
                                        Toast.makeText(getApplicationContext(), "FLC member with that USN does not exist!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "No search results found.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,Addmember.class);
                startActivity(intent);
            }
        });

        admin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,Removemem.class);
                startActivity(intent);
            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}