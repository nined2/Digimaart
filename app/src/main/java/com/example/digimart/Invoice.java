package com.example.digimart;

import android.content.SharedPreferences;
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

import com.vishnusivadas.advanced_httpurlconnection.PutData;

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
    private TextView textname,textphoneno,textmail,textitem, dateTextView, totalAmountTextView;
    private Button btnXMLtoPDF;
    private List<Product1> productList; // Replace with your product data structure

    View invoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Initialize your views
        textname = findViewById(R.id.name);
        textmail = findViewById(R.id.mail);
        textphoneno = findViewById(R.id.phoneno);
        textitem = findViewById(R.id.item);

        invoice = findViewById(R.id.invoice);
        dateTextView = findViewById(R.id.dateTextView);
        productListView = findViewById(R.id.productListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        setMail();
        setName();
        setNumber();
        setItem();

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
    private void setName() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = sharedPreferences.getString("phoneno", "");

        String[] field = new String[1];
        field[0] = "phoneno";
        String[] data = new String[1];
        data[0] = phoneNumber;
        PutData putData = new PutData("http://172.18.0.135/phpdb/hname.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                textname.setText("Name: " + result);
            }
        }
    }

    private void setMail() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = sharedPreferences.getString("phoneno", "");

        String[] field = new String[1];
        field[0] = "phoneno";
        String[] data = new String[1];
        data[0] = phoneNumber;
        PutData putData = new PutData("http://172.18.0.135/phpdb/hmail.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                textmail.setText("Email: " + result);
            }
        }
    }

    private void setNumber(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = sharedPreferences.getString("phoneno", "");
        textphoneno.setText("Phone no.:" + phoneNumber);
    }

    private void setItem() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = sharedPreferences.getString("phoneno", "");

        String[] field = new String[1];
        field[0] = "phoneno";
        String[] data = new String[1];
        data[0] = phoneNumber;
        PutData putData = new PutData("http://172.18.0.135/phpdb/total_items.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                textitem.setText("Total Items: " + result);
            }
        }
    }

    private void convertXMTtoPDF() {
        // Get the view you want to convert to a PDF
        View view = findViewById(R.id.invoice); // Replace with your view's ID

        // Create a PdfDocument
        PdfDocument document = new PdfDocument();

        // Create a PageInfo
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Draw the view on the page
        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Define the file where the PDF will be saved
        File file = new File(Environment.getExternalStorageDirectory(), "my_pdf_file.pdf"); // Adjust the file path as needed

        try {
            // Create a FileOutputStream
            FileOutputStream fos = new FileOutputStream(file);

            // Write the PDF document to the FileOutputStream
            document.writeTo(fos);

            // Close the FileOutputStream
            fos.close();

            // Close the PdfDocument
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
