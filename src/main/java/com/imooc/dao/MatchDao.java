package com.imooc.dao;

import com.imooc.entity.Match;
import com.imooc.entity.User;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface MatchDao {
     /*查询该用户参与的比赛和该比赛的状态 ,输入：该用户的参与比赛字段*/
    List<Match> queryMatchAndStatus( @Param("userMatch")String userMatch, @Param("openid")String openid);
    /*查询该用户的历史发布比赛记录  ,输入：用户Openid*/
    List<Match> queryMatchHistory(String openid);
   /*创建比赛，将比赛信息插入数据库*/
    int createMatch(Match match);
    /*取出对于uuid的赛事信息*/
    List<Match> queryMatch(String uuid);
    /*查询该用户参与的比赛和该比赛的状态根据uuid和openid */
    List<Match> queryMatchAndStatusByUuid(@Param("uuid")String uuid, @Param("openid")String openid);
    /*编辑比赛/修改比赛信息*/
    int updateMatch(Match match);
   /*删除对应用户对应uuid的比赛表和状态表的信息*/
    int deleteMatchAndStatus(@Param("uuid")String uuid, @Param("openid")String openid);
    /*修改比赛表的报名和请假字段*/
    int updateMatchJoinAndLeave(@Param("userJoin")String userJoin, @Param("userLeave")String userLeave,@Param("uuid")String uuid);
}
