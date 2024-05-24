package com.cbnusoftandriod.countryforoldman.model;

public class Shop {

    private String shopname;

    private String phonenumber;

    private String address;

    private String category;

    private String owner_id;

    /**
     * owner_id는 로그인 시 로그인한 사용자의 id를 기억해둬야함.
     * @param shopname
     * @param phonenumber
     * @param address
     * @param category
     * @param owner_id
     */
    public Shop(String shopname, String phonenumber, String address, String category, String owner_id) {
        this.shopname = shopname;
        this.phonenumber = phonenumber;
        this.address = address;
        this.category = category;
        this.owner_id = owner_id;
    }

    public String getShopname() {
        return shopname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() { return address; }

    public String getCategory() {
        return category;
    }

    public String getOwner_id() {
        return owner_id;
    }
}
