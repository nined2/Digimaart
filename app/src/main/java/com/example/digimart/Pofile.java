package com.example.digimart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pofile extends AppCompatActivity implements BottomSheets.OnOkButtonClickListener {
    private CircleImageView profileImageView;

    PhoneNumberUtil phoneNumberUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pofile);
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