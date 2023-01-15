package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Addmember extends AppCompatActivity {

    StorageReference reference= FirebaseStorage.getInstance().getReference();
    EditText usn1;
    String usn,image;
    EditText name1;
    EditText branch1;
    EditText role1;
    EditText ph1;
    EditText email1;
    Button upload;
    Button add;
    ImageView img;
    DatabaseReference db,dbr;
    Uri imageUri;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth1;
    FirebaseUser mUser;

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            img.setImageBitmap(selectedImageBitmap);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_left);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFF2EEEB")));

        usn1=findViewById(R.id.usn3);
        email1=findViewById(R.id.email3);
        ph1=findViewById(R.id.ph3);
        name1=findViewById(R.id.name3);
        branch1=findViewById(R.id.branch3);
        role1=findViewById(R.id.role3);
        img=findViewById(R.id.img);
        mAuth1=FirebaseAuth.getInstance();
        mUser=mAuth1.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        upload=findViewById(R.id.browse);
        add=findViewById(R.id.addbutton);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Addmember.this).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent galleryIntent=new Intent();
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        launchSomeActivity.launch(galleryIntent);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usn = usn1.getText().toString().trim().toUpperCase();
                String name = name1.getText().toString().trim();
                String branch = branch1.getText().toString().trim().toUpperCase();
                String role = role1.getText().toString().trim();
                String phoneNo = ph1.getText().toString().trim();
                String email = email1.getText().toString().trim().toLowerCase();
                String password = usn.toLowerCase();
                if(usn.isEmpty() || name.isEmpty() || branch.isEmpty() || role.isEmpty() || phoneNo.isEmpty() || email.isEmpty() || imageUri == null){
                    Toast.makeText(Addmember.this,"Please enter the required details",Toast.LENGTH_SHORT).show();
                }
                else if(usn.length()<6){
                    usn1.setError("usn cannot be less than 6 characters");
                }
                else {
                    progressDialog.setTitle("Uploading data");
                    progressDialog.show();

                    StorageReference fileRef = reference.child(usn);
                    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    //Toast.makeText(Addmember.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    image = uri.toString();
                                    Store s = new Store(name, usn, email, branch, phoneNo, role, image);
                                    db = FirebaseDatabase.getInstance().getReference().child("FLC-members");
                                    db.child(usn).setValue(s);

                                    try {
                                        mAuth1.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Addmember.this, "Added " + name + " Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    progressDialog.dismiss();
                                                    //Toast.makeText(Addmember.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    catch (Exception e) {
                                    }
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
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded : " + (int) percent + " %");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Addmember.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}