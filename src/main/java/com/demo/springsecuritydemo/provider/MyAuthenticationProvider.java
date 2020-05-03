package com.demo.springsecuritydemo.provider;

import com.demo.springsecuritydemo.detail.MyWebAuthenticationDetail;
import com.demo.springsecuritydemo.exception.VerificationCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 实现图片验证码的AuthenticationProvider
 *
 * @author suvue
 * @date 2020/5/3
 */
@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    /**
     * 附加认证过程
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //实现图片验证码的校验逻辑
        final MyWebAuthenticationDetail details = (MyWebAuthenticationDetail) authentication.getDetails();
        final String imageCode = details.getImageCode();
        final String savedImageCode = details.getSavedImageCode();
        //校验不通过，抛出异常
        if (StringUtils.isEmpty(imageCode) || StringUtils.isEmpty(savedImageCode) || !Objects.equals(imageCode,
                savedImageCode)) {
            throw new VerificationCodeException();
        }
        //调用父类完成密码验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
