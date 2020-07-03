package com.demo.springsecuritydemo.config;

import com.demo.springsecuritydemo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * session会话管理相关配置
 *
 * @author suvue
 * @date 2020/5/3
 */
@EnableWebSecurity
public class WebSecurityConfig3 extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                .antMatchers("/app/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                //预防csrf攻击
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .formLogin()
                .permitAll()
                .and()
                .sessionManagement()
                //设置单个用户允许同时在线的最大会话数
                .maximumSessions(1)
                //当会话数达到最大数时，组织新会话建立
                .maxSessionsPreventsLogin(true)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置自定义的数据库模型
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
