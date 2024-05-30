package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.repository.ShopRepository;
//import com.cbnusoftandriod.countryforoldman.utils.CoordinateHelper;

public class RegisterShop extends AppCompatActivity {

    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;
    private EditText etShopName;
    private EditText etPhoneNumber;
    private EditText etAddress;
    private Button btnRegisterShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_registershop);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        etShopName = findViewById(R.id.etShopName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        btnRegisterShop = findViewById(R.id.btnRegisterShop);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: // 한식
                        updateSubCategory(R.array.detail_menu_koreanfood);
                        break;
                    case 2: // 일식
                        updateSubCategory(R.array.detail_menu_jpanesefood);
                        break;
                    case 3: // 양식
                        updateSubCategory(R.array.detail_menu_westernfood);
                        break;
                    case 4: // 중식
                        updateSubCategory(R.array.detail_menu_chinesefood);
                        break;
                    case 5: // 간식
                        updateSubCategory(R.array.detail_menu_snack);
                        break;
                    default:
                        spinnerSubCategory.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerSubCategory.setVisibility(View.GONE);
            }
        });

        btnRegisterShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerShop();
            }
        });
    }

    private void updateSubCategory(int arrayResId) {
        spinnerSubCategory.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubCategory.setAdapter(adapter);
    }

    private void registerShop() {
        String shopName = etShopName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String address = etAddress.getText().toString();
        String category = (String) spinnerSubCategory.getSelectedItem();

        // 가게 정보 유효성 검사
        if (shopName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || category.isEmpty()) {
            Toast.makeText(RegisterShop.this, "모든 빈 칸을 채워주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 주소를 위도와 경도로 변환
        //double[] coordinates = CoordinateHelper.getCoordinatesFromAddress(RegisterShop.this, address);
        //if (coordinates == null) {
        //    Toast.makeText(RegisterShop.this, "유효한 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
        //    return;
        //}

        // ShopRepository를 사용하여 가게 등록
        ShopRepository shopRepository = new ShopRepository(RegisterShop.this);
        long newShopId = shopRepository.registerShop(shopName, phoneNumber, address, category);

        if (newShopId != -1) {
            Toast.makeText(RegisterShop.this, "가게 등록 성공!", Toast.LENGTH_SHORT).show();
            // 등록 성공 시 메인 화면으로 이동 또는 다른 적절한 액션
        }
    }
}
