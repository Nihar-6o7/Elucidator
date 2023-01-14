package com.example.myapplication;

public class Store {
    String name;
    String usn;
    String email;
    String branch;
    String phoneNo;
    String role;

    public String getName() {
        return name;
    }

    public String getUsn() {
        return usn;
    }

    public String getEmail() {
        return email;
    }

    public String getBranch() {
        return branch;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getRole() {
        return role;
    }

    public Store(String name, String usn, String email, String branch, String ph, String role) {
        this.name = name;
        this.usn = usn;
        this.email = email;
        this.branch = branch;
        this.phoneNo = ph;
        this.role = role;
    }
}
