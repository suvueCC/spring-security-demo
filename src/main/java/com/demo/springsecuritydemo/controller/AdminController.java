package com.demo.springsecuritydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员测试控制器
 *
 * @author suvue
 * @date 2020/5/2
 */
@RestController
@RequestMapping("/admin/api")
public class AdminController {
    @GetMapping("/hello")
    public String hello() {
        return "hello admin";
    }
}
