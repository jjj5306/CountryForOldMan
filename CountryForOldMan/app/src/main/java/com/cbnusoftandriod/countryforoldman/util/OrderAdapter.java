package com.cbnusoftandriod.countryforoldman.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbnusoftandriod.countryforoldman.R;
import com.cbnusoftandriod.countryforoldman.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.phoneNumberTextView.setText("전화번호: " + order.getPhonenumber());
        holder.addressTextView.setText("주소: " + order.getAddress());
        holder.menuTextView.setText("메뉴: " + order.getMenu());
        holder.paymentMethodTextView.setText("결제 방법: " + order.getPay());
        holder.priceTextView.setText("가격: " + order.getPrice());
        holder.userRequirementTextView.setText("사용자 요청사항: " + order.getUserReq());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView phoneNumberTextView, addressTextView, menuTextView, paymentMethodTextView, priceTextView, userRequirementTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumberTextView = itemView.findViewById(R.id.phone_number_text_view);
            addressTextView = itemView.findViewById(R.id.address_text_view);
            menuTextView = itemView.findViewById(R.id.menu_text_view);
            paymentMethodTextView = itemView.findViewById(R.id.payment_method_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            userRequirementTextView = itemView.findViewById(R.id.user_requirement_text_view);
        }
    }
}
