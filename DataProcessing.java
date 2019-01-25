package com.Servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.mysql.jdbc.ResultSetMetaData;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class DataProcessing {
	
	//增加字符串数据，加上一个字符，以逗号分隔
	public String increase_string(String allUser,String user){   //传入两个参数，一个是本身原来的字符串，另一个是要加入的
		if(allUser == null || allUser.isEmpty()) {    //先判断原来的字符串是不是为空
			return user;
		}else {
	        List<String> test = new ArrayList<String>();  //需要先建一个空的new Array
	        test.addAll(Arrays.asList(allUser.split(",")));  //再用asList分解这个字符串，new Array将这个分解后的字符全部添加进去
	        if(test.contains(user)) {
	        	return allUser;                //判断是否存在该字符串，如果存在不添加
	        }else
	        for(int i=0;i<test.size();i++){
	      	 System.out.println(test.get(i));              //遍历test
	           }
	       test.add(user);                                 //增加这个字符
	       String increase_result = String.join(",", test);
	       return increase_result;
		}
	}
		
	//减少字符串数据，减去一个字符，以逗号分隔
	public String reduce_string(String allUser,String user){  //传入两个参数，一个是本身原来的字符串，另一个是要减去的
		if(allUser == null || allUser.isEmpty()) 
		{	
			return allUser;
        }else {
            List<String> test = new ArrayList<String>();    //需要先建一个空的new Array
            test.addAll(Arrays.asList(allUser.split(",")));    //再用asList分解这个字符串，new Array将这个分解后的字符全部添加进去
            for(int i=0;i<test.size();i++){
               if(test.get(i).equals(user)) {
              	 test.remove(i);    //遍历test,如果存在user,则将他移除
              	 i--;              
               }                        
          }
                                    //去除掉这个字符，即这个人
            String increase_result = String.join(",", test);
            return increase_result;
    	   }
	}
	
	//生成随机数
	 public  String getId(){
         UUID uuid = UUID.randomUUID();
         return uuid.toString().replace("-", "");
  }
	 
	 //数据库中取出的rusultSet数据转化成json数据
	 public String resultSetToJson(ResultSet rs) throws SQLException,JSONException
	    {
	       // json数组
	       JSONArray array = new JSONArray();
	      
	       // 获取列数
	       ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
	       int columnCount = metaData.getColumnCount();
	      
	       // 遍历ResultSet中的每条数据
	        while (rs.next()) {
	            JSONObject jsonObj = new JSONObject();
	           
	            // 遍历每一列
	            for (int i = 1; i <= columnCount; i++) {
	                String columnName =metaData.getColumnLabel(i);
	                String value = rs.getString(columnName);
	                jsonObj.put(columnName, value);
	            } 
	            array.add(jsonObj);
	        }
	      
	       return array.toString();
	    }
	 
	 //判断以逗号相隔的字符串中是否存在某个字符串
	 public boolean judge_string (String str,String id)  {  
		 String a=str;
		 List<String> test=Arrays.asList(a.split(",")); 
		 if(test.contains(id)) {
			return true;
		 }else {
			return false;
		}
		 
	 }
	//计算以逗号相隔的字符串的字符串个数
	public int count_string(String string){   //传入两个参数，一个是本身原来的字符串，另一个是要加入的

			String[] split=string.split(",");
			return split.length;
		}
	
	//获取当前日期时间
	public String get_time(){   //传入两个参数，一个是本身原来的字符串，另一个是要加入的
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		 return df.format(new Date());
	}
	
}
