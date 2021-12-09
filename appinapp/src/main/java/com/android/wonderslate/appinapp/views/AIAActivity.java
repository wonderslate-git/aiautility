package com.android.wonderslate.appinapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class AIAActivity extends AppCompatActivity implements PaymentResultListener {
    WSSharedPrefs wsSharedPrefs;
    WebView webView;
    String bookId;
    Activity clientContext;

    public AIAActivity(Activity clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wsSharedPrefs = WSSharedPrefs.getInstance(clientContext);
    }

    public void startPayment(String bookId, String bookTitle, String price, WebView webView) {
        Log.d("AppInApp", "start payment");
        Log.d("AppInApp", "id " + bookId);
        Log.d("AppInApp", "title " + bookTitle);
        Log.d("AppInApp", "price " + price);

        this.webView = webView;
        this.bookId = bookId;

        final Activity activity = this;
        final Checkout co = new Checkout();
        //co.setImage(R.mipmap.ic_launcher); //Company logo
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Dear Sir"); //Company Name
            options.put("description", bookTitle + " (Course " + bookId + ")");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", (Double.parseDouble(price) * 100) + "");
            JSONObject preFill = new JSONObject();
            preFill.put("name", "Anirudha");
            preFill.put("email", "anirudha.wonderslate@gmail.com"); //hardcoded email Todo: get the user email from the client app
            preFill.put("contact", "8884369973");
            JSONObject ReadOnly = new JSONObject();
            ReadOnly.put("email", "true");
            ReadOnly.put("contact", "true");
            JSONObject theme = new JSONObject();
            theme.put("color", "#F05A2A");
            options.put("prefill", preFill);
            options.put("theme", theme);
            options.put("readonly", ReadOnly);
            JSONObject notes = new JSONObject();
            notes.put("bookId", bookId);
            notes.put("username", "8884369973");
            options.put("notes", notes);

            co.open(activity, options);
        } catch (Exception e) {
//            Utils.showErrorToast(ShopBookDetailsActivity.this, -3);
            Log.e("AppInApp", "Payment Failure", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d("AppInApp", "Razorpay Payment Success");
        Log.d("AppInApp", "Razorpay Payment Details: " + s);

        webView.evaluateJavascript("buyBookAppinApp("+s+","+bookId+","+""+","+""+");", null);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("AppInApp", "Payment Failure: " + s + " code: " + i);
        Toast.makeText(this, "Payment Failure: " + s, Toast.LENGTH_SHORT).show();
    }
}