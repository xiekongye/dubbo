package com.kyxie.study.dubbo.dubboprovider.service;

import com.kyxie.study.dubbo.dubboproviderapi.pojo.User;
import com.kyxie.study.dubbo.dubboproviderapi.service.ITestService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TestService Impl
 * Create by kyxie on 2018/3/8 12:08
 */
@Component("testService")
public class TestServiceImpl implements ITestService{

    public List<User> queryAllUsers() {
        List<User> users = new ArrayList<User>();
        for (int i = 0;i < 10;i++){
            User user = new User();
            user.setAge(20 + i);
            user.setId(Long.valueOf(i + 1));
            user.setPassword("123456");
            user.setUsername(String.format("userName_%d",i));
            users.add(user);
        }
        return users;
    }
}
