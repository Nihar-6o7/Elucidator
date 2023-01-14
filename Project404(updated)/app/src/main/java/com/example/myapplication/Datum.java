
package com.example.myapplication;


import android.os.Parcel;
import android.os.Parcelable;

public class Datum implements Parcelable {

    private String role;
    private String name;
    private String email;
    private String phoneNo;
    private String usn;
    private String branch;
    private String image;

    public Datum() {

    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Datum(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);
        this.role = data[0];
        this.name = data[1];
        this.email = data[2];
        this.phoneNo = data[3];
        this.usn = data[4];
        this.branch = data[5];
        this.image = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.role, this.name, this.email, this.phoneNo, this.usn, this.branch, this.image});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}

