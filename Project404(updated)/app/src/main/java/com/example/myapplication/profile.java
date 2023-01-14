package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

public class profile {

    public void next(Datum d, Context context)
    {
        String name=d.getName();
        String branch=d.getBranch();
        String usn=d.getUsn();
        String phone_no=d.getPhoneNo();
        String email=d.getEmail();
        String role=d.getRole();
        String img=d.getImage();

        Intent intent=new Intent(context, searchById.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Name",name);
        intent.putExtra("USN",usn);
        intent.putExtra("Branch",branch);
        intent.putExtra("Phone",phone_no);
        intent.putExtra("Email",email);
        intent.putExtra("Role",role);
        intent.putExtra("Image",img);
        context.startActivity(intent);
    }
}
