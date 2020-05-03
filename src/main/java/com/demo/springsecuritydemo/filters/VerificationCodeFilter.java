package com.demo.springsecuritydemo.filters;

import com.demo.springsecuritydemo.exception.VerificationCodeException;
import com.demo.springsecuritydemo.handler.MyAuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 专门用于校验验证码的过滤器
 *
 * @author suvue
 * @date 2020/5/2
 */
public class VerificationCodeFilter extends OncePerRequestFilter {
    private AuthenticationFailureHandler authenticationFailureHandler = new MyAuthenticationFailureHandler();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //非登录请求不校验验证码
        if (!"/auth/form".equals(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            try {
                verificationCode(httpServletRequest);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (VerificationCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }
    }

    private void verificationCode(HttpServletRequest httpServletRequest) {
        final String requestCode = httpServletRequest.getParameter("captcha");
        final HttpSession session = httpServletRequest.getSession();
        final String savedCode = (String) session.getAttribute("captcha");
        if (StringUtils.isEmpty(savedCode)) {
            //随手清除验证码，无论是失败，还是成功。客户端应该在登录失败时刷新验证码
            session.removeAttribute("captcha");
        }
        //校验不通过，抛出异常
        if (StringUtils.isEmpty(requestCode) || StringUtils.isEmpty(savedCode) || !Objects.equals(requestCode,
                savedCode)) {
            throw new VerificationCodeException();
        }
    }
}
