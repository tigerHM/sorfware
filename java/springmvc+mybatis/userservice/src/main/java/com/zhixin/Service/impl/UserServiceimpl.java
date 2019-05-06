package com.zhixin.Service.impl;

import com.zhixin.Dao.UserDao;
import com.zhixin.entity.User;
import com.zhixin.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    @Override
    public User queryUser(String username) {
        User user = userDao.queryUser(username);
        return user;
    }
}
