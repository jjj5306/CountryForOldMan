package com.cbnusoftandriod.countryforoldman;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.repository.UserDAO;
import com.cbnusoftandriod.countryforoldman.repository.UserRepository;
import com.cbnusoftandriod.countryforoldman.util.GeocoderHelper;

public class SignUpActivity extends AppCompatActivity {

    public EditText etname;
    public EditText etPassword;
    public EditText etConfirmPassword;
    public EditText etPhoneNumber;
    public TextView etAddress;
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
                UserRepository userRepository = new UserRepository(SignUpActivity.this);

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
                if (userDAO.isUserPhoneNumberExists(phoneNumber))
                    Toast.makeText(SignUpActivity.this, "이미 존재하는 회원입니다.", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(SignUpActivity.this, "사용 가능한 전화번호입니다.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnEnterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });
    }

    private void showAddressDialog() {
        final Dialog dialog = new Dialog(SignUpActivity.this);
        dialog.setContentView(R.layout.auth_signup_address);

        EditText etDialogAddress = dialog.findViewById(R.id.etDialogAddress);

        Button btnDialogValidate = dialog.findViewById(R.id.btnDialogValidate);
        Button btnInputAddress = dialog.findViewById(R.id.btnInputAddress);

        btnDialogValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredAddress = etDialogAddress.getText().toString();
                if (enteredAddress.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 주소 유효성 검사를 수행한 후 주소를 설정
                    new ValidateAddressTask(dialog, enteredAddress).execute();
                }
            }
        });

        btnInputAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredAddress = etDialogAddress.getText().toString();
                if (enteredAddress.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new GetRoadAddressTask(dialog, enteredAddress).execute();
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
                Toast.makeText(SignUpActivity.this, "유효한 주소입니다.", Toast.LENGTH_SHORT).show();
                etAddress.setText(address); // 주소를 TextView에 설정
            } else {
                Toast.makeText(SignUpActivity.this, "유효하지 않은 주소입니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignUpActivity.this, "유효한 주소입니다.", Toast.LENGTH_SHORT).show();
                etAddress.setText(roadAddress); // 도로명 주소를 TextView에 설정
                dialog.dismiss();
            } else {
                Toast.makeText(SignUpActivity.this, "유효하지 않은 주소입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
