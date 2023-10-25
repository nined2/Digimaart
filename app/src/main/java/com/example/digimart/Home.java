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

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Home extends AppCompatActivity {

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

    private void sendData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneno = sharedPreferences.getString("phoneno", "");
        String[] field = new String[2];
        field[0] = "phoneno";

        String[] data = new String[2];
        data[0] = phoneno;
        PutData putData = new PutData("http://192.168.0.105/phpdb/usercreate.php","POST",field,data);
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
            }
        }
    });
}