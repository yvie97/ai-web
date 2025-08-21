package com.yvie.ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 浏览器的同源策略安全机制：协议、域名、端口都相同，当前端应用（如localhost:3000）访问后端API（如localhost:8080）时，会产生跨域
// 这个配置的效果是：完全开放的CORS策略，允许任何域、任何方法、任何头信息访问所有API接口，适用于开发环境，方便前后端分离开发
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override // 添加跨域资源共享（CORS）配置
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口跨域
                .allowedOrigins("*") // 允许所有跨域访问的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP请求方法
                .allowedHeaders("*"); // 允许所有请求头
    }
}