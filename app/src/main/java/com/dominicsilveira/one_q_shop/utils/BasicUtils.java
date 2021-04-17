package com.dominicsilveira.one_q_shop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dominicsilveira.one_q_shop.ui.cart.CartActivity;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class BasicUtils {

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }

    public static void setColorFilter(@NonNull Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static String getToken(Context context){
        SharedPreferences sh = context.getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        return sh.getString("token", "0");
    }

    public static void setActionBar(AppCompatActivity appCompatActivity, String title) {
        appCompatActivity.getSupportActionBar().setTitle(title);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public Bitmap uriToBitmap(Context context,Uri newImg) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT >= 29) {
            ImageDecoder.Source source = ImageDecoder.createSource(context.getApplicationContext().getContentResolver(), newImg);
            try {
                return ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                return MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), newImg);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
