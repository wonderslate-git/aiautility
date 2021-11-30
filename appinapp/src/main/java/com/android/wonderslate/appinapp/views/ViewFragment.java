package com.android.wonderslate.appinapp.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.wonderslate.appinapp.R;
import com.android.wonderslate.appinapp.util.AIAWebLoader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "siteId";
    private static final String ARG_PARAM2 = "secret";
    private static final String ARG_PARAM3 = "userName";
    private static final String ARG_PARAM4 = "userMobile";

    // TODO: Rename and change types of parameters
    private String mSiteId;
    private String mSecret;
    private String mName;
    private String mMobile;

    private View rootView;
    private WebView aiaWebView;
    private LinearLayout errorLayout;
    private TextView errorTxtView;

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
    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(String siteId, String secret, String name, String mobile) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, siteId);
        args.putString(ARG_PARAM2, secret);
        args.putString(ARG_PARAM3, name);
        args.putString(ARG_PARAM4, mobile);
        fragment.setArguments(args);
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
        aiaWebView = rootView.findViewById(R.id.aia_webview);

        AIAWebLoader aiaWebLoader = new AIAWebLoader(aiaWebView, getActivity());
        aiaWebLoader.loadAIA(mSiteId, mMobile, mSecret, mName);
    }

    public void refreshView() {
        AIAWebLoader aiaWebLoader = new AIAWebLoader(aiaWebView, getActivity());
        aiaWebLoader.loadAIA(mSiteId, mMobile, mSecret, mName);
    }
}