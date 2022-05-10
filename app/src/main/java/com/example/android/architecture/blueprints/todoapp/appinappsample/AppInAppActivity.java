package com.example.android.architecture.blueprints.todoapp.appinappsample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.interfaces.UserPurchaseHistoryCallback;
import com.example.android.architecture.blueprints.todoapp.R;
import com.google.android.material.textfield.TextInputEditText;

import com.android.wonderslate.appinapp.AppInAppUtility;

public class AppInAppActivity extends AppCompatActivity {
    public WebView appInAppView;
    public String userName, userMobile, userEmail;
    public boolean isUserCredAvailable;
    public FrameLayout appInAppFrame;
    public Toolbar toolbar;
    ToDoSharedPrefs toDoSharedPrefs;
    AppInAppUtility appInAppUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_in_app);

        init();
    }

    protected void init() {
        appInAppView = findViewById(R.id.appinappwebview);
        appInAppFrame = findViewById(R.id.appinappframe);
        toolbar = findViewById(R.id.aia_toolbar);

        toDoSharedPrefs = ToDoSharedPrefs.getInstance(AppInAppActivity.this);

        String name, email, mobile, secret, siteId;

        name = toDoSharedPrefs.getUserName();
        email = toDoSharedPrefs.getUseremail();
        mobile = toDoSharedPrefs.getUsermobile();
        secret = toDoSharedPrefs.getAccessToken();
        siteId = toDoSharedPrefs.getSiteId();

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("EBooks Library");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if ((name == null || email == null || mobile == null || secret == null || siteId == null) ||
                (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || secret.isEmpty() || siteId.isEmpty())) {
            toDoSharedPrefs.clearSharedPrefs();
            new CredentialDialog().showDialog(this);
        }
        else {
            startAppInApp(name, email, mobile, null);
        }
    }

    public void startAppInApp(String name, String email, String mobile, @Nullable Dialog dialog) {
        appInAppUtility = AppInAppUtility.getInstance( "b534cZ9845", name, mobile, email, "28");

        getSupportFragmentManager().beginTransaction().add(R.id.appinappframe, appInAppUtility
                .getAIAFragment()).commit();

        if (dialog != null) {
            dialog.dismiss();
        }

        toDoSharedPrefs.setSiteId("28");
        toDoSharedPrefs.setAccessToken("b534cZ9845");
        toDoSharedPrefs.setUsername(name);
        toDoSharedPrefs.setUsermobile(mobile);
        toDoSharedPrefs.setUserEmail(email);

        /*appInAppUtility.getAIAPurchaseOrder(new UserPurchaseHistoryCallback() {
            @Override
            public void onSuccess(String responseCode, String responseStatus, String responseBody) {
                Toast.makeText(AppInAppActivity.this, "Purchase Response: " + responseStatus, Toast.LENGTH_SHORT).show();
                Log.e("AIAActivity", "Purchase History: " + responseBody);
            }

            @Override
            public void onFailure(String responseCode, String responseStatus, String message) {
                Toast.makeText(AppInAppActivity.this, "Error Response: " + message, Toast.LENGTH_SHORT).show();
                Log.e("AIAActivity", "Purchase History Error: " + message);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aia_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        else if (item.getItemId() == R.id.menu_reset) {
            Toast.makeText(this, "Clearing your data...", Toast.LENGTH_SHORT).show();
            ToDoSharedPrefs.getInstance(this).clearSharedPrefs();
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.menu_purchase_order) {
                appInAppUtility.getAIAPurchaseOrder(new UserPurchaseHistoryCallback() {
                @Override
                public void onSuccess(String responseCode, String responseStatus, String responseBody) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AppInAppActivity.this, "Purchase Response: " + responseStatus, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("AIAActivity", "Purchase History: " + responseBody);
                }

                @Override
                public void onFailure(String responseCode, String responseStatus, String message) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AppInAppActivity.this, "Error Response: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("AIAActivity", "Purchase History Error: " + message);
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    public class CredentialDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_user_credentials);

            TextInputEditText userNameText, userMobileText, userEmailText;
            Button okBtn, cancelBtn;

            userNameText = dialog.findViewById(R.id.username_edit_text);
            userMobileText = dialog.findViewById(R.id.phone_edit_text);
            userEmailText = dialog.findViewById(R.id.useremail_edit_text);
            okBtn = dialog.findViewById(R.id.btn_dialog_ok);
            cancelBtn = dialog.findViewById(R.id.btn_dialog_cancel);


            okBtn.setOnClickListener(v -> {
                if (userMobileText.getText() != null && userMobileText.getText().toString().isEmpty()) {
                    userMobileText.setError("Mandatory Field");
                }
                else if (userNameText.getText() != null && userNameText.getText().toString().isEmpty()) {
                    userNameText.setError("Mandatory Field");
                }
                else if (userEmailText.getText() != null && userEmailText.getText().toString().isEmpty()) {
                    userEmailText.setError("Mandatory Field");
                }
                else {
                    userMobile = userMobileText.getText().toString().trim();
                    userName = userNameText.getText().toString().trim();
                    userEmail = userEmailText.getText().toString().trim();

                    startAppInApp(userName, userEmail, userMobile, dialog);
                }
            });

            cancelBtn.setOnClickListener(v -> {
                Toast.makeText(activity, "Please login to access App-In-App feature", Toast.LENGTH_LONG).show();
                activity.finish();
                dialog.dismiss();
            });

            dialog.show();

        }
    }
}