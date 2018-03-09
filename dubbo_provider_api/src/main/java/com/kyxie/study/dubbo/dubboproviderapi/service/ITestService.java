package com.kyxie.study.dubbo.dubboproviderapi.service;

import com.kyxie.study.dubbo.dubboproviderapi.pojo.User;

import java.util.List;

/**
 * Interface
 * Create by kyxie on 2018/3/8 12:07
 */
public interface ITestService {

    List<User> queryAllUsers();

}
