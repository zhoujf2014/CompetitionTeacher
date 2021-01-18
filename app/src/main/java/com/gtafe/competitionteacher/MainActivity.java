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

    @BindView(R.id.bt_study)
    TextView mBtStudy;
    @BindView(R.id.bt_train)
    TextView mBtTrain;
    @BindView(R.id.bt_competition)
    TextView mBtCompetition;
    @BindView(R.id.tv_ip)
    TextView mTvIp;
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

    @OnClick({R.id.bt_study, R.id.bt_train, R.id.bt_competition})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_study:
                refreshWifiList();
                break;
            case R.id.bt_train:
                break;
            case R.id.bt_competition:
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