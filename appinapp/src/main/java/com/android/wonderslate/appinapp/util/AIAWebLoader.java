package com.android.wonderslate.appinapp.util;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.webkit.WebView.setWebContentsDebuggingEnabled;

import static com.android.wonderslate.appinapp.data.remote.APIs.LAUNCH_URL;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wonderslate.appinapp.data.remote.ServerURLManager;
import com.android.wonderslate.appinapp.interfaces.AIAJSInterface;
import com.android.wonderslate.appinapp.interfaces.ContentLoaderCallback;
import com.android.wonderslate.appinapp.views.ViewFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

public class AIAWebLoader {
    WebView webView;
    static Context context;
    static ContentLoaderCallback contentLoaderCallback;
    static int i = 0;
    ViewFragment viewFragment;

    public AIAWebLoader(WebView webView, Context context, ViewFragment v) {
        this.webView = webView;
        AIAWebLoader.context = context;
        viewFragment = v;
    }

    @SuppressLint("SetJavaScriptEnabled")
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

        File dir = context.getCacheDir();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
            webView.getSettings().setAppCachePath(dir.getPath());
        }

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDatabaseEnabled(true);

        webView.addJavascriptInterface(new AIAJSInterface(webView, context, viewFragment), "JSInterface");

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

    public void loadAIA(String siteId, String mobile, String secretKey, String username, ContentLoaderCallback contentLoaderCallback){
        configureWebView(webView);

        AIAWebLoader.contentLoaderCallback = contentLoaderCallback;

        /*if (checkLocalData()) {
            File file = new File(context.getFilesDir() + "/webarchive/");
            if (file.exists()) {
                if (file.listFiles() != null) {
                    int noOfFiles = file.listFiles().length;

                    if (noOfFiles > 0) {
                        String url = file.getAbsolutePath() + File.separator + "aia2.mht";
                        Log.d("AppInApp", "Loading Web Archive: " + url);
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

    /*public void onShopDataReceived() {
        viewFragment.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(AppConstants.sample_books_response);
                    String jsEvaluation = "";
                    jsEvaluation = "onShopBooksReceived(" + jsonObject + ");";

                    webView.evaluateJavascript(jsEvaluation, null);
                } catch (JSONException e) {
                    String jsEvaluation = "";
                    jsEvaluation = "onShopBooksReceived(" + AppConstants.sample_books_response + ");";
                    webView.evaluateJavascript(jsEvaluation, null);
                    e.printStackTrace();
                }
            }
        });
    }*/

    class eBooksWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {}

        /**
         * Notify the host application that a page has started loading. This method
         * is called once for each main frame load so a page with iframes or
         * framesets will call onPageStarted one time for the main frame. This also
         * means that onPageStarted will not be called when the contents of an
         * embedded frame changes, i.e. clicking a link whose target is an iframe,
         * it will also not be called for fragment navigations (navigations to
         * #fragment_id).
         *
         * @param view    The WebView that is initiating the callback.
         * @param url     The url to be loaded.
         * @param favicon The favicon for this page if it already exists in the
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        /**
         * Notify the host application that the WebView will load the resource
         * specified by the given url.
         *
         * @param view The WebView that is initiating the callback.
         * @param url  The url of the resource the WebView will load.
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * Notify the host application of a resource request and allow the
         * application to return the data.  If the return value is {@code null}, the WebView
         * will continue to load the resource as usual.  Otherwise, the return
         * response and data will be used.
         *
         * <p>This callback is invoked for a variety of URL schemes (e.g., {@code http(s):}, {@code
         * data:}, {@code file:}, etc.), not only those schemes which send requests over the network.
         * This is not called for {@code javascript:} URLs, {@code blob:} URLs, or for assets accessed
         * via {@code file:///android_asset/} or {@code file:///android_res/} URLs.
         *
         * <p>In the case of redirects, this is only called for the initial resource URL, not any
         * subsequent redirect URLs.
         *
         * <p class="note"><b>Note:</b> This method is called on a thread
         * other than the UI thread so clients should exercise caution
         * when accessing private data or the view system.
         *
         * <p class="note"><b>Note:</b> When Safe Browsing is enabled, these URLs still undergo Safe
         * Browsing checks. If this is undesired, whitelist the URL with {@link
         * WebView#setSafeBrowsingWhitelist} or ignore the warning with {@link #onSafeBrowsingHit}.
         *
         * @param view    The {@link WebView} that is requesting the
         *                resource.
         * @param request Object containing the details of the request.
         * @return A {@link WebResourceResponse} containing the
         * response information or {@code null} if the WebView should load the
         * resource itself.
         */
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        /**
         * Report web resource loading error to the host application. These errors usually indicate
         * inability to connect to the server. Note that unlike the deprecated version of the callback,
         * the new version will be called for any resource (iframe, image, etc.), not just for the main
         * page. Thus, it is recommended to perform minimum required work in this callback.
         *
         * @param view    The WebView that is initiating the callback.
         * @param request The originating request.
         * @param error   Information about the error occurred.
         */
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.e("AiALoader", "Error received from webview: " + error.toString());
        }

        /**
         * Notify the host application that an HTTP error has been received from the server while
         * loading a resource.  HTTP errors have status codes &gt;= 400.  This callback will be called
         * for any resource (iframe, image, etc.), not just for the main page. Thus, it is recommended
         * to perform minimum required work in this callback. Note that the content of the server
         * response may not be provided within the {@code errorResponse} parameter.
         *
         * @param view          The WebView that is initiating the callback.
         * @param request       The originating request.
         * @param errorResponse Information about the error occurred.
         */
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.e("AiALoader", "HTTP Error received from webview: " + errorResponse.toString());
        }

        /**
         * Notify the host application that an SSL error occurred while loading a
         * resource. The host application must call either {@link SslErrorHandler#cancel} or
         * {@link SslErrorHandler#proceed}. Note that the decision may be retained for use in
         * response to future SSL errors. The default behavior is to cancel the
         * load.
         * <p>
         * This API is only called for recoverable SSL certificate errors. In the case of
         * non-recoverable errors (such as when the server fails the client), WebView will call {@link
         * #onReceivedError(WebView, WebResourceRequest, WebResourceError)} with {@link
         * #ERROR_FAILED_SSL_HANDSHAKE}.
         * <p>
         * Applications are advised not to prompt the user about SSL errors, as
         * the user is unlikely to be able to make an informed security decision
         * and WebView does not provide any UI for showing the details of the
         * error in a meaningful way.
         * <p>
         * Application overrides of this method may display custom error pages or
         * silently log issues, but it is strongly recommended to always call
         * {@link SslErrorHandler#cancel} and never allow proceeding past errors.
         *
         * @param view    The WebView that is initiating the callback.
         * @param handler An {@link SslErrorHandler} that will handle the user's
         *                response.
         * @param error   The SSL error object.
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.e("AiALoader", "SSL Error received from webview: " + error.toString());
        }
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

            if (view.getProgress() == 100) {
                contentLoaderCallback.onLoadFinished();

                /*if (new CommonUtils().isOnline(context)) {
                    String webArchiveFileName = "aia" + ++i + ".mht";
                    File file = new File(context.getFilesDir() + "/webarchive/");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    else {
                        Log.d("AiALoader", "List of files: " + Arrays.toString(file.listFiles()));
                    }

                    String pathToArchive = file.getAbsolutePath() + File.separator + webArchiveFileName;
                    Log.d("AppInApp", "Saving Web Archive: " + pathToArchive);
                    //Thread.sleep(2000);
                    view.saveWebArchive(pathToArchive);

                }*/
            }
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
