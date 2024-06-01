package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.model.Shop;
import com.cbnusoftandriod.countryforoldman.repository.ShopRepository;

import java.util.ArrayList;
import java.util.List;

public class MenuDetail extends AppCompatActivity {
    private static final String TAG = "MenuDetail";
    private TextView shopName1, shopPhone1, shopName2, shopPhone2, shopName3, shopPhone3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select_detailmenu);

        shopName1 = findViewById(R.id.shopName1);
        shopPhone1 = findViewById(R.id.shopPhone1);
        shopName2 = findViewById(R.id.shopName2);
        shopPhone2 = findViewById(R.id.shopPhone2);
        shopName3 = findViewById(R.id.shopName3);
        shopPhone3 = findViewById(R.id.shopPhone3);

        // 선택한 메뉴
        String selectedMenu = getIntent().getStringExtra("selectedMenu");

        // 현재 로그인한 사용자 주소
        String userAddress = MainActivity.getUser().getAddress();
        ShopRepository shopRepository = new ShopRepository(this);
        List<Shop> nearestShops = shopRepository.getNearestShops(userAddress, selectedMenu);

        Log.d(TAG, "가까운 가게 수: " + nearestShops.size());

        // 가게 정보 출력
        if (nearestShops.size() > 0) {
            shopName1.setText(nearestShops.get(0).getShopname());
            shopPhone1.setText(nearestShops.get(0).getPhonenumber());
            shopName1.setVisibility(View.VISIBLE);
            shopPhone1.setVisibility(View.VISIBLE);
            Log.d(TAG, "가게 1: " + nearestShops.get(0).getShopname());
        } else {
            shopName1.setVisibility(View.GONE);
            shopPhone1.setVisibility(View.GONE);
        }

        if (nearestShops.size() > 1) {
            shopName2.setText(nearestShops.get(1).getShopname());
            shopPhone2.setText(nearestShops.get(1).getPhonenumber());
            shopName2.setVisibility(View.VISIBLE);
            shopPhone2.setVisibility(View.VISIBLE);
            Log.d(TAG, "가게 2: " + nearestShops.get(1).getShopname());
        } else {
            shopName2.setVisibility(View.GONE);
            shopPhone2.setVisibility(View.GONE);
        }

        if (nearestShops.size() > 2) {
            shopName3.setText(nearestShops.get(2).getShopname());
            shopPhone3.setText(nearestShops.get(2).getPhonenumber());
            shopName3.setVisibility(View.VISIBLE);
            shopPhone3.setVisibility(View.VISIBLE);
            Log.d(TAG, "가게 3: " + nearestShops.get(2).getShopname());
        } else {
            shopName3.setVisibility(View.GONE);
            shopPhone3.setVisibility(View.GONE);
        }
    }
}
