package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.model.Order;
import com.cbnusoftandriod.countryforoldman.model.User;
import com.cbnusoftandriod.countryforoldman.repository.OrderRepository;
import com.cbnusoftandriod.countryforoldman.repository.UserDAO;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private LinearLayout orderContainer;
    private OrderRepository orderRepository;
    private List<Order> orders;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_orderlist_page);

        orderContainer = findViewById(R.id.orderContainer);

        // UserDAO 초기화
        userDAO = new UserDAO(this);

        // 현재 사용자 가져오기
        User user = MainActivity.getUser();

        // 사용자의 전화번호로 소유자 ID 가져오기
        long owner = userDAO.getIdByPhoneNumber(user.getPhonenumber());

        // OrderRepository 초기화
        orderRepository = new OrderRepository(this);

        // 실제 데이터 가져오기
        orders = orderRepository.getOrderList(owner); // 실제 소유자 ID를 사용

        // 동적으로 주문 뷰 추가
        for (Order order : orders) {
            addOrderView(order);
        }
    }

    private void addOrderView(Order order) {
        View orderView = LayoutInflater.from(this).inflate(R.layout.owner_orderlist, orderContainer, false);

        TextView orderPhoneNumber = orderView.findViewById(R.id.order_phoneNumber);
        TextView orderAddress = orderView.findViewById(R.id.order_address);
        TextView orderFoodName = orderView.findViewById(R.id.order_foodname);
        TextView productPrice = orderView.findViewById(R.id.product_price);
        TextView orderPaymentMethod = orderView.findViewById(R.id.order_paymentMethod);
        TextView orderRequests = orderView.findViewById(R.id.order_requests);

        orderPhoneNumber.setText(order.getPhonenumber());
        orderAddress.setText(order.getAddress());
        orderFoodName.setText(order.getMenu());
        productPrice.setText(order.getPrice());
        orderPaymentMethod.setText(order.getPay());
        orderRequests.setText(order.getUserReq());

        orderContainer.addView(orderView);
    }
}
