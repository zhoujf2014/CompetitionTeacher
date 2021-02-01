package com.gtafe.competitionteacher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gtafe.competitionlib.GtaToast;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.utils.Util;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhouJF on 2020-09-28.
 */
public class GtaAddCompetitonDialog extends AlertDialog {

    public final ConnectDialogHolder mConnectDialogHolder;

    OnButtonClickLisener mOnButtonClickLisener;
    public final Context mContext;
    public ManageDataBean.TestBean testBean;

    public GtaAddCompetitonDialog(Context context, ManageDataBean.TestBean testBean) {
        super(context);
        mContext = context;
        this.testBean = testBean;
        View view = View.inflate(context, R.layout.view_add_competition, null);


        setView(view);
        mConnectDialogHolder = new ConnectDialogHolder(view);
    }


    public void setButtonConfir(String msg) {
        mConnectDialogHolder.setButtonConfir(msg);
    }

    public void setButtonCancle(String msg) {
        mConnectDialogHolder.setButtonCancle(msg);
    }

    public void setOnclikLisener(OnButtonClickLisener onclikLisener) {
        mOnButtonClickLisener = onclikLisener;
    }

    class ConnectDialogHolder implements View.OnFocusChangeListener {
        @BindView(R.id.view_comfir_cancle)
        Button mDialogConnectCancle;
        @BindView(R.id.view_comfir_sure)
        Button mDialogConnectSetting;
        @BindView(R.id.view_title)
        TextView mViewTitle;
        @BindView(R.id.view_competiton_title)
        EditText mViewCompetitionTitle;
        @BindView(R.id.view_competiton_time_start)
        TextView mViewCompetitionStart;
        @BindView(R.id.view_competiton_time_end)
        TextView mViewCompetitionEnd;
        @BindView(R.id.view_competiton_time_des)
        EditText mViewCompetitionDes;


        AlertDialog alertDialog;
        public long startTime;
        public long mEndTime;

        ConnectDialogHolder(View view) {
            ButterKnife.bind(this, view);

            if (testBean != null) {
                startTime = testBean.getTime_start();
                mEndTime = testBean.getTime_stop();
                mViewCompetitionTitle.setText(testBean.getTitle());
                mViewCompetitionStart.setText(Util.getFormatDate(testBean.getTime_start()));
                mViewCompetitionEnd.setText(Util.getFormatDate(testBean.getTime_stop()));
                mViewCompetitionDes.setText(testBean.getDes());
            }

        }

        @OnClick({R.id.view_comfir_cancle, R.id.view_competiton_time_start, R.id.view_competiton_time_end, R.id.view_comfir_sure})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.view_competiton_time_start:
                case R.id.view_competiton_time_end:

                    TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {

                            switch (view.getId()) {
                                case R.id.view_competiton_time_start:
                                    startTime = date.getTime();
                                    if (mEndTime == 0 || startTime - mEndTime < 0) {

                                        mViewCompetitionStart.setText(Util.getFormatDate(date));
                                    } else {
                                        GtaToast.toastOne(mContext, "开始时间要小于结束时间");
                                    }
                                    mViewCompetitionStart.clearFocus();

                                    break;
                                case R.id.view_competiton_time_end:
                                    mEndTime = date.getTime();
                                    if (startTime - mEndTime < 0) {
                                        mViewCompetitionEnd.setText(Util.getFormatDate(date));
                                    } else {
                                        GtaToast.toastOne(mContext, "开始时间要小于结束时间");
                                    }
                                    mViewCompetitionEnd.clearFocus();
                                    break;
                            }


                        }
                    }).setType(new boolean[]{true, true, true, true, true, false}).isDialog(true).isCenterLabel(true).setOutSideCancelable(false).setTitleText("选择竞赛日期").setOutSideColor(Color.GREEN).build();

                    Dialog mDialog = pvTime.getDialog();
                    if (mDialog != null) {

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                800,
                                800,
                                Gravity.CENTER);
//
                        params.leftMargin = 0;
                        params.rightMargin = 0;
                        pvTime.getDialogContainerLayout().setLayoutParams(params);

                        Window dialogWindow = mDialog.getWindow();
                        if (dialogWindow != null) {
                            dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                            dialogWindow.setGravity(Gravity.CENTER);//改成Bottom,底部显示
                            dialogWindow.setDimAmount(0.3f);
                        }
                    }
                    pvTime.show();


                    break;
                case R.id.view_comfir_cancle:
                    if (mOnButtonClickLisener != null) {
                        mOnButtonClickLisener.OnCancleButtonClick();
                    }
                    cancel();
                    break;
                case R.id.view_comfir_sure:
                    if (mOnButtonClickLisener != null) {
                        String competitionTitle = mViewCompetitionTitle.getText().toString().trim();

                        String competitionDes = mViewCompetitionDes.getText().toString().trim();
                        if (TextUtils.isEmpty(competitionTitle)) {
                            GtaToast.toastOne(mContext, "请输入竞赛标题");
                            return;

                        }

                        if (TextUtils.isEmpty(competitionDes)) {
                            GtaToast.toastOne(mContext, "请输入竞赛内容");
                            return;
                        }
                        if (startTime == 0) {
                            GtaToast.toastOne(mContext, "请设置竞赛开始时间");
                            return;

                        }
                        if (mEndTime == 0) {
                            GtaToast.toastOne(mContext, "请设置竞赛结束时间");
                            return;

                        }
                        ManageDataBean.TestBean testBean = new ManageDataBean.TestBean(competitionTitle, startTime, mEndTime, competitionDes);
                        mOnButtonClickLisener.OnConfirmButtonClick(testBean);
                    }
                    cancel();
                    break;
            }
        }


        public void setButtonConfir(String msg) {
            if (msg == null) {
                mDialogConnectSetting.setVisibility(View.GONE);
            } else {
                mDialogConnectSetting.setVisibility(View.VISIBLE);

                mDialogConnectSetting.setText(msg);
            }
        }

        public void setButtonCancle(String msg) {


            if (msg == null) {
                mDialogConnectCancle.setVisibility(View.GONE);
            } else {
                mDialogConnectCancle.setVisibility(View.VISIBLE);
                mDialogConnectCancle.setText(msg);
            }
        }

        @Override
        public void onFocusChange(View view, boolean b) {


            if (b) {
            }
        }
    }

    public interface OnButtonClickLisener {
        void OnConfirmButtonClick(ManageDataBean.TestBean testBean);

        void OnCancleButtonClick();
    }
}
