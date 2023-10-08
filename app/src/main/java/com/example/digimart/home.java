package com.example.digimart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    ImageView orderimg, scanimg, helpimg, paymentimg;
    TextView textVieworder, textViewscan, textViewhelp, textViewpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        orderimg = findViewById(R.id.orderimg);
        scanimg = findViewById(R.id.scanimg);
        helpimg = findViewById(R.id.helpimg);
        paymentimg = findViewById(R.id.paymentimg);

        textVieworder = findViewById(R.id.textVieworder);
        textViewscan = findViewById(R.id.textViewscan);
        textViewhelp = findViewById(R.id.textViewhelp);
        textViewpayment = findViewById(R.id.textViewpayment);

        scanimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QRCodeScanner.class);
                startActivity(intent);
                finish();
            }
        });
        textViewscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QRCodeScanner.class);
                startActivity(intent);
                finish();
            }
        });


    }
}