package com.ws.android.playground.aiaimpl.todoapp.appinappsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class ToDoSharedPrefs {
    private static final String TAG = "ToDoSharedPrefs";

    private static final String SHARED_PREFS_NAME = "ToDoSharedPrefs";
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
    private static final String SHARED_PREFS_CLIENTNAME = "ClientName";
    private static final String SHARED_PREFS_BOUGHT_BOOK_ID = "BookId";
    private static final String SHARED_PREFS_BOUGHT_BOOK_TITLE = "BookTitle";
    private static final String SHARED_PREFS_BOUGHT_BOOK_PRICE = "BookPrice";
    private static final String SHARED_PREFS_BOUGHT_BOOK_AUTHOR = "BookAuthor";
    private static final String SHARED_PREFS_BOUGHT_BOOK_COVER = "BookCover";

    private Context appContext = null;
    private static ToDoSharedPrefs wsSharedPrefs;
    private SharedPreferences sharedPrefs = null;

    private String serviceSiteId;

    private ToDoSharedPrefs(Context context) {
        this.appContext = context;
    }

    public static ToDoSharedPrefs getInstance(Context context) {
        if (wsSharedPrefs == null) {
            wsSharedPrefs = new ToDoSharedPrefs(context);

            wsSharedPrefs.sharedPrefs =
                    context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        }
        return wsSharedPrefs;
    }

    public synchronized void clearSharedPrefs() {
        this.sharedPrefs.edit().clear().commit();
    }

    public synchronized String getSiteId() {
        return this.sharedPrefs.getString(SHARED_PREFS_SERVICE_SITE_ID, "");
    }

    public void setSiteId(String id) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_SERVICE_SITE_ID, id).commit();
    }

    public synchronized String getAccessToken() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String token) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_ACCESS_TOKEN, token).commit();
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
    }

    public String getServiceSiteId() {
        return serviceSiteId;
    }

    public void setUsername(String name) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_USERNAME, name).commit();
    }

    public String getUserName() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USERNAME, "");
    }

    public String getUserId() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USERID, "");
    }

    public void setUserId(String userId) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_USERID, userId).commit();
    }

    public void setUsermobile(String mobile) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_USERMOBILE, mobile).commit();
    }

    public String getUsermobile() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USERMOBILE, "");
    }

    public void setChapterFirstTimeStatus(String id, boolean value) {
        this.sharedPrefs.edit().putBoolean(id, value).commit();
    }

    public void setCookie(String id, String value) {
        this.sharedPrefs.edit().putString(id, value).commit();
    }

    public void setUserEmail(String token) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_USEREMAIL, token).commit();
    }

    public String getUseremail() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_USEREMAIL, "");
        //return this.useremail;
    }

    public void setClientName(String value) {
        this.sharedPrefs.edit().putString(SHARED_PREFS_CLIENTNAME, value).commit();
    }

    public String getClientName() {
        return wsSharedPrefs.sharedPrefs.getString(SHARED_PREFS_CLIENTNAME, "");
    }
}
