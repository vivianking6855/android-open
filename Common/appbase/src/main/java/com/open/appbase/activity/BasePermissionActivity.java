package com.open.appbase.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * The type Base permission activity.
 * BasePermissionActivity used for granted dangerous after M
 * include dangerous permission, Setting write permission and Overlay Permission
 * use it like:
 * protected String[] getPermissions() {
 * return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
 * }
 * you should call setPermissionAlterDialog to set AlertDialogue strings when user choose never show
 *
 * @author vivian
 */
public abstract class BasePermissionActivity extends BaseActivity {
    // permission request code----------------------------
    // normal dangerous permission request code
    private final static int PERMISSION_REQUEST_CODE_RECORD = 10000;
    // overlay permission for ALERT WINDOW
    private final static int OVERLAY_PERMISSION_REQ_CODE = 10001;
    // write settings
    private final static int REQUEST_CODE_ASK_WRITE_SETTINGS = 10002;

    // never show dlg------------------------------
    // if system request dialogue show
    private boolean mSystemPermissionShowing = false;
    // hint dialogue for never show
    private AlertDialog mNeverShowHintDlg;
    // never show hint strings
    private static String[] neverShowRes;
    private static final String TITLE = "permission title";
    private static final String MESSAGE = "permission message";
    private static final String POSITIVE = "positive";
    private static final String NEGATIVE = "negative";

    // sp file for permission to save some permission deny status
    private static String sPermissonFile = "permission";

    // if app first request to avoid endless pop up for onResume permission request
    protected static boolean isUserChooseDenyLastTime = false;

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
     * @param notGranted all not granted permissions
     */
    protected abstract void permissionDeny(String[] notGranted);

    /**
     * Sets share preference file name for permission to save some status.
     *
     * @param name the name, default sp file name is "permission"
     */
    protected void setSharePreferenceFileName(String name) {
        sPermissonFile = name;
    }

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

    /**
     * Sets permission alter dialog with string ids
     *
     * @param title    the title
     * @param message  the message
     * @param positive the positive
     * @param negative the negative
     */
    protected void setPermissionAlterDialog(@StringRes int title, @StringRes int message,
                                            @StringRes int positive, @StringRes int negative) {
        neverShowRes = new String[]{
                getString(title),
                getString(message),
                getString(positive),
                getString(negative)
        };
    }

    /**
     * Sets permission alter dialog with strings
     *
     * @param strs the strs
     */
    protected void setPermissionAlterDialog(String[] strs) {
        neverShowRes = strs;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isUserChooseDenyLastTime) {
            dealWithPermission();
        }
    }

    /**
     * deal with normal dangerous permission,such as Camera, Storage, etc
     */
    private void dealWithPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (userNotGranted()) {// permission not granted
            String[] notGranted = userChooseNeverShow();
            if (notGranted.length > 0 && notGranted[0] != null) {
                showNeverShowHintDialogue(notGranted);
            } else {
                if (!mSystemPermissionShowing) {
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
            String[] notGranted = userNotGrantedMore();
            if (notGranted.length > 0 && notGranted[0] != null) {
                // user choose deny,
                permissionDeny(notGranted);
                isUserChooseDenyLastTime = true;
            } else {
                permissionGranted();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean getPermissionFistDeny(String permission) {
        SharedPreferences sp = getSharedPreferences(sPermissonFile, MODE_PRIVATE);
        return sp.getBoolean(permission, true);
    }

    private void setPermissionDeny(String permission) {
        SharedPreferences sp = getSharedPreferences(sPermissonFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(permission, false);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    OverlayDeny();
                } else {
                    // Already hold the SYSTEM_ALERT_WINDOW permission, do add view or something.
                    OverlayGranted();
                }
                break;
            case REQUEST_CODE_ASK_WRITE_SETTINGS:
                if (!Settings.System.canWrite(this)) {
                    writeSettingsDeny();
                } else {
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
    private String[] userChooseNeverShow() {
        String[] permissions = getPermissions();
        String[] notGranted = new String[permissions.length];
        int pos = 0;
        for (int i = 0; i < permissions.length; i++) {
            // if user choose never show before, request system permission will not work
            boolean never_show = !ActivityCompat.shouldShowRequestPermissionRationale(BasePermissionActivity.this,
                    permissions[i]);
            boolean notGrant = ContextCompat.checkSelfPermission(BasePermissionActivity.this,
                    permissions[i]) != PackageManager.PERMISSION_GRANTED;
            boolean firstDeny = getPermissionFistDeny(permissions[i]);
            if (notGrant && never_show && !firstDeny) {
                notGranted[pos] = permissions[i];
                pos++;
            }
        }
        return notGranted;
    }

    /**
     * if user not granted any one of permissions
     */
    private boolean userNotGranted() {
        String[] permissions = getPermissions();
        for (int i = 0; i < permissions.length; i++) {
            int hasPermission = ContextCompat.checkSelfPermission(BasePermissionActivity.this,
                    permissions[i]);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    private String[] userNotGrantedMore() {
        String[] permissions = getPermissions();
        String[] notGranted = new String[permissions.length];
        int pos = 0;
        for (int i = 0; i < permissions.length; i++) {
            int hasWriteStoragePermission = ContextCompat.checkSelfPermission(BasePermissionActivity.this,
                    permissions[i]);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                notGranted[pos] = permissions[i];
                pos++;
                setPermissionDeny(permissions[i]);
            }
        }

        return notGranted;
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
    private void showNeverShowHintDialogue(String[] notGranted) {
        mNeverShowHintDlg = new AlertDialog.Builder(BasePermissionActivity.this)
                .setCancelable(false)
                .setTitle(neverShowRes == null ? TITLE : neverShowRes[0])
                .setMessage(neverShowRes == null ? MESSAGE : neverShowRes[1])
                .setPositiveButton(neverShowRes == null ? POSITIVE : neverShowRes[2], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user choose never show,you could change your resolution here for your project
                        gotoSettingsAppDetail();
                    }
                })
                .setNegativeButton(neverShowRes == null ? NEGATIVE : neverShowRes[3], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionDeny(notGranted);
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

    /**
     * Request draw over lay.
     */
    protected void requestDrawOverLay() {
        // no need to request permission before M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (!Settings.canDrawOverlays(BasePermissionActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + BasePermissionActivity.this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
            OverlayGranted();
        }
    }

    /**
     * Request write settings.
     */
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

