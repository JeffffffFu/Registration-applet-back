package com.imooc.accessToken;

import net.sf.json.JSONObject;

public class tokenThread implements Runnable {

    // 第三方用户唯一凭证
    public static String appid = "";
    // 第三方用户唯一凭证密钥
    public static String appsecret = "";
    public static String json_accessToken = "";
    public static String accessToken="";
    public static String expiresIn="";

    public void run() {
        while (true) {
            try {
                json_accessToken = new CommonUtil().getToken(appid, appsecret);
                JSONObject jsonObject=new JSONObject().fromObject(json_accessToken);
                accessToken=jsonObject.getString("access_token");
                expiresIn=jsonObject.getString("expires_in");
                if ( accessToken!="") {
                    //调用存储到数据库
                    System.out.println("获取access_token成功，有效时长: "+expiresIn);
                    System.out.println("access_token值: "+accessToken);
                    // 休眠7000秒
                    Thread.sleep(7000*1000);
                } else {
                    // 如果access_token为null，60秒后再获取
                    Thread.sleep(60 * 1000);
                    System.out.println("再次获取");
                }
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e1) {

                }

            }
        }
    }

}