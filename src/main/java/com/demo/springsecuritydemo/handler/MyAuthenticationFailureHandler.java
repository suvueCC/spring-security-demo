package com.demo.springsecuritydemo.handler;

import cn.hutool.json.JSONUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 自定义的校验异常处理器
 *
 * @author suvue
 * @date 2020/5/2
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        final PrintWriter out = response.getWriter();
        final HashMap<String, Object> result = new HashMap<>();
        result.put("error_code", "401");
        result.put("message", exception.getMessage());
        result.put("class", exception.getClass().toString());
        //JSONUtil为huTool包下的
        out.write(JSONUtil.toJsonStr(result));
        out.close();
    }
}
