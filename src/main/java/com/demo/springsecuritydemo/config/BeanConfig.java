package com.demo.springsecuritydemo.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * 配置一些所需要的bean
 *
 * @author suvue
 * @date 2020/5/2
 */
@Component
public class BeanConfig {
    @Autowired
    DataSource dataSource;

    //@Bean
    /*public UserDetailsService userDetailsService() {
        final JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        if (!manager.userExists("user")) {
            manager.createUser(User.withUsername("user").password("123").roles("USER").build());
        }
        if (!manager.userExists("admin")) {
            manager.createUser(User.withUsername("admin").password("123").roles("USER", "ADMIN").build());
        }
        return manager;
    }*/
    /*

     */

    /**
     * 基于内存实现的多用户支持
     *
     * @author suvue
     * @date 2020/5/2
     */

    //@Bean
    //public UserDetailsService userDetailsService(){
    //    final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //    manager.createUser(User.withUsername("user").password("123").roles("USER").build());
    //    manager.createUser(User.withUsername("admin").password("123").roles("USER").build());
    //    return manager;
    //}

    /**
     * 使用委托加密密码器（官方推荐）
     *
     * @author suvue
     * @date 2020/5/3
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 图片验证码
     *
     * @author suvue
     * @date 2020/5/2
     */
    @Bean
    public Producer captcha() {
        //配置图形验证码的基本参数
        final Properties properties = new Properties();
        //图片宽度
        properties.setProperty("kaptcha.image.width", "150");
        //图片长度
        properties.setProperty("kaptcha.image.height", "50");
        //字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        final Config config = new Config(properties);
        //使用默认的图片验证码实现，当然也可以自定义实现
        final DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    /**
     * 注册session事件发布，将Java事件转变为spring事件
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 启用CORS配置源
     *
     * @author suvue
     * @date 2020/5/3
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        //允许从百度站点跨域
        configuration.setAllowedOrigins(Collections.singletonList("https://www.baidu.com"));
        //允许使用get和post方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        //允许代凭证
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //对所有url生效
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
