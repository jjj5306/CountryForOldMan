package com.cbnusoftandriod.countryforoldman.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.model.Shop;
import com.cbnusoftandriod.countryforoldman.model.User;

public class ShopDAO {
    private static ShopDAO instance;
    private final DatabaseHelper databaseHelper;
    private final Context context; // context 필드 추가

    private ShopDAO(Context context) {
        this.context = context; // context 필드 초기화
        databaseHelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public static ShopDAO getInstance(Context context) {
        if (instance == null) {
            synchronized (ShopDAO.class) {
                if (instance == null) {
                    instance = new ShopDAO(context);
                }
            }
        }
        return instance;
    }

    // 가게 추가
    public long insert(Shop shop) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShopEntity.COLUMN_NAME_SHOPNAME, shop.getShopname());
        values.put(ShopEntity.COLUMN_NAME_PHONENUMBER, shop.getPhonenumber());
        values.put(ShopEntity.COLUMN_NAME_ADDRESS, shop.getAddress());
        values.put(ShopEntity.COLUMN_NAME_CATEGORY, shop.getCategory());

        // 현재 로그인 한 사용자 정보 얻어오기
        User user = MainActivity.getUser();
        UserDAO userDAO = UserDAO.getInstance(context); // context 전달

        values.put(ShopEntity.COLUMN_NAME_OWNER_ID, userDAO.getIdByPhoneNumber(user.getPhonenumber()));

        // 데이터베이스에 가게 추가 작업 수행
        return db.insert(ShopEntity.TABLE_NAME, null, values);
    }

    public String getPhoneNumberById(Integer _id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String phoneNumber = null;
        String[] columns = {ShopEntity.COLUMN_NAME_PHONENUMBER};
        String selection = "_id = ?";
        String[] selectionArgs = {String.valueOf(_id)};

        Cursor cursor = db.query(ShopEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_PHONENUMBER));
            }
            cursor.close();
        }
        return phoneNumber;
    }
}
