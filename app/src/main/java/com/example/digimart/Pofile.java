package com.example.digimart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pofile extends AppCompatActivity implements BottomSheets.OnOkButtonClickListener {
    private CircleImageView profileImageView;
    Button update;
    TextView textname, textmail, textphoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pofile);

        update = findViewById(R.id.update);
        textname = findViewById(R.id.name);
        textmail = findViewById(R.id.mail);
        textphoneno = findViewById(R.id.phoneno);

        profileImageView = findViewById(R.id.profile_image);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int selectedImageResId = preferences.getInt("selectedImageResId", R.drawable.prof);
        profileImageView.setImageResource(selectedImageResId);
        ImageView editIcon = findViewById(R.id.editIcon);

        editIcon.setOnClickListener(view -> {
            BottomSheets bottomSheetFragment = new BottomSheets();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        // Get user data from the server

        setNumber();
        setName();
        setMail();


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

    private void setNumber(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", 0);
        String phoneNumber = sharedPreferences.getString("phoneno", "");
        textphoneno.setText(phoneNumber);
    }

    @Override
    public void onOkButtonClick(int imageResId) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selectedImageResId", imageResId);
        editor.apply();

        profileImageView.setImageResource(imageResId);
    }
}