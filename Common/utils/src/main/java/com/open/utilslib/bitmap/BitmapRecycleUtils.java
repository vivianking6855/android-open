package com.open.utilslib.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.open.utilslib.BuildConfig;

/**
 * The type Bitmap utils.
 */
public class BitmapRecycleUtils {
    private static final String TAG = "BitmapUtils";

    /**
     * Safe recycle and gc.
     *
     * @param bitmap the bitmap
     */
    public static void safeRecycleAndGC(Bitmap bitmap) {
        if (bitmap == null || !bitmap.isRecycled()) {
            return;
        }
        safeRecycle(bitmap);
        System.gc();
    }

    /**
     * Safe recycle.
     *
     * @param bitmap the bitmap
     */
    public static void safeRecycle(Bitmap bitmap) {
        if (bitmap == null || !bitmap.isRecycled()) {
            return;
        }

        bitmap.recycle();
        bitmap = null;
    }

    /**
     * Recycle image view bitmap.
     *
     * @param view the view
     */
    public static void recycleImageViewBitmap(ImageView view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            if (BuildConfig.DEBUG) Log.d(TAG, ">>> recycleImageViewBitmap: recycle BitmapDrawable");
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            safeRecycle(bitmap);
        } else {
            Log.d(TAG, ">>> recycleImageViewBitmap not BitmapDrawable, no recycle");
        }
    }

    /**
     * Recycle view background.
     *
     * @param view the view
     */
    public static void recycleViewBackground(View view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        if (drawable instanceof BitmapDrawable) {
            Log.d(TAG, ">>> recycleViewBackground: recycle BitmapDrawable");
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            view.setBackground(null);
            bitmapDrawable.setCallback(null);
            safeRecycle(bitmap);
        } else {
            Log.d(TAG, ">>> recycleViewBackground not BitmapDrawable, no recycle");
        }
    }
}
