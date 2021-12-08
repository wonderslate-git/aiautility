package com.android.wonderslate.appinapp.interfaces;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.util.PaymentUtility;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class AIAJSInterface implements PaymentResultListener {
    private Activity context;
    private WebView webView;
    WSSharedPrefs wsSharedPrefs;

    public AIAJSInterface(WebView webView, Activity context) {
        this.webView = webView;
        this.context = context;
        wsSharedPrefs = WSSharedPrefs.getInstance(context);
    }

    @JavascriptInterface
    public void startPayment(String bookId, String price, String bookTitle) {
        Log.d("AppInApp", "start payment");
        Log.d("AppInApp", "id " + bookId);
        Log.d("AppInApp", "title " + bookTitle);
        Log.d("AppInApp", "price " + price);

        final Activity activity = context;
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
            preFill.put("name", wsSharedPrefs.getUserName());
            preFill.put("email", "anirudha.wonderslate@gmail.com"); //hardcoded email Todo: get the user email from the client app
            preFill.put("contact", wsSharedPrefs.getUsermobile());
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
            notes.put("username", wsSharedPrefs.getUsermobile());
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
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("AppInApp", "Payment Failure: " + s + " code: " + i);
    }
}
