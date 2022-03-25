package com.android.wonderslate.appinapp.data.model;

public final class AIANetworkModel {
    private final String baseURL;
    private final String[] requiredURLComps;
    private final String[] optionalURLComps;
    private final Integer userServiceCode;

    public AIANetworkModel(int userServiceCode, String baseURL,
                      String[] requiredURLComps, String[] optionalURLComps) {
        this.baseURL = baseURL;
        this.requiredURLComps = requiredURLComps;
        this.optionalURLComps = optionalURLComps;
        this.userServiceCode = userServiceCode;
    }

    public Boolean validateURL() {
        return true;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String[] getRequiredURLComps() {
        return requiredURLComps;
    }

    public String[] getOptionalURLComps() {
        return optionalURLComps;
    }
}
