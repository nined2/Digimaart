package com.example.digimart; // Change this to your actual package name

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BottomSheets.OnOkButtonClickListener {
    private CircleImageView profileImageView;
    private PhoneNumberUtil phoneNumberUtil;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImageView = findViewById(R.id.profile_image);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int selectedImageResId = preferences.getInt("selectedImageResId", R.drawable.prof);

        phoneNumberUtil = PhoneNumberUtil.getInstance();
        profileImageView.setImageResource(selectedImageResId);

        ImageView editIcon = findViewById(R.id.editIcon);
        editIcon.setOnClickListener(view -> {
            BottomSheets bottomSheetFragment = new BottomSheets();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                Intent intent = new Intent(MainActivity.this, hasLoggedIn ? QRCodeScanner.class : SignUp.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onOkButtonClick(int imageResId) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selectedImageResId", imageResId);
        editor.apply();

        profileImageView.setImageResource(imageResId);
    }

    public boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
        try {
            String phoneNumberWithCountryCode = "+" + countryCode + phoneNumber;
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumberWithCountryCode, null);
            return phoneNumberUtil.isValidNumberForRegion(parsedPhoneNumber, countryCode);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Phone number is not valid
        }
    }
}
