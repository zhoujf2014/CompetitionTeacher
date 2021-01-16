package com.gtafe.competitionteacher;

/**
 * Created by ZhouJF on 2021-01-15.
 */

class ManageDataBean {
    public String id;
    public int CMD;
    public String MSG;
    public String centerControlNum;
    public TestBean mTestBean;
    public static int state_hand;
    public static int state_power;
    public static int state_connet;
    public static int state_control;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCMD() {
        return CMD;
    }

    public void setCMD(int CMD) {
        this.CMD = CMD;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getCenterControlNum() {
        return centerControlNum;
    }

    public void setCenterControlNum(String centerControlNum) {
        this.centerControlNum = centerControlNum;
    }

    public TestBean getTestBean() {
        return mTestBean;
    }

    public void setTestBean(TestBean testBean) {
        mTestBean = testBean;
    }

    public static int getState_hand() {
        return state_hand;
    }

    public static void setState_hand(int state_hand) {
        ManageDataBean.state_hand = state_hand;
    }

    public static int getState_power() {
        return state_power;
    }

    public static void setState_power(int state_power) {
        ManageDataBean.state_power = state_power;
    }

    public static int getState_connet() {
        return state_connet;
    }

    public static void setState_connet(int state_connet) {
        ManageDataBean.state_connet = state_connet;
    }

    public static int getState_control() {
        return state_control;
    }

    public static void setState_control(int state_control) {
        ManageDataBean.state_control = state_control;
    }

    class TestBean{
        private String Title;
        private String time_start;
        private String time_stop;
        private String test;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getTime_stop() {
            return time_stop;
        }

        public void setTime_stop(String time_stop) {
            this.time_stop = time_stop;
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }
}
