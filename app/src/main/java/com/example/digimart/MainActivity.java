package com.example.digimart; // Change this to your actual package name

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = sharedPreferences.getString("phoneno", "");

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(LOGIN2.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                Intent intent = new Intent(MainActivity.this, hasLoggedIn ? Home.class : LOGIN2.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }}