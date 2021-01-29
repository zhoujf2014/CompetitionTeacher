package com.gtafe.competitionlib;

/**
 * Created by ZhouJF on 2021-01-15.
 */

public class ManageDataBean {

    public enum EMU_CMD {
        HERAT, CONTROL, BIANHAO,JUSHOU,YONGDIAN,CHANGEMODE,REQUEST,
    }

    public enum EMU_MODE {
        COMPELITE, STUDY, TEST
    }

    private String id;
    public int heartCount;
    public EMU_CMD CMD;
    public EMU_MODE MODE;
    public String SN ;
    private String MSG;
    private String centerControlNum;
    private String bianhao;
    private TestBean mTestBean;
    private int state_hand;
    private int state_power;
    private int state_connet;
    private int state_control;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public TestBean getTestBean() {
        return mTestBean;
    }

    public void setTestBean(TestBean testBean) {
        mTestBean = testBean;
    }

    public int getState_hand() {
        return state_hand;
    }

    public void setState_hand(int state_hand) {
        this.state_hand = state_hand;
    }

    public int getState_power() {
        return state_power;
    }

    public void setState_power(int state_power) {
        this.state_power = state_power;
    }

    public int getState_connet() {
        return state_connet;
    }

    public void setState_connet(int state_connet) {
        this.state_connet = state_connet;
    }

    public int getState_control() {
        return state_control;
    }

    public void setState_control(int state_control) {
        this.state_control = state_control;
    }

    public static class TestBean {

        private String Title;
        private String time_start;
        private String time_stop;
        private String des;
        private int state = 1;//1:新建，2：修改，3：删除，4：等待开始，5：正在考试，6：考试结束
       // private int state = 1;//1:新建，2：修改，3：删除，4：等待开始，5：正在考试，6：考试结束

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public TestBean(String title, String time_start, String time_stop, String des) {
            Title = title;
            this.time_start = time_start;
            this.time_stop = time_stop;
            this.des = des;
        }

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

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}
