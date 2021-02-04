package com.gtafe.competitionteacher;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.SharePrefrenceUtils;
import com.gtafe.competitionlib.utils.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZhouJF on 2021-01-23.
 */
public class TeacherApplication extends Application {
    public static ManageDataBean.TestBean mTestBean;
    public static List<ManageDataBean> sManageDataBeans;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, TeacherAppService.class));
        } else {
            startService(new Intent(this, TeacherAppService.class));
        }
        Constant.SN = Util.tryGetWifiMac(this);
        String manageData = SharePrefrenceUtils.getString(this, Constant.MANAGEDATA);
        sManageDataBeans = new Gson().fromJson(manageData, new TypeToken<List<ManageDataBean>>() {
        }.getType());
        if (sManageDataBeans!=null) {
            for (ManageDataBean manageDataBean : sManageDataBeans) {
                manageDataBean.setState_hand(0);
                manageDataBean.setState_power(0);
                manageDataBean.setState_connet(0);
                manageDataBean.setState_control(0);
            }
        }else {
            sManageDataBeans = new ArrayList<>();
        }

    }
}
