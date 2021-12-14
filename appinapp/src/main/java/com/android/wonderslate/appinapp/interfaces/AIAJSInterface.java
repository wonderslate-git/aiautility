package com.android.wonderslate.appinapp.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.views.AIAActivity;

public class AIAJSInterface {
    private Context context;
    private WebView webView;
    WSSharedPrefs wsSharedPrefs;
    private AIAActivity activityContext;

    public AIAJSInterface(WebView webView, Context context) {
        this.webView = webView;
        this.context = context;
        wsSharedPrefs = WSSharedPrefs.getInstance(context);
    }

    @JavascriptInterface
    public void startPayment(String bookId, String price, String bookTitle, String clientName, String author, String coverImage) {
        Intent AIAIntent = new Intent(context, AIAActivity.class);
        AIAIntent.putExtra("id", bookId);
        AIAIntent.putExtra("title", bookTitle);
        AIAIntent.putExtra("price", price);
        AIAIntent.putExtra("client", clientName);
        AIAIntent.putExtra("author", author);
        AIAIntent.putExtra("coverImage", coverImage);
        context.startActivity(AIAIntent);
    }

    @JavascriptInterface
    public void onBookPurchase(String status) {
        if (status != null && !status.isEmpty() && status.equalsIgnoreCase("ok")) {
            Log.d("AppInApp", "Book Purchase Successful");

        }
        else {
            Log.d("AppInApp", "Book Purchase Not Successful");
            activityContext.onPaymentError(404, "Payment Failure");
        }
    }
}
