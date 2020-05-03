package com.demo.springsecuritydemo.config;

import com.demo.springsecuritydemo.handler.MyAuthenticationFailureHandler;
import com.demo.springsecuritydemo.handler.MyAuthenticationSuccessHandler;
import com.demo.springsecuritydemo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * 自定登录与注销登录配置
 *
 * @author suvue
 * @date 2020/5/3
 */
//@EnableWebSecurity
public class WebSecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //为tokenRepository指定一个数据源
        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                //开放captcha.jpg的访问权限
                .antMatchers("/app/api/**", "/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .successHandler(new MyAuthenticationSuccessHandler())
                .failureHandler(new MyAuthenticationFailureHandler())
                .permitAll()
                .and()
                //增加自定登录功能，默认为简单散列加密
                .rememberMe().userDetailsService(myUserDetailsService)
                //设置key的作用是每次重启服务器后，自动登录仍然会生效，但是对用户来说风险加大
                //.key("rememberMe")
                //指定一个tokenRepository,用于存取Series和token等用户验证信息
                .tokenRepository(jdbcTokenRepository)
                .and()
                //指定接受注销请求的路由
                .logout().logoutUrl("/myLogout")
                //注销成功，跳转到该路径
                .logoutSuccessUrl("/login")
                //使该用户的httpSession失效
                .invalidateHttpSession(true)
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用我们自定义的数据库模型
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
