package com.open.appbase.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * Uri router.
 * safely router to jump between each components, you need to check intent validate when use hide intent
 * use it like BaseUIRouter.INSTANCE.openWithHideIntent
 */
public enum BaseUIRouter {
    /**
     * Instance ui router.
     */
    INSTANCE;

    /**
     * Open activity boolean.
     *
     * @param context the context
     * @param intent  the intent
     * @return the boolean
     */
    public boolean openWithHideIntent(@NonNull Context context, @NonNull Intent intent) {
        // check intent validate
        if (!resolveActivity(context, intent)) {
            return false;
        }
        // start activity
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean resolveActivity(@NonNull Context context, @NonNull Intent intent) {
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                != null) {
            return true;
        }

        return false;
    }

}
