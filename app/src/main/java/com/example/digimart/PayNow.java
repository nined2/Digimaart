package com.example.digimart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    PaymentSheet paymentSheet;  //previous code class
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);
        fetchApi();
        Button button= findViewById(R.id.pay_button);
      button.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              if(paymentIntentClientSecret!=null) {
                  paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                          new PaymentSheet.Configuration("Codes Easy", configuration));//customers details in configuration
              }
          }
      });
      paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);//14:27
    }
    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult){

        if(paymentSheetResult instanceof PaymentSheetResult.Canceled){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof PaymentSheetResult.Failed){
            Toast.makeText(this, ((PaymentSheetResult.Failed)paymentSheetResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Successful ", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchApi(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://localhost:63343/untitled/index.php?_ijt=qgem2u3unnohb0qa9e2gqgu8mn&_ij_reload=RELOAD_ON_SAVE"; //upload your url

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            configuration = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(),jsonObject.getString("publishableKey"));
                        } catch(JSONException e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);

    }
}