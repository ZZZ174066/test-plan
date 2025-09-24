package com.tsj.Utils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CrossConfig  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                // 允许跨域的域名或IP，星号代表允许所有
                .allowedOrigins("*")
                // 请求允许的方法，如：GET, POST, PUT, DELETE等
                .allowedMethods("*")
                // 预检间隔时间
                .maxAge(168000)
                // 允许头部设置
                .allowedHeaders("*");
    }
}