package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.model.Shop;
import com.cbnusoftandriod.countryforoldman.model.User;
import com.cbnusoftandriod.countryforoldman.util.DistanceCalculator;
import com.cbnusoftandriod.countryforoldman.util.GeocoderHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShopRepository {
    private static final String TAG = "MenuDetail";

    private final DatabaseHelper databaseHelper;
    private final ShopDAO shopDAO;
    private final UserDAO userDAO;
    private final Context context;

    public ShopRepository(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        shopDAO = ShopDAO.getInstance(context); // context 전달하여 초기화
        userDAO = UserDAO.getInstance(context); // UserDAO 초기화
    }

    // 가게 등록
    public long registerShop(String shopname, String phonenumber, String address, String category) {
        // 현재 로그인한 사용자 정보 얻기
        User currentUser = MainActivity.getUser();
        if (currentUser == null) {
            Toast.makeText(context, "로그인 정보가 필요합니다.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        // UserDAO를 사용하여 사용자 ID 얻기
        Long userId = userDAO.getIdByPhoneNumber(currentUser.getPhonenumber());
        if (userId == null) {
            Toast.makeText(context, "유효한 사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        // Shop 객체 생성
        Shop shop = new Shop(shopname, phonenumber, address, category, userId);

        // 주소를 기반으로 좌표 계산
        new GeocodeTask(address, shop, userId).execute();
        return -1; // 초기값으로, GeocodeTask에서 성공적으로 처리되면 값이 설정됨
    }

    private class GeocodeTask extends AsyncTask<Void, Void, double[]> {
        private final String address;
        private final Shop shop;
        private final Long userId;

        public GeocodeTask(String address, Shop shop, Long userId) {
            this.address = address;
            this.shop = shop;
            this.userId = userId;
        }

        @Override
        protected double[] doInBackground(Void... params) {
            return GeocoderHelper.getCoordinatesFromAddress(address);
        }

        @Override
        protected void onPostExecute(double[] result) {
            if (result != null) {
                // 좌표를 얻은 후에 ShopDAO에 가게 추가 작업 수행
                long shopId = shopDAO.insert(shop, result);
                if (shopId != -1) {
                    Toast.makeText(context, "가게 등록 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "가게 등록 실패!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "유효하지 않은 주소입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<Shop> getNearestShops(String userAddress, String selectedCategory) {
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

                // 선택한 카테고리와 일치하는 가게만 추가
                if (category.equalsIgnoreCase(selectedCategory)) {
                    Shop shop = new Shop(shopName, phoneNumber, address, category, ownerId);
                    shops.add(shop);
                }
            }
            cursor.close();
        }

        double[] userCoords = userDAO.getCooByAddress(userAddress);
        if (userCoords == null) {
            Log.d(TAG, "유효하지 않은 사용자 주소");
            return new ArrayList<>(); // 유효한 사용자 주소가 아닌 경우 빈 리스트 반환
        }

        Log.d(TAG, "사용자 좌표: " + userCoords[0] + ", " + userCoords[1]);

        Collections.sort(shops, new Comparator<Shop>() {
            @Override
            public int compare(Shop o1, Shop o2) {
                double[] coords1 = shopDAO.getCooByAddress(o1.getAddress());
                double[] coords2 = shopDAO.getCooByAddress(o2.getAddress());

                if (coords1 == null || coords2 == null) {
                    return 0;
                }

                double distance1 = DistanceCalculator.calculateDistance(userCoords[1], userCoords[0], coords1[1], coords1[0]);
                double distance2 = DistanceCalculator.calculateDistance(userCoords[1], userCoords[0], coords2[1], coords2[0]);

                return Double.compare(distance1, distance2);
            }
        });

        for (Shop shop : shops) {
            double[] coords = shopDAO.getCooByAddress(shop.getAddress());
            if (coords != null) {
                double distance = DistanceCalculator.calculateDistance(userCoords[1], userCoords[0], coords[1], coords[0]);
                Log.d(TAG, "가게: " + shop.getShopname() + ", 카테고리: " + shop.getCategory() + ", 거리: " + distance + "km");
            }
        }

        return shops.size() > 3 ? shops.subList(0, 3) : shops;
    }
}
