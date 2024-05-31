package com.cbnusoftandriod.countryforoldman;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.repository.UserDAO;
import com.cbnusoftandriod.countryforoldman.repository.UserRepository;

public class SignUpActivity extends AppCompatActivity {

    public EditText etname;
    public EditText etPassword;
    public EditText etConfirmPassword;
    public EditText etPhoneNumber;
    public EditText etAddress;
    public CheckBox ownerCheck;
    private boolean returnValue;

    public Button btnCheck;

    private String enteredAddress = "";
    public Button btnEnterAddress;


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
        btnCheck = findViewById(R.id.btnCheckName);


        btnEnterAddress = findViewById(R.id.btnEnterAddress);


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

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDAO userDAO = UserDAO.getInstance(SignUpActivity.this);
                String phoneNumber = etPhoneNumber.getText().toString();
                if(userDAO.isUserPhoneNumberExists(phoneNumber))
                    Toast.makeText(SignUpActivity.this, "이미 존재하는 회원입니다.", Toast.LENGTH_SHORT).show();

            }
        });

        //회원가입 화면에서 주소입력 버튼 누르면 주소입력 pop-up이 보이게 해주는 이벤트 처리 부분
        btnEnterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });
    }

    //pop-up관련 함수
    private void showAddressDialog() {
        //Diaglog 객체를 생성하고 현재 액티비티에 context 전달
        final Dialog dialog = new Dialog(SignUpActivity.this);
        dialog.setContentView(R.layout.auth_signup_address);

        //도로명 주소 입력 부분
        final EditText etDetailAddress = dialog.findViewById(R.id.etDetailAddress);
        Button btnDialogSubmit = dialog.findViewById(R.id.btnDialogSubmit);

        //팝업창에서 주소입력 버튼 클릭하면
        btnDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredAddress = etDetailAddress.getText().toString();
                if (enteredAddress.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    btnEnterAddress.setText(enteredAddress);
                    dialog.dismiss();
                }
            }
        });

        //팝업창 크기 조절
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }
}
