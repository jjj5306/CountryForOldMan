package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnusoftandriod.countryforoldman.repository.UserRepository;

public class Oldmaninfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldman_myinfo);

        UserRepository userRepository = new UserRepository(this);

        AppCompatButton btn = findViewById(R.id.btn_save);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = findViewById(R.id.et_name);
                EditText etPhonenumber = findViewById(R.id.et_num);
                EditText etPassword = findViewById(R.id.et_given_name);
                EditText etAddress = findViewById(R.id.et_phone_number);

                String username = etName.getText().toString();
                String phonenumber = etPhonenumber.getText().toString();
                String password = etPassword.getText().toString();
                String address = etAddress.getText().toString();

                userRepository.updateUser(username, phonenumber, password, address, false);
            }
        });
    }
}
