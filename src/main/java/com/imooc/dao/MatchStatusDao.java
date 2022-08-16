package com.imooc.dao;

import com.imooc.entity.Match_status;

import java.util.List;

public interface MatchStatusDao {
   /*<-将超出当前时间的比赛的状态修改为3 */
    int updateMatchStatus();
    /*创建比赛状态*/
    int createMatchStatus(Match_status match_status);
    /*查询比赛状态*/
      List<Match_status> queryMatchStatus(Match_status match_status);
    /*修改比赛状态的比赛时间*/
    int updateMatchStatusTime(Match_status match_status);
}
