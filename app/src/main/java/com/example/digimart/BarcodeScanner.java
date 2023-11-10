package com.example.digimart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.GsonBuilder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeScanner extends AppCompatActivity {
    Button btn_scan;
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

        Button button = findViewById(R.id.pay_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PayNow.class);
                startActivity(intent);
                finish();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter productAdapter = new ProductAdapter();
        recyclerView.setAdapter(productAdapter);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105/phpdb/cart/")
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

                    PutData putData = new PutData("http://192.168.0.105/phpdb/productadd.php", "POST", field, data);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the OnBackPressedDispatcher
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        // Create an OnBackPressedCallback to handle the back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        };

        // Add the callback to the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, callback);
    }
}