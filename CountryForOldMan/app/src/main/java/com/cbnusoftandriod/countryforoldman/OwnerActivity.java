package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnusoftandriod.countryforoldman.model.Order;
import com.cbnusoftandriod.countryforoldman.repository.OrderRepository;

public class OwnerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login_owner); // Owner LoginPage를 보여줌

        AppCompatButton OrderList = findViewById(R.id.OrderList);
        AppCompatButton RegisterOrderInfo = findViewById(R.id.btnRegisterOrderInfo);
        AppCompatButton EnterDeliveryInfo = findViewById(R.id.buttonEnterDeliveryInfo);
        AppCompatButton ModifyShopInfo = findViewById(R.id.ModifyShopInfo);

        OrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, OrderList.class);
                startActivity(intent);
            }
        });

        RegisterOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, Owner_RegisterOrder.class);
                startActivity(intent);
            }
        });

        EnterDeliveryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, RegisterShopActivity.class);
                startActivity(intent);
            }
        });

        ModifyShopInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, ModifyShopInfo.class);
                startActivity(intent);
            }
        });
    }
}
