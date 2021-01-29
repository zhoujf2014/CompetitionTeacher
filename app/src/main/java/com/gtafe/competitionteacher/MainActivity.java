package com.gtafe.competitionteacher;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.WifiUtils;
import com.gtafe.competitionlib.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_bt_study)
    TextView mBtStudy;
    @BindView(R.id.main_bt_train)
    TextView mBtTrain;
    @BindView(R.id.main_bt_competition)
    TextView mBtCompetition;
    @BindView(R.id.main_tv_ip)
    TextView mTvIp;
    @BindView(R.id.main_et_ip)
    EditText mEtIp;
    @BindView(R.id.main_et_ssid)
    EditText mEtSSID;
    @BindView(R.id.main_et_255)
    EditText mEt255;
    @BindView(R.id.main_et_gateway)
    EditText mEtgateway;
    @BindView(R.id.main_comfir)
    TextView mcomfir;
    @BindView(R.id.main_back)
    TextView mBack;

    @BindView(R.id.main_competition_title)
    TextView mainCompetitionTitle;
    @BindView(R.id.main_competition_time)
    TextView mainCompetitionTime;
    @BindView(R.id.main_competition_yaoqiu)
    TextView mainCompetitionYaoqiu;
    @BindView(R.id.mian_competition_edit)
    ImageView mainCompetitionEdit;

    @BindView(R.id.open_all)
    TextView mOpenAll;
    @BindView(R.id.close_all)
    TextView mCloseAll;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    };
    public WifiManager mWifiManager;

    @Override
    protected void init() {
        mainCompetitionYaoqiu.setSelected(true);//实现跑马灯
        CompelitionAdapter compelitionAdapter = new CompelitionAdapter(mContext);
        mRvList.setLayoutManager(new GridLayoutManager(mContext, 6));
        mRvList.setAdapter(compelitionAdapter);
        List<ManageDataBean> manageDataBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            manageDataBeans.add(new ManageDataBean());
        }
        compelitionAdapter.setData(manageDataBeans);
        compelitionAdapter.notifyDataSetChanged();
    }

    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.main_bt_train, R.id.main_bt_competition, R.id.main_tv_ip, R.id.main_et_ip, R.id.main_et_255, R.id.main_et_gateway, R.id.main_comfir, R.id.main_back, R.id.open_all, R.id.close_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_bt_train:
                break;
            case R.id.main_bt_competition:
                break;
            case R.id.main_tv_ip:
                break;
            case R.id.mian_competition_edit:

                break;
            case R.id.main_et_ip:
                break;
            case R.id.main_et_255:
                break;
            case R.id.main_et_gateway:
                break;
            case R.id.main_comfir:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                break;
            case R.id.main_back:
                finish();
                break;
            case R.id.open_all:
                break;
            case R.id.close_all:
                break;
        }
    }

    private void refreshWifiList() {
        List<ScanResult> wifiList = WifiUtils.getInstance(mContext).getWifiList();
        for (ScanResult scanResult : wifiList) {
            //  scanResult.BSSID;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWifiState();

    }

    private int getWifiState() {
        // Wifi的连接速度及信号强度：
        if (mWifiManager == null) {

            mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        }
        // WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        WifiInfo info = mWifiManager.getConnectionInfo();
        int strength = 0;


        if (info.getBSSID() != null) {
            mEtSSID.setText(info.getSSID());
            String ip = Util.getIP(mContext);
            mEtIp.setText(ip);
            mTvIp.setText(ip);
        }
        return strength;
    }
}