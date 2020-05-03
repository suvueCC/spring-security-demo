package com.demo.springsecuritydemo.service;

import com.demo.springsecuritydemo.entity.SysUsers;
import com.demo.springsecuritydemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 编码实现
 *
 * @author suvue
 * @date 2020/5/2
 */
@Primary
//@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库尝试获取该用户
        final SysUsers user = userMapper.findByUsername(username);
        //用户不存在 抛出异常
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        //将数据库形式的roles解析成UserDetails的权限集
        //AuthorityUtils.commaSeparatedStringToAuthorityList是spring-security提供的
        //该方法用于将逗号隔开的权限集字符串切割成可用权限列表
        //当然也可以自己实现，如用分号来隔开等，参考generateAuthorities
        user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
        return user;
    }

    /**
     * 自行实现权限的转换
     *
     * @author suvue
     * @date 2020/5/2
     */
    public List<GrantedAuthority> generateAuthorities(String roles) {
        final ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        final String[] roleArray = roles.split(";");
        if (roles != null && !"".equals(roles)) {
            for (String role : roleArray) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }
}
