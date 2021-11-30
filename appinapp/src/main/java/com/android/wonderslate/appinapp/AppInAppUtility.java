package com.android.wonderslate.appinapp;

import android.content.Context;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.views.ViewFragment;

public final class AppInAppUtility {

    private Context mContext;
    private String mSiteId;
    private WSSharedPrefs wsSharedPrefs;
    private String mUserName, mUserMobile, mClientSecret;

    private static AppInAppUtility mInstance;

    private AppInAppUtility() {

    }

    public static AppInAppUtility getInstance(Context context, String secret, String userName, String userMobile) {
        if (mInstance == null) {
            mInstance = new AppInAppUtility();
        }
        mInstance.setContext(context);
        mInstance.setSiteId("1");
        mInstance.setmClientSecret(secret);
        mInstance.setmUserName(userName);
        mInstance.setmUserMobile(userMobile);
        return mInstance;
    }

    public ViewFragment getAIAFragment() {
        return ViewFragment.newInstance(mInstance.getmSiteId(), mInstance.getmClientSecret(), mInstance.getmUserName(), mInstance.getmUserMobile());
    }

    private void storeInfo() {

    }

    private void setSiteId(String id) {
        mSiteId = id;
    }

    private void setContext(Context context) {
        mContext = context;
    }

    protected Context getmContext() {
        return mContext;
    }

    protected String getmSiteId() {
        return mSiteId;
    }

    protected WSSharedPrefs getSharedPrefs() {
        if (wsSharedPrefs == null) {
            wsSharedPrefs = WSSharedPrefs.getInstance(mContext);
        }
        return wsSharedPrefs;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserMobile() {
        return mUserMobile;
    }

    public void setmUserMobile(String mUserMobile) {
        this.mUserMobile = mUserMobile;
    }

    public String getmClientSecret() {
        return mClientSecret;
    }

    public void setmClientSecret(String mClientSecret) {
        this.mClientSecret = mClientSecret;
    }
}
