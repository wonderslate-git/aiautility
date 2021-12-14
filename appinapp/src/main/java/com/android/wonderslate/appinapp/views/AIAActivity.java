package com.android.wonderslate.appinapp.views;

import static com.android.wonderslate.appinapp.util.AppConstants.APP_IN_APP_LIBRARY_URL;
import static com.android.wonderslate.appinapp.util.AppConstants.APP_IN_APP_STORE_URL;
import static com.android.wonderslate.appinapp.util.AppConstants.HTTP_IMAGE_FILENAME;
import static com.android.wonderslate.appinapp.util.AppConstants.HTTP_OBJECT_ID;
import static com.android.wonderslate.appinapp.util.AppConstants.URL_SELECTED_BOOK_IMAGE_API;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wonderslate.appinapp.R;
import com.android.wonderslate.appinapp.Wonderslate;
import com.android.wonderslate.appinapp.data.local.WSSharedPrefs;
import com.android.wonderslate.appinapp.util.ImageLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class AIAActivity extends AppCompatActivity implements PaymentResultListener {
    WSSharedPrefs wsSharedPrefs;
    WebView webView;
    String bookId, bookTitle, price, clientName, mPaymentId, mCoverImage, mAuthorName;
    private int imageHeight, imageWidth, cornerRadius;

    private static Activity aiaActivityInstance;

    public AIAActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wsSharedPrefs = WSSharedPrefs.getInstance(this);
        bookId = getIntent().getStringExtra("id");
        bookTitle = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        clientName = getIntent().getStringExtra("client");
        mAuthorName = getIntent().getStringExtra("author");
        mCoverImage = getIntent().getStringExtra("coverImage");
        wsSharedPrefs.setClientName(clientName);
        aiaActivityInstance = AIAActivity.this;
        startPayment(bookId, bookTitle, price, ViewFragment.aiaWebView);
    }

    public void startPayment(String bookId, String bookTitle, String price, WebView webView) {
        this.webView = webView;
        this.bookId = bookId;

        final Activity activity = this;
        final Checkout co = new Checkout();
        //co.setImage(R.mipmap.ic_launcher); //Company logo
        try {
            JSONObject options = new JSONObject();
            options.put("name", wsSharedPrefs.getClientName()); //Company Name
            options.put("description", bookTitle + " (Course " + bookId + ")");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", (Double.parseDouble(price) * 100) + "");
            JSONObject preFill = new JSONObject();
            preFill.put("name", wsSharedPrefs.getUserName());
            preFill.put("email", wsSharedPrefs.getUseremail()); //hardcoded email Todo: get the user email from the client app
            preFill.put("contact", wsSharedPrefs.getUsermobile());
            JSONObject ReadOnly = new JSONObject();
            ReadOnly.put("email", "true");
            ReadOnly.put("contact", "true");
            JSONObject theme = new JSONObject();
            theme.put("color", "#F05A2A");
            options.put("prefill", preFill);
            options.put("theme", theme);
            options.put("readonly", ReadOnly);
            JSONObject notes = new JSONObject();
            notes.put("bookId", bookId);
            notes.put("username", wsSharedPrefs.getUsermobile());
            options.put("notes", notes);

            co.open(activity, options);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AIAActivity.this);
            builder.setTitle("Payment Failure");
            builder.setMessage("The payment for the book could not be instantiated. We regret the inconvenience. Please try again.")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    }).show();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        //Start the loader here
        //Save payment related things in shared prefs -- book id, title, price, author, coverImage
        String jsEvaluation = String.format("buyBookAppinApp('%s','%s','%s');", s, bookId, "mobile");

        mPaymentId = s;

        webView.evaluateJavascript(jsEvaluation, null);
        new PaymentSuccessDialog().showDialog(AIAActivity.this);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AIAActivity.this);
        builder.setTitle("Payment Failure");
        builder.setMessage("The payment for the book did not happen. We regret the inconvenience. " +
                "Any money that was deducted from your account, will be automatically refunded within 5 to 7 business days.")
                .setPositiveButton("Okay", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
    }

    private int calculateDPI(int calculateSizeInt) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        if (metrics.densityDpi >= 120 && metrics.densityDpi <= 200) {
            return calculateSizeInt;
        } else if (metrics.densityDpi >= 201 && metrics.densityDpi <= 280) {
            double size = calculateSizeInt * 1.5;
            return (int) size;
        } else if (metrics.densityDpi >= 281 && metrics.densityDpi <= 400) {
            return calculateSizeInt * 2;
        } else if (metrics.densityDpi >= 401 && metrics.densityDpi <= 540) {
            return calculateSizeInt * 3;
        } else if (metrics.densityDpi >= 541) {
            return calculateSizeInt * 4;
        }
        return calculateSizeInt;
    }

    public class PaymentSuccessDialog {

        TextView orderId, orderName, orderExtraText, orderAmt, paySuccessBrowseBooks, purchaseSuccessImg, paymentsuccessdesc;
        ImageView orderImg;
        Button startReadingBtn;

        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.purchase_success_dialog);

            //Initialize views
            orderId = dialog.findViewById(R.id.paymentorderid);
            orderName = dialog.findViewById(R.id.ordername);
            orderExtraText = dialog.findViewById(R.id.ordername2);
            orderAmt = dialog.findViewById(R.id.orderamt);
            paySuccessBrowseBooks = dialog.findViewById(R.id.paymentsuccessshoplink);
            paymentsuccessdesc = dialog.findViewById(R.id.paymentsuccessdesc);
            orderImg = dialog.findViewById(R.id.orderimg);
            startReadingBtn = dialog.findViewById(R.id.paysuccesslibbtn);
            purchaseSuccessImg = dialog.findViewById(R.id.purchasesuccessimg);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                purchaseSuccessImg.setVisibility(View.GONE);
            } else {
                purchaseSuccessImg.setText(getResources().getString(R.string.paymentsuccessemoticon));
            }

            String coverImgLoc = URL_SELECTED_BOOK_IMAGE_API + "&" + HTTP_IMAGE_FILENAME + "="
                    + mCoverImage + "&" + HTTP_OBJECT_ID + "=" + bookId;

            imageWidth = calculateDPI(64);
            imageHeight = calculateDPI(80);
            cornerRadius = calculateDPI(6);

            ImageLoader.with(activity)
                    .loadAsBitmap(coverImgLoc)
                    .placeholder(R.drawable.book_cover_placeholder)
                    .error(R.drawable.book_cover_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(new CustomTarget<Bitmap>(imageWidth, imageHeight) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            Bitmap imageRounded = Bitmap.createBitmap(resource.getWidth(), resource.getHeight(), resource.getConfig());
                            Canvas canvas = new Canvas(imageRounded);
                            Paint mpaint = new Paint();
                            mpaint.setAntiAlias(true);
                            mpaint.setShader(new BitmapShader(resource, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                            canvas.drawRoundRect((new RectF(0, 0, resource.getWidth(), resource.getHeight())), cornerRadius, cornerRadius, mpaint);
                            orderImg.setImageBitmap(imageRounded);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            if (mPaymentId != null && !mPaymentId.equalsIgnoreCase("")) {
                orderId.setText(String.format("Payment Id: #%s", mPaymentId));
            } else {
                orderId.setVisibility(View.GONE);
            }

            orderName.setText(bookTitle);

            if (mAuthorName != null && !mAuthorName.equalsIgnoreCase("")) {
                orderExtraText.setText(mAuthorName);
            } else {
                orderExtraText.setVisibility(View.GONE);
            }

            orderAmt.setText(String.format("₹ %s", price));

            startReadingBtn.setOnClickListener(view -> {
                Toast.makeText(AIAActivity.this, "Redirecting to Ebooks Library...", Toast.LENGTH_SHORT).show();
                webView.loadUrl(Wonderslate.SERVICE + APP_IN_APP_LIBRARY_URL);
                dialog.dismiss();
                finish();
            });

            paySuccessBrowseBooks.setOnClickListener(view -> {
                Toast.makeText(AIAActivity.this, "Redirecting to Ebooks Store...", Toast.LENGTH_SHORT).show();
                webView.loadUrl(Wonderslate.SERVICE + APP_IN_APP_STORE_URL);
                dialog.dismiss();
                finish();
            });

            dialog.show();

        }
    }
}