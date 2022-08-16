package com.imooc.web;

import com.imooc.accessToken.Request;
import com.imooc.accessToken.tokenThread;
import com.imooc.dao.MatchDao;
import com.imooc.dao.MatchStatusDao;
import com.imooc.dao.UserDao;
import com.imooc.entity.Match;
import com.imooc.entity.Match_status;
import com.imooc.entity.User;
import com.imooc.hanlder.DataProcessing;
import com.imooc.hanlder.MsgSecCheck;
import com.imooc.hanlder.OpenidHandler;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Jeff")

public class MatchController {
    //接口的注入， @Autowired，直接创建接口对象
    @Autowired
    private MatchDao matchDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MatchStatusDao matchStatusDao;

    //  查询该用户参与的比赛和该比赛的状态 ,并取出其最新发布比赛记录
    @RequestMapping(value = "/listMatch", method = RequestMethod.GET)
    private Map<String, Object> listMatch(String openid) {
        System.out.println("开始取出用户所有比赛信息-----------");
        Map<String, Object> Matchlist = new HashMap<String, Object>();
        matchStatusDao.updateMatchStatus();// 根据当前时间修改比赛状态，过期改为"3"
        System.out.println("openid:" + openid);
        List<User> userList = userDao.queryUserInformation(openid);//根据openid取出用户信息，将match字段取出，接着传入用户的match字段去查询比赛和状态
        String userMatch = userList.get(0).getMatch();
        List<Match> matchList = matchDao.queryMatchAndStatus(userMatch,openid);
        System.out.println("user_match:"+userMatch);
        List<Match> matchHistory = matchDao.queryMatchHistory(openid); //查询历史信息
        String accessToken=tokenThread.accessToken;// 获取accessToken
        Matchlist.put("matchList", matchList);
        System.out.println("matchlist:"+matchList);
        Matchlist.put("matchHistory", matchHistory);
        System.out.println("accessToken:"+accessToken);
        Matchlist.put("accessToken", accessToken);
        System.out.println("取出用户所有比赛信息成功！！！");
        return Matchlist;
    }

    //  插入/更新用户信息数据2，仅仅获取openid
    @RequestMapping(value = "/inserUser2", method = RequestMethod.GET)
    private String inserUser2(String code) {
        System.out.println("开始插入/更新用户信息-----------");
        String appsercet = "22f8bb4e86c7ee6a7dfecb881fa5fda0";
        String appid = "wxbd8c3059b444fd81";
        String grant_type = "authorization_code";
        String openIDJson = new OpenidHandler().GetOpenID(appid, appsercet, code); // 向网站请求，调取方法，获取openid
        String openid=(String) JSONObject.fromObject(openIDJson).get("openid");
        System.out.println("openid："+openid);
        if(openid==null){
            System.out.println("插入/更新用户信息失败");
        }else{
            int result = userDao.insertUser2(openid);  //插入/更新用户数据
            System.out.println("插入/更新用户信息成功！！！");
        }
        return openid;//返回给前端，得到的openid
    }

    //  插入/更新用户信息数据，获取code码等去解析用户姓名头像等信息
    @RequestMapping(value = "/inserUser", method = RequestMethod.GET)
    private String inserUser(String code, String user_name, String user_url, String user_country, String user_city, String user_province, String user_gender) {
        System.out.println("开始插入/更新用户信息-----------");
        String appsercet = "22f8bb4e86c7ee6a7dfecb881fa5fda0";
        String appid = "wxbd8c3059b444fd81";
        String grant_type = "authorization_code";
        String user_nameNew = user_name.replaceAll("[\\x{10000}-\\x{10FFFF}]", "").replaceAll("'", "");
        User user = new User(0, "", "", user_nameNew, user_url, user_country, user_province, user_city, user_gender);
        String openIDJson = new OpenidHandler().GetOpenID(appid, appsercet, code); // 向网站请求，调取方法，获取openid
        user.setOpenid((String) JSONObject.fromObject(openIDJson).get("openid"));
        int result = userDao.insertUser(user);  //插入/更新用户数据
        System.out.println("用户名称："+user_nameNew+"  , 当前时间为："+new Date());
        System.out.println("插入/更新用户信息成功！！！");
        return user.getOpenid();//返回给前端，得到的openid
    }

       //创建比赛，将比赛信息存储入数据库
        @RequestMapping(value = "/createMatch", method = RequestMethod.GET)
        @ResponseBody
        private int createMatch(String match_theme, String match_time, String match_week, String
                deadline_time, String deadline_week, String match_address, String match_rule, String match_directions, String
                                           match_people, String openid,double longitude, double latitude){
            System.out.println("开始创建比赛/存储比赛信息-----------");

            //检测文本敏感词,如果不为0，则不成功，不执行数据库存储并直接返回errcodeCheck
            String msg=match_theme+match_address+match_rule+match_directions+match_people; //拼接文本
            int errcodeCheck=new MsgSecCheck().getMsgSecCheck(tokenThread.accessToken, msg);
            if(errcodeCheck!=0){
                System.out.println("含有敏感词！");
                return errcodeCheck;
            }else{
                String uuid=new DataProcessing().getId();//获取一个随机的key
                Match match=new Match(uuid,match_theme,match_address,match_time,match_week,deadline_time,deadline_week,match_rule
                        ,match_people,match_directions,openid,"","",longitude,latitude); //构建比赛信息
                try{
                    int resultMatch=matchDao.createMatch(match);//插入比赛信息
                }catch(Exception e) {
                    System.out.println("插入错误"+e);
                };
                Match_status match_status=new Match_status(uuid,openid,"0",match_time,null); //构建比赛状态
                int resultStatus=matchStatusDao.createMatchStatus(match_status);//插入比赛状态
                List<User> userList=userDao.queryUserInformation(openid);  //取出用户信息
                String userMatchID= new DataProcessing().reduce2_string(userList.get(0).getMatch()); //需要判断该用户下的比赛是否过量（最多100）,过量则需要先删除第一个
                String userMatchIdNew = new DataProcessing().increase_string(userMatchID,String.valueOf(match.getId()));//进行字符串的处理，在原来的字符串后加上新的字符
                int resultUser=userDao.updateUserMatch(userMatchIdNew,openid);//将创建的比赛的id加入到用户表的match字段中
                System.out.println("创建比赛成功！！！");
                System.out.println("返回的code:"+errcodeCheck);
                return errcodeCheck;
            }
        }

    //进入比赛详情界面，查看比赛信息
    @RequestMapping(value = "/viewMatch", method = RequestMethod.GET)
    private Map<String, Object> viewMatch(String uuid,String openid){
        System.out.println("查看比赛详情信息-----------");
        Map<String, Object> Matchlist = new HashMap<String, Object>();
        Boolean disableJoin = false;
        Boolean disableLeave = false;
        List<Match> matchList=matchDao.queryMatchAndStatusByUuid(uuid,openid);// 根据uuid和openid取出比赛信息和状态
        List<User> userListJoin=userDao.getJoinOrLeaveInformation(matchList.get(0).getUserJoin(),uuid); //取出报名的人的信息和点击时间
        List<User> userListLeave=userDao.getJoinOrLeaveInformation(matchList.get(0).getUserLeave(),uuid); //取出请假的人的信息和点击时间
        Match_status match_status=new Match_status(uuid,openid,"","","");
        List<Match_status>match_statusList=matchStatusDao.queryMatchStatus(match_status);//取出比赛状态
        try{
            if(match_statusList.get(0).getMatchStatus().equals("1")) { //判断该用户该场的比赛状态是报名还是请假
                disableJoin =true;
            }else if (match_statusList.get(0).getMatchStatus().equals("2")) {
                disableLeave=true;
            }
        }catch (Exception e){
            System.out.println("matchStatus为空，没有报名和请假："+e.getMessage());
        }
        Matchlist.put("matchList",matchList);
        Matchlist.put("disableJoin",disableJoin);
        Matchlist.put("disableLeave",disableLeave);
        Matchlist.put("userListJoin",userListJoin);
        Matchlist.put("userListLeave",userListLeave);
        System.out.println("查看比赛详情信息成功！！！");
        return Matchlist;

    }

    //编辑比赛，修改比赛信息
      @RequestMapping(value = "/updateMatch", method = RequestMethod.GET)
    private int updateMatch(String uuid,String match_theme, String match_time, String match_week, String
              deadline_time, String deadline_week, String match_address, String match_rule, String match_directions, String
                                                          match_people, String openid,double longitude, double latitude){
        System.out.println("编辑比赛-----------");
        Match match=new Match(uuid,match_theme,match_address,match_time,match_week,deadline_time,deadline_week,match_rule,match_people,
                match_directions,openid,"","",longitude,latitude);
        int result=matchDao.updateMatch(match); //修改比赛信息
        Match_status match_status=new Match_status(uuid,openid,"",match_time,"");
        int resultTime=matchStatusDao.updateMatchStatusTime(match_status);  //修改比赛状态中的时间，所有openid
        System.out.println("编辑比赛成功！！！");
        return result;

    }

    //删除比赛，删除比赛和状态信息
    @RequestMapping(value = "/deleteMatch", method = RequestMethod.GET)
    private int deleteMatch(String uuid,String openid){
        System.out.println("删除比赛-----------");
        int result=matchDao.deleteMatchAndStatus(uuid, openid); //删掉这场比赛，包括所有openid
        System.out.println("删除比赛成功！！！");
        return result;

    }

    //报名比赛
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    private Map<String, Object> registration(String uuid,String openid,String time){
        System.out.println("报名比赛-----------");
        List<Match> matchList=matchDao.queryMatch(uuid);
        List<User> userList=userDao.queryUserInformation(openid);
        String userMatchIDNew=new DataProcessing().increase_string(userList.get(0).getMatch(),String.valueOf(matchList.get(0).getId()));//在原来用户的比赛字段中，加上该比赛的ID
        int result=userDao.updateUserMatch(userMatchIDNew,openid);//更新改用户的比赛字段
        String JoinMatchNew=new DataProcessing().increase_string(matchList.get(0).getUserJoin(),String.valueOf(userList.get(0).getId()));//在原来比赛的报名字段中，加入该报名用户的id
        String LeaveMatchNew=new DataProcessing().reduce_string(matchList.get(0).getUserLeave(),String.valueOf(userList.get(0).getId()));//在原来比赛的请假字段中，减去该报名用户的id
        int resultJoinAndLeave=matchDao.updateMatchJoinAndLeave(JoinMatchNew,LeaveMatchNew,uuid); // 将新的报名和请假字段修改进去
        List<User> userListJoin=userDao.getJoinOrLeaveInformation(JoinMatchNew,uuid); //根据新的报名字段，取出报名的人的信息和点击时间
        List<User> userListLeave=userDao.getJoinOrLeaveInformation(LeaveMatchNew,uuid); //根据新的请假字段，取出请假的人的信息和点击时
        Match_status match_status=new Match_status(uuid,openid,"1",time,new DataProcessing().get_time());
        int resultMatchStatus=matchStatusDao.createMatchStatus(match_status);//插入或更新比赛状态信息
        Map map=new HashMap();
        map.put("json_register",userListJoin);
        map.put("json_leave",userListLeave);
        System.out.println("报名比赛成功！！！");
        return map;

    }

    //请假比赛
    @RequestMapping(value = "/leave", method = RequestMethod.GET)
    private Map<String, Object> leave(String uuid,String openid,String time){
        System.out.println("请假比赛-----------");
        List<Match> matchList=matchDao.queryMatch(uuid);
        List<User> userList=userDao.queryUserInformation(openid);
        String userMatchIDNew=new DataProcessing().increase_string(userList.get(0).getMatch(),String.valueOf(matchList.get(0).getId()));//在原来用户的比赛字段中，加上该比赛的ID
        int result=userDao.updateUserMatch(userMatchIDNew,openid);//更新改用户的比赛字段
        String LeaveMatchNew=new DataProcessing().increase_string(matchList.get(0).getUserLeave(),String.valueOf(userList.get(0).getId()));//在原来比赛的请假字段中，加入该请假用户的id
        String JoinMatchNew=new DataProcessing().reduce_string(matchList.get(0).getUserJoin(),String.valueOf(userList.get(0).getId()));//在原来比赛的报名字段中，减去该请假用户的id
        int resultJoinAndLeave=matchDao.updateMatchJoinAndLeave(JoinMatchNew,LeaveMatchNew,uuid); // 将新的报名和请假字段修改进去
        List<User> userListJoin=userDao.getJoinOrLeaveInformation(JoinMatchNew,uuid); //根据新的报名字段，取出报名的人的信息和点击时间
        List<User> userListLeave=userDao.getJoinOrLeaveInformation(LeaveMatchNew,uuid); //根据新的请假字段，取出请假的人的信息和点击时
        Match_status match_status=new Match_status(uuid,openid,"2",time,new DataProcessing().get_time());
        int resultMatchStatus=matchStatusDao.createMatchStatus(match_status);//插入或更新比赛状态信息
        Map map=new HashMap();
        map.put("json_register",userListJoin);
        map.put("json_leave",userListLeave);
        System.out.println("请假比赛成功！！！");
        return map;

    }

    //微信模板通知
    @RequestMapping(value = "/getFormid", method = RequestMethod.GET)
    private void getFormid(String formid,String openid,String uuid,String join_count,String time) throws IOException {
        System.out.println("获取formid-----------");
        String template_id = "ecM_SJms66W_8poIlrFSoEe1mMgDv6lLQ2uPXAT8yng";
        String page = "pages/index/index?uuid=" + uuid + "&time=" + time + "&sign_share=" + 1;
        String access_token = tokenThread.accessToken;
        List<Match> match_tests=matchDao.queryMatch(uuid);
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", match_tests.get(0).getTheme());
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", match_tests.get(0).getTime());
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value", match_tests.get(0).getAddress());
        JSONObject keyword4 = new JSONObject();
        keyword4.put("value", join_count);
      /*  JSONObject keyword5 = new JSONObject();
        keyword5.put("value", match_tests.get(0).getSponsor());*/
        JSONObject data = new JSONObject();
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("keyword3", keyword3);
        data.put("keyword4", keyword4);
        /*data.put("keyword5", keyword5);*/
        JSONObject json = new JSONObject();
        json.put("touser", openid);
        json.put("template_id", template_id);
        json.put("page", page);
        json.put("form_id", formid);
        json.put("data", data);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token;
        String result = new Request().jsonPost(url, json);
        System.out.println("result:" + result);
        System.out.println("获取formid成功！！！");


    }
}




