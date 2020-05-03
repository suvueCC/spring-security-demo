package com.demo.springsecuritydemo.exception;

import org.springframework.security.core.AuthenticationException;
/**
 * 自定义一个验证码校验失败的异常
 *
 * @author suvue
 * @date 2020/5/2
 */
public class VerificationCodeException extends AuthenticationException {
    public VerificationCodeException() {
        super("图片验证码校验失败！");
    }
}
