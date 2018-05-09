package com.open.utilslib.window;

import android.content.Context;
import android.widget.Toast;

/**
 * The enum Toast utils.
 * to avoid duplicated toast
 */
public enum ToastUtils {
    /**
     * Instance toast utils.
     */
    INSTANCE;

    private Toast sToast;

    /**
     * Show toast.
     *
     * @param context  the context
     * @param text     the text
     * @param duration the duration
     */
    public void showToast(Context context, CharSequence text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, duration);
        }else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    /**
     * cancel toast
     */
    public void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

}