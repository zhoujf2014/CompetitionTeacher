package com.gtafe.competitionteacher;

import com.gtafe.competitionlib.ManageDataBean;

/**
 * Created by ZhouJF on 2021-01-14.
 */
interface DataChangeInterFace {
    void onConnectStateChange(boolean b);

    void onAddnewBianhao(ManageDataBean manageDataBean);
    void onReceivDataFromServer(ManageDataBean manageDataBean);

    void notifyData();
}
