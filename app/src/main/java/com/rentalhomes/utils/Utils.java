package com.rentalhomes.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.rentalhomes.BuildConfig;
import com.rentalhomes.R;

public class Utils {

    private static final String TAG = "Utils";

    public static Point getDisplaySize(WindowManager windowManager) {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public static int dptoPx(int i) {

        return (int) (i * Resources.getSystem().getDisplayMetrics().density);

    }

    public static void shareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rental Homes App");
            String shareMessage = context.getResources().getString(R.string.share_message)+"\nDownload Application from\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "Share with"));
        } catch (Exception e) {
            //e.toString();
        }
    }


    public static void setY(View v, int y) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(params.leftMargin, y, params.rightMargin, params.bottomMargin);
        v.setLayoutParams(params);
    }

    public static void setX(View v, int x) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(x, params.topMargin, params.rightMargin, params.bottomMargin);
        v.setLayoutParams(params);
    }

    public static int getX(View v) {
        Rect myViewRect = new Rect();
        v.getGlobalVisibleRect(myViewRect);
        return myViewRect.left;
    }

    public static int getY(View v) {
        Rect myViewRect = new Rect();
        v.getGlobalVisibleRect(myViewRect);
        return v.getTop();
    }

    public static void setAlpha(View v, float alpha) {
        if (Build.VERSION.SDK_INT < 11) {
            final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            v.startAnimation(animation);
        } else
            v.setAlpha(alpha);
    }

    public static Animation alphaAnim(int duration, float from, float to) {
        final AlphaAnimation animation = new AlphaAnimation(from, to);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        return animation;
    }

}
