package com.cbnusoftandriod.countryforoldman.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.model.Shop;
import com.cbnusoftandriod.countryforoldman.model.User;
import com.cbnusoftandriod.countryforoldman.util.DistanceCalculator;
import com.cbnusoftandriod.countryforoldman.util.GeocoderHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public long insert(Shop shop, double[] coo) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShopEntity.COLUMN_NAME_SHOPNAME, shop.getShopname());
        values.put(ShopEntity.COLUMN_NAME_PHONENUMBER, shop.getPhonenumber());
        values.put(ShopEntity.COLUMN_NAME_ADDRESS, shop.getAddress());
        values.put(ShopEntity.COLUMN_NAME_CATEGORY, shop.getCategory());
        values.put(ShopEntity.COLUMN_NAME_X, coo[0]);
        values.put(ShopEntity.COLUMN_NAME_Y, coo[1]);

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

    public List<Shop> getNearestShops(String userAddress) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Shop> shops = new ArrayList<>();
        String[] columns = {
                ShopEntity.COLUMN_NAME_SHOPNAME,
                ShopEntity.COLUMN_NAME_PHONENUMBER,
                ShopEntity.COLUMN_NAME_ADDRESS,
                ShopEntity.COLUMN_NAME_CATEGORY,
                ShopEntity.COLUMN_NAME_OWNER_ID
        };

        Cursor cursor = db.query(ShopEntity.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String shopName = cursor.getString(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_SHOPNAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_PHONENUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_ADDRESS));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_CATEGORY));
                Long ownerId = cursor.getLong(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_OWNER_ID));

                Shop shop = new Shop(shopName, phoneNumber, address, category, ownerId);
                shops.add(shop);
            }
            cursor.close();
        }

        double[] userCoords = GeocoderHelper.getCoordinatesFromAddress(userAddress);
        if (userCoords == null) {
            return new ArrayList<>(); // 유효한 사용자 주소가 아닌 경우 빈 리스트 반환
        }

        Collections.sort(shops, new Comparator<Shop>() {
            @Override
            public int compare(Shop o1, Shop o2) {
                double[] coords1 = GeocoderHelper.getCoordinatesFromAddress(o1.getAddress());
                double[] coords2 = GeocoderHelper.getCoordinatesFromAddress(o2.getAddress());

                if (coords1 == null || coords2 == null) {
                    return 0;
                }

                double distance1 = DistanceCalculator.calculateDistance(userCoords[0], userCoords[1], coords1[0], coords1[1]);
                double distance2 = DistanceCalculator.calculateDistance(userCoords[0], userCoords[1], coords2[0], coords2[1]);

                return Double.compare(distance1, distance2);
            }
        });

        return shops.size() > 3 ? shops.subList(0, 3) : shops;
    }

    public double[] getCooByAddress(String address) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        double[] coo = new double[2];
        String[] columns = {ShopEntity.COLUMN_NAME_X, ShopEntity.COLUMN_NAME_Y};
        String selection = ShopEntity.COLUMN_NAME_ADDRESS + " = ?";
        String[] selectionArgs = {address};

        Cursor cursor = db.query(ShopEntity.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                coo[0] = cursor.getDouble(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_X));
                coo[1] = cursor.getDouble(cursor.getColumnIndexOrThrow(ShopEntity.COLUMN_NAME_Y));
            }
            cursor.close();
        }

        return coo;
    }
}
