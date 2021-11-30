package wonderslate.android.com.appinapp;

import android.content.Context;

import wonderslate.android.com.appinapp.data.local.WSSharedPrefs;
import wonderslate.android.com.appinapp.util.ApplicationMode;

public final class Wonderslate {

    private static Wonderslate mInstance;
    private Context mContext;
    private String mSiteID = "";
    private WSSharedPrefs sharedPrefs = null;
    public static final int RESULT_STATUS_FAILED = -2;
    public static final int RESULT_STATUS_SUCCESS = 0;
    public static final String BROADCAST_ACTION_DOWNLOAD_FILE = "fileDownload";
    private static ApplicationMode appMode = ApplicationMode.UNDEFINED;

    public static String SERVICE;
    public static String SERVICE1;

    public static Servers currentServer;

    public enum Servers {
        QA, STAGING, PUBLISH, LIVE
    }


    public static final Integer LIBRARY_TYPE_USER = 0;
    public static final Integer LIBRARY_TYPE_STORE = 1;

    private Wonderslate(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized void init(Context context, String siteId) {
        if (mInstance == null) {
            mInstance = new Wonderslate(context);
            mInstance.setSiteId(siteId);
            mInstance.setContext(context);
        }
    }

    public void setService() {
        //Select Base Service URL According To Server.
        currentServer = Servers.LIVE; //Use QA, STAGING, PUBLISH, LIVE

        switch (currentServer){
            case QA:
                SERVICE = mContext.getResources().getString(R.string.service_qa);
                break;
            case STAGING:
                SERVICE = mContext.getResources().getString(R.string.service_staging);
                break;
            case PUBLISH:
                SERVICE = mContext.getResources().getString(R.string.service_publish);
                break;
            case LIVE:
                SERVICE = mContext.getResources().getString(R.string.service_live);
                break;
            default:
                break;
        }
    }

    private void setSiteId(String siteId) {
        mSiteID = siteId;
    }

    private void setContext(Context context) {
        mContext = context;
    }

    public static Wonderslate getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public String getSiteID() {
        return mSiteID;
    }

    public WSSharedPrefs getSharedPrefs() {
        if (this.sharedPrefs == null) {
            this.sharedPrefs = WSSharedPrefs.getInstance(mContext);
        }
        return this.sharedPrefs;
    }

    public void setAppMode(ApplicationMode mode) {
        appMode = mode;
    }

    public ApplicationMode getAppMode() {
        return appMode;
    }

}
