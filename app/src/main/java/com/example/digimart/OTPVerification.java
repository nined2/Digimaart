package com.example.digimart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText phoneNumberEditText, otpEditText;
    private Button sendOTPButton, verifyOTPButton;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mAuth = FirebaseAuth.getInstance();
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        otpEditText = findViewById(R.id.otpEditText);
        sendOTPButton = findViewById(R.id.sendOTPButton);
        verifyOTPButton = findViewById(R.id.verifyOTPButton);

        sendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        OTPVerification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // Automatically handle verification if you receive the SMS code on the same device.
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPVerification.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;
                                sendOTPButton.setVisibility(View.GONE);
                                verifyOTPButton.setVisibility(View.VISIBLE);
                            }
                        }
                );
            }
        });

        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpEditText.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information.
                            Toast.makeText(OTPVerification.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                            // You can redirect the user to the next activity or perform any other actions here.
                        } else {
                            // Sign in failed, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OTPVerification.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OTPVerification.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}