package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import android.os.Bundle;

public class Removemem extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Context context = this;
    EditText name2;
    EditText email2;
    Button remove;
    DatabaseReference db;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth1;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removemem);
        name2 = findViewById(R.id.usn4);
        email2 = findViewById(R.id.email4);
        remove = findViewById(R.id.removebutton);
        mAuth1 = FirebaseAuth.getInstance();
        mUser = mAuth1.getCurrentUser();
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email2.getText().toString().trim().toLowerCase();
                String name = name2.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(Removemem.this, "Please enter the required details", Toast.LENGTH_SHORT).show();
                } else {
                    db = FirebaseDatabase.getInstance().getReference().child("FLC-members");
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("name").getValue().toString().equals(name) || dataSnapshot.child("email").getValue().toString().equals(email)) {
                                    dataSnapshot.getRef().removeValue();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Removemem.this,Admin.class);
        startActivity(intent);
    }
}
