package com.example.digimart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeScanner extends AppCompatActivity {
    Button btn_scan,plus,minus;
    private static final String TAG = "PayNow"; // Log tag

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });

        plus = findViewById(R.id.plusButton);
        minus = findViewById(R.id.minusButton);
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

        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.104/phpdb/cart/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        // Make an API call to get products
        apiInterface.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    if (products != null) {
                        productAdapter.setProducts(products);
                    } else {
                        Log.e("NetworkError", "Response body is null");
                    }
                } else {
                    Log.e("NetworkError", "Response not successful, code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("NetworkError", "Network error: " + t.getMessage());
            }
        });
    }


    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {


            AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String barcode = result.getContents();
                    String[] field = new String[1];
                    field[0] = "barcode";
                    String[] data = new String[1];
                    data[0] = barcode;

                    PutData putData = new PutData("http://192.168.0.104/phpdb/productadd.php", "POST", field, data);
                    if(putData.startPut()) {
                        if (putData.onComplete()) {
                            String str = putData.getResult();
                            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("HTTP Error", putData.getResult());
                        }
                    }
                    dialogInterface.dismiss();
                    Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);
                    startActivity(intent);
                    finish();
                }
            }).show();
        }
    });
    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            showToast("Payment Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            showToast("Payment Failed: " + ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment Successful");
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
    }

    public void fetchApi() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.105:63343/untitled/index.php?_ijt=qgem2u3unnohb0qa9e2gqgu8mn&_ij_reload=RELOAD_ON_SAVE"; // Update with your actual API URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
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
                }, new com.android.volley.Response.ErrorListener() {
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