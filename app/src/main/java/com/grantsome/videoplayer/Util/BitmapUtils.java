package com.grantsome.videoplayer.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by tom on 2017/4/28.
 */

public class BitmapUtils {

    public static Bitmap toBitmap(byte[] src){
        return BitmapFactory.decodeByteArray(src,0,src.length);
    }

    @Nullable
    public static Bitmap toBitmap(Uri uri){
        try {
            InputStream inputStream = ApplicationContext.getContext().getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] toBytes(Bitmap bitmap){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            return outputStream.toByteArray();
    }
}