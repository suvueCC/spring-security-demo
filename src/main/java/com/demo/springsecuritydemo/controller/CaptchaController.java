package com.demo.springsecuritydemo.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码控制器
 *
 * @author suvue
 * @date 2020/5/2
 */
@Controller
public class CaptchaController {
    @Autowired
    private Producer captchaProducer;

    @GetMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置内容类型
        response.setContentType("img/jpeg");
        //创建验证码文本
        final String capText = captchaProducer.createText();
        //将验证码文本设置到session
        request.getSession().setAttribute("captcha",capText);
        //创建验证码图片
        final BufferedImage bi = captchaProducer.createImage(capText);
        //获取响应输出流
        final ServletOutputStream out = response.getOutputStream();
        //将图片验证码数据写入到响应输出流
        ImageIO.write(bi,"jpg",out);
        try {
            out.flush();
        }finally {
            out.close();
        }

    }
}
