package com.android.wonderslate.appinapp;

import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_LIVE;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_PUBLISH;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_QA;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_STAGING;

public final class Wonderslate {

    private static Wonderslate mInstance;

    public static String SERVICE;

    public static Servers currentServer;

    public enum Servers {
        QA, STAGING, PUBLISH, LIVE
    }

    private Wonderslate() {

    }

    public static synchronized void init() {
        if (mInstance == null) {
            mInstance = new Wonderslate();
        }
    }

    public void setService() {
        //Select Base Service URL According To Server.
        currentServer = Servers.PUBLISH; //Use QA, STAGING, PUBLISH, LIVE

        switch (currentServer){
            case QA:
                SERVICE = SERVICE_QA;
                break;
            case STAGING:
                SERVICE = SERVICE_STAGING;
                break;
            case PUBLISH:
                SERVICE = SERVICE_PUBLISH;
                break;
            case LIVE:
                SERVICE = SERVICE_LIVE;
                break;
            default:
                break;
        }
    }

    public static Wonderslate getInstance() {
        return mInstance;
    }

}
