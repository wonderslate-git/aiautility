package com.android.wonderslate.appinapp.interfaces;

public interface UserPurchaseHistoryCallback {
    void onSuccess(String responseCode, String responseStatus, String responseBody);
    void onFailure(String responseCode, String responseStatus, String message);
}
