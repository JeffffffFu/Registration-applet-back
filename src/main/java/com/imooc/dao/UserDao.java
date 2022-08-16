package com.imooc.dao;

import com.imooc.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    /*插入或更新用户信息2*/
    int insertUser2(String openid);
    /*插入或更新用户信息*/
    int insertUser(User user);
    //查询对应用户信息
    List<User> queryUserInformation(String openid);
    /*更改用户信息*/
    int updateUser(User user);
     /*找出对应比赛报名人员/请假人员的信息和点击时间*/
    List<User> getJoinOrLeaveInformation(@Param("id")String id, @Param("uuid")String uuid);
    /*修改指定opened用户的match字段*/
    int updateUserMatch(@Param("match")String id, @Param("openid")String openid);
}
