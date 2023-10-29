package com.example.digimart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayNow extends AppCompatActivity {

    private static final String TAG = "PayNow"; // Log tag

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;

    String name = "Nined";
    String upivirtualid = "7738290111@fam";
    String amount="100";
    String note = "Payment xd";
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        Button button = findViewById(R.id.pay_button);
        Button button1 = findViewById(R.id.gpay_button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payUsingUpi(name,upivirtualid,note,amount);

            }
        });


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

        fetchApi();
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
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
                            JSONObject jsonObject= new JSONObject(response);
                            configuration = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(),jsonObject.getString("publishableKey"));
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
                    JSONObject jsonObject = new JSONObject("{\"paymentIntent\":\"pi_3O6H0HSHXO9o3WhY1Kd0cDde_secret_DYZMsqWYQxDcZR85inQunmbuu\",\"ephemeralKey\":\"ek_test_YWNjdF8xTm5kTklTSFhPOW8zV2hZLFdzYnNpMUFaOGw2UTV5bmtnaXJGblR2S2Z2UWZYSXU_00LYKbZA52\",\"customer\":\"cus_Ou5A3VnERz2gc9\",\"publishableKey\":\"pk_test_51NndNISHXO9o3WhYb7RVXeHfENgUKajXmW2LMSx1pYK0bTA2Gmt7x3t6jHJ8kqqvsK1esdx3cEHS8ifcZCafrDTr00tUmcyldl\"}");
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

    void payUsingUpi(  String name,String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PayNow.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PayNow.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PayNow.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PayNow.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(PayNow.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(PayNow.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}

