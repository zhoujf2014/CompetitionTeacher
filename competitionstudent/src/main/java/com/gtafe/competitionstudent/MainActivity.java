package com.gtafe.competitionstudent;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.utils.Util;
import com.gtafe.competitionstudent.serialport.CMD_MSG_BACK;
import com.gtafe.competitionstudent.serialport.CMD_MSG_CMD;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.COMPlETE;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.CONTROL;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.JUSHOU;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.YONGDIAN;

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

                    if (StudentApplication.mManageDataBean.getState_power() == 1) {
                        if (mStartTime == 0) {
                            mStartTime = System.currentTimeMillis();
                        }
                        mTime = System.currentTimeMillis() - mStartTime;
                        mTvTime.setText(Util.getFormatCountDown(mTime));
                    } else {
                        mTvTime.setText("00:00:00");
                        mStartTime = 0;
                        mTime = 0;
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
    public long mTime;


    @Override
    protected void init() {
        mTvSn.setText("设备SN：" + StudentApplication.SN);
        mTvBianhao.setText("设备编号：" + StudentApplication.Bianhao);
        initTimeThread();
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
                mPixAlerDialog.setMsg("已完成竞赛项目，用时" + Util.getFormatCountDown(mTime));
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
                }


             /*   CMD_MSG_CMD cmd_msg_cmd = new CMD_MSG_CMD();
                cmd_msg_cmd.powerState = 1;
                byte[] pack = cmd_msg_cmd.pack();
                mAppService.sendDataToSerial(pack);*/
                break;
            case R.id.main_jushuo:
                StudentApplication.mManageDataBean.CMD = JUSHOU;
                StudentApplication.mManageDataBean.setState_hand(1);
                mAppService.sendDataToServer(StudentApplication.mManageDataBean);
                startButtonAnimation(mPower);

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

    @Override
    public void onReceivDataFromServer(ManageDataBean userDataBean) {
        GtaAlerDialog mPixAlerDialog;

        switch (userDataBean.CMD) {
            case CHANGEMODE:
                switch (userDataBean.MODE) {
                    case COMPETITION:
                        mMode.setText("竞赛模式");
                        mJushou.setVisibility(View.GONE);
                        mPower.setVisibility(View.GONE);
                        mTvTitle.setText("汽车维修操作");
                        mTvMsg.setVisibility(View.VISIBLE);
                        mTvDate.setVisibility(View.VISIBLE);
                        mStartTime = 0;
                        break;
                    case STUDY:
                        mMode.setText("学习模式");
                        mJushou.setVisibility(View.VISIBLE);
                        mPower.setVisibility(View.VISIBLE);
                        mTvTitle.setText("");
                        mStartTime = 0;
                        mTvMsg.setVisibility(View.GONE);
                        mTvDate.setVisibility(View.GONE);
                        break;
                    case TEST:
                        mMode.setText("训练模式");
                        mStartTime = 0;

                        mJushou.setVisibility(View.GONE);
                        mPower.setVisibility(View.VISIBLE);
                        mTvTitle.setText("");

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
                ManageDataBean.TestBean testBean = userDataBean.getTestBean();
                if (testBean != null) {
                    StudentApplication.sTestBean = testBean;
                    mTvTitle.setText(testBean.getDes());
                    mTvDate.setText(testBean.getDes());
                    mTvTitle.setText(testBean.getDes());
                }
                break;
            case TIMEALERT:

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
        }

    }

    @Override
    public void onReceivDataFromSerial(CMD_MSG_BACK cmd_msg_back) {

        //mAppService.sendDataToSerial(cmd_msg_cmd.pack());
        mTvDianya.setText(String.format("设备电压：%sV", (int) (cmd_msg_back.dianya / 10000f)));
        mTvDianliu.setText(String.format("设备电流：%sA", cmd_msg_back.dianliu / 10000f));
        mTvGonglv.setText(String.format("设备功率：%sW", cmd_msg_back.gonglv / 10000f));
        if (StudentApplication.mManageDataBean.getState_power() != cmd_msg_back.powerState) {
            StudentApplication.mManageDataBean.setState_power(cmd_msg_back.powerState);
            senStateChange();

        }
    }

    private void senStateChange() {
        StudentApplication.mManageDataBean.CMD = CONTROL;
        mAppService.sendDataToServer(StudentApplication.mManageDataBean);
    }


    @Override
    public void onConnectStateChange(boolean state) {
        if (state) {
            mTvConnectState.setText("已连接");
            mTvConnectState.setTextColor(getResources().getColor(R.color.text_green));
        } else {
            mTvConnectState.setText("未连接");
            mTvConnectState.setTextColor(getResources().getColor(R.color.textcolor));

        }
    }

}