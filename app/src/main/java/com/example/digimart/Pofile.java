package com.example.digimart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phoneno", "");
        getUserDataFromServer(phoneNumber);
    }

    private void getUserDataFromServer(String phoneNumber) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.29.53/phpdb/get_user_data.php"; // Replace with your server URL

        RequestBody requestBody = new FormBody.Builder()
                .add("phoneno", phoneNumber)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network error
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONObject json = new JSONObject(responseData);
                        if (json.has("error")) {
                            final String errorMessage = json.getString("error");
                            runOnUiThread(() -> {
                                // Handle error, e.g., show an error message
                                textname.setText(errorMessage);
                            });
                        } else {
                            final String name = json.getString("name");
                            final String mail = json.getString("mail");
                            final String phoneno = json.getString("phoneno");

                            runOnUiThread(() -> {
                                // Update UI with user data
                                textname.setText(name);
                                textmail.setText(mail);
                                textphoneno.setText(phoneno);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
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
}
