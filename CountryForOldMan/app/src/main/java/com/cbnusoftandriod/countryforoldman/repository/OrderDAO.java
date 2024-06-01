package com.cbnusoftandriod.countryforoldman.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbnusoftandriod.countryforoldman.model.Order;
import com.cbnusoftandriod.countryforoldman.repository.DatabaseHelper;

public class OrderDAO {
    private static OrderDAO instance;
    private final DatabaseHelper databaseHelper;
    private final Context context; // context 필드 추가

    public OrderDAO(Context context) {
        this.context = context; // context 필드 초기화
        databaseHelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public static OrderDAO getInstance(Context context) {
        if (instance == null) {
            synchronized (OrderDAO.class) {
                if (instance == null) {
                    instance = new OrderDAO(context);
                }
            }
        }
        return instance;
    }
    public long insert(Order order) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderEntity.COLUMN_NAME_PHONENUMBER, order.getPhonenumber());
        values.put(OrderEntity.COLUMN_NAME_ADDRESS, order.getAddress());
        values.put(OrderEntity.COLUMN_NAME_MENU, order.getMenu());
        values.put(OrderEntity.COLUMN_NAME_PAY, order.getPay());
        values.put(OrderEntity.COLUMN_NAME_PRICE, order.getPrice());
        values.put(OrderEntity.COLUMN_NAME_USER_REQ, order.getUserReq());

        return db.insert(OrderEntity.TABLE_NAME, null, values);
    }

    public long count_row(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + OrderEntity.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getLong(0);
        }
        cursor.close();
        System.out.println(count);
        return count;
    }
}
