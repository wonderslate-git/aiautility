package com.android.wonderslate.appinapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

public class CommonUtils {
   public boolean isOnline(@NonNull Context context) {
      ConnectivityManager cm =
              (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      return activeNetwork != null &&
              activeNetwork.isConnectedOrConnecting();
   }
}
