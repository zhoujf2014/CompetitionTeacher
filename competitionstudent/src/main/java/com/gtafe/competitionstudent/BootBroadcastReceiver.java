package com.gtafe.competitionstudent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ZhouJF on 2018/5/21.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { //开机启动完成后，要做的事情
            Log.e(TAG, "onReceive: android.intent.action.BOOT_COMPLETED" );
            Intent intent1 = new Intent(context, SplashActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  context.startActivity(intent1);
        }

    }
}
