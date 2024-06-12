package com.cbnusoftandriod.countryforoldman.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.model.Order;
import com.cbnusoftandriod.countryforoldman.model.User;
import com.cbnusoftandriod.countryforoldman.repository.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
        values.put(OrderEntity.COLUMN_NAME_OWNER_ID, order.getOwnerid());

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
        return count;
    }

    public List<Order> getOrdersByOwnerId(long ownerId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                "_id",
                OrderEntity.COLUMN_NAME_OWNER_ID,
                OrderEntity.COLUMN_NAME_PHONENUMBER,
                OrderEntity.COLUMN_NAME_ADDRESS,
                OrderEntity.COLUMN_NAME_MENU,
                OrderEntity.COLUMN_NAME_PAY,
                OrderEntity.COLUMN_NAME_PRICE,
                OrderEntity.COLUMN_NAME_USER_REQ
        };

        String selection = OrderEntity.COLUMN_NAME_OWNER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(ownerId) };

        Cursor cursor = db.query(
                OrderEntity.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(OrderEntity.COLUMN_NAME_PHONENUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(OrderEntity.COLUMN_NAME_ADDRESS));
                String menu = cursor.getString(cursor.getColumnIndexOrThrow(OrderEntity.COLUMN_NAME_MENU));
                String pay = cursor.getString(cursor.getColumnIndexOrThrow(OrderEntity.COLUMN_NAME_PAY));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(OrderEntity.COLUMN_NAME_PRICE));
                String userReq = cursor.getString(cursor.getColumnIndexOrThrow(OrderEntity.COLUMN_NAME_USER_REQ));

                Order order = new Order(phoneNumber, address, menu, pay, price, userReq);
                orders.add(order);
            }
            cursor.close();
        }

        return orders;
    }
}