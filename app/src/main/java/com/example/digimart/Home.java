package com.example.digimart;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Home extends AppCompatActivity {

    ImageView orderimg, scanimg, helpimg, trolleyimg,profile1;
    TextView textVieworder, textViewscan, textViewhelp, textViewtrolley,textname,textmail;
    Constraints profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        orderimg = findViewById(R.id.orderimg);
        scanimg = findViewById(R.id.scanimg);
        helpimg = findViewById(R.id.helpimg);
        trolleyimg = findViewById(R.id.trolleyimg);

        textVieworder = findViewById(R.id.textVieworder);
        textViewscan = findViewById(R.id.textViewscan);
        textViewhelp = findViewById(R.id.textViewhelp);
        textViewtrolley= findViewById(R.id.textViewtrolley);
        textname = findViewById(R.id.textView6);
        textmail = findViewById(R.id.textView7);
        profile1 = findViewById(R.id.imageView10);
        String str;

        setMail();
        setName();

        trolleyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),BarcodeScanner.class);
                startActivity(intent);
                finish();
            }
        });

        textViewtrolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BarcodeScanner.class);
                startActivity(intent);
                finish();
            }
        });
        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pofile.class);
                startActivity(intent);
                finish();
            }
        });

        textname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pofile.class);
                startActivity(intent);
                finish();
            }
        });

        textmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Pofile.class);
                startActivity(intent);
                finish();
            }
        });
        scanimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scancode();
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

    private void scancode() {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Volume up to Flash On");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureAct.class);
            barLauncher.launch(options);
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
                textmail.setText(result);
            }
        }
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
                textname.setText(result);
            }
        }
    }

    private void sendData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneno = sharedPreferences.getString("phoneno", "");
        String[] field = new String[2];
        field[0] = "phoneno";

        String[] data = new String[2];
        data[0] = phoneno;
        PutData putData = new PutData("http://172.18.0.135/phpdb/usercreate.php","POST",field,data);

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            if (result.getContents().equals("Trolley 1")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Scanned");
                builder.setMessage("Connected to Trolley 1");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendData();
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
                        startActivity(intent);
                        finish();

                    }
                }).show();
            }

            else if (result.getContents().equals("Trolley 2")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Scanned");
                builder.setMessage("Connected to Trolley 2");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendData();
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }

            else {
                Toast.makeText(getApplicationContext(), "Incorrect QR Code", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Wrong");
                builder.setMessage("Invalid QRCODE");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendData();
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        }
    });
}