package com.imooc.entity;

public class Match {
    protected int id;//主鍵
    protected String Uuid; //唯一编码
    protected String Theme;     //比赛主题
    protected String Address; //比赛地址
    protected String Time;       //比赛时间
    protected String Week;       //比赛星期
    protected String deadlineTime;  //报名截止时间
    protected String deadlineWeek;   //报名截止星期
    protected String Rule;       //比赛赛制
    protected String People;        //限制人数
    protected String  Directions;      //衣服颜色
    protected String sponsorOpenid; //发起人openid
    protected String UserJoin;      //报名参加的人
    protected String UserLeave;      //请假的人
    protected double Longitude;        //经度
    protected double Latitude;        //纬度
    protected Match_status matchStatus; //对象

    //无参构造
    public Match(){}

    //带参构造方法
    public Match(String Uuid,String Theme,String Address,String Time,String Week,String Deadline_Time,String Deadline_Week
            ,String Rule,String People,String  Directions,String Sponsor_openid,String User_join,String User_leave
            ,double Longitude,double Latitude){
        this.Uuid=Uuid;
        this.Theme=Theme;
        this.Address=Address;
        this.Time=Time;
        this.Week=Week;
        this.deadlineTime=Deadline_Time;
        this.deadlineWeek=Deadline_Week;
        this.Rule=Rule;
        this.People=People;
        this.Directions=Directions;
        this.sponsorOpenid=Sponsor_openid;
        this.UserJoin=User_join;
        this.UserLeave=User_leave;
        this.Latitude=Latitude;
        this.Longitude=Longitude;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getWeek() {
        return Week;
    }

    public void setWeek(String week) {
        Week = week;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(String deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    public String getRule() {
        return Rule;
    }

    public void setRule(String rule) {
        Rule = rule;
    }

    public String getPeople() {
        return People;
    }

    public void setPeople(String people) {
        People = people;
    }

    public String getDirections() {
        return Directions;
    }

    public void setDirections(String directions) {
        Directions = directions;
    }

    public String getSponsorOpenid() {
        return sponsorOpenid;
    }

    public void setSponsorOpenid(String sponsorOpenid) {
        this.sponsorOpenid = sponsorOpenid;
    }

    public String getUserJoin() {
        return UserJoin;
    }

    public void setUserJoin(String userJoin) {
        UserJoin = userJoin;
    }

    public String getUserLeave() {
        return UserLeave;
    }

    public void setUserLeave(String userLeave) {
        UserLeave = userLeave;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public Match_status getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Match_status matchStatus) {
        this.matchStatus = matchStatus;
    }




}
