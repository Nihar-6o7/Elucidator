package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;

    ProgressDialog progressDialog;
    TextView ForgotPass;
    TextView skip;
    FirebaseAuth mAuth;
    FirebaseUser nUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        skip = (TextView) findViewById(R.id.skip);
        ForgotPass= (TextView) findViewById(R.id.ForgotPass);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        nUser = mAuth.getCurrentUser();

        //login button backend
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               performLogin();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToNextActivity();
                Toast.makeText(MainActivity.this,"skipped",Toast.LENGTH_SHORT).show();

            }
        });

        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToOTP();
            }
        });
    }
    private void performLogin() {
        String email = username.getText().toString();
        String passwor = password.getText().toString();
        if(email.isEmpty()) {
            username.setError("email cannot be empty");
        }
        else if(passwor.isEmpty()) {
            password.setError("password cannot be empty");
        }
        else {
            progressDialog.setMessage("Please wait");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,passwor).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this,"Login Successful.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Invalid email or password!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void sendUserToNextActivity(){
        Intent intent = new Intent(MainActivity.this,ActivityScreen3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendUserToOTP(){
        Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}