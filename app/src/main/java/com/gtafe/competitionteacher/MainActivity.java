package com.gtafe.competitionteacher;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.gtafe.competitionlib.GtaToast;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.SharePrefrenceUtils;
import com.gtafe.competitionlib.WifiUtils;
import com.gtafe.competitionlib.utils.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.CHANGEMODE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.COMPlETE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.STARTCOMPETITION;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.TESTDATA;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.TIMEALERT;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.YONGDIAN;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.COMPETITION;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.STUDY;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.TEST;
import static com.gtafe.competitionteacher.TeacherApplication.mTestBean;
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
    @BindView(R.id.main_view_competition)
    LinearLayout mainViewCompetition;
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


        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ManageDataBean mManageDataBean;
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    if (mEmu_mode == COMPETITION) {
                        if (mTestBean != null) {
                            long date_start = mTestBean.getTime_start();
                            long time_start = System.currentTimeMillis() - date_start;
                            long date_end = mTestBean.getTime_stop();
                            long time_end = date_end - System.currentTimeMillis();
                            if (time_start > -1 && time_end > -1) {
                                TeacherApplication.sTime = time_start;
                                for (ManageDataBean manageDataBean : sManageDataBeans) {
                                    if (manageDataBean.getState_connet() != 3) {
                                        manageDataBean.setTime(time_start);
                                        mCompelitionAdapter.notifyDataSetChanged();
                                    }
                                }
                                if (!mSendAler && time_end < 10 * 60 * 1000) {
                                    ManageDataBean manageDataBean = new ManageDataBean();
                                    manageDataBean.CMD = TIMEALERT;
                                    manageDataBean.setMSG("距离竞赛结束还有10分钟");
                                    sendDataToAllClient(manageDataBean);
                                    mSendAler = true;
                                }
                            } else if (time_end < -1 && time_end > -2000) {
                                mSendAler = false;
                                ManageDataBean manageDataBean = new ManageDataBean();
                                manageDataBean.CMD = COMPlETE;
                                manageDataBean.setMSG("竞赛结束");
                                sendDataToAllClient(manageDataBean);

                            }
                        }
                    }

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

    public GtaAddCompetitonDialog mGtaAddCompetitonDialog;
    public static ManageDataBean.EMU_MODE mEmu_mode;
    public boolean mSendAler;

    @Override
    protected void init() {
        mainCompetitionYaoqiu.setSelected(true);
        mainCompetitionTitle.setSelected(true);
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
        initCompetition();
        initTimeThread();
        changeModeStudy();
    }

    private void initCompetition() {
        mTestBean = new Gson().fromJson(SharePrefrenceUtils.getString(mContext, Constant.CompetitionBean), ManageDataBean.TestBean.class);
        if (mTestBean != null) {
            mainViewCompetition.setVisibility(View.VISIBLE);
            mainCompetitionTitle.setText(mTestBean.getTitle());
            mainCompetitionTime.setText("竞赛时间：" + Util.getFormatDate(mTestBean.getTime_start()) + "-" + Util.getFormattime(mTestBean.getTime_stop()));
            mainCompetitionYaoqiu.setText("竞赛要求：" + mTestBean.getDes());

        } else {
            mainViewCompetition.setVisibility(View.GONE);
        }
        ManageDataBean manageDataBean = new ManageDataBean();
        manageDataBean.CMD = TESTDATA;
        manageDataBean.setTestBean(mTestBean);
        sendDataToAllClient(manageDataBean);
    }

    private void initTimeThread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isDesdory) {

                    try {
                        Thread.sleep(999);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }

            }
        }.start();
    }

    @Override
    public void onAddnewBianhao(ManageDataBean manageDataBean) {
        if (isFinishing()) {
            return;
        }
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
    public void onTimeChange(ManageDataBean userDataBean) {
        if (isFinishing()) {
            return;
        }
        if (userDataBean.MODE != mEmu_mode) {
            ManageDataBean manageDataBean = new ManageDataBean();
            manageDataBean.CMD = CHANGEMODE;
            manageDataBean.MODE = mEmu_mode;
            if (mTestBean != null) {
                manageDataBean.setTestBean(mTestBean);
            }
            sendDataToAllClient(manageDataBean);
            //initCompetition();
        }
        //
        ManageDataBean singleManageBean = getSingleManageBean(userDataBean);
        if (mEmu_mode != COMPETITION) {
            if (singleManageBean != null) {
                singleManageBean.setTime(userDataBean.getTime());
                singleManageBean.setState_power(userDataBean.getState_power());
                singleManageBean.setState_connet(userDataBean.getState_connet());
                mCompelitionAdapter.notifyDataSetChanged();

            }
        }
        if (singleManageBean!=null) {
            if (singleManageBean.getState_connet() == 0) {
                singleManageBean.setState_connet(1);
            }
        }


    }

    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    int bounds = 85;

    @OnClick({R.id.main_bt_test, R.id.main_bt_competition, R.id.mian_competition_edit, R.id.main_bt_study, R.id.main_tv_ip, R.id.main_et_ip, R.id.main_et_255, R.id.main_et_gateway, R.id.main_comfir, R.id.main_back, R.id.open_all, R.id.close_all})
    public void onClick(View view) {
        ManageDataBean manageDataBean = null;
        switch (view.getId()) {
            case R.id.main_bt_test:

                if (mEmu_mode != COMPETITION || mTestBean == null) {
                    changeModeTest();
                } else {
                    mGtaAlerDialog = new GtaAlerDialog(mContext);
                    mGtaAlerDialog.setButtonCancle("取消");
                    mGtaAlerDialog.setTitle(null, "切换到训练模式");

                    mGtaAlerDialog.setMsg("当前有未结束的竞赛，是否还要切换到训练模式？");
                    mGtaAlerDialog.setButtonConfir("确定");
                    mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                        @Override
                        public void OnConfirmButtonClick() {
                            changeModeTest();
                        }

                        @Override
                        public void OnCancleButtonClick() {

                        }
                    });
                    mGtaAlerDialog.show();
                }
                break;
            case R.id.main_bt_study:
                if (mEmu_mode != COMPETITION || mTestBean == null) {
                    changeModeStudy();
                } else {
                    mGtaAlerDialog = new GtaAlerDialog(mContext);
                    mGtaAlerDialog.setButtonCancle("取消");
                    mGtaAlerDialog.setTitle(null, "切换到学习模式");

                    mGtaAlerDialog.setMsg("当前有未结束的竞赛，是否还要切换到学习模式？");
                    mGtaAlerDialog.setButtonConfir("确定");
                    mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                        @Override
                        public void OnConfirmButtonClick() {
                            changeModeStudy();
                        }

                        @Override
                        public void OnCancleButtonClick() {

                        }
                    });
                    mGtaAlerDialog.show();
                }

                break;
            case R.id.main_bt_competition:
                changeModeCompetition();

                if (mTestBean == null) {

                    mGtaAddCompetitonDialog = new GtaAddCompetitonDialog(mContext, mTestBean);
                    mGtaAddCompetitonDialog.setOnclikLisener(new GtaAddCompetitonDialog.OnButtonClickLisener() {
                        @Override
                        public void OnConfirmButtonClick(ManageDataBean.TestBean testBean) {

                            mTestBean = testBean;
                            SharePrefrenceUtils.putString(mContext, Constant.CompetitionBean, new Gson().toJson(mTestBean));
                            initCompetition();
                        }

                        @Override
                        public void OnCancleButtonClick() {
                        }
                    });
                    mGtaAddCompetitonDialog.show();
                }


                break;
            case R.id.main_tv_ip:
                break;
            case R.id.mian_competition_edit:
                mGtaAddCompetitonDialog = new GtaAddCompetitonDialog(mContext, mTestBean);
                mGtaAddCompetitonDialog.setButtonCancle("删除");
                mGtaAddCompetitonDialog.setOnclikLisener(new GtaAddCompetitonDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick(ManageDataBean.TestBean testBean) {

                        mTestBean = testBean;
                        SharePrefrenceUtils.putString(mContext, Constant.CompetitionBean, new Gson().toJson(mTestBean));
                        initCompetition();
                        TeacherApplication.sTime = 0;
                        mSendAler = false;
                        for (ManageDataBean manageDataBean : sManageDataBeans) {
                            if (manageDataBean.getState_connet() != 3) {
                                manageDataBean.setTime(0);
                                mCompelitionAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void OnCancleButtonClick() {
                        if (mGtaAlerDialog != null && mGtaAlerDialog.isShowing()) {
                            mGtaAlerDialog.cancel();
                        }
                        mGtaAlerDialog = new GtaAlerDialog(mContext);
                        mGtaAlerDialog.setButtonCancle("取消");
                        mGtaAlerDialog.setTitle(null, "删除竞赛");

                        mGtaAlerDialog.setMsg("是否删除竞赛内容");
                        mGtaAlerDialog.setButtonConfir("确定");
                        mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                            @Override
                            public void OnConfirmButtonClick() {
                                mTestBean = null;
                                SharePrefrenceUtils.putString(mContext, Constant.CompetitionBean, "");
                                initCompetition();

                            }

                            @Override
                            public void OnCancleButtonClick() {

                            }
                        });
                        mGtaAlerDialog.show();
                    }
                });
                mGtaAddCompetitonDialog.show();
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
                if (mGtaAlerDialog != null && mGtaAlerDialog.isShowing()) {
                    mGtaAlerDialog.cancel();
                }
                mGtaAlerDialog = new GtaAlerDialog(mContext);
                mGtaAlerDialog.setButtonCancle("取消");
                mGtaAlerDialog.setTitle(null, "提示");

                mGtaAlerDialog.setMsg("是否退出？");
                mGtaAlerDialog.setButtonConfir("确定");
                mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {
                        finish();
                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                mGtaAlerDialog.show();

                break;
            case R.id.open_all:
                if (mGtaAlerDialog != null && mGtaAlerDialog.isShowing()) {
                    mGtaAlerDialog.cancel();
                }
                mGtaAlerDialog = new GtaAlerDialog(mContext);
                mGtaAlerDialog.setButtonCancle("取消");
                mGtaAlerDialog.setTitle(null, "提示");

                mGtaAlerDialog.setMsg("是否打开全部设备电源？");
                mGtaAlerDialog.setButtonConfir("确定");
                mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {
                        ManageDataBean manageDataBean = new ManageDataBean();
                        manageDataBean.CMD = YONGDIAN;
                        manageDataBean.setState_power(1);
                        sendDataToAllClient(manageDataBean);
                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                mGtaAlerDialog.show();

                break;
            case R.id.close_all:
                if (mGtaAlerDialog != null && mGtaAlerDialog.isShowing()) {
                    mGtaAlerDialog.cancel();
                }
                mGtaAlerDialog = new GtaAlerDialog(mContext);
                mGtaAlerDialog.setButtonCancle("取消");
                mGtaAlerDialog.setTitle(null, "提示");

                mGtaAlerDialog.setMsg("是否关闭全部设备电源？");
                mGtaAlerDialog.setButtonConfir("确定");
                mGtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {
                        ManageDataBean manageDataBean = new ManageDataBean();
                        manageDataBean.CMD = YONGDIAN;
                        manageDataBean.setState_power(0);
                        sendDataToAllClient(manageDataBean);
                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                mGtaAlerDialog.show();
                break;
        }
    }

    private void changeModeTest() {
        mEmu_mode = TEST;
        mainViewCompetition.setVisibility(View.GONE);

        Drawable drawable;
        ManageDataBean manageDataBean;
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
        changeCompelition(TEST);

    }

    private void changeModeStudy() {
        mEmu_mode = STUDY;
        mainViewCompetition.setVisibility(View.GONE);
        Drawable drawable;
        ManageDataBean manageDataBean;
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
        changeCompelition(STUDY);
    }

    private void changeCompelition(ManageDataBean.EMU_MODE emu_mode) {
        for (ManageDataBean manageDataBean : sManageDataBeans) {
            manageDataBean.MODE = emu_mode;
        }
    }

    private void changeModeCompetition() {
        mEmu_mode = COMPETITION;
        changeCompelition(COMPETITION);
        Drawable drawable;
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
        ManageDataBean manageDataBean = new ManageDataBean();
        manageDataBean.CMD = CHANGEMODE;
        manageDataBean.MODE = COMPETITION;
        sendDataToAllClient(manageDataBean);
        initCompetition();
    }

    @Override
    public void notifyData() {
        mCompelitionAdapter.notifyDataSetChanged();

    }

    @Override
    public void onReceivDataFromServer(ManageDataBean userDataBean) {
        ManageDataBean singleManageBean = null;
        switch (userDataBean.CMD) {
            case YONGDIAN:
                ManageDataBean singleManageBean1 = getSingleManageBean(userDataBean);
                if (singleManageBean1!=null) {
                    GtaAlerDialog gtaAlerDialog = new GtaAlerDialog(mContext);
                    gtaAlerDialog.setTitle(null, "申请用电");
                    gtaAlerDialog.setMsg(userDataBean.getBianhao() + "\n申请用电");
                    gtaAlerDialog.setButtonConfir("批准");
                    gtaAlerDialog.setButtonCancle("拒绝");
                    gtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                        @Override
                        public void OnConfirmButtonClick() {
                            ManageDataBean manageDataBean = new ManageDataBean();
                            manageDataBean.CMD = YONGDIAN;
                            manageDataBean.SN = userDataBean.SN;
                            manageDataBean.setState_power(1);
                            sendDataToClient(manageDataBean.SN, manageDataBean);
                        }

                        @Override
                        public void OnCancleButtonClick() {

                        }
                    });
                    gtaAlerDialog.show();
                }

                break;
            case JUSHOU:
                singleManageBean = getSingleManageBean(userDataBean);
                if (singleManageBean != null) {
                    singleManageBean.setState_hand(userDataBean.getState_hand());

                }
                break;
            case CONTROL:
                singleManageBean = getSingleManageBean(userDataBean);
                if (singleManageBean != null) {
                    singleManageBean.setState_hand(userDataBean.getState_hand());
                    singleManageBean.setState_connet(userDataBean.getState_connet());
                    singleManageBean.setState_power(userDataBean.getState_power());
                    singleManageBean.setState_control(userDataBean.getState_control());


                }
                break;
            case TESTDATA:
                if (TeacherApplication.mTestBean != null) {
                    ManageDataBean manageDataBean = new ManageDataBean();
                    manageDataBean.CMD = TESTDATA;
                    manageDataBean.setTestBean(TeacherApplication.mTestBean);
                    sendDataToClient(manageDataBean.SN, manageDataBean);
                }
                break;
            case COMPlETE:

                singleManageBean = getSingleManageBean(userDataBean);
                if (singleManageBean != null) {
                    singleManageBean.setState_connet(userDataBean.getState_connet());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppService.removeDataChangeInterFace(this);

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