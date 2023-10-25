package com.example.digimart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayNow extends AppCompatActivity {

    private static final String TAG = "PayNow"; // Log tag

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        Button button = findViewById(R.id.pay_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (paymentIntentClientSecret != null) {
                        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                                new PaymentSheet.Configuration("Codes Easy", configuration));
                    }
                } catch (Exception e) {
                    // Log the error
                    Log.e(TAG, "PaymentSheet Error: " + e.getMessage());
                    // Display an error toast
                    showToast("PaymentSheet Error: " + e.getMessage());
                }
            }
        });

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        fetchApi();
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            showToast("Payment Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed paymentFailed = (PaymentSheetResult.Failed) paymentSheetResult;
            String errorMessage = paymentFailed.getError().getMessage();
            showToast("Payment Failed: " + errorMessage);

            // Log the error
            Log.e("PaymentFailure", "Payment Failed: " + errorMessage);
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment Successful");
        }
    }

    public void fetchApi() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://panel.000webhost.com/dashboard/old-time-apprentice/dashboard"; // Update with your actual API URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));

                            configuration = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")
                            );
                        } catch (Exception e) {
                            // Log the error
                            Log.e(TAG, "API Response Error: " + e.getMessage());
                            // Display an error toast
                            showToast("API Response Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                try {
                    JSONObject jsonObject = new JSONObject("{\"paymentIntent\":\"pi_3O4xZuSHXO9o3WhY0K0OOCtQ_secret_zr6nAUmUl5CXQjIhbEF0wb7TT\",\"ephemeralKey\":\"ek_test_YWNjdF8xTm5kTklTSFhPOW8zV2hZLGJVUlh3T05Nd0FyZ2c2eVRaOUxVTmZpeW1Od1V1ZUc_00ngWCTg1S\",\"customer\":\"cus_Osj1Q55cc9NTZ3\",\"publishableKey\":\"pk_test_51NndNISHXO9o3WhYb7RVXeHfENgUKajXmW2LMSx1pYK0bTA2Gmt7x3t6jHJ8kqqvsK1esdx3cEHS8ifcZCafrDTr00tUmcyldl\"}");
                    configuration = new PaymentSheet.CustomerConfiguration(
                            jsonObject.getString("customer"),
                            jsonObject.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
