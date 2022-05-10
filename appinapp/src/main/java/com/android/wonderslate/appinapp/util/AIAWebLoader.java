package com.android.wonderslate.appinapp.util;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.webkit.WebView.setWebContentsDebuggingEnabled;

import static com.android.wonderslate.appinapp.data.remote.APIs.LAUNCH_URL;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.wonderslate.appinapp.data.remote.ServerURLManager;
import com.android.wonderslate.appinapp.interfaces.AIAJSInterface;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class AIAWebLoader {
    WebView webView;
    Context context;
    int i = 0;

    public AIAWebLoader(WebView webView, Context context) {
        this.webView = webView;
        this.context = context;
    }

    private void configureWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setClickable(true);
        webView.setLongClickable(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.setWebChromeClient(new eBooksChromeClient());

        webView.setWebViewClient(new eBooksWebViewClient());

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.getSettings().setSupportMultipleWindows(true);

        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setDatabaseEnabled(true);

        webView.addJavascriptInterface(new AIAJSInterface(webView, context), "JSInterface");

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        //webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        //ToDo: In the release build enable security against chrome debug
        //setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        setWebContentsDebuggingEnabled(true);

        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            request.setMimeType(mimeType);
            //------------------------COOKIE!!------------------------
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            //------------------------COOKIE!!------------------------
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(context, "Downloading File", Toast.LENGTH_LONG).show();
        });
    }

    public void loadAIA(String siteId, String mobile, String secretKey, String username){
        configureWebView(webView);

        /*if (checkLocalData()) {
            File file = new File(context.getFilesDir() + "/webarchive/");
            if (file.exists()) {
                if (file.listFiles() != null) {
                    int noOfFiles = file.listFiles().length;

                    if (noOfFiles > 0) {
                        String url = "file://" + file.toString() + File.separator + "aia2.mht";
                        webView.loadUrl(url);
                    }
                    else {

                    }
                }
                else {

                }
            }
            else {
                Log.d("AiALoader", "Empty local storage");

            }
        }
        else {
            String url = String.format(ServerURLManager.SERVICE + LAUNCH_URL, siteId, secretKey, mobile, username);
            webView.loadUrl(url);
        }*/
        String url = String.format(ServerURLManager.SERVICE + LAUNCH_URL, siteId, secretKey, mobile, username);
        Log.d("Loader", "URL: " + url);
        webView.loadUrl(url);
        i = 0;
    }

    public boolean checkLocalData() {
        File file = new File(context.getFilesDir() + "/webarchive/");
        if (file.exists()) {
            if (file.listFiles() != null) {
                int noOfFiles = file.listFiles().length;

                Log.d("AiALoader", "No of files: " + noOfFiles);

                if (noOfFiles > 0) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            Log.d("AiALoader", "Empty local storage");
            return false;
        }
    }

    class eBooksWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (isNetworkConnected(context)) {
                String webArchiveFileName = "aia" + ++i + ".mht";
                File file = new File(context.getFilesDir() + "/webarchive/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                else {
                    Log.d("AiALoader", "List of files: " + Arrays.toString(file.listFiles()));
                }

                String pathToArchive = file.toString() + File.separator + webArchiveFileName;
                Log.d("AppInApp", "Web Archive: " + pathToArchive);
                //Thread.sleep(2000);
                view.saveWebArchive(pathToArchive);

            }

        }
    }

    public  boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    static class eBooksChromeClient extends WebChromeClient {
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor

        // Need to accept permissions to use the camera
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            request.grant(request.getResources());
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            if (cm != null && cm.sourceId().length() > 0) {
                Log.i("AppInApp", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId().substring(10));
            } else if (cm != null) {
                Log.i("AppInApp", cm.message() + " -- From line "
                        + cm.lineNumber());
            }
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //result.cancel();
            return false;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            //result.cancel();
            return false;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            //result.cancel();
            return false;
        }
    }

}
