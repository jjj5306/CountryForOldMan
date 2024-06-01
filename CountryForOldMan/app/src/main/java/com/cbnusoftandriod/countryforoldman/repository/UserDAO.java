package com.cbnusoftandriod.countryforoldman.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbnusoftandriod.countryforoldman.model.User;

public class UserDAO {
    private static UserDAO instance;
    private final DatabaseHelper databaseHelper;

    private UserDAO(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public static UserDAO getInstance(Context context) {
        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) {
                    instance = new UserDAO(context);
                }
            }
        }
        return instance;
    }

    // 사용자 추가
    public long insert(User user, double[] coo) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntity.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(UserEntity.COLUMN_NAME_PHONENUMBER, user.getPhonenumber());
        values.put(UserEntity.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(UserEntity.COLUMN_NAME_ADDRESS, user.getAddress());
        values.put(UserEntity.COLUMN_NAME_ROLE, user.getRole() ? 1 : 0);
        values.put(UserEntity.COLUMN_NAME_X, coo[0]);
        values.put(UserEntity.COLUMN_NAME_Y, coo[1]);

        // 데이터베이스에 사용자 추가 작업 수행
        return db.insert(UserEntity.TABLE_NAME, null, values);
    }

    public Long getIdByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = databaseHelper.getDatabase();
        Long id = null;
        String[] columns = {"_id"};
        String selection = "phonenumber = ?";
        String[] selectionArgs = {phoneNumber};

        Cursor cursor = db.query(UserEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            }
            cursor.close();
        }

        return id;
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = databaseHelper.getDatabase();
        User user = null;
        String[] columns = {
                UserEntity.COLUMN_NAME_USERNAME,
                UserEntity.COLUMN_NAME_PHONENUMBER,
                UserEntity.COLUMN_NAME_PASSWORD,
                UserEntity.COLUMN_NAME_ADDRESS,
                UserEntity.COLUMN_NAME_ROLE
        };
        String selection = "phonenumber = ?";
        String[] selectionArgs = {phoneNumber};

        Cursor cursor = db.query(UserEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_PASSWORD));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_ADDRESS));
                boolean role = cursor.getInt(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_ROLE)) == 1;

                user = new User(username, phoneNumber, password, address, role);
                //2024.05.18 유저 객체 생성할때 context를 null로 줘도 상관없는건지 모르겠음
            }
            cursor.close();
        }

        return user;
    }

    public double[] getCooByAddress(String address) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        double[] coo = new double[2];
        String[] columns = {UserEntity.COLUMN_NAME_X, UserEntity.COLUMN_NAME_Y};
        String selection = UserEntity.COLUMN_NAME_ADDRESS + " = ?";
        String[] selectionArgs = {address};

        Cursor cursor = db.query(UserEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                coo[0] = cursor.getDouble(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_X));
                coo[1] = cursor.getDouble(cursor.getColumnIndexOrThrow(UserEntity.COLUMN_NAME_Y));
            }
            cursor.close();
        }

        return coo;
    }

    public boolean isUserPhoneNumberExists(String phonenumber) { //존재하면 true 없으면 false
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            String query = "SELECT 1 FROM user WHERE phonenumber = ? LIMIT 1";
            cursor = db.rawQuery(query, new String[]{phonenumber});
            exists = cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            //db.close();
        }
        return exists;
    }
}
