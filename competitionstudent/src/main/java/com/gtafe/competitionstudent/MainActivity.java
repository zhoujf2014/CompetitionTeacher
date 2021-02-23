package com.gtafe.competitionstudent;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.utils.Util;
import com.gtafe.competitionstudent.serialport.CMD_MSG_BACK;
import com.gtafe.competitionstudent.serialport.CMD_MSG_CMD;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

import static android.media.MediaCodec.MetricsConstants.MODE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.COMPlETE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.CONTROL;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.JUSHOU;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.TESTDATA;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.YONGDIAN;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.COMPETITION;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.STUDY;
import static com.gtafe.competitionlib.ManageDataBean.EMU_MODE.TEST;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_mode)
    TextView mMode;
    @BindView(R.id.main_jushuo)
    TextView mJushou;
    @BindView(R.id.main_power)
    TextView mPower;
    @BindView(R.id.main_tv_sn)
    TextView mTvSn;
    @BindView(R.id.main_tv_connect_state)
    TextView mTvConnectState;
    @BindView(R.id.main_tv_title)
    TextView mTvTitle;
    @BindView(R.id.main_tv_time)
    TextView mTvTime;
    @BindView(R.id.main_tv_msg)
    TextView mTvMsg;
    @BindView(R.id.main_tv_date)
    TextView mTvDate;
    @BindView(R.id.main_comfir)
    TextView mComfir;

    @BindView(R.id.mian_bianhao)
    TextView mTvBianhao;
    @BindView(R.id.mian_dianya)
    TextView mTvDianya;
    @BindView(R.id.mian_dianliu)
    TextView mTvDianliu;
    @BindView(R.id.mian_gonglv)
    TextView mTvGonglv;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    if (StudentApplication.mManageDataBean.MODE == COMPETITION) {
                        if (StudentApplication.mManageDataBean.getState_connet() != 3) {

                            mTvTime.setText(Util.getFormatCountDown(StudentApplication.mManageDataBean.time));
                        }
                        //StudentApplication.mManageDataBean.setState_connet(2);
                    } else {

                        if (StudentApplication.mManageDataBean.getState_power() == 1) {
                            if (mStartTime == 0) {
                                mStartTime = System.currentTimeMillis();
                            }
                            StudentApplication.mManageDataBean.setTime(System.currentTimeMillis() - mStartTime);
                            // StudentApplication.mManageDataBean.setState_connet(2);
                            mTvTime.setText(Util.getFormatCountDown(StudentApplication.mManageDataBean.time));
                        } else {
                            mTvTime.setText("00:00:00");
                            mStartTime = 0;
                            StudentApplication.mManageDataBean.time = 0;
                            //StudentApplication.mManageDataBean.setState_connet(1);
                        }
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }

        }
    };
    public long mStartTime;


    @Override
    protected void init() {
        mTvSn.setText("设备SN：" + StudentApplication.SN);
        mTvBianhao.setText("设备编号：" + StudentApplication.mManageDataBean.getBianhao());
        initTimeThread();
        modeStudy();

    }

    private void initTimeThread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {

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
    protected int setView() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.main_comfir, R.id.main_power, R.id.main_jushuo})
    public void onClicked(View view) {
        GtaAlerDialog mPixAlerDialog;
        switch (view.getId()) {
            case R.id.main_comfir:
                mPixAlerDialog = new GtaAlerDialog(mContext);
                mPixAlerDialog.setMsg("已完成竞赛项目，用时" + Util.getFormatCountDown(StudentApplication.mManageDataBean.time));
                mPixAlerDialog.setTitle(getResources().getDrawable(R.mipmap.wancheng2), "");
                mPixAlerDialog.setButtonConfir("确定");
                mPixAlerDialog.setButtonCancle("取消");
                mPixAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {
                        StudentApplication.mManageDataBean.CMD = COMPlETE;
                        StudentApplication.mManageDataBean.setState_connet(3);
                        mAppService.sendDataToServer(StudentApplication.mManageDataBean);
                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                mPixAlerDialog.show();


                break;
            case R.id.main_power:


                if (StudentApplication.mManageDataBean.getState_power() == 1) {
                    mPixAlerDialog = new GtaAlerDialog(mContext);
                    mPixAlerDialog.setMsg("是否关闭电源？");
                    mPixAlerDialog.setTitle(null, "关闭电源");
                    mPixAlerDialog.setButtonConfir("确定");
                    mPixAlerDialog.setButtonCancle("取消");
                    mPixAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                        @Override
                        public void OnConfirmButtonClick() {
                            CMD_MSG_CMD cmd_msg_cmd = new CMD_MSG_CMD();
                            cmd_msg_cmd.powerState = 0;
                            byte[] pack = cmd_msg_cmd.pack();
                            mAppService.sendDataToSerial(pack);

                            // StudentApplication.mManageDataBean.setState_power(0);
                        }

                        @Override
                        public void OnCancleButtonClick() {

                        }
                    });
                    mPixAlerDialog.show();


                } else {
                    if (StudentApplication.mManageDataBean.MODE == TEST) {
                        mPixAlerDialog = new GtaAlerDialog(mContext);
                        mPixAlerDialog.setMsg("是否打开电源？");
                        mPixAlerDialog.setTitle(null, "打开电源");
                        mPixAlerDialog.setButtonConfir("确定");
                        mPixAlerDialog.setButtonCancle("取消");
                        mPixAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                            @Override
                            public void OnConfirmButtonClick() {
                                CMD_MSG_CMD cmd_msg_cmd = new CMD_MSG_CMD();
                                cmd_msg_cmd.powerState = 1;
                                byte[] pack = cmd_msg_cmd.pack();
                                mAppService.sendDataToSerial(pack);
                                //StudentApplication.mManageDataBean.setState_power(1);

                            }

                            @Override
                            public void OnCancleButtonClick() {

                            }
                        });
                        mPixAlerDialog.show();
                    } else {
                        StudentApplication.mManageDataBean.CMD = YONGDIAN;
                        mAppService.sendDataToServer(StudentApplication.mManageDataBean);
                        startButtonAnimation(mPower);
                        //StudentApplication.mManageDataBean.setState_power(1);

                    }

                }


              /*  CMD_MSG_CMD cmd_msg_cmd = new CMD_MSG_CMD();
                cmd_msg_cmd.powerState = 1;
                byte[] pack = cmd_msg_cmd.pack();
                mAppService.sendDataToSerial(pack);*/
                break;
            case R.id.main_jushuo:
                if (StudentApplication.mManageDataBean.getState_hand() == 0) {
                    StudentApplication.mManageDataBean.CMD = JUSHOU;
                    StudentApplication.mManageDataBean.setState_hand(1);
                    mAppService.sendDataToServer(StudentApplication.mManageDataBean);
                    startButtonAnimation(mJushou);
                } else {
                    StudentApplication.mManageDataBean.CMD = JUSHOU;
                    StudentApplication.mManageDataBean.setState_hand(0);
                    mAppService.sendDataToServer(StudentApplication.mManageDataBean);
                    mJushou.clearAnimation();
                }


    /*       CMD_MSG_CMD cmd_msg_cmd1 = new CMD_MSG_CMD();
            cmd_msg_cmd1.powerState = 0;
            byte[] pack1 = cmd_msg_cmd1.pack();
            mAppService.sendDataToSerial(pack1);*/
                break;

        }
    }

    private long startButtonAnimation(View view) {
        Animation animation2 = new ScaleAnimation(1, 0.88f, 1, 0.88f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(500);
        animation2.setRepeatMode(Animation.REVERSE);
        animation2.setRepeatCount(Animation.INFINITE);
        view.startAnimation(animation2);
        return view.getId();
    }

    GtaAlerDialog mPixAlerDialog;

    @Override
    public void onReceivDataFromServer(ManageDataBean userDataBean) {


        switch (userDataBean.CMD) {
            case CHANGEMODE:
                ManageDataBean.TestBean testBean = userDataBean.getTestBean();
                if (testBean != null) {
                    StudentApplication.mManageDataBean.setTestBean(testBean);
                }

                switch (userDataBean.MODE) {
                    case COMPETITION:
                        StudentApplication.mManageDataBean.MODE = COMPETITION;

                        mMode.setText("竞赛模式");
                        mJushou.setVisibility(View.GONE);
                        mPower.setVisibility(View.GONE);
                        mTvMsg.setVisibility(View.VISIBLE);
                        mTvDate.setVisibility(View.VISIBLE);
                        mComfir.setVisibility(View.VISIBLE);
                        mTvTitle.setText("");
                        mStartTime = 0;
                        StudentApplication.mManageDataBean.time = 0;
                        setTestData(StudentApplication.mManageDataBean.getTestBean());
                        break;
                    case STUDY:
                        modeStudy();
                        break;
                    case TEST:
                        mMode.setText("训练模式");
                        mStartTime = 0;
                        StudentApplication.mManageDataBean.time = 0;
                        mJushou.setVisibility(View.GONE);
                        mPower.setVisibility(View.VISIBLE);
                        StudentApplication.mManageDataBean.MODE = TEST;
                        if (StudentApplication.mManageDataBean.getState_power() == 1) {
                            mPower.setText("关闭电源");
                        } else {
                            mPower.setText("打开电源");
                        }
                        mTvMsg.setVisibility(View.GONE);
                        mTvDate.setVisibility(View.GONE);




                        break;
                }
                break;
            case YONGDIAN:
                CMD_MSG_CMD cmd_msg_cmd = new CMD_MSG_CMD();
                cmd_msg_cmd.powerState = (byte) userDataBean.getState_power();
                byte[] pack = cmd_msg_cmd.pack();
                mAppService.sendDataToSerial(pack);

                mPower.clearAnimation();
                break;

            case JUSHOU:
                mJushou.clearAnimation();
                break;
            case STARTCOMPETITION:


                setTestData(userDataBean.getTestBean());

                break;
            case TIMEALERT:
                if (mPixAlerDialog != null && mPixAlerDialog.isShowing()) {
                    mPixAlerDialog.cancel();
                }
                mPixAlerDialog = new GtaAlerDialog(mContext);
                mPixAlerDialog.setMsg(userDataBean.getMSG());
                mPixAlerDialog.setTitle(getResources().getDrawable(R.mipmap.wancheng2), "");
                mPixAlerDialog.setButtonConfir("确定");
                mPixAlerDialog.setButtonCancle(null);
                mPixAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {

                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                mPixAlerDialog.show();
                break;
            case COMPlETE:
                if (mPixAlerDialog != null && mPixAlerDialog.isShowing()) {
                    mPixAlerDialog.cancel();
                }
                StudentApplication.mManageDataBean.setState_connet(3);
                mPixAlerDialog = new GtaAlerDialog(mContext);
                mPixAlerDialog.setMsg("竞赛时间已经用完，");
                mPixAlerDialog.setTitle(null, "竞赛结束");
                mPixAlerDialog.setButtonConfir("确定");
                mPixAlerDialog.setButtonCancle(null);
                mPixAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {

                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                mPixAlerDialog.show();


                break;
            case TESTDATA:

                setTestData(userDataBean.getTestBean());

                break;
            case HERAT:
                if (StudentApplication.mManageDataBean.MODE == COMPETITION && StudentApplication.mManageDataBean.getState_connet() != 3) {

                    StudentApplication.mManageDataBean.setTime(userDataBean.getTime());
                }

                break;
        }

    }

    private void modeStudy() {
        StudentApplication.mManageDataBean.MODE = STUDY;

        mMode.setText("学习模式");
        mJushou.setVisibility(View.VISIBLE);
        mPower.setVisibility(View.VISIBLE);
        mTvTitle.setText("");
        mStartTime = 0;
        StudentApplication.mManageDataBean.time = 0;
        mTvMsg.setVisibility(View.GONE);
        mTvDate.setVisibility(View.GONE);
        mComfir.setVisibility(View.GONE);
    }

    private void setTestData(ManageDataBean.TestBean testBean) {
        if (testBean != null) {
            StudentApplication.sTestBean = testBean;
            if (StudentApplication.mManageDataBean.MODE == COMPETITION) {
                mTvTitle.setText(testBean.getTitle());
                mTvDate.setText("竞赛时间：" + Util.getFormatDate(testBean.getTime_start()) + "-" + Util.getFormattime(testBean.getTime_stop()));

                mTvMsg.setText("竞赛要求：" + testBean.getDes());
            }

        }
    }

    @Override
    public void onReceivDataFromSerial(CMD_MSG_BACK cmd_msg_back) {

        //mAppService.sendDataToSerial(cmd_msg_cmd.pack());
        mTvDianya.setText(String.format("设备电压：%sV", (int) (cmd_msg_back.dianya / 10000f)));
        mTvDianliu.setText(String.format("设备电流：%sA", cmd_msg_back.dianliu / 100 / 100f));
        mTvGonglv.setText(String.format("设备功率：%sW", cmd_msg_back.gonglv / 100 / 100f));
        if (StudentApplication.mManageDataBean.getState_power() != cmd_msg_back.powerState) {
            StudentApplication.mManageDataBean.setState_power(cmd_msg_back.powerState);
            senStateChange();
            if (StudentApplication.mManageDataBean.getState_power() == 1) {
                if (StudentApplication.mManageDataBean.MODE.equals(TEST)) {

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppService.removeDataChangeInterFace(this);
    }

    private void senStateChange() {
        StudentApplication.mManageDataBean.CMD = CONTROL;
        mAppService.sendDataToServer(StudentApplication.mManageDataBean);
        if (StudentApplication.mManageDataBean.getState_power() == 1) {

            mPower.setText("关闭电源");
        } else {
            if (StudentApplication.mManageDataBean.MODE == TEST) {
                mPower.setText("打开电源");
            } else {

                mPower.setText("申请用电");
            }
        }
    }


    @Override
    public void onConnectStateChange(boolean state) {
        if (state) {
            StudentApplication.mManageDataBean.setState_connet(1);
            mTvConnectState.setText("已连接");
            mTvConnectState.setTextColor(getResources().getColor(R.color.text_green));
        } else {
            mTvConnectState.setText("未连接");
            StudentApplication.mManageDataBean.setState_connet(0);
            mTvConnectState.setTextColor(getResources().getColor(R.color.textcolor));
        }
    }
}