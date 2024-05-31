package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MenuCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_category);

        AppCompatButton koreanButton = findViewById(R.id.koreanButton);
        AppCompatButton westernButton = findViewById(R.id.westernButton);
        AppCompatButton chineseButton = findViewById(R.id.chineseButton);
        AppCompatButton japaneseButton = findViewById(R.id.japaneseButton);
        AppCompatButton snackButton = findViewById(R.id.snackButton);

        koreanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenu("korean");
            }
        });

        westernButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenu("western");
            }
        });

        chineseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenu("chinese");
            }
        });

        japaneseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenu("japanese");
            }
        });

        snackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenu("snack");
            }
        });
    }

    private void navigateToMenu(String menuType) {
        Intent intent = new Intent(MenuCategory.this, MenuSelect.class);
        intent.putExtra("menuType", menuType);
        startActivity(intent);
    }
}
