package com.gtafe.competitionteacher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bt_study)
    Button mBtStudy;
    @BindView(R.id.bt_train)
    Button mBtTrain;
    @BindView(R.id.bt_competition)
    Button mBtCompetition;
    @BindView(R.id.tv_ip)
    TextView mTvIp;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;



    @Override
    protected void init() {
        CompelitionAdapter compelitionAdapter = new CompelitionAdapter(mContext);
        mRvList.setLayoutManager(new GridLayoutManager(mContext,6));
        mRvList.setAdapter(compelitionAdapter);
    }

    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.bt_study, R.id.bt_train, R.id.bt_competition})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_study:
                break;
            case R.id.bt_train:
                break;
            case R.id.bt_competition:
                break;
        }
    }
}