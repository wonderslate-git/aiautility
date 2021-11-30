package com.example.android.architecture.blueprints.todoapp.util;

import static android.webkit.WebView.setWebContentsDebuggingEnabled;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.architecture.blueprints.todoapp.BuildConfig;
import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.appinappsample.AppInAppActivity;
import com.google.android.material.textfield.TextInputEditText;

public class AppInAppUtil {
    WebView webView;
    Activity activity;
    String userName, userMobile;

    public AppInAppUtil(WebView webView, Activity activity) {
        this.webView = webView;
        this.activity = activity;
    }

    public void startAppInApp() {
        new CredentialDialog().showDialog(activity);
    }

    protected void configureWebView(WebView webView) {
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

    private void loadEbooksLibrary(String siteId, String mobile, String secretKey, String username){
        Toast.makeText(activity, "Loading your E-Books Library. Have patience please...", Toast.LENGTH_LONG).show();
        configureWebView(webView);

        String url = String.format("https://www.wonderslate.com/intelligence/sessionGenerator?siteId=%s&secretKey=b534cZ9845&loginId=%s&name=%s", "1", mobile, username);
        webView.loadUrl(url);
    }

    public void refreshWebView() {
        if (webView != null) {
            loadEbooksLibrary("1", "9790798287", "some_secret", "Anirudha");
        }
    }

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

    public class CredentialDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_user_credentials);

            TextInputEditText userNameText, userMobileText;
            Button okBtn, cancelBtn;

            userNameText = dialog.findViewById(R.id.username_edit_text);
            userMobileText = dialog.findViewById(R.id.phone_edit_text);
            okBtn = dialog.findViewById(R.id.btn_dialog_ok);
            cancelBtn = dialog.findViewById(R.id.btn_dialog_cancel);


            okBtn.setOnClickListener(v -> {
                if (userMobileText.getText() != null && userMobileText.getText().toString().isEmpty()) {
                    userMobileText.setError("Mandatory Field");
                }
                else if (userNameText.getText() != null && userNameText.getText().toString().isEmpty()) {
                    userNameText.setError("Mandatory Field");
                }
                else {
                    userMobile = userMobileText.getText().toString();
                    userName = userNameText.getText().toString();

                    loadEbooksLibrary("1", userMobile, "some+secret", userName);
                    dialog.dismiss();
                }
            });

            cancelBtn.setOnClickListener(v -> {
                Toast.makeText(activity, "Please login to access App-In-App feature", Toast.LENGTH_LONG).show();
                activity.finish();
                dialog.dismiss();
            });

            dialog.show();

        }
    }
}
