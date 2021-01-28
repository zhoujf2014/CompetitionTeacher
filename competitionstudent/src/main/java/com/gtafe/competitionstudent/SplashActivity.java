package com.gtafe.competitionstudent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SplashActivity extends BaseActivity {


    public boolean mSetting;

    @Override
    protected void init() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                int i = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    if (i > 1) {

                        break;
                    }
                }

                if (!mSetting) {
                    startActivity(new Intent(mContext, MainActivity.class));
                }

            }
        }.start();
        checkPermission();
    }

    @Override
    protected int setView() {
        return R.layout.activity_splash;
    }

    public void splashSetting(View view) {
        mSetting = true;
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    private void checkPermission() {

        if (!hasPermissions(mContext, RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions,
                                           @androidx.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        /*
                         * If the user turned down the permission request in the past and chose the
                         * Don't ask again option in the permission request system dialog.
                         */
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[index])) {
                            Toast.makeText(mContext, "Required permission " + permissions[index]
                                            + " not granted. "
                                            + "Please go to settings and turn on for app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mContext, "Required permission " + permissions[index]
                                    + " not granted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,

    };
}