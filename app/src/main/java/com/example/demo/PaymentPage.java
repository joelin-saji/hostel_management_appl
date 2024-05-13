package com.example.demo;

import static com.example.demo.R.id.sept_button;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentPage extends AppCompatActivity implements PaymentResultListener {

    private Button sep_but;
    private TextView paytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        Checkout.preload(getApplicationContext());

        paytext=(TextView)findViewById(R.id.paytext);

        sep_but = findViewById(R.id.sept_button);


        sep_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makepayment();
            }
        });

    }

    private void makepayment() {

        //object named "checkout"
        Checkout checkout = new Checkout();
        //user test id
        checkout.setKeyID("rzp_test_R95cnQXjr7lfid");
        //  checkout.setImage(R.drawable.logo);
        final PaymentPage activity = this;
        try {
            //passing json exception
            JSONObject options = new JSONObject();

            options.put("name", "Student Accomodation");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "EUR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","8157992257");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);


            //to open the razorpay gateway
            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }


    @Override
    public void onPaymentSuccess(String s) {
        paytext.setText("Successful. Payment Id: "+s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        paytext.setText("Failed: Due to : "+s);
    }
}