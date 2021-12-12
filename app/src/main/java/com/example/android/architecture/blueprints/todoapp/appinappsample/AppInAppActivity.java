package com.example.android.architecture.blueprints.todoapp.appinappsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.architecture.blueprints.todoapp.R;
import com.google.android.material.textfield.TextInputEditText;

import com.android.wonderslate.appinapp.AppInAppUtility;

public class AppInAppActivity extends AppCompatActivity {
    public WebView appInAppView;
    public String userName, userMobile, userEmail;
    public boolean isUserCredAvailable;
    public FrameLayout appInAppFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_in_app);

        init();
    }

    protected void init() {
        appInAppView = findViewById(R.id.appinappwebview);
        appInAppFrame = findViewById(R.id.appinappframe);
        appInAppFrame.setVisibility(View.VISIBLE);

        //new AppInAppUtil(appInAppView, AppInAppActivity.this).startAppInApp();

        new CredentialDialog().showDialog(this);
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

                    getSupportFragmentManager().beginTransaction().add(R.id.appinappframe, AppInAppUtility.getInstance( "b534cZ9845",
                            userName, userMobile, userEmail, "28")
                            .getAIAFragment()).commit();
                    dialog.dismiss();
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