package com.example.digimart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);

                Intent intent;
                if(hasLoggedIn){
                    intent = new Intent(MainActivity.this, QRCodeScanner.class);
                }
                else {
                    intent = new Intent(MainActivity.this, Login.class);
                }
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
