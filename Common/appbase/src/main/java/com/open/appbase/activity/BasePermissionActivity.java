package com.open.appbase.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * The type Base permission activity.
 * BasePermissionActivity used for granted dangerous after M
 * include dangerous permission, Setting write permission and Overlay Permission
 * use it like:
 * public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
 * protected String[] getPermissions() {
 * return new String[]{READ_STORAGE};
 * }
 * you should call setAlterDialogStrings to set AlertDialogue strings when user choose never show
 *
 * @author vivian
 */
public abstract class BasePermissionActivity extends BaseActivity {
    protected final static String TAG = "Permission";

    // normal dangerous permission request code
    private final static int PERMISSION_REQUEST_CODE_RECORD = 10000;
    // overlay permission for ALERT WINDOW
    private final static int OVERLAY_PERMISSION_REQ_CODE = 10001;
    // write settings
    private final static int REQUEST_CODE_ASK_WRITE_SETTINGS = 10002;
    // if system request dialogue show
    private boolean mSystemPermissionShowing = false;
    // hint dialogue for never show
    private AlertDialog mNeverShowHintDlg;
    // never show hint strings
    private static int[] neverShowRes;

    /**
     * Get permissions string [ ].
     * get all necessary dangerous permissions
     *
     * @return the string [ ]
     */
    protected abstract String[] getPermissions();

    /**
     * Permission granted.
     */
    protected abstract void permissionGranted();

    /**
     * Permission deny.
     */
    protected abstract void permissionDeny();

    /**
     * Overlay permission deny.
     */
    protected void OverlayDeny() {
    }

    /**
     * Overlay permission granted.
     */
    protected void OverlayGranted() {
    }

    /**
     * Write settings permission deny.
     */
    protected void writeSettingsDeny() {
    }

    /**
     * Write settings permission granted.
     */
    protected void writeSettingsGranted() {
    }

    protected void setAlterDialogStrings(@StringRes int title, @StringRes int message,
                                         @StringRes int positive, @StringRes int negative) {
        neverShowRes = new int[]{title, message, positive, negative};
    }

    @Override
    protected void onResume() {
        super.onResume();
        dealWithPermission();
    }

    /**
     * deal with normal dangerous permission,such as Camera, Storage, etc
     */
    private void dealWithPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (userNotGranted()) {// permission not granted
            Log.d(TAG, "permission not granted");
            if (userChooseNeverShow()) {
                Log.d(TAG, "user choose never show");
                showNeverShowHintDialogue();
            } else {
                if (!mSystemPermissionShowing) {
                    Log.d(TAG, "show system permission dialogue");
                    // show system permission dialogue
                    mSystemPermissionShowing = true;
                    showSystemRequestDialog();
                }
            }
        } else {// permission granted
            permissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_RECORD) {
            mSystemPermissionShowing = false;
            if (userNotGranted()) {
                // user choose deny,
                permissionDeny();
            } else {
                permissionGranted();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    //Log.d(TAG, "DrawOverlays Permission was denied");
                    OverlayDeny();
                } else {
                    // Already hold the SYSTEM_ALERT_WINDOW permission, do add view or something.
                    //Log.d(TAG, "DrawOverlays Permission granted");
                    OverlayGranted();
                }
                break;
            case REQUEST_CODE_ASK_WRITE_SETTINGS:
                if (!Settings.System.canWrite(this)) {
                    //Log.d(TAG, "write settings Permission was denied");
                    writeSettingsDeny();
                } else {
                    //Log.d(TAG, "write settings Permission granted");
                    writeSettingsGranted();
                }
                break;
            default:
                break;
        }

    }

    /**
     * if user choose never show for any one of permissions
     */
    private boolean userChooseNeverShow() {
        String[] permissions = getPermissions();
        for (int i = 0; i < permissions.length; i++) {
            // if user choose never show before, request system permission will not work
            boolean never_show = !ActivityCompat.shouldShowRequestPermissionRationale(BasePermissionActivity.this,
                    permissions[i]);
            if (never_show) {
                return true;
            }
        }
        return false;
    }

    /**
     * if user not granted any one of permissions
     */
    private boolean userNotGranted() {
        String[] permissions = getPermissions();
        for (int i = 0; i < permissions.length; i++) {
            int hasWriteStoragePermission = ContextCompat.checkSelfPermission(BasePermissionActivity.this,
                    permissions[i]);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    /**
     * show system permission request dlg
     */
    private void showSystemRequestDialog() {
        // show system permission dialogue
        ActivityCompat.requestPermissions(BasePermissionActivity.this, getPermissions(),
                PERMISSION_REQUEST_CODE_RECORD);
    }

    /**
     * show never show hint dlg
     */
    private void showNeverShowHintDialogue() {
        if (neverShowRes == null) {
            return;
        }

        mNeverShowHintDlg = new AlertDialog.Builder(BasePermissionActivity.this)
                .setCancelable(false)
                .setTitle(getString(neverShowRes[0]))
                .setMessage(getString(neverShowRes[1]))
                .setPositiveButton(getString(neverShowRes[2]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user choose never show,you could change your resolution here for your project
                        gotoSettingsAppDetail();
                    }
                })
                .setNegativeButton(getString(neverShowRes[3]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionDeny();
                    }
                })
                .show();
    }

    private void gotoSettingsAppDetail() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    protected void requestDrawOverLay() {
        // no need to request permission before M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (!Settings.canDrawOverlays(BasePermissionActivity.this)) {
            Log.d(TAG, "can not DrawOverlays need requset permission");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + BasePermissionActivity.this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
            OverlayGranted();
        }
    }

    protected void requestWriteSettings() {
        // no need to request permission before M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, REQUEST_CODE_ASK_WRITE_SETTINGS);
        } else {
            // Already hode ACTION_MANAGE_WRITE_SETTINGS permission
            writeSettingsGranted();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // dismiss never show dlg hint
        if (mNeverShowHintDlg != null && mNeverShowHintDlg.isShowing()) {
            mNeverShowHintDlg.dismiss();
        }
    }
}

