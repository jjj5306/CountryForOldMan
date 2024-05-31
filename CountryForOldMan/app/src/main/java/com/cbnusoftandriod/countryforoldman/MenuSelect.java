package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MenuSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_select);

        LinearLayout menuLayout = findViewById(R.id.menuLayout);

        String menuType = getIntent().getStringExtra("menuType");

        String[] koreanMenu = {"김치찌개", "비빔밥", "불고기"};
        String[] westernMenu = {"피자", "스파게티", "스테이크"};
        String[] chineseMenu = {"짜장면", "짬뽕", "탕수육"};
        String[] japaneseMenu = {"스시", "라멘", "돈부리"};
        String[] snackMenu = {"떡볶이", "순대", "튀김"};

        String[] selectedMenu;

        switch (menuType) {
            case "korean":
                selectedMenu = koreanMenu;
                break;
            case "western":
                selectedMenu = westernMenu;
                break;
            case "chinese":
                selectedMenu = chineseMenu;
                break;
            case "japanese":
                selectedMenu = japaneseMenu;
                break;
            case "snack":
                selectedMenu = snackMenu;
                break;
            default:
                selectedMenu = new String[]{};
                break;
        }

        for (String menu : selectedMenu) {
            Button button = new Button(this);
            button.setText(menu);

            // LinearLayout.LayoutParams 객체를 생성하고 margin 값을 설정
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            int marginInDp = 10; // DP 단위의 margin 값
            int marginInPx = dpToPx(marginInDp); // DP를 픽셀 단위로 변환
            params.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
            button.setTextSize(50);
            button.setTextColor(Color.parseColor("#FFFFFF"));
            button.setLayoutParams(params);

            // 버튼 클릭하면 detail 메뉴로 이동
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // MenuDetail 액티비티로 이동
                    Intent intent = new Intent(MenuSelect.this, MenuDetail.class);
                    startActivity(intent);
                }
            });

            // Drawable 리소스를 배경으로 설정
            button.setBackgroundResource(R.drawable.button_background);

            menuLayout.addView(button);
        }

    }
    // DP를 픽셀 단위로 변환하는 메서드
    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
