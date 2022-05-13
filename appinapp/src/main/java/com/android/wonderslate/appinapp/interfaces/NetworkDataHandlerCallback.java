package com.android.wonderslate.appinapp.interfaces;

public interface NetworkDataHandlerCallback {
    void onSuccess(Integer responseCode, String responseStatus, String responseBody);
    void onFailure(Integer responseCode, String message);
}
