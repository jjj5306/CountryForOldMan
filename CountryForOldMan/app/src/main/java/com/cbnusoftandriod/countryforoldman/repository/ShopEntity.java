package com.cbnusoftandriod.countryforoldman.repository;

public class ShopEntity {
    public static final String TABLE_NAME = "shop";
    public static final String COLUMN_NAME_SHOPNAME = "shopname";
    public static final String COLUMN_NAME_PHONENUMBER = "phonenumber";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final String COLUMN_NAME_CATEGORY = "category";
    public static final String COLUMN_NAME_OWNER_ID = "owner_id";
    public static final String COLUMN_NAME_X = "x";
    public static final String COLUMN_NAME_Y = "y";

    public static final String SQL_CREATE_SHOP_TABLE =
            "CREATE TABLE " + ShopEntity.TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ShopEntity.COLUMN_NAME_SHOPNAME + " TEXT, " +
                    ShopEntity.COLUMN_NAME_PHONENUMBER + " TEXT, " +
                    ShopEntity.COLUMN_NAME_ADDRESS + " TEXT, " +
                    ShopEntity.COLUMN_NAME_CATEGORY + " TEXT, " +
                    ShopEntity.COLUMN_NAME_OWNER_ID + " INTEGER, " +
                    ShopEntity.COLUMN_NAME_X + " REAL, " +
                    ShopEntity.COLUMN_NAME_Y + " REAL, " +
                    "FOREIGN KEY(" + ShopEntity.COLUMN_NAME_OWNER_ID + ") REFERENCES " +
                    UserEntity.TABLE_NAME + "(_id) ON DELETE CASCADE);";

    public static final String SQL_DELETE_SHOP_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
