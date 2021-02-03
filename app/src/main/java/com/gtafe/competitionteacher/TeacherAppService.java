package com.gtafe.competitionteacher;

import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.TESTDATA;

/**
 * Created by ZhouJF on 2020/11/20.
 */
public class TeacherAppService extends Service {
    private static final String TAG = "UDBService";
    public IntentFilter mFilter;
    public NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
    public boolean mPixairConnect;
    private Gson gson = new Gson();

    public byte headForward = 0;
    private List<SocketClient> socketClients = new ArrayList<>();
    List<DataChangeInterFace> mDataChangeInterFaces = new ArrayList<>();
    public SendThread mSendThread;

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
            Notification notification = new Notification.Builder(this, "竞赛").setSmallIcon(R.drawable.ic_launcher).build();
            startForeground(1, notification);
        }
        SocketServerThread.start();
        mSendThread = new SendThread();
        mSendThread.start();
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
                    for (DataChangeInterFace dataChangeInterFace : mDataChangeInterFaces) {
                        dataChangeInterFace.notifyData();
                    }
                    break;
                case 2:

                    break;
                case 3:
                    interpretWifiData((ManageDataBean) msg.obj);
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

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
            while (!mDestroy) {
                connectting = true;
                while (connectting) {
                    try {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket");
                        server = new ServerSocket(8088);
                        server.setSoTimeout(10000);
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

                        SocketClient socketClient = new SocketClient(socket);
                        socketClient.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    private void interpretWifiData(ManageDataBean manageDataBean) {
        switch (manageDataBean.CMD) {
            case HERAT:
                mSendThread.send(manageDataBean.SN, gson.toJson(manageDataBean));
                break;

            case BIANHAO:
                for (DataChangeInterFace dataChangeInterFace : mDataChangeInterFaces) {
                    dataChangeInterFace.onAddnewBianhao(manageDataBean);
                }
                break;
            default:
                for (DataChangeInterFace dataChangeInterFace : mDataChangeInterFaces) {
                    dataChangeInterFace.onReceivDataFromServer(manageDataBean);
                }

                break;

        }
    }


    public void sendDataToAllClient(ManageDataBean manageDataBean) {
        mSendThread.sendToAllClient(gson.toJson(manageDataBean));
    }

    public void sendDataToClient(String sn, ManageDataBean manageDataBean) {
        mSendThread.send(sn, gson.toJson(manageDataBean));
    }

    private class SendThread extends Thread {
        private Handler mSendHandler;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            mSendHandler = new Handler() {
                @SuppressLint("HandlerLeak")
                @Override
                public void handleMessage(Message msg) {
                    if (mDestroy) {
                        return;
                    }
                    switch (msg.what) {

                        case 0:
                            break;
                        case 1:

                            for (int i = 0; i < socketClients.size(); i++) {
                                socketClients.get(i).sendData((String) msg.obj);
                                try {
                                    Thread.sleep(30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 2:
                            Bundle bundle = msg.getData();
                            String sn = bundle.getString("sn");
                            String data = bundle.getString("data");

                            for (int i = 0; i < socketClients.size(); i++) {
                                SocketClient socketClient = socketClients.get(i);
                                if (socketClient != null) {
                                    if (socketClient.SN.equals(sn)) {
                                        socketClient.sendData(data);
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }

                            }

                            break;
                    }
                }
            };
            Looper.loop();
        }

        public void send(String SN, String data) {
            if (mSendHandler != null) {
                Message message = mSendHandler.obtainMessage();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("sn", SN);
                bundle.putString("data", data);
                message.setData(bundle);
                mSendHandler.sendMessage(message);
            }
        }

        public void sendToAllClient(String data) {
            if (mSendHandler != null) {
                Message message = mSendHandler.obtainMessage();
                message.what = 1;
                message.obj = data;
                mSendHandler.sendMessage(message);
            }
        }


    }

    private class SocketClient extends Thread {
        private Socket socket;
        //客户端地址信息
        private String host;
        public String SN = "";
        private boolean connect = true;
        private OutputStream mOut;
        private InputStream mIn;
        private String equipmentNumber;

        public SocketClient(Socket socket) {
            this.socket = socket;
            //通过socket可以得知远端计算机信息
            InetAddress address = socket.getInetAddress();
            host = address.getHostAddress();
            Log.e(TAG, "ClientHandler: " + address);
            addOut(this);


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
                while (connect && mIn != null && mOut != null) {
                    if (mIn.available() > 0) {
                        byte[] bytes = new byte[1024];
                        int read = mIn.read(bytes);
                        Log.e(TAG, "run: " + read);
                        if (read > 10) {

                            String jsonString = new String(bytes, 0, read);

                            String[] split = jsonString.split("data=");
                            for (int i = 0; i < split.length; i++) {
                                String data = split[i];
                                if (data.startsWith("{") && data.endsWith("}")) {
                                    ManageDataBean manageDataBean = gson.fromJson(data, ManageDataBean.class);
                                    if (manageDataBean != null) {
                                        if (this.SN == null) {
                                            this.SN = manageDataBean.SN;
                                            for (ManageDataBean manageDataBean1 : TeacherApplication.sManageDataBeans) {
                                                if (manageDataBean1.SN.equals(this.SN)) {
                                                    manageDataBean1.setState_connet(1);
                                                    sendNotifyTag();
                                                    break;
                                                }
                                                senTestData(manageDataBean1);
                                            }
                                        }
                                        Message message = mHandler.obtainMessage();
                                        message.what = 3;
                                        message.obj = manageDataBean;
                                        mHandler.sendMessage(message);
                                    }
                                }
                            }


                            Log.e(TAG, "run: " + jsonString);

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
                removeOut(this);
                for (ManageDataBean manageDataBean1 : TeacherApplication.sManageDataBeans) {
                    if (manageDataBean1.SN.equals(this.SN)) {
                        manageDataBean1.setState_connet(0);
                        sendNotifyTag();
                        break;
                    }
                }
            }
        }

        private void sendNotifyTag() {
            Message message = mHandler.obtainMessage();
            message.what = 1;

            mHandler.sendMessage(message);
        }


        private void sendHeartData(ManageDataBean manageDataBean) {
            sendData(gson.toJson(manageDataBean));
        }

        public void sendData(String data) {
            if (mOut != null) {
                try {
                    mOut.write(data.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    connect = false;


                }
            }

        }


    }

    private void senTestData(ManageDataBean manageDataBean) {
        if (TeacherApplication.mTestBean != null) {
            manageDataBean.CMD = TESTDATA;
            manageDataBean.setTestBean(TeacherApplication.mTestBean);
            sendDataToClient(manageDataBean.SN, manageDataBean);
        }


    }

    private synchronized void addOut(SocketClient socketClient) {
        socketClients.add(socketClient);

    }

    private synchronized void removeOut(SocketClient socketClient) {

        socketClients.remove(socketClient);


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
        TeacherAppService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TeacherAppService.this;
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
