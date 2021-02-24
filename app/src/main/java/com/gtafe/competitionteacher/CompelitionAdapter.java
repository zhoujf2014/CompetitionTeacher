package com.gtafe.competitionteacher;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionlib.SharePrefrenceUtils;
import com.gtafe.competitionlib.utils.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.JUSHOU;
import static com.gtafe.competitionlib.ManageDataBean.EMU_CMD.YONGDIAN;
import static com.gtafe.competitionteacher.MainActivity.mIsSetting;
import static com.gtafe.competitionteacher.TeacherApplication.sManageDataBeans;

/**
 * Created by ZhouJF on 2021/1/15.
 */
public class CompelitionAdapter extends RecyclerView.Adapter {
    private List<ManageDataBean> mManageDataBeans;
    private MainActivity mMainActivity;

    public CompelitionAdapter(MainActivity context) {
        mMainActivity = context;
    }

    public void setData(List<ManageDataBean> manageDataBeans) {
        this.mManageDataBeans = manageDataBeans;

        Collections.sort(manageDataBeans, new Comparator<ManageDataBean>() {
            @Override
            public int compare(ManageDataBean o1, ManageDataBean o2) {

                return o1.getBianhao().compareTo(o2.getBianhao());

            }
        });
        SharePrefrenceUtils.putString(mMainActivity, Constant.MANAGEDATA, new Gson().toJson(manageDataBeans));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mMainActivity, R.layout.item_compelition, null);

        return new CompelitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CompelitionViewHolder compelitionViewHolder = (CompelitionViewHolder) holder;
        compelitionViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        if (mManageDataBeans == null) {
            return 0;
        }
        return mManageDataBeans.size();
    }


    class CompelitionViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        @BindView(R.id.item_name)
        TextView mItemName;
        @BindView(R.id.item_hand)
        ImageView mItemHand;
        @BindView(R.id.item_power)
        ImageView mItemPower;
        @BindView(R.id.item_state)
        TextView mItemState;
        @BindView(R.id.item_time)
        TextView mItemTime;
        View mView ;
        public ManageDataBean mManageDataBean;


        public CompelitionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            mView= itemView;
        }

        @OnClick({R.id.item_hand})
        public void onClicked(View view) {
            switch (view.getId()) {
                case R.id.item_hand:

                    GtaAlerDialog gtaAlerDialog = new GtaAlerDialog(mMainActivity);
                    gtaAlerDialog.setTitle(null,"工位");
                    gtaAlerDialog.setMsg(mManageDataBean.getBianhao() + "\n举手提问");
                    gtaAlerDialog.setButtonConfir("已知悉");

                    gtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                        @Override
                        public void OnConfirmButtonClick() {
                            ManageDataBean manageDataBean = new ManageDataBean();
                            manageDataBean.CMD = JUSHOU;
                            manageDataBean.SN = mManageDataBean.SN;
                            manageDataBean.setState_hand(0);
                            mMainActivity.sendDataToClient(manageDataBean.SN, manageDataBean);
                            mManageDataBean.setState_hand(0);
                            mItemHand.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void OnCancleButtonClick() {

                        }
                    });
                    gtaAlerDialog.show();
                    break;
            }

        }


        public void setData(int position) {
            mManageDataBean = mManageDataBeans.get(position);
            mItemName.setText(mManageDataBean.getBianhao());
            mItemHand.setVisibility(mManageDataBean.getState_hand() == 1 ? View.VISIBLE : View.INVISIBLE);
            mItemPower.setVisibility(mManageDataBean.getState_power() == 1 ? View.VISIBLE : View.INVISIBLE);
            switch (mManageDataBean.getState_connet()) {
                case 0:
                    mItemState.setText("未连接");

                    mItemName.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_unconnect));
                    mItemState.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_unconnect));
                    mItemTime.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_unconnect));
                    break;
                case 1:
                    mItemState.setText("已连接");
                    mItemName.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_connect));
                    mItemState.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_connect));
                    mItemTime.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_connect));
                    break;
                case 2:
                    mItemState.setText("训练中");
                    mItemName.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_testing));
                    mItemState.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_testing));
                    mItemTime.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_testing));
                    break;
                case 3:
                    mItemState.setText("训练结束");
                    mItemName.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_testend));
                    mItemState.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_testend));
                    mItemTime.setTextColor(mMainActivity.getResources().getColor(R.color.textcolor_testend));
                    break;
                case 4:
                    break;
            }
            mItemTime.setText(Util.getFormatCountDown(mManageDataBean.getTime()));
        }

        @Override
        public boolean onLongClick(View view) {
            if (mIsSetting) {

                GtaAlerDialog gtaAlerDialog = new GtaAlerDialog(mMainActivity);
                gtaAlerDialog.setButtonCancle("取消");
                gtaAlerDialog.setTitle(null, "删除设备");

                gtaAlerDialog.setMsg("是否删除设备？\n设备SN:" + mManageDataBean.SN + "\n" + "设备编号：" + mManageDataBean.getBianhao());
                gtaAlerDialog.setButtonConfir("确定");
                gtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                    @Override
                    public void OnConfirmButtonClick() {

                        sManageDataBeans.remove(mManageDataBean);
                        SharePrefrenceUtils.putString(mMainActivity, Constant.MANAGEDATA, new Gson().toJson(sManageDataBeans));

                        notifyDataSetChanged();
                    }

                    @Override
                    public void OnCancleButtonClick() {

                    }
                });
                gtaAlerDialog.show();
            }

            return false;
        }

        @Override
        public void onClick(View view) {
            GtaAlerDialog gtaAlerDialog = new GtaAlerDialog(mMainActivity);
            gtaAlerDialog.setButtonCancle("取消");
            gtaAlerDialog.setTitle(null, "电源控制");

            gtaAlerDialog.setMsg((mManageDataBean.getState_power() == 1 ? "是否关闭设备电源？" : "是否打开设备电源") + "\n设备SN:" + mManageDataBean.SN + "\n" + "设备编号：" + mManageDataBean.getBianhao());
            gtaAlerDialog.setButtonConfir("确定");
            gtaAlerDialog.setOnclikLisener(new GtaAlerDialog.OnButtonClickLisener() {
                @Override
                public void OnConfirmButtonClick() {
                    ManageDataBean manageDataBean = new ManageDataBean();
                    manageDataBean.CMD = YONGDIAN;
                    manageDataBean.SN =mManageDataBean.SN;
                    manageDataBean.setState_power(mManageDataBean.getState_power()^1);
                    mMainActivity.sendDataToClient(mManageDataBean.SN, manageDataBean);
                }

                @Override
                public void OnCancleButtonClick() {

                }
            });
            gtaAlerDialog.show();
        }
    }
}
