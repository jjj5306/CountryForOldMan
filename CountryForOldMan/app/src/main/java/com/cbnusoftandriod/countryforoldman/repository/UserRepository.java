package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.widget.Toast;

import com.cbnusoftandriod.countryforoldman.model.User;

public class UserRepository {
    private DatabaseHelper databaseHelper;
    private UserDAO userDAO;
    private Context context;

    public UserRepository(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        userDAO = UserDAO.getInstance(context); // context 전달하여 초기화
    }

    //회원가입
    public long registerUser(String username, String phoneNumber, String password, String address, Boolean role) {
        // User 객체 생성
        User user = new User(username, phoneNumber, password, address, role);

        // UserDAO를 사용하여 데이터베이스에 사용자 추가
        return userDAO.insert(user);
    }

    public User loginUser(String phoneNumber, String password) {
        User user = userDAO.getUserByPhoneNumber(phoneNumber);

        // 사용자가 존재하고 비밀번호가 일치하는 경우 로그인 성공
        if (user == null) {
            Toast.makeText(context, "입력하신 사용자 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return null;
        } else if (!user.getPassword().equals(password)) {
            Toast.makeText(context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show();
            return user;
        }
    }
}
