package com.gtafe.competitionstudent;

import com.gtafe.competitionlib.ManageDataBean;
import com.gtafe.competitionstudent.serialport.CMD_MSG_BACK;

/**
 * Created by ZhouJF on 2021-01-14.
 */
interface DataChangeInterFace {
    void onConnectStateChange(boolean b);

    void onReceivDataFromServer(ManageDataBean userDataBean);

    void onReceivDataFromSerial(CMD_MSG_BACK obj);
}
