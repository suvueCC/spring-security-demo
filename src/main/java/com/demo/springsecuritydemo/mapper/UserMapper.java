package com.demo.springsecuritydemo.mapper;

import com.demo.springsecuritydemo.entity.SysUsers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    /**
     * 根据用户名获取用户信息
     *
     * @author suvue
     * @date 2020/5/2
     */
    @Select("select * from sys_users where username = #{username}")
    SysUsers findByUsername(@Param("username")String username);
}
