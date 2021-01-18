package com.gtafe.competitionteacher;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    TextView mEtIp;
    @BindView(R.id.main_et_255)
    TextView mEt255;
    @BindView(R.id.main_et_gateway)
    TextView mEtgateway;
    @BindView(R.id.main_comfir)
    TextView mcomfir;
    @BindView(R.id.main_back)
    TextView mBack;
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

    @Override
    protected void init() {
        CompelitionAdapter compelitionAdapter = new CompelitionAdapter(mContext);
        mRvList.setLayoutManager(new GridLayoutManager(mContext, 3));
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
            case R.id.main_et_ip:
                break;
            case R.id.main_et_255:
                break;
            case R.id.main_et_gateway:
                break;
            case R.id.main_comfir:
                break;
            case R.id.main_back:
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
}