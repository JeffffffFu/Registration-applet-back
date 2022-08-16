package com.imooc.accessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
@Component
//继承ApplicationRunner ，项目启动则自动跑下面的方法
public class GetToken implements ApplicationRunner {
    //从applications.properties中配置获取
//    @Value("${jdbc.appsercet}")
//    private String appsercet;
//    @Value("${jdbc.appid}")
//    private String appid;
    private String appsercet = "22f8bb4e86c7ee6a7dfecb881fa5fda0";;
    private String appid="wxbd8c3059b444fd81";
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("开始自动跑方法获取token----");
        // 获取applications.properties中配置的参数
        tokenThread.appid= appid;
        tokenThread.appsecret = appsercet;
        // 未配置appid、appsecret时给出提示
        if ("".equals(tokenThread.appid) || "".equals(tokenThread.appsecret)) {
            System.out.println("appid and appsecret configuration error, please check carefully.");
        } else {
            new Thread(new tokenThread()).start(); // 启动定时获取access_token的线程
        }
    }
}
