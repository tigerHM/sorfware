package com.zhixin.Controller;

import com.zhixin.entity.User;
import com.zhixin.Service.impl.UserServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceimpl userService;

    @RequestMapping("/login")
    public String cherkLogin(String username, String password){
        User user = userService.queryUser(username);
        if(user == null){
            return "用户名不存在";
        }else if (user.getPassword()!=password){
            return "密码错误";
        }
        return "Success";
    }
}
