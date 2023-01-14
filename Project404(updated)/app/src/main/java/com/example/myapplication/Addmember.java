package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Locale;

public class Addmember extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Context context=this;
    EditText usn1;
    EditText name1;
    EditText branch1;
    EditText role1;
    EditText ph1;
    EditText email1;
    Button add;
    DatabaseReference db;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth1;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);

        firebaseDatabase = FirebaseDatabase.getInstance();

        usn1=findViewById(R.id.usn3);
        email1=findViewById(R.id.email3);
        ph1=findViewById(R.id.ph3);
        name1=findViewById(R.id.name3);
        branch1=findViewById(R.id.branch3);
        role1=findViewById(R.id.role3);
        mAuth1=FirebaseAuth.getInstance();
        mUser=mAuth1.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        add=findViewById(R.id.addbutton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usn = usn1.getText().toString().trim().toUpperCase();
                String name = name1.getText().toString().trim().toLowerCase();
                String branch = branch1.getText().toString().trim();
                String role = role1.getText().toString().trim().toLowerCase();
                String phoneNo = ph1.getText().toString().trim();
                String email = email1.getText().toString().trim().toLowerCase();
                String password = usn.toLowerCase();
                if(usn.isEmpty() || name.isEmpty() || branch.isEmpty() || role.isEmpty() || phoneNo.isEmpty() || email.isEmpty()){
                    Toast.makeText(Addmember.this,"Please enter the required details",Toast.LENGTH_SHORT).show();
                }
                else{
                    Store s = new Store(name,usn,email,branch,phoneNo,role);
                    db = FirebaseDatabase.getInstance().getReference().child("FLC-members");
                    db.push().setValue(s);
                    try {
                        mAuth1.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Addmember.this, "added " + name, Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Addmember.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    catch (Exception e){}
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Addmember.this,Admin.class);
        startActivity(intent);
    }
}