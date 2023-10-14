package com.example.digimart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class LOGIN2 extends AppCompatActivity {
    public static String PREFS_NAME="MyPrefsFile";
    TextView textViewLogin;
    EditText textInputEditTextphoneno,textInputEditTextpassword;
    Button loginbutton;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        TextView textViewLogin = findViewById(R.id.textViewsignup);
        EditText textInputEditTextphoneno = findViewById(R.id.phoneno);
        EditText textInputEditTextpassword = findViewById(R.id.password);
        Button loginbutton = findViewById(R.id.login_b);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();

            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneno,password;
                phoneno = String.valueOf(textInputEditTextphoneno.getText());
                password = String.valueOf(textInputEditTextpassword.getText());

                if(!phoneno.equals("") && !password.equals(""))
                {
                    progress.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "phoneno";
                            field[1] = "password";

                            String[] data = new String[2];
                            data[0] = phoneno;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.43.201/loginsignup/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN2.PREFS_NAME,0);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putBoolean("hasLoggedIn",true);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), QRCodeScanner.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}