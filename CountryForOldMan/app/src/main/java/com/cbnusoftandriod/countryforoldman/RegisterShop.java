package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterShop extends AppCompatActivity {

    //Spinner : html의 <select> - <option> 과 바슷하게 여러개중 하나 고를때 사용하는 위젯, 보통 ComboBox라고 불러용
    //이 자바클래스에서는
    //메인 spinner : 한식, 양식, 중식, 일식, 스낵
    //sub spinner : 에서는 각 카테고리 별 세부 spinner를 지정
    //adapter : 데이터와 spinner를 이어줌
    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_registershop);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);

        //setOnItemSelectedListener는 Android의 Spinner 또는 AdapterView와 같은 위젯에서 항목이 선택될 때 호출되는
        //이벤트 리스너를 설정하는 메서드. 이 리스너를 통해 사용자가 항목을 선택했을 때 특정 동작을 수행함(like onclick)
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //선택된 항목의 위치를 position 변수를 이용해 지정
            //<?>은 generic 타입으로 어떤 타입든지 올수 있음을 의미
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: // 한식
                        updateSubCategory(R.array.detail_menu_koreanfood);
                        break;
                    case 2: // 일식
                        updateSubCategory(R.array.detail_menu_westernfood);
                        break;
                    case 3: // 양식
                        updateSubCategory(R.array.detail_menu_jpanesefood);
                        break;
                    case 4: // 중식
                        updateSubCategory(R.array.detail_menu_chinesefood);
                        break;
                    case 5: // 간식
                        updateSubCategory(R.array.detail_menu_snack);
                        break;
                    default: //이거 없어도 되는뎅 명시해주는게 좋은듯
                        spinnerSubCategory.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            //Spinner에서 아무 항목도 선택되지 않을때 --> 세부메뉴 spinner 안보임
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerSubCategory.setVisibility(View.GONE);
            }
        });
    }


    private void updateSubCategory(int arrayResId) {
        //상세 메뉴 카테고리가 보이도록 설정
        spinnerSubCategory.setVisibility(View.VISIBLE);

        //ArrayAdapter 생성 및 설정 --> this : 현재 엑티비티 ; arrayResId : 사용될 배열 리소스의 ID(여기서는 menu_array에 있는 아이디
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);

        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategory.setAdapter(adapter);
    }
}
