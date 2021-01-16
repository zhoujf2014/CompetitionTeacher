package com.gtafe.competitionteacher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZhouJF on 2021/1/15.
 */
public class CompelitionAdapter extends RecyclerView.Adapter {
    private static List<ManageDataBean> mManageDataBeans;
    private Context mContext;

    public CompelitionAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ManageDataBean> manageDataBeans) {
        this.mManageDataBeans = manageDataBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_compelition, null);

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


    static class CompelitionViewHolder extends RecyclerView.ViewHolder {

        public CompelitionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, view);

        }

        public void setData(int position) {
            ManageDataBean manageDataBean = mManageDataBeans.get(position);
        }
    }
}
