package com.gtafe.competitionstudent;

import android.view.View;
import android.widget.TextView;

import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionstudent.serialport.CMD_MSG_BACK;
import com.gtafe.competitionstudent.serialport.CMD_MSG_CMD;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.CONTROL;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.JUSHOU;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.YONGDIAN;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_mode)
    TextView mMode;
    @BindView(R.id.main_jushuo)
    TextView mJushou;
    @BindView(R.id.main_power)
    TextView mpower;
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


    @Override
    protected void init() {
        mTvSn.setText("设备SN：" + StudentApplication.SN);
        mTvBianhao.setText("设备编号：" + StudentApplication.Bianhao);
    }

    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.main_comfir, R.id.main_power, R.id.main_jushuo})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.main_comfir:
                GtaAlerDialog pixAlerDialog = new GtaAlerDialog(mContext);
                pixAlerDialog.setMsg("已完成竞赛项目，用时120分钟");
                pixAlerDialog.setTitle(getResources().getDrawable(R.mipmap.wancheng2), "");
                pixAlerDialog.setButtonConfir("确定");
                pixAlerDialog.setButtonCancle("取消");
                pixAlerDialog.show();


                break;
            case R.id.main_power:
                StudentApplication.mManageDataBean.CMD = YONGDIAN;
                mAppService.sendDataToServer(StudentApplication.mManageDataBean);

                break;
            case R.id.main_jushuo:
                StudentApplication.mManageDataBean.CMD = JUSHOU;
                StudentApplication.mManageDataBean.setState_hand(1);
                mAppService.sendDataToServer(StudentApplication.mManageDataBean);
                break;

        }
    }


    @Override
    public void onReceivDataFromServer(ManageDataBean userDataBean) {
        switch (userDataBean.CMD) {
            case CHANGEMODE:
                switch (userDataBean.MODE) {
                    case COMPETITION:

                        mMode.setText("竞赛模式");

                        mJushou.setVisibility(View.GONE);
                        mpower.setVisibility(View.GONE);
                        mTvTitle.setText("汽车维修操作");
                        mTvMsg.setVisibility(View.VISIBLE);
                        mTvDate.setVisibility(View.VISIBLE);
                        break;
                    case STUDY:
                        mMode.setText("学习模式");
                        mJushou.setVisibility(View.VISIBLE);
                        mpower.setVisibility(View.VISIBLE);
                        mTvTitle.setText("");

                        mTvMsg.setVisibility(View.GONE);
                        mTvDate.setVisibility(View.GONE);
                        break;
                    case TEST:
                        mMode.setText("训练模式");
                        mJushou.setVisibility(View.GONE);
                        mpower.setVisibility(View.VISIBLE);
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
                break;

            case JUSHOU:
                mJushou.clearAnimation();
                break;
        }

    }

    @Override
    public void onReceivDataFromSerial(CMD_MSG_BACK cmd_msg_back) {

        //mAppService.sendDataToSerial(cmd_msg_cmd.pack());
        mTvDianya.setText(String.format("设备电压：%sV", cmd_msg_back.dianya / 10000f));
        mTvDianliu.setText(String.format("设备电流：%sV", cmd_msg_back.dianliu / 10000f));
        mTvGonglv.setText(String.format("设备功率：%sV", cmd_msg_back.gonglv / 10000f));
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