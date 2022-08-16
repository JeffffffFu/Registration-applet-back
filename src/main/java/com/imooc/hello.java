package com.imooc;

import com.imooc.dao.MatchDao;
import com.imooc.entity.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class hello {
    @Autowired
    private MatchDao matchDao;
    @RequestMapping(value="/hello",method = RequestMethod.GET)

    public String hello(){

        System.out.println("matchlist:");
        return "Hello SpringBoot!!!!2323232323";

    }
}
