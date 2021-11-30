package com.android.wonderslate.appinapp.util;

import static android.webkit.WebView.setWebContentsDebuggingEnabled;

import android.app.Activity;
import android.os.Build;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.wonderslate.appinapp.BuildConfig;

public class AIAWebLoader {
    WebView webView;
    Activity activity;
    String userName, userMobile;

    public AIAWebLoader(WebView webView, Activity activity) {
        this.webView = webView;
        this.activity = activity;
    }

    private void startAppInApp() {

    }

    private void configureWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setClickable(true);
        webView.setLongClickable(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebChromeClient(new eBooksChromeClient());

        webView.setWebViewClient(new eBooksWebViewClient());

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
    }

    public void loadAIA(String siteId, String mobile, String secretKey, String username){
        configureWebView(webView);

        String url = String.format("https://qa.wonderslate.com/intelligence/sessionGenerator?siteId=%s&secretKey=b534cZ9845&loginId=%s&name=%s", "1", mobile, username);
        webView.loadUrl(url);
    }

    /*public void refreshWebView() {
        if (webView != null) {
            loadAIA("1", "9790798287", "some_secret", "Anirudha");
        }
    }*/

    class eBooksWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    class eBooksChromeClient extends WebChromeClient {
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor

        // Need to accept permissions to use the camera
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                request.grant(request.getResources());
            }
            else {
                Toast.makeText(activity, "This is not supported on this Android Version.", Toast.LENGTH_SHORT).show();
            }
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            /*if (cm != null && cm.sourceId().length() > 0) {
                Log.e(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId().substring(10));
            } else if (cm != null) {
                Log.e(TAG, cm.message() + " -- From line "
                        + cm.lineNumber());
            }*/
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.cancel();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            result.cancel();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            result.cancel();
            return true;
        }
    }
}
