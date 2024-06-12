package com.cbnusoftandriod.countryforoldman.repository;


import android.content.Context;
import android.widget.Toast;

import com.cbnusoftandriod.countryforoldman.MainActivity;
import com.cbnusoftandriod.countryforoldman.model.Order;
import com.cbnusoftandriod.countryforoldman.model.User;

import java.util.List;

public class OrderRepository {
    private DatabaseHelper databaseHelper;
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private Context context;
    User user= MainActivity.getUser();


    public OrderRepository(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
        orderDAO = OrderDAO.getInstance(context); // context 전달하여 초기화
        userDAO = UserDAO.getInstance(context); // UserDAO 초기화

    }
    public long registerOrder(Order order) {
        // User 객체 생성
        if(user==null){
            Toast.makeText(context, "로그인 정보가 필요합니다.", Toast.LENGTH_SHORT).show();
            return -1;
        }
        order.setOwnerid(userDAO.getIdByPhoneNumber(user.getPhonenumber()));
        // UserDAO를 사용하여 데이터베이스에 사용자 추가
        return orderDAO.insert(order);
    }
    public long row_count(){
        return orderDAO.count_row();
    }


    public List<Order> getOrderList(long ownerid){
        return orderDAO.getOrdersByOwnerId(ownerid);
    }
}