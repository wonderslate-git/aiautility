package com.android.wonderslate.appinapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.data.remote.AIANetworkManager;
import com.android.wonderslate.appinapp.data.remote.AIARemoteCallback;
import com.android.wonderslate.appinapp.data.remote.APIs;
import com.android.wonderslate.appinapp.data.remote.ServerURLManager;
import com.android.wonderslate.appinapp.interfaces.UserPurchaseHistoryCallback;
import com.android.wonderslate.appinapp.views.ViewFragment;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * App In App Library powered by ServerURLManager Technologies.
 * This class is the entry point to App In App Library.
 * @author Wonderslate Technologies
 */
public final class AppInAppUtility {

    private String mUserName, mUserMobile, mClientSecret, mUserEmail, mSiteId;

    private static AppInAppUtility mInstance;
    private ViewFragment viewFragment;

    private AppInAppUtility() {}

    /**
     * Returns AppInApp static instance. This instance is the entry point into the library.
     * The instance returned can be null, depending on the value of context param
     * @param secret client secret.
     * @param userName the registered name of the user who is currently logged in to the client app.
     * @param userMobile the registered mobile number of the user
     * @return App In App instance
     */
    public static AppInAppUtility getInstance(@NonNull String secret, @NonNull String userName,
                                              @NonNull String userMobile, @NonNull String userEmail, @NonNull String siteId) {
        if (mInstance == null) {
            mInstance = new AppInAppUtility();
        }
        mInstance.init(secret, userName, userMobile, userEmail, siteId);
        return mInstance;
    }

    /**
     * Returns fragment instance of type ViewFragment. This fragment instance needs to fill up the container Activity in the client app.
     * @return fragment of type ViewFragment
     */
    @NonNull
    public ViewFragment getAIAFragment() {
        viewFragment = ViewFragment.newInstance(mSiteId, mClientSecret, mUserName,
                mUserMobile, mUserEmail);

        return viewFragment;
    }

    public void refresh() {
        if (viewFragment != null) {
            viewFragment.refreshView();
        }
    }

    public void getAIAPurchaseOrder(UserPurchaseHistoryCallback purchaseHistoryCallback) {
        final String[] response = new String[1];
        //Call Purchase History API

        if (viewFragment.getActivity() != null) {
            AIANetworkManager networkManager = AIANetworkManager.getInstance(viewFragment.getActivity().getApplicationContext());

            APIs apis = APIs.getInstance(mSiteId);
            String aiaUsername = WSSharedPrefs.getInstance(viewFragment.getContext()).getSiteId() + "_" + WSSharedPrefs.getInstance(viewFragment.getContext()).getUsermobile();
            HashMap<String, String> params = new HashMap<>();
            params.put("username", aiaUsername);
            String url = apis.getServiceURL(APIs.SERVICE_GET_USER_PURCHASE_HISTORY, params);

            Log.i("App In App", "API Call URL: " + url);

            AIARemoteCallback aiaRemoteCallback = new AIARemoteCallback() {
                @Override
                public void onSuccess(UrlRequest request, UrlResponseInfo info, byte[] bodyBytes, long latencyNanos) {
                    try {
                        response[0] = new JSONObject(new String(bodyBytes)).toString();
                        Log.d("response", "api response: " + response[0]);
                        purchaseHistoryCallback.onSuccess(Integer.toString(info.getHttpStatusCode()), info.getHttpStatusText(), response[0]);
                    } catch (JSONException e) {
                        response[0] = "";
                        purchaseHistoryCallback.onFailure("400", "Status: Failed", e.getMessage());
                    }
                }

                @Override
                public void onFailure(UrlRequest request, UrlResponseInfo info, CronetException exception) {
                    response[0] = "";
                    purchaseHistoryCallback.onFailure(Integer.toString(info.getHttpStatusCode()), info.getHttpStatusText(), exception.getMessage());
                }

                @Override
                public void onRequestCanceled(UrlRequest request, UrlResponseInfo info) {
                    response[0] = "";
                    purchaseHistoryCallback.onFailure(Integer.toString(info.getHttpStatusCode()), info.getHttpStatusText(), "Network request was cancelled");
                }
            };

            UrlRequest.Builder builder = networkManager.getCronetEngine()
                    .newUrlRequestBuilder(
                            url,
                            aiaRemoteCallback,
                            networkManager.getCronetCallbackExecutorService())
                    .setHttpMethod("GET")
                    .addHeader("Accept-Charset", "UTF-8")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    // Cronet supports QoS if you specify request priorities
                    .setPriority(UrlRequest.Builder.REQUEST_PRIORITY_IDLE);

            // Start the request
            builder.build().start();
        }
    }

    private void init(@NonNull String secret, @NonNull String userName, @NonNull String userMobile,
                      @NonNull String userEmail, @Nullable String siteId) {
        ServerURLManager.init();
        ServerURLManager.getInstance().setService();
        mUserName = userName;
        mUserMobile = userMobile;
        mUserEmail = userEmail;
        mSiteId = siteId;
        mClientSecret = secret;
    }
}
