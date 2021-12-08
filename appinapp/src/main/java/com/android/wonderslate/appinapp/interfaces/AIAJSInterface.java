package com.android.wonderslate.appinapp.interfaces;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.wonderslate.appinapp.util.PaymentUtility;

public class AIAJSInterface {
    private Activity context;
    private WebView webView;

    public AIAJSInterface(WebView webView, Activity context) {
        this.webView = webView;
        this.context = context;
    }

    @JavascriptInterface
    public void startPayment(String bookId, String price, String bookTitle) {
        Log.d("AppInApp", "start payment");
        Log.d("AppInApp", "id " + bookId);
        Log.d("AppInApp", "title " + bookTitle);
        Log.d("AppInApp", "price " + price);

        new PaymentUtility(webView, context).startPayment(bookId, price, bookTitle);
    }
}
