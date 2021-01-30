package com.gtafe.competitionteacher;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gtafe.competitionlib.GtaToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhouJF on 2020-09-28.
 *
 */
public class GtaAddCompetitonDialog extends AlertDialog {

    public final ConnectDialogHolder mConnectDialogHolder;

    OnButtonClickLisener mOnButtonClickLisener;
    public final Context mContext;

    public GtaAddCompetitonDialog(Context context) {
        super(context);
        mContext = context;
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

    class ConnectDialogHolder {
        @BindView(R.id.view_comfir_cancle)
        Button mDialogConnectCancle;
        @BindView(R.id.view_comfir_sure)
        Button mDialogConnectSetting;
        @BindView(R.id.view_title)
        TextView mViewTitle;
        @BindView(R.id.view_competiton_title)
        EditText mViewCompetitionTitle;
        @BindView(R.id.view_competiton_time_start)
        EditText mViewCompetitionStart;
        @BindView(R.id.view_competiton_time_end)
        EditText mViewCompetitionEnd;
        @BindView(R.id.view_competiton_time_des)
        EditText mViewCompetitionDes;

        @BindView(R.id.circletimerview)
        CircleAlarmTimerView circleAlarmTimerView;
        AlertDialog alertDialog;

        ConnectDialogHolder(View view) {
            ButterKnife.bind(this, view);


            circleAlarmTimerView.setOnTimeChangedListener(new CircleAlarmTimerView.OnTimeChangedListener() {
                @Override
                public void start(String starting) {
                    mViewCompetitionStart.setText(starting);
                }

                @Override
                public void end(String ending) {
                    mViewCompetitionEnd.setText(ending);
                }
            });
        }

        @OnClick({R.id.view_comfir_cancle, R.id.view_comfir_sure})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.view_comfir_cancle:
                    if (mOnButtonClickLisener != null) {
                        mOnButtonClickLisener.OnCancleButtonClick();
                    }
                    cancel();
                    break;
                case R.id.view_comfir_sure:
                    if (mOnButtonClickLisener != null) {
                        String competitionTitle = mViewCompetitionTitle.getText().toString().trim();
                        String competitionStart = mViewCompetitionStart.getText().toString().trim();
                        String competitionEnd = mViewCompetitionEnd.getText().toString().trim();
                        String competitionDes = mViewCompetitionDes.getText().toString().trim();
                        if (TextUtils.isEmpty(competitionTitle)) {
                            GtaToast.toastOne(mContext, "请输入竞赛标题");
                        }
                        if (TextUtils.isEmpty(competitionStart)) {
                            GtaToast.toastOne(mContext, "请输入竞赛开始时间");
                        }
                        if (TextUtils.isEmpty(competitionEnd)) {
                            GtaToast.toastOne(mContext, "请输入竞赛结束时间");
                        }
                        if (TextUtils.isEmpty(competitionDes)) {
                            GtaToast.toastOne(mContext, "请输入竞赛内容");
                        }
                        mOnButtonClickLisener.OnConfirmButtonClick(competitionTitle, competitionStart, competitionEnd, competitionDes);
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
    }

    public interface OnButtonClickLisener {
        void OnConfirmButtonClick(String title, String startTime, String endTime, String des);

        void OnCancleButtonClick();
    }
}
