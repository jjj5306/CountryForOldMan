package com.cbnusoftandriod.countryforoldman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnusoftandriod.countryforoldman.model.Order;
import com.cbnusoftandriod.countryforoldman.repository.OrderRepository;

public class Owner_RegisterOrder extends AppCompatActivity {

    private static final String TAG = "OrderActivity";

    private EditText phoneNumberField;
    private EditText addressField;
    private EditText menuField;
    private EditText priceField;
    private EditText notesField;
    private RadioGroup paymentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_registerorder);

        // Find views by ID
        phoneNumberField = findViewById(R.id.phone_number_field);
        addressField = findViewById(R.id.address_field);
        menuField = findViewById(R.id.menu_field);
        priceField = findViewById(R.id.price_field);
        notesField = findViewById(R.id.notes_field);
        paymentGroup = findViewById(R.id.payment_group);

        // Find the button and set a click listener
        AppCompatButton orderButton = findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log and System.out for debugging
                Log.d(TAG, "Button clicked");
                System.out.println("Button clicked");

                // Retrieve data from input fields
                String phoneNumber = phoneNumberField.getText().toString();
                String address = addressField.getText().toString();
                String menu = menuField.getText().toString();
                String price = priceField.getText().toString();
                String notes = notesField.getText().toString();

                // Retrieve selected payment method
                int selectedPaymentId = paymentGroup.getCheckedRadioButtonId();
                RadioButton selectedPaymentButton = findViewById(selectedPaymentId);
                String paymentMethod = selectedPaymentButton != null ? selectedPaymentButton.getText().toString() : "";

                // Create an Order object with the retrieved data
                Order order = new Order(phoneNumber, address, menu, paymentMethod, price, notes);
                OrderRepository orderRepository = new OrderRepository(Owner_RegisterOrder.this);
                orderRepository.registerOrder(order);

                // Intent to navigate to OrderList
                Intent intent = new Intent(Owner_RegisterOrder.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
    }
}
