package com.example.digimart;

import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Invoice extends AppCompatActivity {

    private ListView productListView;
    private TextView userInfoTextView, dateTextView, totalAmountTextView;
    private Button btnXMLtoPDF;
    private List<Product1> productList; // Replace with your product data structure

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Initialize your views
        userInfoTextView = findViewById(R.id.userInfoTextView);
        dateTextView = findViewById(R.id.dateTextView);
        productListView = findViewById(R.id.productListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        // Fetch user data (name, phone, email) from your database
        String userName = "Akshit"; // Replace with actual user data
        String userPhone = "12345"; // Replace with actual user data
        String userEmail = "akshit@example.com"; // Replace with actual user data

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
        totalAmountTextView.setText("Total Amount: " + totalAmount + "rs");


        btnXMLtoPDF = findViewById(R.id.generatePDFButton);
        btnXMLtoPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertXMTtoPDF();
            }
        });

    }
    private List<Product1> getProductsFromDatabase() {
        // Replace with actual database query
        List<Product1> productList = new ArrayList<>();

        // Sample data
        productList.add(new Product1("Chips", 10.0, 1));
        productList.add(new Product1("Biscuit", 20.0, 1));

        return productList;
    }

    private double calculateTotalAmount(List<Product1> productList) {
        double totalAmount = 0.0;
        for (Product1 product : productList) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
        return totalAmount;
    }

    private void convertXMTtoPDF() {
        //Inflate the layout that you want to convert to pdf
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //for device version is less use this or use else
            this.getDisplay().getRealMetrics(displayMetrics);
        } else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);


        PdfDocument document = new PdfDocument();
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Log.d("mylog", "View Width:  " + viewWidth);
        Log.d("mylog", "View Height:" + viewHeight);

        //Canvas
        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        //finish page
        document.finishPage(page);

        File DownloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "exampleXML.pdf";
        File file = new File(DownloadsDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Converted XML to PDF Sucessfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("mylog", "Error while writing " + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Implement the methods for fetching products from the database and calculating the total amount.
}
