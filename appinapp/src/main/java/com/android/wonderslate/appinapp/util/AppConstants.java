package com.android.wonderslate.appinapp.util;

import com.android.wonderslate.appinapp.data.remote.ServerURLManager;

public final class AppConstants {
    //Image Path URLs
    public static final String URL_SELECTED_BOOK_IMAGE_API =
            ServerURLManager.SERVICE + "funlearn/showProfileImage?type=books&imgType=passport"; // ?id=" AND &fileName=
    public static final String HTTP_IMAGE_FILENAME = "fileName";
    public static final String HTTP_OBJECT_ID = "id";

    //Server URLs
    public static final String SERVICE_QA = "https://qa.wonderslate.com/";
    public static final String SERVICE_STAGING = "https://staging.wonderslate.com/";
    public static final String SERVICE_PUBLISH = "https://publish.wonderslate.com/";
    public static final String SERVICE_LIVE = "https://www.wonderslate.com/";
    public static final String SERVICE_QADEV = "https://dev.wonderslate.com/";

    //Razorpay Fields & Strings
    public static final String RZP_COMPANY_NAME = "name";
    public static final String RZP_TRANSACTION_DESCRIPTION = "description";
    public static final String RZP_CURRENCY = "currency";
    public static final String RZP_CURRENCY_INR = "INR";
    public static final String RZP_TRANSACTION_AMOUNT = "amount";
    public static final String RZP_PREFILL_USER_NAME = "name";
    public static final String RZP_PREFILL_USER_EMAIL = "email";
    public static final String RZP_PREFILL_USER_CONTACT = "contact";
    public static final String RZP_THEME_COLOR = "color";
    public static final String RZP_PREFILL = "prefill";
    public static final String RZP_READ_ONLY = "readonly";
    public static final String RZP_THEME = "theme";
    public static final String RZP_NOTES = "notes";
    public static final String RZP_BOOK_ID = "bookId";
    public static final String RZP_USER_NAME = "username";

}
