package com.example.digimart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCodeScanner extends AppCompatActivity {
    Button btn_scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            if (result.getContents().equals("Trolley 1")){
                AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanner.this);
                builder.setTitle("Scanned");
                builder.setMessage("Connected to Trolley 1");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }

            else if (result.getContents().equals("Trolley 2")){
                AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeScanner.this);
                builder.setTitle("Scanned");
                builder.setMessage("Connected to Trolley 2");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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