package com.demo.springsecuritydemo.config;

import com.demo.springsecuritydemo.filters.VerificationCodeFilter;
import com.demo.springsecuritydemo.handler.MyAuthenticationFailureHandler;
import com.demo.springsecuritydemo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * 自定义验证完善图片验证码
 *
 * @author suvue
 * @date 2020/5/3
 */
//@EnableWebSecurity
public class WebSecurityConfig1 extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> myWebAuthenticationDetailsSource;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                //开放captcha.jpg的访问权限
                .antMatchers("/app/api/**","/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .permitAll()
                //把我们写的detailsSource传给控制校验的filter
                .authenticationDetailsSource(myWebAuthenticationDetailsSource)
                .loginPage("/myLogin.html")
                .loginProcessingUrl("/auth/form")
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    httpServletResponse.setContentType("application/json;charset=UTF-8");
                    final PrintWriter out = httpServletResponse.getWriter();
                    out.write("{\"error_code\":\"0\",\"message\":\"欢迎登录系统！\"}");
                })
                .failureHandler(new MyAuthenticationFailureHandler());

        //将过滤器添加再UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用我们自定义的数据库模型
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
        //添加我们的校验流程
        auth.authenticationProvider(authenticationProvider);
    }
}
