package com.gtafe.competitionstudent;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.utils.Util;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;


/**
 * Created by ZhouJF on 2021-01-23.
 */
public class StudentApplication extends Application {
    private static final String TAG = "StudentApplication";

    public static String Bianhao;
    public static String SN;

    public static SerialPort mSerialPort;
    public static ManageDataBean mManageDataBean;
    public static ManageDataBean.TestBean sTestBean;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, StudentAppService.class));
        } else {
            startService(new Intent(this, StudentAppService.class));
        }
        mManageDataBean = new ManageDataBean();
        mManageDataBean.SN = Util.tryGetWifiMac(this);

        mManageDataBean.setBianhao(SharePrefrenceUtils.getString(this, Constant.BIANHAO));
    }



    public static SerialPort getSerialPort() throws IOException {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] allDevices = serialPortFinder.getAllDevices();
        for (String allDevice : allDevices) {
            Log.e(TAG, "getSerialPort: " + allDevice);
        }
        if (mSerialPort == null) {
            synchronized (StudentApplication.class) {
                if (mSerialPort == null) {
                    Log.e(TAG, "CreatSerialPort1: ");
                    mSerialPort = new SerialPort(new File("/dev/ttyS4"), 9600, 0);
                    //mSerialPort = new SerialPort(new File("/dev/ttyS0"), 57600, 0);
                    Log.e(TAG, "CreatSerialPort:2 ");
                }
            }
        }
        return mSerialPort;
    }

}
