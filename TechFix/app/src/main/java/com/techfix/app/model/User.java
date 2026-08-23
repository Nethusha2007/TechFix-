package com.techfix.app.model;

/** A registered customer / user account. */
public class User {
    public int id;
    public String fullName;
    public String email;
    public String phone;
    public String password;
    public String address;
    public boolean isAdmin = false;

    public User() {
    }

    public User(int id, String fullName, String email, String phone, String password, String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }
}
