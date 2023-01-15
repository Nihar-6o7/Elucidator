package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class Access{
    static boolean skipped;
}

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    public static String em;
    public static String pass;
    ProgressDialog progressDialog;
    TextView ForgotPass;
    TextView skip;
    FirebaseAuth mAuth;
    FirebaseUser nUser;
    DatabaseReference db;
    boolean vis;
    String email,passwor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        skip = (TextView) findViewById(R.id.skip);
        ForgotPass = (TextView) findViewById(R.id.ForgotPass);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        nUser = mAuth.getCurrentUser();

        //login button backend
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Access.skipped = false;
                performLogin();
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= password.getRight() - password.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = password.getSelectionEnd();
                        if (vis) {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            vis = false;
                        } else {
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_24, 0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            vis = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Access.skipped = true;
                sendUserToNextActivity();
                Toast.makeText(MainActivity.this, "skipped", Toast.LENGTH_SHORT).show();
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
        email = username.getText().toString().trim();
        passwor = password.getText().toString().trim();
        if (email.isEmpty()) {
            username.setError("email cannot be empty");
        } else if (passwor.isEmpty()) {
            password.setError("password cannot be empty");
        } else {
            progressDialog.setMessage("Please wait");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, passwor).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        em=email;
                        pass=passwor;
                        db = FirebaseDatabase.getInstance().getReference().child("FLC-members");
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int c = 0;
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String r = dataSnapshot.child("role").getValue().toString();
                                    if (dataSnapshot.child("email").getValue().toString().equals(email) && (r.equals("President") || r.equals("Vice President") || r.toLowerCase().contains("secretary"))) {
                                        c = 1;
                                        progressDialog.dismiss();
                                        sendUserToAdmin();
                                    }
                                }
                                if (c == 0) {
                                    progressDialog.dismiss();
                                    sendUserToNextActivity();
                                    Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    /*@Override
    public void onBackPressed() {
        this.finishAffinity();
    }*/

    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, ActivityScreen3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendUserToAdmin() {
        Intent intent = new Intent(MainActivity.this, Admin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendUserToOTP() {
        Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
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

