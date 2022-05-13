package com.android.wonderslate.appinapp.data.remote;

import static com.android.wonderslate.appinapp.util.AppConstants.ERR_GENERIC;
import static com.android.wonderslate.appinapp.util.AppConstants.ERR_NO_NETWORK;
import static com.android.wonderslate.appinapp.util.AppConstants.ERR_WHILE_PARSING_DATA;
import static com.android.wonderslate.appinapp.util.AppConstants.SUCCESS;

import android.app.Activity;
import android.util.Log;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.interfaces.NetworkDataHandlerCallback;
import com.android.wonderslate.appinapp.util.CommonUtils;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public final class NetworkDataHandler {

   public static void getStoreBooksList(Activity activity, String categories, String level, String syllabus,
                                        String grade, String subject, String pageNo, String publisherId,
                                        NetworkDataHandlerCallback networkDataHandlerCallback) {
      if (activity != null) {
         if (!new CommonUtils().isOnline(activity)) {
            networkDataHandlerCallback.onFailure(ERR_NO_NETWORK, "Failed to connect to network. Please check the network connection.");
            return;
         }

         final String[] response = new String[1];

         AIANetworkManager networkManager = AIANetworkManager.getInstance(activity.getApplicationContext());

         APIs apis = APIs.getInstance(WSSharedPrefs.getInstance(activity).getSiteId());
         HashMap<String, String> params = new HashMap<>();
         params.put("categories", categories);
         params.put("level", level);
         params.put("syllabus", syllabus);
         params.put("grade", grade);
         params.put("subject", subject);
         params.put("pageNo", pageNo);
         params.put("publisherId", publisherId);

         String url = apis.getServiceURL(APIs.SERVICE_GET_STORE_BOOKS, params);

         Log.i("App In App", "API Call URL: " + url);

         AIARemoteCallback aiaRemoteCallback = new AIARemoteCallback() {
            @Override
            public void onSuccess(UrlRequest request, UrlResponseInfo info, byte[] bodyBytes, long latencyNanos) {
               try {
                  response[0] = new JSONObject(new String(bodyBytes)).toString();
                  Log.d("response", "api response: " + response[0]);
                  networkDataHandlerCallback.onSuccess(SUCCESS, info.getHttpStatusText(), response[0]);
               } catch (JSONException e) {
                  response[0] = "";
                  networkDataHandlerCallback.onFailure(ERR_WHILE_PARSING_DATA, e.getMessage());
               }
            }

            @Override
            public void onFailure(UrlRequest request, UrlResponseInfo info, CronetException exception) {
               response[0] = "";
               networkDataHandlerCallback.onFailure(ERR_GENERIC, exception.getMessage());
            }

            @Override
            public void onRequestCanceled(UrlRequest request, UrlResponseInfo info) {
               response[0] = "";
               networkDataHandlerCallback.onFailure(ERR_GENERIC, "Network request was cancelled");
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
}
