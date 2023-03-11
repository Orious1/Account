package com.demo.account.service.impl;

import com.demo.account.entity.User;
import com.demo.account.mapper.UserMapper;
import com.demo.account.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        User userInDb=userMapper.findUserByWxId(user.getWxId());
        if (ObjectUtils.isEmpty(userInDb)) {
            Date date = new Date();
            Timestamp timeStamp = new Timestamp(date.getTime());
            userMapper.addUser(userMapper.uidGenerate()+1,user.getUserName(),user.getWxId(),timeStamp);
            userInDb=userMapper.findUserByWxId(user.getWxId());
        }
        return userInDb;
    }
}
