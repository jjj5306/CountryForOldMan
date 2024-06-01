package com.cbnusoftandriod.countryforoldman;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnusoftandriod.countryforoldman.model.Order;

public class OrderActivity extends AppCompatActivity {

    public EditText menuField;
    public EditText priceField;
    public EditText notesField;
    public RadioGroup paymentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_registerorder);

        // Find views by ID
        menuField = findViewById(R.id.menu_field);
        priceField = findViewById(R.id.price_field);
        notesField = findViewById(R.id.notes_field);
        paymentGroup = findViewById(R.id.payment_group);

        // Find the button and set a click listener
        Button orderButton = findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderButtonClick();
            }
        });


    }

    public void handleOrderButtonClick() {


        // Retrieve data from input fields
        String menu = menuField.getText().toString();
        String price = priceField.getText().toString();
        String notes = notesField.getText().toString();

        // Retrieve selected payment method
        int selectedPaymentId = paymentGroup.getCheckedRadioButtonId();
        RadioButton selectedPaymentButton = findViewById(selectedPaymentId);
        String paymentMethod = selectedPaymentButton != null ? selectedPaymentButton.getText().toString() : "";

        // System.out for debugging

        // Create an Order object with the retrieved data
        Order order = new Order(menu, paymentMethod, price, notes);

        // Display the Order object information using a Toast
        Toast.makeText(this, "Order:\n" + order.toString(), Toast.LENGTH_LONG).show();

        

        // TODO: Implement the logic to send the Order object to the server
    }
}
