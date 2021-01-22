package com.gtafe.competitionstudent;

import android.content.Intent;
import android.view.View;

public class SplashActivity extends BaseActivity {


    public boolean mSetting;

    @Override
    protected void init() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                int i = 0;
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    if (i>10) {

                        break;
                    }
                }

                if (!mSetting) {
                    startActivity(new Intent(mContext,MainActivity.class));
                }

            }
        }.start();
    }

    @Override
    protected int setView() {
        return R.layout.activity_splash;
    }

    public void splashSetting(View view) {
        mSetting = true;
        startActivity(new Intent(mContext,LoginActivity.class));
    }

}