package com.Servlet;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mysql.jdbc.StringUtils;
import com.sun.security.jgss.InquireSecContextPermission;

import net.sf.json.JSONArray;

import net.sf.json.JSONObject;


/**
 * Servlet implementation class WxServlet
 */
@WebServlet("/WxServlet")

//Servlet继承BaseServlet，即可实现多个servlet的方法，实现多个操作
public class MyServlet extends BaseServlet {
  

    /**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	//存储比赛数据
	public void storage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
        // TODO Auto-generated method stub
        
        response.setContentType("text/html;charset=utf-8");          
        /* 设置响应头允许ajax跨域访问 */  
        response.setHeader("Access-Control-Allow-Origin", "*");  
        /* 星号表示所有的异域请求都可以接受， */  
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");  
       
        //获取微信小程序get的参数值并打印
        String match_theme= request.getParameter("match_theme"); 
        System.out.println("match_theme="+match_theme);   
        String match_time = request.getParameter("match_time");  
        String match_week=request.getParameter("match_week");
        String match_address = request.getParameter("match_address");
        String match_rule=request.getParameter("match_rule");
        String match_color=request.getParameter("match_color");
        String match_people=request.getParameter("match_people");
        String match_remarks=request.getParameter("match_remarks");
        String sponsor=request.getParameter("user_name");
        String openid=request.getParameter("openid");
        String longitude1=request.getParameter("longitude");
        String latitude1=request.getParameter("latitude");
        double longitude=Double.valueOf(longitude1);
        double latitude=Double.valueOf(latitude1);
        System.out.println("longitude="+longitude);  
        
         //创建唯一ID，附属给每条数据
        DataProcessing key=new DataProcessing();
        String uuid=key.getId();
        String id=null;
        String match=null;
        Match em=new Match(uuid,match_theme, match_time,match_week, match_address,match_rule,match_color,match_people,match_remarks,sponsor,"","",longitude,latitude);
        Operate add=new Operate();
        add.add_match(em);   //将这条数据存入数据库
        //根据UUid找出比赛的信息
        ResultSet resultSet=new Operate().find_matchInformation(uuid);
        while(resultSet.next()) {
        	id=resultSet.getString("id");  //取出比赛ID
        }
        System.out.println("id="+id);
        ResultSet user_information=new Operate().find_userId(openid);  //根据openid找出用户的信息
        while(user_information.next()) {
        	match=user_information.getString("match");  //取出用户的比赛字段
        }
       //比赛字段中加入比赛ID，然后存进这个openid的用户中
        String new_userMatch=new DataProcessing().increase_string(match, id);   //进行字符串的处理，在原来的字符串后加上新的字符，用re_leave接住
       //在user表中，将比赛的id加入到对应用户的比赛字段中。
        new Operate().update_userMatch(new_userMatch, openid);
        new Operate().add_matchStatus(uuid, openid,"0", match_time);
        //使用Gson类需要导入gson-2.8.0.jar
        String json = new Gson().toJson(em);
        //返回值给微信小程序
        Writer out = response.getWriter(); 
        out.write(json);
        out.flush();  
        
        
    }
	
	//取出比赛数据
	public void take(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		 response.setContentType("text/html;charset=utf-8");          
	        /* 设置响应头允许ajax跨域访问 */  
	        response.setHeader("Access-Control-Allow-Origin", "*");  
	        /* 星号表示所有的异域请求都可以接受， */  
	        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
	        String openid=request.getParameter("openid");
	        System.out.println("取出openid:"+openid);
	        String user_match=null;
	        ResultSet user_information=new Operate().find_userId(openid);
	        while(user_information.next()) {
	        	user_match=user_information.getString("match");
	        }
	       System.out.println("user_match:"+user_match);
             //取出查询到的数据集
	        ResultSet rs=new Operate().inquire_match(user_match,openid);
	        //调用转化方法，将其转化成json数据
	        String json=new DataProcessing().resultSetToJson(rs);
	        System.out.println("打印比赛数据："+json);
	        Writer out = response.getWriter();
            out.write(json);
	        out.flush();        
	}
	
	//插入和更新用户数据
	public   void user_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		 response.setContentType("text/html;charset=utf-8");          
	        /* 设置响应头允许ajax跨域访问 */  
	        response.setHeader("Access-Control-Allow-Origin", "*");  
	        /* 星号表示所有的异域请求都可以接受， */  
	        response.setHeader("Access-Control-Allow-Methods", "GET,POST");

            String appsercet=request.getParameter("appsercet");
            String appid=request.getParameter("appid");
            String Code=request.getParameter("code");
            String grant_type=request.getParameter("grant_type");
            String user_name=request.getParameter("user_name");
            String user_url=request.getParameter("user_url");
            System.out.println("Code:"+Code);
            System.out.println("appsercet:"+appsercet);
            System.out.println("appid:"+appid);
            System.out.println("grant_type:"+grant_type);
            System.out.println("user_name:"+user_name);
            System.out.println("user_url:"+user_url);
            new WeiXinOpenid();
           //向网站请求，调取方法，获取openid
			String openID=WeiXinOpenid.GetOpenID(appid, appsercet, Code);
            //将json格式的数据，取出相应的数值
            JSONObject jsonObject = JSONObject.fromObject(openID);
              String openid= (String) jsonObject.get("openid");
             System.out.println("openid:"+openid);  
             //判断该openid是否已经存在,如果存在则更新姓名和头像，如果不存在则插入新记录
             ResultSet user_imformation=new Operate().inquire_openid(openid);
             if(user_imformation.next()) {                   //判断数据集是否有值,如果有则更新，没有就添加
            	 User user=new User(openid,user_name,user_url);
            	 new Operate().update_user(user);
             }else {
            	    User user=new User(openid,user_name,user_url);
                    System.out.println("user:"+user);
                     new Operate().add_user(user);
             }
            //将值处理为json数据，返回值给微信小程序
            String json = new Gson().toJson(openID);
            Writer out = response.getWriter();
            out.write(openid);
            out.flush();
	        
	}
	
     //用户点击报名，思路为：比赛表的报名字段中加一个ID，请假字段中减去相应ID.取出这场比赛中的所有报名人的姓名和头像，传回给微信小程序
	public void sign_up(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		 response.setContentType("text/html;charset=utf-8");          
	        /* 设置响应头允许ajax跨域访问 */  
	        response.setHeader("Access-Control-Allow-Origin", "*");  
	        /* 星号表示所有的异域请求都可以接受， */  
	        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
	        String id_match=null;     //存储需要加入的比赛ID
	        String user_match=null; //存储从数据库中取出的对应用户的比赛字段
	        String id=null;
	        String status="1";
	        String join=null;   //从报名字段中取出的字符串存入其中
	        String leave=null;  //从情节字段中取出的字符串存入其中
	        String openid=request.getParameter("openid");
	        String uuid=request.getParameter("uuid");
	        String time=request.getParameter("time");
	        System.out.println("openid:"+openid);
	        System.out.println("uuid:"+uuid);	        
	        ResultSet userId=new Operate().find_userId(openid);  //根据openid找出用户的id
	        while(userId.next()) {
              id=userId.getString("id");       //获取新的报名人
              user_match=userId.getString("match");  //获取已存在的比赛字段
	        }    
	        ResultSet matchId=new Operate().find_matchInformation(uuid);
	        while(matchId.next()) {
	             id_match=matchId.getString("id");       //获取比赛id
		        } 
	        //将需要加入的比赛ID加入到取出的字段中
	        String new_userMatch=new DataProcessing().increase_string(user_match, id_match);   //进行字符串的处理，在原来的字符串后加上新的字符，用re_leave接住
	       //在user表中，将比赛的id加入到对应用户的比赛字段中。
	        new Operate().update_userMatch(new_userMatch, openid);
	         //找出对应uuid的比赛的报名字段下的字符串。将得到的用户id加入到match表的报名中，并将请假字段中该用户去除
	        ResultSet user_join=new Operate().find_matchInformation(uuid);
	        while(user_join.next()) {
	        	join=user_join.getString("user_join");       //取出数据集中，报名的人
	        	leave=user_join.getString("user_leave");    //取出数据集中，请假的人
	        }	    
	        String rs_join=new DataProcessing().increase_string(join, id);   //进行字符串的处理，在原来的字符串后加上新的字符，用re_join接住
	        String rs_leave=new DataProcessing().reduce_string(leave, id) ; //进行字符串的处理，在原来的字符串中减去某个字符，用re_leave接住
	        //在match表中，将这场比赛新的报名字段字符串和请假字段字符串，替换原来的报名和请假。
	        new Operate().update_join(uuid,rs_join,rs_leave);
	        //取出这场比赛中的所有报名人和请假人的姓名和头像，进行处理后，传回给微信小程序
	        ResultSet register_information=new Operate().register_information(rs_join);
	        ResultSet leave_information=new Operate().leave_information(rs_leave);
	        String json_register=new DataProcessing().resultSetToJson(register_information);
	        String json_leave=new DataProcessing().resultSetToJson(leave_information);
	         //获取比赛状态，如果没有这场则插入并赋予其报名状态，如果有则将其转态变为更新状态
	        ResultSet match_status=new Operate().inquire_matchStatus(uuid, openid);
            if(match_status.next()) {                   //判断数据集是否有值,如果有则更新，没有就添加
           	 new Operate().update_matchStatus(uuid, openid,status);
            }else {
                    new Operate().add_matchStatus(uuid, openid,status,time);
            }
	       //进行两个json数组的合并嵌套，需要JSONObject对象进行合并嵌套
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("json_register", json_register);
	        jsonObject.put("json_leave", json_leave);
	        String json = new Gson().toJson(jsonObject);  //转化成json数组
	        //将数据传回小程序
            Writer out=response.getWriter();
            out.write(json);
            out.flush();
	        
	}
	
    //用户点击请假，思路为：比赛表的报名字段中减一个ID，请假字段中加上相应ID，取出这场比赛中的所有报名人的姓名和头像，传回给微信小程序
	public void leave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		 response.setContentType("text/html;charset=utf-8");          
	        /* 设置响应头允许ajax跨域访问 */  
	        response.setHeader("Access-Control-Allow-Origin", "*");  
	        /* 星号表示所有的异域请求都可以接受， */  
	        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
	        String status="2";
	        String id=null;            //存储用户的id
	        String id_match=null;     //存储需要加入的比赛ID
	        String user_match=null; //存储从数据库中取出的对应用户的比赛字段
	        String join=null;   //从报名字段中取出的字符串存入其中
	        String leave=null;  //从情节字段中取出的字符串存入其中
	        String openid=request.getParameter("openid");
	        String uuid=request.getParameter("uuid");
	        String time=request.getParameter("time");
	        System.out.println("openid:"+openid);
	        System.out.println("uuid:"+uuid);	        
	        ResultSet userId=new Operate().find_userId(openid);  //根据openid找出用户的信息
	        while(userId.next()) {
             id=userId.getString("id");       //获取新的请假人id
             user_match=userId.getString("match");  //获取已存在的比赛字段
	        }    
	        ResultSet matchId=new Operate().find_matchInformation(uuid);
	        while(matchId.next()) {
	             id_match=matchId.getString("id");       //获取比赛id
		        } 
	         //将需要加入的比赛ID加入到取出的字段中
	        String new_userMatch=new DataProcessing().increase_string(user_match, id_match);   //进行字符串的处理，在原来的字符串后加上新的字符，用re_leave接住
	       //在user表中，将比赛的id加入到对应用户的比赛字段中。
	        new Operate().update_userMatch(new_userMatch, openid);
	        //找出对应uuid的比赛的报名字段下的字符串。将得到的用户id加入到match表的报名中，并将请假字段中该用户去除
	        ResultSet user_join=new Operate().find_matchInformation(uuid);
	        while(user_join.next()) {
	        	join=user_join.getString("user_join");       //取出数据集中，报名的人
	        	leave=user_join.getString("user_leave");    //取出数据集中，请假的人
	        }	    
	        System.out.println("user_join:"+join);
	        System.out.println("user_leave:"+leave);
	        String rs_leave=new DataProcessing().increase_string(leave, id);   //进行字符串的处理，在原来的字符串后加上新的字符，用re_leave接住
	        String rs_join=new DataProcessing().reduce_string(join, id); //进行字符串的处理，在原来的字符串中减去某个字符，用re_join接住
	        //在match表中，将这场比赛新的报名字段字符串和请假字段字符串，替换原来的报名和请假。
	        new Operate().update_join(uuid,rs_join,rs_leave);
	        //取出这场比赛中的所有报名人和请假人的姓名和头像，进行处理后，传回给微信小程序
	        ResultSet register_information=new Operate().register_information(rs_join);
	        ResultSet leave_information=new Operate().leave_information(rs_leave);
	        String json_register=new DataProcessing().resultSetToJson(register_information);
	        String json_leave=new DataProcessing().resultSetToJson(leave_information);
	         //获取比赛状态，如果没有这场则插入并赋予其报名状态，如果有则将其转态变为更新状态
	        ResultSet match_status=new Operate().inquire_matchStatus(uuid, openid);
           if(match_status.next()) {                   //判断数据集是否有值,如果有则更新，没有就添加
          	 new Operate().update_matchStatus(uuid, openid,status);
           }else {
                   new Operate().add_matchStatus(uuid, openid, status,time);
           }
	        //进行两个json数组的合并嵌套，需要JSONObject对象进行合并嵌套
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("json_register", json_register);
	        jsonObject.put("json_leave", json_leave);
	        String json = new Gson().toJson(jsonObject);//转化成json数组
	        //将数据传回小程序
            Writer out=response.getWriter();
            out.write(json);
            out.flush();
	        
	}
	
	//根据赛事的uuid取出对应比赛的信息
	public void take_basisUuid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		 response.setContentType("text/html;charset=utf-8");          
	        /* 设置响应头允许ajax跨域访问 */  
	        response.setHeader("Access-Control-Allow-Origin", "*");  
	        /* 星号表示所有的异域请求都可以接受， */  
	        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
	        String uuid=request.getParameter("uuid");
	        String openid=request.getParameter("openid");
	        String join=null;   //从报名字段中取出的字符串存入其中
	        String leave=null;  //从情节字段中取出的字符串存入其中
	        String id=null;
	        double longitude=0;
	        double latitude=0;
	        System.out.println("uuid:"+uuid);
	        System.out.println("openid:"+openid);
             //取出查询到的数据集
	        ResultSet user_id=new Operate().find_userId(openid);//根据openid用户的id
	        while(user_id.next()) {
	        	id=user_id.getString("id");
	        }
	        ResultSet result=new Operate().find_matchInformation(uuid);  //根据uuid查出对应比赛信息      
	        //取出数据集中，报名和请假的人
	        while(result.next()) {
	        	join=result.getString("user_join");  
	        	leave=result.getString("user_leave"); 
	        }
	         //判断该用户ID是否已经报名或请假，存在则返回true，否则为false
	        Boolean disable_join=new DataProcessing().judge_string(join, id);
	        Boolean disable_leave=new DataProcessing().judge_string(leave, id);
	        //取出这场比赛中的所有报名人和请假人的姓名和头像，进行处理后，传回给微信小程序
	        ResultSet register_information=new Operate().register_information(join);
	        ResultSet leave_information=new Operate().leave_information(leave);
	        String json_register=new DataProcessing().resultSetToJson(register_information);
	        String json_leave=new DataProcessing().resultSetToJson(leave_information);
	        //因为上面.next过了，可能导致这个数据集中的数据消失了，他们两段代码位置调换也会导致一侧的数据没有。所以只能重新去数据库查询了一遍。
	        ResultSet rs=new Operate().find_matchStatus(uuid, openid); //根据uuid和openid拼接出含有比赛状态的新表    
	        while(rs.next()) {
	        	longitude=rs.getDouble("longitude");  //因为经纬度是double类型 所以需要单独取出来 再拼接进去
	        	latitude=rs.getDouble("latitude");
	        }
	        ResultSet re=new Operate().find_matchStatus(uuid, openid); 
	        String match_information=new DataProcessing().resultSetToJson(re);
	       //进行三个json数组的合并嵌套，需要JSONObject对象进行合并嵌套
	        JSONObject jsonObject = new JSONObject();	  
	        jsonObject.put("json_register", json_register);
	        jsonObject.put("json_leave", json_leave);
	        jsonObject.put("match_information", match_information);
	        jsonObject.put("disable_join", disable_join);
	        jsonObject.put("disable_leave", disable_leave);
	        jsonObject.put("longitude", longitude);
	        jsonObject.put("latitude", latitude);
	        String json = new Gson().toJson(jsonObject);   //将JSON对象转化成json数组
            //将打包好的数据传回给小程序
	        Writer out = response.getWriter();
            out.write(json);
	        out.flush();        
	}

	//更改比赛数据
	public void edit_match(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
        // TODO Auto-generated method stub
        
        response.setContentType("text/html;charset=utf-8");          
        /* 设置响应头允许ajax跨域访问 */  
        response.setHeader("Access-Control-Allow-Origin", "*");  
        /* 星号表示所有的异域请求都可以接受， */  
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");  
       
        //获取微信小程序get的参数值并打印
        String theme= request.getParameter("match_theme"); 
        System.out.println("更改比赛数据match_theme="+theme);   
        String time = request.getParameter("match_time");  
        String week=request.getParameter("match_week");
        String address = request.getParameter("match_address");
        String rule=request.getParameter("match_rule");
        String color=request.getParameter("match_color");
        String remarks=request.getParameter("match_remarks");
        String openid=request.getParameter("openid");
        String uuid=request.getParameter("uuid");
        System.out.println("更改比赛数据uuid="+uuid);
        System.out.println("更改比赛数据openid="+openid);
        String longitude1=request.getParameter("longitude");
        String latitude1=request.getParameter("latitude");
        System.out.println("更改比赛数据longitude="+longitude1);
        System.out.println("更改比赛数据latitude="+latitude1);
        double longitude=Double.valueOf(longitude1);
        double latitude=Double.valueOf(latitude1);
        System.out.println("longitude="+longitude);  
         int result=new Operate().edit_match(theme, time, week, address, longitude, latitude, rule, color, remarks, uuid);
         int result2=new Operate().edit_matchStatus(uuid, openid, time);
        //返回值给微信小程序
        Writer out = response.getWriter(); 
        out.write("更改成功");
        out.flush();  
        
        
    }

	//删除比赛数据
	public void delete_match(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
        // TODO Auto-generated method stub
        
        response.setContentType("text/html;charset=utf-8");          
        /* 设置响应头允许ajax跨域访问 */  
        response.setHeader("Access-Control-Allow-Origin", "*");  
        /* 星号表示所有的异域请求都可以接受， */  
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");  
       
        //获取微信小程序get的参数值
        String openid=request.getParameter("openid");
        String uuid=request.getParameter("uuid");
        System.out.println("删除比赛数据uuid="+uuid);
        System.out.println("删除比赛数据openid="+openid);
         Boolean delete=new Operate().delete_match(uuid,openid);
        
        //返回值给微信小程序
        Writer out = response.getWriter(); 
        out.write("更改成功");
        out.flush();  
        
        
    }
}
	
  
    
	

