package com.android.wonderslate.appinapp.data.remote;

import android.util.Log;

import com.android.wonderslate.appinapp.data.model.AIANetworkModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class APIs {
    private final static String TAG = "APIs";

    private static final String SITEID = "siteId";
    private static String siteIdValue;

    private volatile HashMap<Integer, AIANetworkModel> networkModelHashMap;
    private HashMap<String, String> requiredCpmpsDefaults;

    private static volatile APIs apiManager = null;

    private static Object mutex = new Object();

    private APIs() {
    }

    public static APIs getInstance(String siteId) {
        siteIdValue = siteId;
        APIs instance = apiManager;
        if (instance == null) {
            synchronized (mutex) {
                instance = apiManager;
                if (instance == null) {
                    apiManager = instance = new APIs();
                    setupAPI(apiManager);
                }
            }
        }
        return instance;
    }

    //API ENDPOINT SERIAL NUMBER
    public static final Integer SERVICE_GET_USER_PURCHASE_HISTORY = 1;
    public static final Integer SERVICE_GET_STORE_BOOKS = 2;

    //API Endpoint URLs
    public static final String LAUNCH_URL = "intelligence/sessionGenerator?siteId=%s&secretKey=%s&loginId=%s&name=%s";
    public static final String APP_IN_APP_LIBRARY_URL = "wsLibrary/myLibrary";
    public static final String APP_IN_APP_STORE_URL = "appinapp/store";
    public static final String APP_IN_APP_PURCHASE_HISTORY = "creation/getAIAUserOrdersList";
    public static final String APP_IN_APP_STORE_BOOKS_LIST = "wonderpublish/getNewBooksList";

    private static void setupAPI(APIs apiManager) {
        AIANetworkModel networkModel;
        String[] requiredComps;
        String[] optionalComps;

        apiManager.networkModelHashMap = new HashMap<>(0);

        // User's Purchase History
        requiredComps = new String[]{SITEID};
        optionalComps = new String[]{};
        networkModel = new AIANetworkModel(SERVICE_GET_USER_PURCHASE_HISTORY, APP_IN_APP_PURCHASE_HISTORY, requiredComps, optionalComps);
        apiManager.networkModelHashMap.put(SERVICE_GET_USER_PURCHASE_HISTORY, networkModel);

        // Store Book List
        requiredComps = new String[]{SITEID};
        optionalComps = new String[]{};
        networkModel = new AIANetworkModel(SERVICE_GET_STORE_BOOKS, APP_IN_APP_STORE_BOOKS_LIST, requiredComps, optionalComps);
        apiManager.networkModelHashMap.put(SERVICE_GET_STORE_BOOKS, networkModel);

        apiManager.requiredCpmpsDefaults = new HashMap<>(0);
        //apiManager.requiredCpmpsDefaults.put(ConstantsHelper.HTTP_MODE, "chapter");
    }

    public String getServiceURL(Integer serviceCode, HashMap<String, String> urlExtras) {
        AIANetworkModel sap = networkModelHashMap.get(serviceCode);
        String baseURL = sap.getBaseURL();
        List<String> optionals = Arrays.asList(sap.getOptionalURLComps());
        List<String> requireds = Arrays.asList(sap.getRequiredURLComps());

        HashMap<String, Boolean> mandatoryMapping = new HashMap<>(optionals.size());
        for (String param : requireds) {
            mandatoryMapping.put(param, false);
        }
        StringBuilder sbURL = new StringBuilder();
        String binding = "?";
        sbURL.append(ServerURLManager.SERVICE);
        sbURL.append(baseURL);
        if (urlExtras != null) {
            urlExtras.put("siteId", siteIdValue);
            for (String key : urlExtras.keySet()) {
                if (requireds.contains(key)) {
                    mandatoryMapping.put(key, true);
                }

                try {
                    sbURL.append(binding).append(key).append("=");
                    String urlExtraData = urlExtras.get(key);
                    if (urlExtraData == null) {
                        urlExtraData = "null";
                    }
                    String encodedLink = URLEncoder.encode(urlExtraData, "UTF-8");
                    sbURL.append(encodedLink);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Exception while getting ServiceURL", e);
                }
                binding = "&";
            }
        }

        return sbURL.toString();
    }
}
