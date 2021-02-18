package com.gtafe.competitionlib;

/**
 * Created by ZhouJF on 2021-01-15.
 */

public class ManageDataBean {

    public enum EMU_CMD {
        HERAT, CONTROL, BIANHAO,JUSHOU,YONGDIAN,CHANGEMODE,REQUEST,COMPlETE, STARTCOMPETITION,TIMEALERT,TESTDATA
    }

    public enum EMU_MODE {
        COMPETITION, STUDY, TEST
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
    private int state_connet;//0未连接，1：已连接，2：正在竞赛，3：已完成考核
    private int state_control;
    public long time;

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

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
        private long time_start;
        private long time_stop;
        private String des;
        private int state = 1;//1:新建，2：修改，3：删除，4：等待开始，5：正在考试，6：考试结束
       // private int state = 1;//1:新建，2：修改，3：删除，4：等待开始，5：正在考试，6：考试结束6：考试结束

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public TestBean(String title, long time_start, long time_stop, String des) {
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

        public long getTime_start() {
            return time_start;
        }

        public void setTime_start(long time_start) {
            this.time_start = time_start;
        }

        public long getTime_stop() {
            return time_stop;
        }

        public void setTime_stop(long time_stop) {
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
