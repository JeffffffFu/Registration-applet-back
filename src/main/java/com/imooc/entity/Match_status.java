package com.imooc.entity;

public class Match_status {

    public int statusId;
    public String statusUuid;
    public String matchStatus;
    public String matchTime;
    public String clickTime;
    public String openid;

    //无参构造方法
    public Match_status(){}

    //带参构造方法
    public Match_status(String statusUuid,String openid,String matchStatus,String matchTime,String clickTime){
        this.statusUuid=statusUuid;
        this.openid=openid;
        this.matchStatus=matchStatus;
        this.matchTime=matchTime;
        this.clickTime=clickTime;

    }
    public String getStatusUuid() {
        return statusUuid;
    }

    public void setStatusUuid(String statusUuid) {
        this.statusUuid = statusUuid;
    }
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }






}
