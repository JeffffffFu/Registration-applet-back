package com.imooc.entity;

public class User {


    protected int id; //判断数据是否存在
    protected String openid;
    protected String userName;
    protected String userUrl;
    protected String match;
    protected String userCountry;
    protected String userProvince;
    protected String userCity;
    protected String userGender;
    protected Match_status matchStatus; //对象


    //无参构造方法
    public User(){}

    //带参构造方法
    public User(int i,String match,String openid,String userName,String userUrl,String userCountry,String userProvince,String userCity,String userGender){
        this.id=i;
        this.openid=openid;
        this.userName=userName;
        this.userUrl=userUrl;
        this.match=match;
        this.userCountry=userCountry;
        this.userProvince=userProvince;
        this.userCity=userCity;
        this.userGender=userGender;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) { this.userName = userName; }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public Match_status getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Match_status matchStatus) {
        this.matchStatus = matchStatus;
    }



}
