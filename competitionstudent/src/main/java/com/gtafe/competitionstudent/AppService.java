package com.gtafe.competitionstudent;

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
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.gtafe.competitionlib.ManageDataBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Gson gson = new Gson();

    public byte headForward = 0;
    List<DataChangeInterFace> mDataChangeInterFaces = new ArrayList<>();

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
    private boolean serving = true;
    private Thread SocketServerThread = new Thread(new Runnable() {

        private boolean connectting = true;
        private ServerSocket server;

        @Override
        public void run() {
            while (mDestroy) {
                connectting = true;
                while (connectting) {
                    try {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket");
                        server = new ServerSocket(8888);
                        server.setSoTimeout(1000);
                        connectting = false;
                    } catch (IOException e) {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket失败 2秒后重新连接");
                        SystemClock.sleep(1000);
                        e.printStackTrace();
                    }
                }
                while (!server.isClosed()) {
                    try {
                        Log.e(TAG, "start: 等待客户端连接……");
                        Socket socket = server.accept();
                        Log.e(TAG, "start: 一个客户端连接了！");

                        /**
                         * 当一个客户端连接后，启动一个线程，来负责与该客户端交互
                         */

                        ClientHandler handler = new ClientHandler(socket);
                        new Thread(handler).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });
    private class ClientHandler implements Runnable {
        private Socket socket;
        //客户端地址信息
        private String host;
        private OutputStream mOut;
        private InputStream mIn;
        private String equipmentNumber;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            //通过socket可以得知远端计算机信息
            InetAddress address = socket.getInetAddress();
            host = address.getHostAddress();
            Log.e(TAG, "ClientHandler: " + address);
        }

        public void run() {
            try {
                mOut = socket.getOutputStream();
                mIn = socket.getInputStream();
            } catch (Exception e) {
                Log.e(TAG, "获取输入流失败");
                e.printStackTrace();
                return;
            }

            try {
                while (mIn != null) {
                    if (mIn.available() > 0) {
                        byte[] bytes = new byte[1024];
                        int read = mIn.read(bytes);
                        Log.e(TAG, "run: " + read);
                        if (read > 10) {
                            String jsonString = new String(bytes, 0, read);
                            if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
                                ManageDataBean manageDataBean = gson.fromJson(jsonString, ManageDataBean.class);
                                if (manageDataBean != null) {
                                    //interpretWifiData(manageDataBean);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "接收wifi设备数据失败");
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        if (mOut != null) {
                            removeOut(mOut);
                            mOut.close();
                        }
                        if (mIn != null) {
                            mIn.close();
                            mIn = null;
                        }
                        if (socket.isInputShutdown()) {
                            socket.shutdownInput();
                        }
                        if (socket.isOutputShutdown()) {
                            socket.shutdownOutput();
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    private void removeOut(OutputStream out) {

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
