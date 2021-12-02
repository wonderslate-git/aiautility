package com.android.wonderslate.appinapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.views.ViewFragment;
import com.android.wonderslate.appinapp.views.ViewInterface;

/**
 * App In App Library powered by Wonderslate Technologies.
 * This class is the entry point to App In App Library.
 * @author Wonderslate Technologies
 */
public final class AppInAppUtility {

    private Context mContext;
    private WSSharedPrefs wsSharedPrefs;
    private String mUserName, mUserMobile, mClientSecret;

    private static AppInAppUtility mInstance;
    private ViewFragment viewFragment;

    private AppInAppUtility() {}

    /**
     * Returns AppInApp static instance. This instance is the entry point into the library.
     * The instance returned can be null, depending on the value of context param.
     * @param context application context is required(mandatory).
     * @param secret client secret.
     * @param userName the registered name of the user who is currently logged in to the client app.
     * @param userMobile the registered mobile number of the user
     * @return App In App instance
     */
    public static AppInAppUtility getInstance(@NonNull Context context, @NonNull String secret, @NonNull String userName, @NonNull String userMobile, @NonNull String siteId) {
        if (mInstance == null) {
            mInstance = new AppInAppUtility();
        }
        mInstance.setContext(context);
        mInstance.init(mInstance.getmContext(), secret, userName, userMobile, siteId);
        return mInstance;
    }

    /**
     * Returns fragment instance of type ViewFragment. This fragment instance needs to fill up the container Activity in the client app.
     * @return fragment of type ViewFragment
     */
    @NonNull
    public ViewFragment getAIAFragment() {
        viewFragment = ViewFragment.newInstance(wsSharedPrefs.getSiteId(), wsSharedPrefs.getAccessToken(), wsSharedPrefs.getUserName(), wsSharedPrefs.getUsermobile());
        return viewFragment;
    }

    private void init(@NonNull Context context, @NonNull String secret, @NonNull String userName, @NonNull String userMobile, @Nullable String siteId) {
        wsSharedPrefs = WSSharedPrefs.getInstance(context);
        wsSharedPrefs.setUsername(userName);
        wsSharedPrefs.setUsermobile(userMobile);
        wsSharedPrefs.setAccessToken(secret);
        wsSharedPrefs.setSiteId(siteId);
    }

    private void setContext(Context context) {
        mContext = context;
    }

    private Context getmContext() {
        return mContext;
    }
}
