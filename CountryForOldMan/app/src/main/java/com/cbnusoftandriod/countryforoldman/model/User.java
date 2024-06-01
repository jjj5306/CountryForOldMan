package com.cbnusoftandriod.countryforoldman.model;

public class User {

    private final String username;
    private final String phonenumber;
    private final String password;
    private final String address;
    private final Boolean role;


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