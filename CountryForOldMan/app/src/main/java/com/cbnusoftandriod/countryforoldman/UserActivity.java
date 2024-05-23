package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login_oldman); // UserLogin page를 보여줌
    }
}
