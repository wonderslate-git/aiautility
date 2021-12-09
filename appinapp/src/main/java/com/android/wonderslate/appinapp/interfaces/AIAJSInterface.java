package com.android.wonderslate.appinapp.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.util.PaymentUtility;
import com.android.wonderslate.appinapp.views.AIAActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class AIAJSInterface {
    private Activity context;
    private WebView webView;
    WSSharedPrefs wsSharedPrefs;
    AIAActivity aiaActivity;

    public AIAJSInterface(WebView webView, Activity context) {
        this.webView = webView;
        this.context = context;
        wsSharedPrefs = WSSharedPrefs.getInstance(context);
        aiaActivity = new AIAActivity(this.context);
        Intent AIAIntent = new Intent(context, AIAActivity.class);
        context.startActivity(AIAIntent);
    }

    @JavascriptInterface
    public void startPayment(String bookId, String price, String bookTitle) {
        aiaActivity.startPayment(bookId, bookTitle, price, webView);
    }
}
