package com.example.digimart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    TextView textViewLogin;
    TextInputEditText textInputEditTextphoneno,textInputEditTextpassword;
    Button loginbutton;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewLogin = findViewById(R.id.textViewLogin);
        textInputEditTextphoneno = findViewById(R.id.phoneno);
        textInputEditTextpassword = findViewById(R.id.password);
        loginbutton = findViewById(R.id.login_b);
        progress = findViewById(R.id.progress);

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
                            PutData putData = new PutData("http://192.168.0.106/LogIn-SignUp-master/ulogin.php", "POST", field, data);
                            if (putData.startPut()) {
                                progress.setVisibility(View.GONE);
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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