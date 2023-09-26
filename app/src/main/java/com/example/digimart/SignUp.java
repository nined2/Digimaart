package com.example.digimart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextname, textInputEditTextphoneno, textInputEditTextmail, textInputEditTextpassword;
    Button signup_b;
    TextView textViewlogin;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextname = findViewById(R.id.name);
        textInputEditTextphoneno = findViewById(R.id.phoneno);
        textInputEditTextmail = findViewById(R.id.mail);
        textInputEditTextpassword = findViewById(R.id.password);
        signup_b = findViewById(R.id.signup_b);
        textViewlogin = findViewById(R.id.textViewLogin);
        progressBar = findViewById(R.id.progressbar);

        textViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        signup_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name,phoneno,mail,password;
                name = String.valueOf(textInputEditTextname.getText());
                phoneno = String.valueOf(textInputEditTextphoneno.getText());
                mail = String.valueOf(textInputEditTextmail.getText());
                password = String.valueOf(textInputEditTextpassword.getText());

                if(!name.equals("") && !phoneno.equals("") && !mail.equals("") && !password.equals(""))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[4];
                            field[0] = "name";
                            field[1] = "phoneno";
                            field[2] = "mail";
                            field[3] = "password";

                            String[] data = new String[4];
                            data[0] = name;
                            data[1] = phoneno;
                            data[2] = mail;
                            data[3] = password;
                            PutData putData = new PutData("http://192.168.56.212/LogIn-SignUp-master/usignup.php", "POST", field, data);
                            if (putData.startPut()) {
                                progressBar.setVisibility(View.GONE);
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Signup Success")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
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