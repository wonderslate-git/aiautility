package com.android.wonderslate.appinapp.views;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wonderslate.appinapp.R;
import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.data.remote.NetworkDataHandler;
import com.android.wonderslate.appinapp.interfaces.ContentLoaderCallback;
import com.android.wonderslate.appinapp.interfaces.NetworkDataHandlerCallback;
import com.android.wonderslate.appinapp.util.AIAWebLoader;
import com.android.wonderslate.appinapp.util.AppConstants;
import com.android.wonderslate.appinapp.util.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment implements ContentLoaderCallback, View.OnClickListener {
    private static String TAG = "AIAFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "siteId";
    private static final String ARG_PARAM2 = "secret";
    private static final String ARG_PARAM3 = "userName";
    private static final String ARG_PARAM4 = "userMobile";
    private static final String ARG_PARAM5 = "userEmail";

    // TODO: Rename and change types of parameters
    private String mSiteId;
    private String mSecret;
    private String mName;
    private String mMobile;
    private String mEmail;

    private View rootView;
    public static WebView aiaWebView;
    private LinearLayout errorLayout, noNetworkLayout;
    private TextView errorTxtView;
    //private AVLoadingIndicatorView aiaLoader;
    private Snackbar aiaSnackBar;
    private View aiaOverlay;
    private ProgressBar aiaLoader;
    private Button reloadPageBtn;

    WSSharedPrefs wsSharedPrefs;

    static ViewFragment aiaViewFragment;

    CommonUtils commonUtils;

    private ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param siteId Parameter 1.
     * @param secret Parameter 2.
     * @param name Parameter 1.
     * @param mobile Parameter 2.
     * @return A new instance of fragment ViewFragment.
     */
    public static ViewFragment newInstance(String siteId, String secret, String name, String mobile, String email) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, siteId);
        args.putString(ARG_PARAM2, secret);
        args.putString(ARG_PARAM3, name);
        args.putString(ARG_PARAM4, mobile);
        args.putString(ARG_PARAM5, email);
        fragment.setArguments(args);
        aiaViewFragment = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSiteId = getArguments().getString(ARG_PARAM1);
            mSecret = getArguments().getString(ARG_PARAM2);
            mName = getArguments().getString(ARG_PARAM3);
            mMobile = getArguments().getString(ARG_PARAM4);
            mEmail = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_view, container, false);
        init();
        return rootView;
    }

    private void init() {
        commonUtils = new CommonUtils();
        aiaWebView = rootView.findViewById(R.id.aia_webview);
        aiaLoader = rootView.findViewById(R.id.aia_loader);
        aiaOverlay = rootView.findViewById(R.id.aia_overlay);
        noNetworkLayout = rootView.findViewById(R.id.no_internet_connection_layout);
        noNetworkLayout.setVisibility(View.GONE);
        reloadPageBtn = rootView.findViewById(R.id.reload_page_btn);
        reloadPageBtn.setOnClickListener(this);

        if (checkNetworkAndStart()) {
            showUILoaders(true);

            AIAWebLoader aiaWebLoader = new AIAWebLoader(aiaWebView, this.getContext(), aiaViewFragment);
            aiaWebLoader.loadAIA(mSiteId, mMobile, mSecret, mName, this);
        }

        //Store the values in Shared Prefs with the Fragment's Context
        wsSharedPrefs = WSSharedPrefs.getInstance(this.getContext());
        wsSharedPrefs.setSiteId(mSiteId);
        wsSharedPrefs.setAccessToken(mSecret);
        wsSharedPrefs.setUsername(mName);
        wsSharedPrefs.setUsermobile(mMobile);
        wsSharedPrefs.setUserEmail(mEmail);
    }

    private boolean checkNetworkAndStart() {
        if (getAIAContext() != null) {
            if (!commonUtils.isOnline(getAIAContext())) {
                noNetworkLayout.setVisibility(View.VISIBLE);
                aiaWebView.setVisibility(View.GONE);
                aiaLoader.setVisibility(View.GONE);
                return false;
            }
            else {
                noNetworkLayout.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.i(TAG, "Back btn was pressed.");
                if (aiaWebView != null && aiaWebView.canGoBack()) {
                    aiaWebView.goBack();
                }
                else {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            }
        });
    }

    public void refreshView() {
        if (checkNetworkAndStart()) {
            AIAWebLoader aiaWebLoader = new AIAWebLoader(aiaWebView, this.getContext(), aiaViewFragment);
            aiaWebLoader.loadAIA(mSiteId, mMobile, mSecret, mName, this);
            showUILoaders(true);
            /*NetworkDataHandler.getStoreBooksList(requireActivity(), "true", "null", "null",
                    "null", "null", "0", "null", new NetworkDataHandlerCallback() {
                @Override
                public void onSuccess(Integer responseCode, String responseStatus, String responseBody) {
                    Log.e("AIA Fragment", "shop books list: " + responseBody);
                }

                @Override
                public void onFailure(Integer responseCode, String message) {
                    Log.e("AIA Fragment", "shop books list failed " + message);
                }
            });*/
        }
    }

    /*public void onShopDataReceived() {
        AIAWebLoader aiaWebLoader = new AIAWebLoader(aiaWebView, this.getContext(), aiaViewFragment);
        aiaWebLoader.onShopDataReceived();
    }*/

    public void showUILoaders(boolean flag) {
        if (flag) {
            if (noNetworkLayout != null) {
                noNetworkLayout.setVisibility(View.GONE);
            }
            if (aiaWebView != null) {
                aiaWebView.setVisibility(View.GONE);
            }
            if (aiaLoader != null) {
                aiaLoader.setVisibility(View.VISIBLE);
            }
            if (aiaOverlay != null) {
                aiaOverlay.setVisibility(View.VISIBLE);
            }
            aiaSnackBar = Snackbar.make(rootView, "Loading eBooks. Please wait...", Snackbar.LENGTH_INDEFINITE);
            aiaSnackBar.show();
        }
        else {
            if (noNetworkLayout != null) {
                noNetworkLayout.setVisibility(View.GONE);
            }
            if (aiaWebView != null) {
                aiaWebView.setVisibility(View.VISIBLE);
            }
            if (aiaLoader != null) {
                aiaLoader.setVisibility(View.GONE);
            }
            if (aiaSnackBar != null) {
                aiaSnackBar.dismiss();
            }
            if (aiaOverlay != null) {
                aiaOverlay.setVisibility(View.GONE);
            }
        }
    }

    public static Context getAIAContext() {
        if (aiaViewFragment != null) {
            if (aiaViewFragment.getActivity() != null) {
                return aiaViewFragment.getActivity().getApplicationContext();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public void onLoadFinished() {
        showUILoaders(false);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reload_page_btn) {
            refreshView();
        }
    }
}