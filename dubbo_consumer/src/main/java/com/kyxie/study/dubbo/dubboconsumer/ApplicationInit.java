package com.kyxie.study.dubbo.dubboconsumer;

import com.kyxie.study.dubbo.dubboproviderapi.pojo.User;
import com.kyxie.study.dubbo.dubboproviderapi.service.ITestService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create by kyxie on 2018/3/9 16:29
 */

public class ApplicationInit {

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml","/dubbo/dubbo-consumer.xml"});

        context.start();

        ITestService service = (ITestService) context.getBean(ITestService.class);

        List<User> users = service.queryAllUsers();

        System.out.println("DONE!");
    }

}
