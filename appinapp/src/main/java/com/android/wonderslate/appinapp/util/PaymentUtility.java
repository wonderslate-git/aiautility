package com.android.wonderslate.appinapp.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentUtility implements PaymentResultListener {
    WebView webView;
    Activity activity;
    WSSharedPrefs wsSharedPrefs;

    public PaymentUtility(WebView webView, Activity activity) {
        this.webView = webView;
        this.activity = activity;
        wsSharedPrefs = WSSharedPrefs.getInstance(activity);
    }

    public void startPayment(String id, String price, String title) {
        final Activity activity = this.activity;
        final Checkout co = new Checkout();
        //co.setImage(R.mipmap.ic_launcher);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Dear Sir");
            options.put("description", title + " (Course " + id + ")");
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
            notes.put("bookId", id);
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

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("AppInApp", "Payment Failure: " + s + " code: " + i);
    }
}
