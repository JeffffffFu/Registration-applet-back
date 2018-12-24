package com.Servlet;

public class User {
	protected  String Openid;
	protected  String User_name;
	protected  String User_url;
	protected String  Match;

   public User(String openid,String user_name,String user_url) {
	   this.Openid=openid;
	   this.User_name=user_name;
	   this.User_url=user_url;
   }

public String getOpenid() {
	return Openid;
}

public void setOpenid(String openid) {
	Openid = openid;
}

public String getUser_name() {
	return User_name;
}

public void setUser_name(String user_name) {
	User_name = user_name;
}

public String getUser_url() {
	return User_url;
}

public void setUser_url(String user_url) {
	User_url = user_url;
}
   
   }
