package com.demo.account.mapper;

import com.demo.account.entity.User;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

public interface UserMapper {

    // 这时我们就可以通过@ResultMap()注解中传入Results注解的参数id来引用Results注解中的内容
    // 例如使用@ResultMap(value = "userMapper")
    @Results(id="userMapper" ,value={
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column = "username",property = "userName"),
            @Result(column = "wxid",property = "wxId"),
            @Result(column = "register_date",property = "registerDate")
    })
    @Select("select * from user")
    List<User> selectAllUsers();


    @Insert("insert into user(uid,username,wxid,register_date) VALUES(#{uid},#{userName},#{wxId},#{registerDate})")
    void addUser(int uid, String userName, String wxId, Timestamp registerDate);

    @ResultMap(value = "userMapper")
    @Select("select * from user where wxid=#{wxId}")
    User findUserByWxId(String wxId);

    @Select("select count(*) from user")
    int uidGenerate();
}
