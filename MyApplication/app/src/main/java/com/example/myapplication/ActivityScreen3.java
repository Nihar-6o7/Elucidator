package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;

public class ActivityScreen3 extends AppCompatActivity {

    EditText usn;
    EditText name;
    EditText branch;
    Button enter;
    ProgressDialog progressDialog;
    DatabaseReference db;
    String NAME,USN,BRANCH,PH,ROLE,EMAIL;//stores data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);
        usn=findViewById(R.id.usn);
        name=findViewById(R.id.name);
        branch=findViewById(R.id.branch);
        progressDialog = new ProgressDialog(this);
        enter=findViewById(R.id.enterbutton);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usn.getText().toString().isEmpty()){
                    usn.setError("enter usn");
                }
                else if(name.getText().toString().isEmpty()){
                    name.setError("enter name");
                }
                else if(branch.getText().toString().isEmpty()){
                    branch.setError("enter branch");
                }
                else {
                    getData();
                }
            }
        });
    }

    void getData(){
        db=FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists() && dataSnapshot.child("name").getValue().toString().equals(name.getText().toString()) && dataSnapshot.child("usn").getValue().toString().equals(usn.getText().toString()) && dataSnapshot.child("branch").getValue().toString().equals(branch.getText().toString())) {
                        NAME = dataSnapshot.child("name").getValue().toString();
                        USN = dataSnapshot.child("usn").getValue().toString();
                        PH = dataSnapshot.child("ph").getValue().toString();
                        EMAIL = dataSnapshot.child("email").getValue().toString();
                        ROLE = dataSnapshot.child("role").getValue().toString();
                        BRANCH = dataSnapshot.child("branch").getValue().toString();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}