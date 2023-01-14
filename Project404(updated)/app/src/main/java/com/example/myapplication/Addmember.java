package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

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
    Button browse;
    DatabaseReference db;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth1;
    FirebaseUser mUser;
    Bitmap bitmap;
    Uri filepath;
    ImageView img;

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
        browse=findViewById(R.id.browse);
        mAuth1=FirebaseAuth.getInstance();
        img=findViewById(R.id.img);
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
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference uploader = storage.getReference("Image1"+new Random().nextInt(1000));
                if(usn.isEmpty() || name.isEmpty() || branch.isEmpty() || role.isEmpty() || phoneNo.isEmpty() || email.isEmpty()){
                    Toast.makeText(Addmember.this,"Please enter the required details",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.setTitle("Uploading data");
                    progressDialog.show();
                    uploader.putFile(filepath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            progressDialog.dismiss();
                                            databaseReference=firebaseDatabase.getReference().child("FLC-members");
                                            Store s = new Store(name,usn,email,branch,phoneNo,role,uri.toString());
                                            databaseReference.push().setValue(s);
                                            email1.setText("");
                                            ph1.setText("");
                                            role1.setText("");
                                            branch1.setText("");
                                            name1.setText("");
                                            usn1.setText("");
                                            img.setImageResource(R.drawable.default_profile);
                                        }
                                    });
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded :"+(int)percent+" %");
                                }
                            });
                    try {
                        mAuth1.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Addmember.this, "Added " +name+" Successfully", Toast.LENGTH_SHORT).show();
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
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Addmember.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        }).check();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1  && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Addmember.this,Admin.class);
        startActivity(intent);
    }
}