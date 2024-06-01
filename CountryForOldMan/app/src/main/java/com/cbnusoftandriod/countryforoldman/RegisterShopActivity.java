package com.cbnusoftandriod.countryforoldman;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnusoftandriod.countryforoldman.repository.ShopRepository;
import com.cbnusoftandriod.countryforoldman.util.GeocoderHelper;

public class RegisterShopActivity extends AppCompatActivity {

    private static final String TAG = "RegisterShop";

    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;
    private EditText etShopName;
    private EditText etPhoneNumber;
    private TextView shop_etAddress;
    private Button btnRegisterShop;

    //주소입력을 위한 변수들 - 2024-06-01
    private AppCompatButton shop_btnEnterAddress;
    private String shop_enteredAddress = "";
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_registershop);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        etShopName = findViewById(R.id.editTextShopName); // ID 수정
        etPhoneNumber = findViewById(R.id.editTextPhoneNumber); // ID 수정
        shop_etAddress = findViewById(R.id.shop_etAddress);
        btnRegisterShop = findViewById(R.id.btnRegisterShop);
        shop_btnEnterAddress = findViewById(R.id.shop_btnEnterAddress);

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

        shop_btnEnterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
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
        String address = shop_etAddress.getText().toString();
        String category = (String) spinnerSubCategory.getSelectedItem();

        // 가게 정보 유효성 검사
        if (shopName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || category.isEmpty()) {
            Toast.makeText(RegisterShopActivity.this, "모든 빈 칸을 채워주세요!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "유효성 검사 실패: 빈 칸 존재");
            return;
        }

        // 주소를 위도와 경도로 변환


        // ShopRepository를 사용하여 가게 등록
        ShopRepository shopRepository = new ShopRepository(RegisterShopActivity.this);
        long newShopId = shopRepository.registerShop(shopName, phoneNumber, address, category);

        if (newShopId != -1) {
            Toast.makeText(RegisterShopActivity.this, "가게 등록 성공!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "가게 등록 성공: ID = " + newShopId);
            // 등록 성공 시 메인 화면으로 이동 또는 다른 적절한 액션
            Intent intent = new Intent(RegisterShopActivity.this, OwnerActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterShopActivity.this, "가게 등록 실패!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "가게 등록 실패");
        }
    }

    ////////////////////////////////주소 입력 관련 함수//////////////////////////////////

    private void showAddressDialog() {
        final Dialog dialog = new Dialog(RegisterShopActivity.this);
        dialog.setContentView(R.layout.owner_registershop_address);

        EditText shop_etDialogAddress = dialog.findViewById(R.id.shop_etDialogAddress);

        Button shop_btnDialogValidate = dialog.findViewById(R.id.shop_btnDialogValidate);
        Button shop_btnInputAddress = dialog.findViewById(R.id.shop_btnInputAddress);

        shop_btnDialogValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_enteredAddress = shop_etDialogAddress.getText().toString();
                if (shop_enteredAddress.isEmpty()) {
                    Toast.makeText(RegisterShopActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 주소 유효성 검사를 수행한 후 주소를 설정
                    new RegisterShopActivity.ValidateAddressTask(dialog, shop_enteredAddress).execute();
                }
            }
        });

        shop_btnInputAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_enteredAddress = shop_etDialogAddress.getText().toString();
                if (shop_enteredAddress.isEmpty()) {
                    Toast.makeText(RegisterShopActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new RegisterShopActivity.GetRoadAddressTask(dialog, shop_enteredAddress).execute();
                }
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private class ValidateAddressTask extends AsyncTask<Void, Void, double[]> {
        private Dialog dialog;
        private String address;

        public ValidateAddressTask(Dialog dialog, String address) {
            this.dialog = dialog;
            this.address = address;
        }

        @Override
        protected double[] doInBackground(Void... params) {
            return GeocoderHelper.getCoordinatesFromAddress(address);
        }

        @Override
        protected void onPostExecute(double[] result) {
            if (result != null) {
                Toast.makeText(RegisterShopActivity.this, "유효한 주소입니다.", Toast.LENGTH_SHORT).show();
                shop_etAddress.setText(address); // 주소를 TextView에 설정
            } else {
                Toast.makeText(RegisterShopActivity.this, "유효하지 않은 주소입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetRoadAddressTask extends AsyncTask<Void, Void, String> {
        private Dialog dialog;
        private String address;

        public GetRoadAddressTask(Dialog dialog, String address) {
            this.dialog = dialog;
            this.address = address;
        }

        @Override
        protected String doInBackground(Void... params) {
            return GeocoderHelper.getRoadAddressFromAddress(address);
        }

        @Override
        protected void onPostExecute(String roadAddress) {
            if (roadAddress != null) {
                Toast.makeText(RegisterShopActivity.this, "유효한 주소입니다.", Toast.LENGTH_SHORT).show();
                shop_etAddress.setText(roadAddress); // 도로명 주소를 TextView에 설정
                dialog.dismiss();
            } else {
                Toast.makeText(RegisterShopActivity.this, "유효하지 않은 주소입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
