package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login_oldman); // UserLogin page를 보여줌


        AppCompatButton myInfo = findViewById(R.id.my_info);
        AppCompatButton deliveryButton = findViewById(R.id.delivery_button);

        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, Oldmaninfo.class);
                startActivity(intent);
            }
        });
        deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MenuCategory.class);
                startActivity(intent);
            }
        });
    }
}