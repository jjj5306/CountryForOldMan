package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.widget.Toast;

import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.model.Shop;
import com.cbnusoftandriod.countryforoldman.model.User;

public class ShopRepository {
    private DatabaseHelper databaseHelper;
    private ShopDAO shopDAO;
    private UserDAO userDAO;
    private Context context;

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

        // ShopDAO를 사용하여 데이터베이스에 가게 추가
        long shopId = shopDAO.insert(shop);

        if (shopId != -1) {
            Toast.makeText(context, "가게 등록 성공!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "가게 등록 실패!", Toast.LENGTH_SHORT).show();
        }

        return shopId;
    }
}
