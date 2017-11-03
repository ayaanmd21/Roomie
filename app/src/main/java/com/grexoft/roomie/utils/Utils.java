package com.grexoft.roomie.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.grexoft.roomie.R;
import com.grexoft.roomie.helper.VolleyHelper;

import de.hdodenhof.circleimageview.CircleImageView;


public class Utils {

    private static boolean isDialogShown;

    public static void hideSoftKeyboard(Context mContext) {
        View view = ((Activity) mContext).getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static void showSnackBar(View view, String errorMessage, int time, String action, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar
                .make(view, errorMessage, time);
        if (!TextUtils.isEmpty(action) && listener != null) {

            snackbar.setAction(action, listener);
        }
        snackbar.show();
    }

    public static void showAlertDialog(Context context, String title, String message, boolean cancelable, final DialogInterface.OnClickListener listener) {
        if (isDialogShown) return;
        isDialogShown = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDialogShown = false;
                        if (listener != null) {
                            listener.onClick(dialog, which);
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isDialogShown = false;
                    }
                })
                .show();
    }

    public static void showSnackBar(View view, String errorMsg) {
        Snackbar snackbar = Snackbar
                .make(view, errorMsg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    public static void setCompoundDrawable(EditText editText, Context context) {

        for (Drawable drawable : editText.getCompoundDrawables()) {
            if (drawable != null) {
                if (editText.hasFocus())
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN));
                else
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.text_color_grey), PorterDuff.Mode.SRC_IN));
            }
        }

    }

    private static final float BITMAP_SCALE = 0.9f;
    private static final float BLUR_RADIUS = 20f;

    @SuppressLint("NewApi")
    public static Bitmap scaleAndBlur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(20f);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    @SuppressLint("NewApi")
    public static Bitmap blur(Bitmap image, Context context) {
        if (null == image) return null;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public enum CLICK_TYPE {
        DETAIL(0), NEW(1), EDIT(2), SINGLE_SELECT(3), MULTI_SELECT(4);

        private final int id;

        CLICK_TYPE(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }

        public int getValue() {
            return id;
        }
    }

    public static void setUserProfilePic(String Url, final CircleImageView circleImageView, final Context context, VolleyHelper volleyHelper) {


        volleyHelper.callApiGetImage(Url, new VolleyHelper.VolleyBitmapCallback() {
            @Override
            public void onSuccess(Bitmap result) {
                circleImageView.setImageBitmap(result);

            }

            @Override
            public void onError(String error) {
                circleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_image_view));

            }
        });
    }

    public static void setUserBackgroundPic(String Url, final ImageView imageView, final Context context, VolleyHelper volleyHelper) {


        volleyHelper.callApiGetImage(Url, new VolleyHelper.VolleyBitmapCallback() {
            @Override
            public void onSuccess(Bitmap result) {

                imageView.setImageBitmap(Utils.blur(result, context));

            }

            @Override
            public void onError(String error) {

                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.material));
            }
        });
    }


//    private boolean checkWriteExternalPermission(Context context) {
//        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED);
//    }


    public static boolean checkPermission(Context mContext, String permission) {
        return (ContextCompat.checkSelfPermission(mContext, permission)
                == PackageManager.PERMISSION_GRANTED);
    }

    public static void askPermission(String permission, int requestCode, Context mContext) {
        ActivityCompat.requestPermissions(
                (Activity) mContext,
                new String[]{permission},
                requestCode
        );
    }


    public static void askPermissions(String[] permissions, int requestCode, Context mContext) {
        ActivityCompat.requestPermissions(
                (Activity) mContext,
                permissions,
                requestCode);
    }

}
