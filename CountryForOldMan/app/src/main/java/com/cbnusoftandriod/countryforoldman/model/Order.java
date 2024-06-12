package com.cbnusoftandriod.countryforoldman.model;


import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.repository.UserDAO;

public class Order {

    private long ownerid=-1;
    private String phonenumber;
    private String address;
    private String menu;
    private String pay;
    private String price;
    private String userReq;

    public Order( String phonenumber, String address,String menu, String pay, String price, String userReq) {
        this.menu = menu;
        this.pay = pay;
        this.price = price;
        this.userReq = userReq;
        this.phonenumber = phonenumber;
        this.address = address;
    }


    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public String getMenu() {
        return menu;
    }

    public String getPay() {
        return pay;
    }

    public String getPrice() {
        return price;
    }

    public String getUserReq() {
        return userReq;
    }

    public long getOwnerid() {
        return ownerid;
    }
    public void setOwnerid(long ownerid) {
        this.ownerid = ownerid;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUserReq(String userReq) {
        this.userReq = userReq;
    }


}