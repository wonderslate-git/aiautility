package com.android.wonderslate.appinapp.interfaces;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class AIAJSInterface {
    private Context context;

    public AIAJSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void startPayment(String bookId, String bookTitle, String price) {
        Log.d("AppInApp", "start payment");
        Log.d("AppInApp", "id " + bookId);
        Log.d("AppInApp", "title " + bookTitle);
        Log.d("AppInApp", "price " + price);
    }
}
