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
    private String mUserName, mUserMobile, mClientSecret, mUserEmail, mSiteId;

    private static AppInAppUtility mInstance;

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
        return ViewFragment.newInstance(mSiteId, mClientSecret, mUserName,
                mUserMobile, mUserEmail);
    }

    private void init(@NonNull String secret, @NonNull String userName, @NonNull String userMobile,
                      @NonNull String userEmail, @Nullable String siteId) {
        mUserName = userName;
        mUserMobile = userMobile;
        mUserEmail = userEmail;
        mSiteId = siteId;
        mClientSecret = secret;
    }
}
