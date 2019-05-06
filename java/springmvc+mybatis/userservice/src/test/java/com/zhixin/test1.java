package com.zhixin;


import com.zhixin.Service.UserService;
import com.zhixin.entity.User;
import org.junit.Test;

import javax.annotation.Resource;

public class test1 extends MVCtest {
    @Resource
    private UserService userService;

    @Test
    public void insertUser() {
        User user = new User("test2", "123456", "111111");
        userService.insertUser(user);
        System.out.println("插入数据成功");
    }

    @Test
    public void queryUser() {
        User user = userService.queryUser("test1");
        System.out.println("user:"+user.getId()+" "+user.getUsername());
    }
}
