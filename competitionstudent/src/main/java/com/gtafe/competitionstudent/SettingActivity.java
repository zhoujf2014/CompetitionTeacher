package com.gtafe.competitionstudent;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gtafe.competitionlib.GtaToast;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionstudent.serialport.CMD_MSG_BACK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gtafe.competitionstudent.StudentApplication.Bianhao;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.setting_back)
    TextView mBack;
    @BindView(R.id.setting_ip)
    TextView mTvIp;
    @BindView(R.id.setting_sn)
    TextView mTvSN;
    @BindView(R.id.setting_save)
    TextView mSave;
    @BindView(R.id.setting_connet)
    TextView mConnect;
    @BindView(R.id.setting_send)
    TextView mSend;
    @BindView(R.id.setting_et_ip)
    TextView mEtIp;
    @BindView(R.id.setting_connetstate)
    TextView mConnectState;
    @BindView(R.id.setting_et_bianhao)
    TextView mBianhao;
    private String mIp;

    @Override
    protected void init() {
        mIp = getSharedPreferences(Constant.PACKGE, MODE_PRIVATE).getString(Constant.CENTER_IP, "10.2.8.22");
        mEtIp.setText(mIp);
        mTvIp.setText("教师端IP:" + mIp);
        mBianhao.setText(Bianhao);
    }

    @Override
    protected int setView() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.setting_back, R.id.setting_save, R.id.setting_connet, R.id.setting_send})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
                break;
            case R.id.setting_save:
                String trim = mEtIp.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    GtaToast.toastOne(mContext, "请输入IP地址");
                    return;
                }
                String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(trim);
                if (!matcher.matches()) {
                    GtaToast.toastOne(mContext, "请输入正确的IP地址");
                    return;
                }
                mIp = trim;
                SharePrefrenceUtils.putString(mContext, Constant.CENTER_IP, mIp);
                GtaToast.toastOne(mContext, "保存成功");
                break;
            case R.id.setting_connet:
                if (mAppService != null) {

                    mAppService.connetServer(mIp);
                } else {
                    GtaToast.toastOne(mContext, "操作失败");

                }
                break;
            case R.id.setting_send:
                String bianhao = mBianhao.getText().toString().trim();
                if (TextUtils.isEmpty(bianhao)) {
                    GtaToast.toastOne(mContext, "请输入编号");
                    return;
                }
                if (mAppService != null) {
                    mAppService.sendBianhao(bianhao);
                }
                break;


        }
    }

    @Override
    public void onConnectStateChange(boolean state) {
        mTvSN.setText("设备SN：" + StudentApplication.SN);

        if (state) {

            mConnectState.setText("已连接");
            mConnectState.setTextColor(getResources().getColor(R.color.text_green));


        } else {
            mConnectState.setText("未连接");
            mConnectState.setTextColor(getResources().getColor(R.color.textcolor));

        }
    }

    @Override
    public void onReceivDataFromServer(ManageDataBean userDataBean) {
        switch (userDataBean.CMD) {
            case YONGDIAN:
                GtaToast.toastOne(mContext, "打开电源");
                break;
        }
    }

    @Override
    public void onReceivDataFromSerial(CMD_MSG_BACK obj) {

    }
}