package com.open.utilslib.file;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public final class CloseUtils {

    private final static String TAG = CloseUtils.class.getSimpleName();

    /**
     * close IO
     *
     * @param closeables closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Log.w(TAG, "closeIO ex:", e);
                }
            }
        }
    }

}
