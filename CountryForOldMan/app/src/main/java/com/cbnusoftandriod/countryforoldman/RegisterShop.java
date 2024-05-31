package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.repository.ShopRepository;
import com.cbnusoftandriod.countryforoldman.util.GeocoderHelper;


public class RegisterShop extends AppCompatActivity {

    private static final String TAG = "RegisterShop";

    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;
    private TextView etShopName;
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
            Log.d(TAG, "유효성 검사 실패: 빈 칸 존재");
            return;
        }

        // 주소를 위도와 경도로 변환
        double[] coordinates = GeocoderHelper.getCoordinatesFromAddress(RegisterShop.this, address);
        if (coordinates == null) {
            Toast.makeText(RegisterShop.this, "유효한 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "유효하지 않은 주소: " + address);
            return;
        }

        Log.d(TAG, "유효한 주소 변환 성공: 위도 = " + coordinates[0] + ", 경도 = " + coordinates[1]);

        // ShopRepository를 사용하여 가게 등록
        ShopRepository shopRepository = new ShopRepository(RegisterShop.this);
        long newShopId = shopRepository.registerShop(shopName, phoneNumber, address, category);

        if (newShopId != -1) {
            Toast.makeText(RegisterShop.this, "가게 등록 성공!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "가게 등록 성공: ID = " + newShopId);
            // 등록 성공 시 메인 화면으로 이동 또는 다른 적절한 액션
        } else {
            Toast.makeText(RegisterShop.this, "가게 등록 실패!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "가게 등록 실패");
        }
    }
}
