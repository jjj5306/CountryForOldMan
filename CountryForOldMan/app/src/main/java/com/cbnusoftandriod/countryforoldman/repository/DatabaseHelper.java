package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "countryforoldmap.db";
    private static final int DATABASE_VERSION = 12;
    private static volatile DatabaseHelper databaseHelper = null;
    private SQLiteDatabase database = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 싱글톤 패턴 구현
     *
     * @param context
     * @return
     */
    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserEntity.SQL_CREATE_USER_TABLE);
        db.execSQL(ShopEntity.SQL_CREATE_SHOP_TABLE);
        db.execSQL(OrderEntity.SQL_CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserEntity.SQL_DELETE_USER_ENTRIES);
        db.execSQL(ShopEntity.SQL_DELETE_SHOP_ENTRIES);
        db.execSQL(OrderEntity.SQL_DELETE_ORDER_ENTRIES);
        onCreate(db);
    }

    public synchronized SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            database = getWritableDatabase();
        }
        return database;
    }
}
