package com.example.digimart;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Invoice extends AppCompatActivity {

    private ListView productListView;
    private TextView userInfoTextView, dateTextView, totalAmountTextView;
    private Button generatePDFButton;
    private List<Product1> productList; // Replace with your product data structure

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize your views
        userInfoTextView = findViewById(R.id.userInfoTextView);
        dateTextView = findViewById(R.id.dateTextView);
        productListView = findViewById(R.id.productListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        generatePDFButton = findViewById(R.id.generatePDFButton);

        // Fetch user data (name, phone, email) from your database
        String userName = "John Doe"; // Replace with actual user data
        String userPhone = "12345"; // Replace with actual user data
        String userEmail = "john.doe@example.com"; // Replace with actual user data

        // Populate user information
        String userInfo = "User: " + userName + "\nPhone: " + userPhone + "\nEmail: " + userEmail;
        userInfoTextView.setText(userInfo);

        // Populate date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String dateTime = dateFormat.format(new Date());
        dateTextView.setText("Date and Time: " + dateTime);

        // Fetch product data from your database
        productList = getProductsFromDatabase(); // Implement this method

        // Create a custom adapter for the product list
        ProductListAdapter1 adapter = new ProductListAdapter1(this, productList);
        productListView.setAdapter(adapter);

        // Calculate and display the total amount
        double totalAmount = calculateTotalAmount(productList); // Implement this method
        totalAmountTextView.setText("Total Amount: $" + totalAmount);


    }
    private List<Product1> getProductsFromDatabase() {
        // Replace with actual database query
        List<Product1> productList = new ArrayList<>();

        // Sample data
        productList.add(new Product1("Product A", 10.0, 2));
        productList.add(new Product1("Product B", 15.0, 3));
        productList.add(new Product1("Product C", 20.0, 1));

        return productList;
    }

    private double calculateTotalAmount(List<Product1> productList) {
        double totalAmount = 0.0;
        for (Product1 product : productList) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
        return totalAmount;
    }



    // Implement the methods for fetching products from the database and calculating the total amount.
}
