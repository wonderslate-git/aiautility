package com.android.wonderslate.appinapp.util;

import com.android.wonderslate.appinapp.Wonderslate;

public final class AppConstants {
    public static final String URL_SELECTED_BOOK_IMAGE_API =
            Wonderslate.SERVICE + "funlearn/showProfileImage?type=books&imgType=passport"; // ?id=" AND &fileName=

    public static final String HTTP_IMAGE_FILENAME = "fileName";

    public static final String HTTP_OBJECT_ID = "id";

    public static final String SERVICE_QA = "https://qa.wonderslate.com/";
    public static final String SERVICE_STAGING = "https://staging.wonderslate.com/";
    public static final String SERVICE_PUBLISH = "https://publish.wonderslate.com/";
    public static final String SERVICE_LIVE = "https://www.wonderslate.com/";
    public static final String SERVICE_QADEV = "https://dev.wonderslate.com/";

    public static final String LAUNCH_URL = "intelligence/sessionGenerator?siteId=%s&secretKey=%s&loginId=%s&name=%s";
    public static final String APP_IN_APP_LIBRARY_URL = "wsLibrary/myLibrary";
    public static final String APP_IN_APP_STORE_URL = "appinapp/store";
}
