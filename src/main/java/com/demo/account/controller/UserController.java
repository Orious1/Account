package com.demo.account.controller;

import com.demo.account.entity.User;
import com.demo.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * *
     * @param userName
     * @param wxId
     * @return "success"
     * 功能：用户登录
     */
    @RequestMapping(method = RequestMethod.POST,value ="/login")
    public User login(String userName,String wxId){
        User user=new User(userName,wxId);
        User userInDb=userService.login(user);
        return userInDb;
    }
}
