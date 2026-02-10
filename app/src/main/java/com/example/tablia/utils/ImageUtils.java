package com.example.tablia.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageUtils {
    public static String uriToBase64(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap base64ToBitmap(String b64) {
        try {
            byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}