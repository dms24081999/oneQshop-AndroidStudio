package com.dominicsilveira.one_q_shop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

public class BasicUtils {

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
