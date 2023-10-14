package com.example.digimart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerification extends AppCompatActivity {

    private EditText otpEditText;
    private Button verifyButton;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        otpEditText = findViewById(R.id.otpEditText);
        verifyButton = findViewById(R.id.verifyButton);

        mAuth = FirebaseAuth.getInstance();

        // Get the verification ID that was sent with the SMS
        verificationId = getIntent().getStringExtra("verificationId");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpEditText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    otpEditText.setError("Enter valid OTP");
                    otpEditText.requestFocus();
                    return;
                }

                // Verify the OTP
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
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
                            // Authentication successful, you can now save the user's data to the database
                            // Example: Save user data to Firebase Realtime Database or Firestore
                            // After saving data, you can navigate to the next screen
                            // For example, navigating to the home activity
                            Intent intent = new Intent(OTPVerification.this, LOGIN2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Authentication failed
                            Toast.makeText(OTPVerification.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
