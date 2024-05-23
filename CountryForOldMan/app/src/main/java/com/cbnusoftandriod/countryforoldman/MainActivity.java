package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnusoftandriod.countryforoldman.model.User;
import com.cbnusoftandriod.countryforoldman.repository.DatabaseHelper;
import com.cbnusoftandriod.countryforoldman.repository.UserDAO;
import com.cbnusoftandriod.countryforoldman.repository.UserRepository;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    private EditText etPhoneNumber;
    private EditText etPassword;
    private UserDAO userDAO;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_firstpage);
        // 데이터베이스 생성
        databaseHelper = DatabaseHelper.getInstance(this);
        databaseHelper.getDatabase();



        // 첫 화면에서 사용자가 입력한 전화번호와 비밀번호
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);

        Button LoginButton = findViewById(R.id.LoginButton);

        // 로그인 버튼 클릭시 로그인 함수 호출
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhoneNumber.getText().toString();
                String password = etPassword.getText().toString();

                // 전화번호 or 비밀번호 입력 안되면 알림창 출력
                if (phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "입력되지 않은 부분이 있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // UserRepository를 사용하여 로그인 처리
                UserRepository userRepository = new UserRepository(MainActivity.this);
                User user = userRepository.loginUser(phoneNumber, password);

                // 로그인 정보와 일치하는 유저가 있을 경우 권한 분류 코드 실행
                if (user != null) {
                    if (user.getRole()) {
                        // Owner 페이지로 이동
                        Intent intent = new Intent(MainActivity.this, OwnerActivity.class);
                        startActivity(intent);
                    } else {
                        // 일반 사용자 페이지로 이동
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        //회원가입버튼 누르면 회원가입 창으로 이동하는 부분
        Button btnSignup = findViewById(R.id.SignUpButton);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    }
}