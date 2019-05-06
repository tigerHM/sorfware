package com.zhixin.Service;

import com.zhixin.entity.User;

public interface UserService {

    void insertUser(User user);

    User queryUser(String username);
}
