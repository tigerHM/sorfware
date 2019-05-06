package com.zhixin.Dao;

import com.zhixin.entity.User;

public interface UserDao {

    User queryUser(String username);

    void insertUser(User user);
}
