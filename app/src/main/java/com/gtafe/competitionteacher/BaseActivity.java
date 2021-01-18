package com.gtafe.competitionteacher;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;


/**
 * Created by ZhouJF on 2018/4/7.
 */

public abstract class BaseActivity extends AppCompatActivity implements DataChangeInterFace {
    protected static final String TAG = "BaseActivity";
    public Context mContext;
    public Activity mActivity;
    public boolean isDebug;
    public AppService mAppService;

    protected boolean isShow;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            switch (msg.what) {


            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        init();


    }

    protected abstract void init();


    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        isShow = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
        Intent intent = new Intent(mContext, AppService.class);
        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mAppService = ((AppService.LocalBinder) iBinder).getService();
                mAppService.addDataChangeInterFace(BaseActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent,conn,0);
    }

    protected void showLog(String TAG, String msg) {
        if (isDebug) {
            Log.e("APPNAME:assetsmanage " + TAG, msg);
        }
    }

    protected void showToast(String msg) {
        GtaToast.toastOne(mContext, msg);
    }

    protected abstract int setView();

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     */
    protected void initState() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

          /*  //透明导航栏---虚拟键冲突
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            mBaseBinding.llBar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBaseBinding.llBar.getLayoutParams();
            params.height = statusHeight;
            mBaseBinding.llBar.setLayoutParams(params);*/
        }
    }

    //把actionBar的文字标题居中
    public void setActionBarTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
       /* supportActionBar.setDisplayShowTitleEnabled(false);
// 显示自定义视图
        supportActionBar.setDisplayShowCustomEnabled(true);
//此处自定义了一个actionbar为了让标题居中显示
        actionbarLayout = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        supportActionBar.setCustomView(
                actionbarLayout,
                new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        ActionBar.LayoutParams mP = (ActionBar.LayoutParams) actionbarLayout
                .getLayoutParams();
        mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
                | Gravity.CENTER_HORIZONTAL;

        supportActionBar.setCustomView(actionbarLayout, mP);
        TextView titleText = actionbarLayout.findViewById(R.id.anctionbar_title);
        titleText.setText(title);*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectStateChange(boolean b) {

    }
}
