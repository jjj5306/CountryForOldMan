package com.cbnusoftandriod.countryforoldman.model;

import android.content.Context;

import com.cbnusoftandriod.countryforoldman.repository.UserDAO;

public class User {

    private String username;
    private String phonenumber;
    private String password;
    private String address;
    private Boolean role;


    public User(String username, String phonenumber, String password, String address, Boolean role) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.password = password;
        this.address = address;
        this.role = role;


    }

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getRole() {
        return role;
    }

}