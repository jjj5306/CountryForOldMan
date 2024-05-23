package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.repository.UserRepository;

public class SignUpActivity extends AppCompatActivity {

    public EditText etname;
    public EditText etPassword;
    public EditText etConfirmPassword;
    public EditText etPhoneNumber;
    public EditText etAddress;
    public CheckBox ownerCheck;
    private boolean returnValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_signup);

        etname = findViewById(R.id.etname);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etAddress = findViewById(R.id.etAddress);
        ownerCheck = findViewById(R.id.ownerCheck);

        ownerCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                returnValue = isChecked;
            }
        });

        Button successSignup = findViewById(R.id.btnStartCFO);
        successSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UserRepository 객체 생성
                UserRepository userRepository = new UserRepository(SignUpActivity.this); // null 대신 context 전달

                // 회원가입 정보 가져오기
                String name = etname.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String password = etPassword.getText().toString();
                String address = etAddress.getText().toString();

                // 회원가입 정보 유효성 검사
                if (name.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || address.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "모든 빈 칸을 채워주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(etConfirmPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // UserRepository를 사용하여 회원가입 처리
                long newUserId = userRepository.registerUser(name, phoneNumber, password, address, returnValue);

                if (newUserId != -1) {
                    Toast.makeText(SignUpActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
