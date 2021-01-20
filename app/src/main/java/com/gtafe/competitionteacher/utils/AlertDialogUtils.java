package com.gtafe.competitionteacher.utils;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by ZhouJF on 2019/3/26.
 */

public class AlertDialogUtils {

    public static ProgressDialog sProgressDialog;


    public static ProgressDialog showDialog(final Activity activity, String s) {
        cancelDialog();
        if (activity.isFinishing()) {
            return null;
        }
        sProgressDialog = new ProgressDialog(activity);

        sProgressDialog.setMessage(s);
        sProgressDialog.setCancelable(true);
        sProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (sProgressDialog != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (sProgressDialog != null) {
                                sProgressDialog.cancel();
                            }
                        }
                    });
                }
            }
        }).start();
        return sProgressDialog;
    }

    public static void cancelDialog() {
        if (sProgressDialog != null) {
            sProgressDialog.cancel();
        }
    }

}
