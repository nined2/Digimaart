package com.example.digimart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    EditText textInputEditTextname, textInputEditTextphoneno, textInputEditTextmail, textInputEditTextpassword, otpEditText;
    Button signupButton, verifyOTPButton;
    TextView textViewloginHere, alreadylogin;
    String phoneNumber; // Store the phone number for OTP verification
    ImageView imageView10,imageView11,imageView12,imageView13;
    boolean isVerificationInProgress = false;

    // Firebase variables
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider phoneAuthProvider;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        phoneAuthProvider = PhoneAuthProvider.getInstance();

        textInputEditTextname = findViewById(R.id.name);
        textInputEditTextphoneno = findViewById(R.id.phoneno);
        textInputEditTextmail = findViewById(R.id.mail);
        textInputEditTextpassword = findViewById(R.id.password);
        signupButton = findViewById(R.id.signup_b);
        textViewloginHere = findViewById(R.id.textViewLoginHere);
        verifyOTPButton = findViewById(R.id.verify_otp_button);
        otpEditText = findViewById(R.id.otpEditText);
        alreadylogin = findViewById(R.id.alreadylogin);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);
        imageView13 = findViewById(R.id.imageView13);

        // Hide OTP components initially
        verifyOTPButton.setVisibility(View.GONE);
        otpEditText.setVisibility(View.GONE);

        textViewloginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LOGIN2.class);
                startActivity(intent);
                finish();
            }
        });

        // Click listener for the "Verify OTP" button
        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOTP = otpEditText.getText().toString().trim();
                if (!enteredOTP.isEmpty()) {
                    // Verify the entered OTP
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enteredOTP);
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String name = String.valueOf(textInputEditTextname.getText());
                                        String mail = String.valueOf(textInputEditTextmail.getText());
                                        String password = String.valueOf(textInputEditTextpassword.getText());
                                        String phoneno = String.valueOf(textInputEditTextphoneno.getText());
                                        // OTP verification is successful
                                        // Proceed to save user data in the database
                                        saveUserDataToDatabase(name, phoneno, mail, password);
                                    } else {
                                        // Handle verification failure
                                        Toast.makeText(getApplicationContext(), "Verification failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVerificationInProgress) {
                    String name,phoneno,mail,password;
                    name = String.valueOf(textInputEditTextname.getText());
                    phoneno = String.valueOf(textInputEditTextphoneno.getText());
                    mail = String.valueOf(textInputEditTextmail.getText());
                    password = String.valueOf(textInputEditTextpassword.getText());


                    if (!name.equals("") && !phoneno.equals("") && !mail.equals("") && !password.equals("")) {
                        String cleanPhoneNumber = phoneno.replaceAll("[^0-9]", "");

                        // Add the "+91" prefix to the phone number
                        String formattedPhoneNumber = "+91" + cleanPhoneNumber;
                        phoneNumber = formattedPhoneNumber; // Store the phone number for OTP verification

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,
                                60,
                                TimeUnit.SECONDS,
                                SignUp.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onCodeSent(String verificationId,
                                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        SignUp.this.verificationId = verificationId;
                                    }

                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                                        // Automatically handle verification when the device is able to receive SMS
                                        firebaseAuth.signInWithCredential(credential)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // OTP verification is successful
                                                            // Proceed to save user data in the database
                                                            saveUserDataToDatabase(name, phoneno, mail, password);
                                                        } else {
                                                            // Handle verification failure
                                                            Toast.makeText(getApplicationContext(), "Verification failed.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        // Handle verification failure
                                        Toast.makeText(getApplicationContext(), "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        // Hide form fields and show OTP input field
                        textInputEditTextname.setVisibility(View.GONE);
                        textInputEditTextphoneno.setVisibility(View.GONE);
                        textInputEditTextmail.setVisibility(View.GONE);
                        textInputEditTextpassword.setVisibility(View.GONE);
                        signupButton.setVisibility(View.GONE);
                        imageView10.setVisibility(View.GONE);
                        imageView11.setVisibility(View.GONE);
                        imageView12.setVisibility(View.GONE);
                        imageView13.setVisibility(View.GONE);


                        verifyOTPButton.setVisibility(View.VISIBLE);
                        otpEditText.setVisibility(View.VISIBLE);

                        isVerificationInProgress = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User is verifying OTP
                    String enteredOTP = otpEditText.getText().toString().trim();
                    if (!enteredOTP.isEmpty()) {
                        // Automatically handled in the onVerificationCompleted callback
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveUserDataToDatabase(String name, String phoneno, String mail, String password) {
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

                PutData putData = new PutData("http://192.168.29.53/phpdb/usignup.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Signup Success")) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LOGIN2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            Log.e("ProfileActivity", "Error: " + result);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failure",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
