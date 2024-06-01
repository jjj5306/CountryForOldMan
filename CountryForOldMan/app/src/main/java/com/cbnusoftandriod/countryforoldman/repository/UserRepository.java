package com.cbnusoftandriod.countryforoldman.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cbnusoftandriod.countryforoldman.model.User;
import com.cbnusoftandriod.countryforoldman.util.GeocoderHelper;

public class UserRepository {
    private final DatabaseHelper databaseHelper;
    private final UserDAO userDAO;
    private final Context context;

    public UserRepository(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        userDAO = UserDAO.getInstance(context); // context 전달하여 초기화
    }

    // 회원가입
    public long registerUser(String username, String phoneNumber, String password, String address, Boolean role) {
        // User 객체 생성
        User user = new User(username, phoneNumber, password, address, role);

        // 주소를 기반으로 좌표 계산
        new GeocodeTask(address, user).execute();
        return -1; // 초기값으로, GeocodeTask에서 성공적으로 처리되면 값이 설정됨
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

    private class GeocodeTask extends AsyncTask<Void, Void, double[]> {
        private final String address;
        private final User user;

        public GeocodeTask(String address, User user) {
            this.address = address;
            this.user = user;
        }

        @Override
        protected double[] doInBackground(Void... params) {
            return GeocoderHelper.getCoordinatesFromAddress(address);
        }

        @Override
        protected void onPostExecute(double[] result) {
            if (result != null) {
                // 좌표를 얻은 후에 UserDAO에 사용자 추가 작업 수행
                long userId = userDAO.insert(user, result);
                if (userId != -1) {
                    Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "유효하지 않은 주소입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
