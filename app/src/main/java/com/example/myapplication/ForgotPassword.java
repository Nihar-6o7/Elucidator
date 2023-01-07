package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button send;
    EditText Email1;
    private String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth= FirebaseAuth.getInstance();
        Email1 = findViewById(R.id.Email1);
        send = findViewById(R.id.Send);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ValidateData();
            }
        });
    }

    private void ValidateData(){
        email=Email1.getText().toString();
        if(email.isEmpty()) {
            Email1.setError("please enter the email");
        }
        else {
            forgotPassword();
        }
    }

    private void forgotPassword(){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check you Email",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(ForgotPassword.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}