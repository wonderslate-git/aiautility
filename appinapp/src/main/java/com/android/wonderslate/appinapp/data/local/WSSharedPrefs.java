package com.android.wonderslate.appinapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class WSSharedPrefs {
    private static final String TAG = "WSSharedPrefs";

    private static final String SHARED_PREFS_NAME = "WSSharedPrefs";
    private static final String SHARED_PREFS_USERNAME = "UserName";
    private static final String SHARED_PREFS_USERID = "UserId";
    private static final String SHARED_PREFS_USEREMAIL = "UserEmail";
    private static final String SHARED_PREFS_ACCESS_TOKEN = "AccessToken";
    private static final String SHARED_PREFS_DEVICE_REGISTRATION = "DeviceRegistration";
    private static final String SHARED_PREFS_TOPIC_SUBSCRIPTION = "TopicSubscription";
    private static String SHARED_PREFS_RES_ID;
    private static final String SHARED_PREFS_SERVICE_SITE_ID = "SiteId";
    private static final String SHARED_PREFS_DEVICE_ID = "deviceID";
    private static final String SHARED_PREFS_DEEP_LINK = "deepLink";
    private static final String SHARED_PREFS_DATA = "Data";
    private static final String SHARED_PREFS_USERMOBILE = "UserMobile";

    private Context appContext = null;
    private static WSSharedPrefs wsSharedPrefs;
    private SharedPreferences sharedPrefs = null;

    private String accessToken;
    private String username;
    private String useremail;
    private String usermobile;
    private String userId;
    private String serviceSiteId;

    private WSSharedPrefs(Context context) {
        this.appContext = context;
    }

    public static WSSharedPrefs getInstance(Context context) {
        if (wsSharedPrefs == null) {
            wsSharedPrefs = new WSSharedPrefs(context);

            wsSharedPrefs.sharedPrefs =
                    context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

            wsSharedPrefs.accessToken =
                    wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_ACCESS_TOKEN, "nil");
            wsSharedPrefs.serviceSiteId =
                    wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_SERVICE_SITE_ID, "1");

            wsSharedPrefs.userId = wsSharedPrefs
                    .sharedPrefs.getString(SHARED_PREFS_USERID, "");

        }
        return wsSharedPrefs;
    }

    public synchronized String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String token) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_ACCESS_TOKEN, token).commit();
        this.accessToken = token;
    }

    public void deviceRegistered(boolean value) {
        this.sharedPrefs.edit().putBoolean(SHARED_PREFS_DEVICE_REGISTRATION, value).commit();
    }

    public boolean isDeviceRegistered() {
        return this.sharedPrefs.getBoolean(SHARED_PREFS_DEVICE_REGISTRATION, false);
    }

    public void subscribedToTopic(boolean value) {
        this.sharedPrefs.edit().putBoolean(SHARED_PREFS_TOPIC_SUBSCRIPTION, value).commit();
    }

    public boolean isSubscribedToTopic() {
        return this.sharedPrefs.getBoolean(SHARED_PREFS_TOPIC_SUBSCRIPTION, false);
    }

    public boolean isImageReplaced(String resId) {
        SHARED_PREFS_RES_ID = resId;
        return this.sharedPrefs.getBoolean(SHARED_PREFS_RES_ID + "_READ_IMAGE", true);
    }

    public void setImageReplaced(String resId, boolean imageReplaced) {
        SHARED_PREFS_RES_ID = resId;
        this.sharedPrefs.edit().putBoolean(SHARED_PREFS_RES_ID + "_READ_IMAGE", imageReplaced).commit();
    }

    public void setSVGUpdated(String resId, boolean imageReplaced) {
        SHARED_PREFS_RES_ID = resId;
        this.sharedPrefs.edit().putBoolean(SHARED_PREFS_RES_ID + "_SVG_UPDATED", imageReplaced).commit();
    }

    public boolean isSVGUpdated(String resId) {
        SHARED_PREFS_RES_ID = resId;
        return this.sharedPrefs.getBoolean(SHARED_PREFS_RES_ID + "_SVG_UPDATED", false);
    }

    public void setDeviceID(String id) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_DEVICE_ID, id).commit();
    }

    public String getDeviceID() {
        return this.sharedPrefs.getString(SHARED_PREFS_DEVICE_ID, "");
    }

    public void setDeepLinkUri(Uri deepLinkUri) {
        if (deepLinkUri != null) {
            this.sharedPrefs.edit().putString(SHARED_PREFS_DEEP_LINK, deepLinkUri.toString()).commit();
        } else {
            this.sharedPrefs.edit().putString(SHARED_PREFS_DEEP_LINK, "").commit();
        }
    }

    public Uri getDeepLinkUri() {
        return Uri.parse(this.sharedPrefs.getString(SHARED_PREFS_DEEP_LINK, ""));
    }

    public void clearAccessToken() {
        this.sharedPrefs.edit().putString(SHARED_PREFS_ACCESS_TOKEN, "nil").commit();
        this.sharedPrefs.edit().putString(SHARED_PREFS_DATA, "nil").commit();
        this.accessToken = "nil";
    }

    public String getServiceSiteId() {
        return serviceSiteId;
    }

    public void setUsername(String token) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_USERNAME, token).commit();
        this.username = token;
    }

    public String getUserId() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USERID, "");
    }

    public void setUserId(String userId) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_USERID, userId).commit();
        this.userId = userId;
    }

    public void setUsermobile(String token) {
        if (token != null && !token.isEmpty() && !token.equalsIgnoreCase("null")) {
            //do nothing
        } else
            token = "";
        this.sharedPrefs.edit().putString(SHARED_PREFS_USERMOBILE, token).commit();
        this.usermobile = token;
    }

    public String getUsermobile() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USERMOBILE, "");
        //return this.usermobile;
    }

    public void setChapterFirstTimeStatus(String id, boolean value) {
        this.sharedPrefs.edit().putBoolean(id, value).commit();
    }

    public void setCookie(String id, String value) {
        this.sharedPrefs.edit().putString(id, value).commit();
    }

    public void setUserEmail(String token) {
        if (token != null && !token.isEmpty() && !token.equalsIgnoreCase("null")) {
            //do nothing
        } else
            token = "";
        this.sharedPrefs.edit().putString(SHARED_PREFS_USEREMAIL, token).commit();
        this.useremail = token;
    }

    public String getUseremail() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USEREMAIL, "");
        //return this.useremail;
    }
}
