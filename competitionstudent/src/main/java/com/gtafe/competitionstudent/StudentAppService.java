package com.gtafe.competitionstudent;

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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.utils.Util;
import com.gtafe.competitionstudent.serialport.CMD_MSG_BACK;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android_serialport_api.SerialPort;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.gtafe.competitionstudent.StudentApplication.mManageDataBean;

/**
 * Created by ZhouJF on 2020/11/20.
 */
public class StudentAppService extends Service {
    private static final String TAG = "StudentAppService";
    public IntentFilter mFilter;
    public NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
    public boolean mPixairConnect;
    private Gson gson = new Gson();

    public byte headForward = 0;
    List<DataChangeInterFace> mDataChangeInterFaces = new ArrayList<>();
    public SocketThread mSocketThread;
    public Context mContext;
    public SerialPortThread mSerialPortThread;
    public SendThread mSendThread;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
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

        String ip = getSharedPreferences(Constant.PACKGE, MODE_PRIVATE).getString(Constant.CENTER_IP, "10.2.8.22");
        connetServer(ip);
        mSendThread = new SendThread();
        mSendThread.start();

        mSerialPortThread = new SerialPortThread();
        mSerialPortThread.start();

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
                        dataChangeInterFace.onConnectStateChange((Boolean) msg.obj);
                    }
                    break;
                case 2:
                    for (DataChangeInterFace dataChangeInterFace : mDataChangeInterFaces) {
                        dataChangeInterFace.onReceivDataFromServer((ManageDataBean) msg.obj);
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
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
                    break;
                case 15:
                    break;
                case 16:
                    break;
                case 17:
                    break;
                case 18:
                    break;
                case 19:
                    break;
                case 20:
                    for (DataChangeInterFace dataChangeInterFace : mDataChangeInterFaces) {
                        dataChangeInterFace.onReceivDataFromSerial((CMD_MSG_BACK) msg.obj);
                    }
                    break;
            }
        }
    };


    public void connetServer(String ip) {

        if (mSocketThread != null) {
            mSocketThread.disConnect();
        }

        mSocketThread = new SocketThread(ip, 8088);
        mSocketThread.start();
    }

    public void sendBianhao(String bianhao) {
        if (mSocketThread != null) {

            ManageDataBean manageDataBean = new ManageDataBean();
            manageDataBean.SN = StudentApplication.SN;
            manageDataBean.CMD = ManageDataBean.EMU_CMD.BIANHAO;
            manageDataBean.setBianhao(bianhao);
            mSendThread.sendData(gson.toJson(manageDataBean));
            SharePrefrenceUtils.putString(mContext, Constant.BIANHAO, bianhao);
            mManageDataBean.setBianhao(bianhao);
        }
    }

    public void sendDataToServer(ManageDataBean manageDataBean) {
        if (mSocketThread != null) {
            mSendThread.sendData(gson.toJson(manageDataBean));
        }
    }

    public void sendDataToSerial(byte[] bytes) {
        if (mSerialPortThread != null) {
            mSerialPortThread.sendData(bytes);
        }
    }

    private class SocketThread extends Thread {
        private boolean connectting = false;
        private boolean reConnectting = false;
        private Socket socket = null;
        private String ip;
        private int port = 8088;
        private OutputStream mOut;
        private InputStream mIn;
        private boolean serving = true;

        public SocketThread(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            serving = true;
            while (!mDestroy && serving) {
                heartCount = 0;
                Log.e(TAG, "run: 1");
                StudentApplication.SN = Util.tryGetWifiMac(mContext);

                connectting = false;
                while (serving && !connectting) {
                    Log.e(TAG, "run: 2");

                    try {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket");
                   /*   String ip = getSharedPreferences(Constant.PACKGE, MODE_PRIVATE).getString(Constant.CENTER_IP, "10.2.8.22");
                        int port = getSharedPreferences(Constant.PACKGE, MODE_PRIVATE).getInt(Constant.CENTER_PORT, 8055);*/
                        socket = new Socket(ip, port);
                        socket.setSoTimeout(500);
                        connectting = true;
                    } catch (IOException e) {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket失败 5秒后重新连接");
                        SystemClock.sleep(2000);
                        e.printStackTrace();
                    }
                }
                try {
                    mOut = socket.getOutputStream();
                    mIn = socket.getInputStream();
                    StudentApplication.mManageDataBean.CMD = ManageDataBean.EMU_CMD.CONTROL;
                    mManageDataBean.setState_connet(1);
                    sendDataToServer(mManageDataBean);
                } catch (Exception e) {
                    Log.e(TAG, "获取输入流失败");
                    e.printStackTrace();
                    continue;
                }
                heartCount = 0;
                try {
                    while (serving && mIn != null) {
                        // Log.e(TAG, "run: 3  " + socket.isInputShutdown());

                        if (mIn.available() > 0) {
                            Thread.sleep(10);
                            byte[] bytes = new byte[1024 * 2];
                            int read = mIn.read(bytes);
                            if (read > 10) {
                                String jsonString = new String(bytes, 0, read);
                                Log.e(TAG, "run: " + jsonString);
                                String[] split = jsonString.split("data=");
                                for (int i = 0; i < split.length; i++) {
                                    String data = split[i];
                                    if (data.startsWith("{") && data.endsWith("}")) {

                                        ManageDataBean userDataBean = gson.fromJson(data, ManageDataBean.class);
                                        if (userDataBean != null) {
                                            receivDataFromServer(userDataBean);
                                        }
                                    }
                                }
                            }
                            Thread.sleep(30);
                        }
                    }
                } catch (Exception e) {

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
                            mIn = null;
                            mOut = null;
                            socket = null;
                        }
                    }
                }
                Log.e(TAG, "run: 4");

                Log.e(TAG, "服务器断开，正在重新连接服务器");

            }
        }

        public void disConnect() {
            serving = false;
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
        }

        int heartCount = 0;

        public void sendHeartData() {
            StudentApplication.mManageDataBean.CMD = ManageDataBean.EMU_CMD.HERAT;
            StudentApplication.mManageDataBean.heartCount = heartCount++;
            String data = gson.toJson(StudentApplication.mManageDataBean);
            sendData(data);
        }

        private void sendData(String data) {
            data = "data=" + data;
            if (mOut != null) {
                try {
                    mOut.write(data.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class SerialPortThread extends Thread {
        boolean readDataing = true;
        SerialPort mSerialPort;
        public OutputStream mOutputStream;
        public InputStream mInputStream;

        @Override
        public void run() {
            readDataing = true;
            while (readDataing) {
                boolean connectSerialPort = true;
                while (connectSerialPort) {
                    try {
                        mSerialPort = StudentApplication.getSerialPort();
                        connectSerialPort = false;
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();

                while (mInputStream != null) {
                    try {
                        if (mInputStream.available() > 0) {
                            Thread.sleep(3);
                            byte[] sbuffer = new byte[128];
                            int size = mInputStream.read(sbuffer);
                            Log.e(TAG, "读取到的数据：" + Util.byteToHexString(sbuffer, size));
                            if (size == 21) {
                                byte[] bytes = Arrays.copyOf(sbuffer, size);
                                CMD_MSG_BACK cmd_msg_back = new CMD_MSG_BACK(bytes);
                                Message message = mHandler.obtainMessage();
                                message.what = 20;
                                message.obj = cmd_msg_back;
                                mHandler.sendMessage(message);
                                continue;
                            }
                            Thread.sleep(30);
                        }
                    } catch (IOException e) {
                        mInputStream = null;

                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        mInputStream = null;
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "run: 读取串口结束");
                mSerialPort.close();
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void sendData(byte[] bytes) {
            if (mOutputStream != null) {
                try {
                    mOutputStream.write(bytes);
                    Log.e(TAG, "sendData: " + Util.byteToHexString(bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ;

    private void receivDataFromServer(ManageDataBean userDataBean) {
        heatCount = 0;
        Message message = mHandler.obtainMessage();
        message.what = 2;
        message.obj = userDataBean;
        mHandler.sendMessage(message);
        connectStateChange(true);
    }

    private void connectStateChange(boolean state) {
        Message message = mHandler.obtainMessage();
        message.what = 1;
        message.obj = state;
        mHandler.sendMessage(message);

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
        StudentAppService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StudentAppService.this;
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

    private int heatCount = 0;

    private class SendThread extends Thread {
        private Handler mSendHandler;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            mSendHandler = new Handler() {

                public void handleMessage(Message msg) {
                    if (mDestroy) {
                        return;
                    }
                    switch (msg.what) {

                        case 0:
                            if (mSocketThread != null) {
                                mSocketThread.sendHeartData();
                            }
                            sendHeartData();
                            heatCount++;
                            if (heatCount > 10) {
                                heatCount = 0;
                                connectStateChange(false);
                                String ip = getSharedPreferences(Constant.PACKGE, MODE_PRIVATE).getString(Constant.CENTER_IP, "10.2.8.22");

                                connetServer(ip);
                            }

                            break;
                        case 1:
                            break;
                        case 2:
                            mSocketThread.sendData((String) msg.obj);
                            break;
                    }
                }
            };
            sendHeartData();
            Looper.loop();
        }

        public void send(byte[] data) {
            if (mSendHandler != null) {
                Message message = mSendHandler.obtainMessage();
                message.what = 1;
                message.obj = data;
                mSendHandler.sendMessageDelayed(message, 1000);
            }
        }

        public void sendHeartData() {
            if (mSendHandler != null) {
                Message message = mSendHandler.obtainMessage();
                message.what = 0;
                mSendHandler.sendMessageDelayed(message, 1000);
            }
        }

        public void sendData(String data) {
            if (mSendHandler != null) {
                Message message = mSendHandler.obtainMessage();
                message.what = 2;
                message.obj = data;
                mSendHandler.sendMessageDelayed(message, 1000);
            }
        }


    }


}
