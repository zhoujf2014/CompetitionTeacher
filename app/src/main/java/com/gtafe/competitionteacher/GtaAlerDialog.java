package com.gtafe.competitionteacher;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhouJF on 2020-09-28.
 */
public class GtaAlerDialog extends AlertDialog {

    public final ConnectDialogHolder mConnectDialogHolder;

    OnButtonClickLisener mOnButtonClickLisener;

    public GtaAlerDialog(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.view_comfir, null);
        setView(view);
        mConnectDialogHolder = new ConnectDialogHolder(view);
    }

    public void setTitle(Drawable icon, String title) {
        mConnectDialogHolder.setTitle(icon,title);
    }

    public void setMsg(String msg) {
        mConnectDialogHolder.setMsg(msg);
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
        @BindView(R.id.view_comfir_title)
        TextView mDialogConnectTitle;
        @BindView(R.id.view_comfir_msg)
        TextView mDialogConnectMsg;
        AlertDialog alertDialog;

        ConnectDialogHolder(View view) {
            ButterKnife.bind(this, view);

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
                        mOnButtonClickLisener.OnConfirmButtonClick();
                    }
                    cancel();
                    break;
            }
        }

        public void setTitle(Drawable icon, String title) {
            mDialogConnectTitle.setText(title);
            if (icon!=null) {
                icon.setBounds(0, 0, 120,120);
                mDialogConnectTitle.setCompoundDrawables(icon,null,null,null);
            }

        }

        public void setMsg(String msg) {
            mDialogConnectMsg.setText(msg);

            mDialogConnectMsg.setGravity(Gravity.CENTER);

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
        void OnConfirmButtonClick();

        void OnCancleButtonClick();
    }
}
