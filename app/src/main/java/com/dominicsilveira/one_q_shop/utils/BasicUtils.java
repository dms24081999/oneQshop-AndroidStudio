package com.dominicsilveira.one_q_shop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

public class BasicUtils {

    public static void setColorFilter(@NonNull Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
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
