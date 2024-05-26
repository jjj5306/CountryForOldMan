package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.security.acl.Owner;

public class OwnerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login_owner); // Owner LoginPage를 보여줌

        AppCompatButton EnterDeliveryInfo = findViewById(R.id.buttonEnterDeliveryInfo);
        EnterDeliveryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerActivity.this, RegisterShop.class);
                startActivity(intent);
            }
        });
    }
}
