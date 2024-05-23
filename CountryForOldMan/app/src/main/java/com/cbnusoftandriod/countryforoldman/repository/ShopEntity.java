package com.cbnusoftandriod.countryforoldman.repository;

public class ShopEntity {
    public static final String TABLE_NAME = "shop";
    public static final String COLUMN_NAME_SHOPNAME = "shopname";
    public static final String COLUMN_NAME_PHONENUMBER = "phonenumber";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final String COLUMN_NAME_CATEGORY = "category";
    public static final String COLUMN_NAME_OWNERID = "owner_id"; // Owner ID 컬럼 추가
    public static final String SQL_CREATE_SHOP_TABLE =
            "CREATE TABLE " + ShopEntity.TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ShopEntity.COLUMN_NAME_SHOPNAME + " TEXT," +
                    ShopEntity.COLUMN_NAME_PHONENUMBER + " TEXT," +
                    ShopEntity.COLUMN_NAME_ADDRESS + " TEXT," +
                    ShopEntity.COLUMN_NAME_CATEGORY + " TEXT," + // 쉼표 추가
                    ShopEntity.COLUMN_NAME_OWNERID + " INTEGER)"; // Owner ID 컬럼 추가
    public static final String SQL_DELETE_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
