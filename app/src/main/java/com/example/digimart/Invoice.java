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
    private TextView textname, textphoneno, textmail, dateTextView, totalAmountTextView;
    private Button btnXMLtoPDF;
    private List<Product1> productList; // Replace with your product data structure

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Initialize your views
        textname = findViewById(R.id.name);
        textmail = findViewById(R.id.mail);
        textphoneno = findViewById(R.id.phoneno);

        dateTextView = findViewById(R.id.dateTextView);
        productListView = findViewById(R.id.productListView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        setMail();
        setName();
        setNumber();

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
                convertXMLtoPDF();
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
        String phoneNumber = "12345";

        String[] field = new String[1];
        field[0] = "phoneno";
        String[] data = new String[1];
        data[0] = phoneNumber;
        PutData putData = new PutData("http://192.168.254.212/phpdb/hname.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                textname.setText("Name: " + result);
            }
        }
    }

    private void setMail() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = "12345";

        String[] field = new String[1];
        field[0] = "phoneno";
        String[] data = new String[1];
        data[0] = phoneNumber;
        PutData putData = new PutData("http://192.168.254.212/phpdb/hmail.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                textmail.setText("Email: " + result);
            }
        }
    }

    private void setNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile",  0);
        String phoneNumber = "12345";
        textphoneno.setText("Phone no.:" + phoneNumber);
    }

    private void convertXMLtoPDF() {
        // Inflate the layout you want to convert to PDF
        View view = LayoutInflater.from(this).inflate(R.layout.activity_invoice, null);

        // Measure and layout the view
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getDisplay().getRealMetrics(displayMetrics);
        } else {
            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, displayMetrics.widthPixels, view.getMeasuredHeight());

        // Create a PDF document
        PdfDocument document = new PdfDocument();
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the canvas
        Canvas canvas = page.getCanvas();

        // Draw the entire view onto the canvas
        view.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Create the PDF file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "BillInvoice.pdf";
        File file = new File(downloadsDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);

            // Write the document to the file
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Converted XML to PDF Successfully and saved in Downloads", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("mylog", "Error while writing " + e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}