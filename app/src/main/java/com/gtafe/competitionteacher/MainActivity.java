package com.gtafe.competitionteacher;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gtafe.competitionlib.GtaToast;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.WifiUtils;
import com.gtafe.competitionlib.utils.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.CHANGEMODE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.YONGDIAN;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.COMPELITE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.STUDY;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.TEST;
import static com.gtafe.competitionteacher.TeacherApplication.sManageDataBeans;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_bt_study)
    TextView mBtStudy;
    @BindView(R.id.main_bt_test)
    TextView mBtTest;
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
    @BindView(R.id.main_view_mode)
    View mMainViewMode;
    @BindView(R.id.main_view_ip)
    View mMainViewIp;


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

    public static boolean mIsSetting;
    public CompelitionAdapter mCompelitionAdapter;
    public GtaAlerDialog mGtaAlerDialog;

    @Override
    protected void init() {
        Intent intent = getIntent();
        String mUser = intent.getStringExtra(Constant.USER);

        mIsSetting = mUser.equals("admin");
        if (mIsSetting) {
            mMainViewIp.setVisibility(View.VISIBLE);
            mMainViewMode.setVisibility(View.GONE);
            mTvIp.setVisibility(View.VISIBLE);
        } else {
            mMainViewIp.setVisibility(View.GONE);
            mMainViewMode.setVisibility(View.VISIBLE);
            mTvIp.setVisibility(View.GONE);

        }

        mCompelitionAdapter = new CompelitionAdapter(this);
        mRvList.setLayoutManager(new GridLayoutManager(mContext, mIsSetting ? 3 : 6));
        mRvList.setAdapter(mCompelitionAdapter);

        mCompelitionAdapter.setData(sManageDataBeans);

    }

    @Override
    public void onAddnewBianhao(ManageDataBean manageDataBean) {
        if (mIsSetting) {

            if (mGtaAlerDialog != null && mGtaAlerDialog.isShowing()) {
                mGtaAlerDialog.cancel();
            }
            mGtaAlerDialog = new GtaAlerDialog(mContext);
            mGtaAlerDialog.setButtonCancle("取消");
            mGtaAlerDialog.setTitle(null, "添加设备");
            for (ManageDataBean dataBean : sManageDataBeans) {
                if (dataBean.SN.equals(manageDataBean.SN)) {

                    mGtaAlerDialog.setTitle(null, "设备已经添加，是否替换？");
                    break;
                }
            }
            mGtaAlerDialog.setMsg("设备SN:" + manageDataBean.SN + "\n" + "设备编号：" + manageDataBean.getBianhao());
            mGtaAlerDialog.setButtonConfir("确定");
            mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                @Override
                public void OnConfirmButtonClick() {

                    boolean added = false;
                    for (ManageDataBean dataBean : sManageDataBeans) {
                        if (dataBean.SN.equals(manageDataBean.SN)) {
                            dataBean.setBianhao(manageDataBean.getBianhao());
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        sManageDataBeans.add(manageDataBean);
                    }

                    mCompelitionAdapter.setData(sManageDataBeans);

                }

                @Override
                public void OnCancleButtonClick() {

                }
            });
            mGtaAlerDialog.show();
        } else {
            GtaToast.toastOne(mContext, "添加设备需要管理员账号登录");
        }
    }

    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    int bounds = 85;

    @OnClick({R.id.main_bt_test, R.id.main_bt_competition, R.id.main_bt_study, R.id.main_tv_ip, R.id.main_et_ip, R.id.main_et_255, R.id.main_et_gateway, R.id.main_comfir, R.id.main_back, R.id.open_all, R.id.close_all})
    public void onClick(View view) {
        ManageDataBean manageDataBean = null;
        Drawable drawable;
        switch (view.getId()) {
            case R.id.main_bt_test:
                mBtTest.setTextColor(getResources().getColor(R.color.mode_yellow));
                mBtStudy.setTextColor(getResources().getColor(R.color.white));
                mBtCompetition.setTextColor(getResources().getColor(R.color.white));

                drawable = getResources().getDrawable(R.drawable.xunlianmoshilogo2);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtTest.setCompoundDrawables(null, drawable, null, null);

                drawable = getResources().getDrawable(R.drawable.xueximoshilogo);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtStudy.setCompoundDrawables(null, drawable, null, null);

                drawable = getResources().getDrawable(R.drawable.jingsaimoshilogo2);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtCompetition.setCompoundDrawables(null, drawable, null, null);


                manageDataBean = new ManageDataBean();
                manageDataBean.CMD = CHANGEMODE;
                manageDataBean.MODE = TEST;
                sendDataToAllClient(manageDataBean);
                break;
            case R.id.main_bt_study:

                mBtTest.setTextColor(getResources().getColor(R.color.white));
                mBtStudy.setTextColor(getResources().getColor(R.color.mode_yellow));
                mBtCompetition.setTextColor(getResources().getColor(R.color.white));

                drawable = getResources().getDrawable(R.drawable.xunlianmoshilogo);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtTest.setCompoundDrawables(null, drawable, null, null);

                drawable = getResources().getDrawable(R.drawable.xueximoshilogo);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtStudy.setCompoundDrawables(null, drawable, null, null);

                drawable = getResources().getDrawable(R.drawable.jingsaimoshilogo2);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtCompetition.setCompoundDrawables(null, drawable, null, null);

                manageDataBean = new ManageDataBean();
                manageDataBean.CMD = CHANGEMODE;
                manageDataBean.MODE = STUDY;
                sendDataToAllClient(manageDataBean);


                break;
            case R.id.main_bt_competition:
                mBtTest.setTextColor(getResources().getColor(R.color.white));
                mBtStudy.setTextColor(getResources().getColor(R.color.white));
                mBtCompetition.setTextColor(getResources().getColor(R.color.mode_yellow));

                drawable = getResources().getDrawable(R.drawable.xunlianmoshilogo);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtTest.setCompoundDrawables(null, drawable, null, null);

                drawable = getResources().getDrawable(R.drawable.xueximoshilogo);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtStudy.setCompoundDrawables(null, drawable, null, null);

                drawable = getResources().getDrawable(R.drawable.jingsaimoshilogo);
                drawable.setBounds(0, 0, bounds, bounds);
                mBtCompetition.setCompoundDrawables(null, drawable, null, null);


                manageDataBean = new ManageDataBean();
                manageDataBean.CMD = CHANGEMODE;
                manageDataBean.MODE = COMPELITE;
                sendDataToAllClient(manageDataBean);
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
                manageDataBean = new ManageDataBean();
                manageDataBean.CMD = YONGDIAN;
                manageDataBean.setState_power(1);
                sendDataToAllClient(manageDataBean);
                break;
            case R.id.close_all:
                manageDataBean = new ManageDataBean();
                manageDataBean.CMD = YONGDIAN;
                manageDataBean.setState_power(0);
                sendDataToAllClient(manageDataBean);
                break;
        }
    }
    @Override
    public void notifyData() {
        mCompelitionAdapter.notifyDataSetChanged();

    }
    @Override
    public void onReceivDataFromServer(ManageDataBean userDataBean) {

        switch (userDataBean.CMD) {
            case YONGDIAN:
                GtaAlerDialog gtaAlerDialog = new GtaAlerDialog(mContext);
                gtaAlerDialog.setTitle("申请用电");
                gtaAlerDialog.setMessage(userDataBean.getBianhao()+"\n申请用电");
                gtaAlerDialog.setButtonConfir("批准");
                gtaAlerDialog.setButtonCancle("拒绝");
                gtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {
                        ManageDataBean manageDataBean = new ManageDataBean();
                        manageDataBean.CMD = YONGDIAN;
                        manageDataBean.SN = userDataBean.SN;
                        manageDataBean.setState_power(1);
                        sendDataToClient(manageDataBean.SN,manageDataBean);
                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                gtaAlerDialog.show();
                break;
            case JUSHOU:
                ManageDataBean singleManageBean = getSingleManageBean(userDataBean);
                if (singleManageBean != null) {
                    singleManageBean.setState_hand(userDataBean.getState_hand());
                }
                mCompelitionAdapter.notifyDataSetChanged();
                break;
        }
    }

    private ManageDataBean getSingleManageBean(ManageDataBean userDataBean) {
        for (ManageDataBean manageDataBean : sManageDataBeans) {
            if (manageDataBean.SN.equals(userDataBean.SN)) {

                return manageDataBean;
            }
        }
        return null;
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