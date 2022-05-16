package com.android.wonderslate.appinapp.data.remote;

import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_LIVE;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_PUBLISH;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_QA;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_QADEV;
import static com.android.wonderslate.appinapp.util.AppConstants.SERVICE_STAGING;

public final class ServerURLManager {

    private static ServerURLManager mInstance;

    public static String SERVICE;

    public static Servers currentServer;

    public enum Servers {
        QA, QADEV, STAGING, PUBLISH, LIVE
    }

    private ServerURLManager() {

    }

    public static synchronized void init() {
        if (mInstance == null) {
            mInstance = new ServerURLManager();
        }
    }

    public void setService() {
        //Select Base Service URL According To Server.
        currentServer = Servers.STAGING; //Use QA, STAGING, PUBLISH, LIVE

        switch (currentServer){
            case QA:
                SERVICE = SERVICE_QA;
                break;
            case QADEV:
                SERVICE = SERVICE_QADEV;
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

    public static ServerURLManager getInstance() {
        return mInstance;
    }

}
