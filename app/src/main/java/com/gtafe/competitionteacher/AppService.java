package com.gtafe.competitionteacher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by ZhouJF on 2020/11/20.
 */
public class AppService extends Service {
    private static final String TAG = "UDBService";
    public IntentFilter mFilter;
    public NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
    public boolean mPixairConnect;

    public byte headForward = 0;
    List<DataChangeInterFace> mDataChangeInterFaces = new ArrayList<>();


    public void addDataChangeInterFace(DataChangeInterFace mDataChangeInterFace) {
        mDataChangeInterFaces.add(mDataChangeInterFace);

    }

    public void removeDataChangeInterFace(DataChangeInterFace mDataChangeInterFace) {
        if (mDataChangeInterFaces.contains(mDataChangeInterFace)) {
            mDataChangeInterFaces.remove(mDataChangeInterFace);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mDestroy) {
                return;
            }
            switch (msg.what) {
                case 1:
                    break;
                case 10:

                    break;
                case 11:

                    break;
                case 12:

                    break;
                case 13:

                    break;
                case 14:
                case 15:

                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        mFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mNetworkConnectChangedReceiver, mFilter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel("竞赛", "竞赛", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, "竞赛").setSmallIcon(R.drawable.ic_launcher_foreground).build();
            startForeground(1, notification);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkConnectChangedReceiver);
        mDestroy = true;
        Log.e(TAG, "DatagramSocket run onDestroy: ");
        stopForeground(0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    private LocalBinder mLocalBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        AppService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AppService.this;
        }
    }


    private boolean mDestroy = false;

    private class NetworkConnectChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
            // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {



                for (DataChangeInterFace dataChangeInterFace : mDataChangeInterFaces) {
                    if (dataChangeInterFace != null) {

                        dataChangeInterFace.onConnectStateChange(false);
                    }
                }
            }

        }
    }

}
