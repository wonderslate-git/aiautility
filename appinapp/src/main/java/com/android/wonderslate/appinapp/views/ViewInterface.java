package com.android.wonderslate.appinapp.views;

import com.android.wonderslate.appinapp.views.ViewFragment;

public interface ViewInterface {
    ViewFragment newInstance(String siteId, String secret, String name, String mobile);
    void destroy();
    void refresh();
}
