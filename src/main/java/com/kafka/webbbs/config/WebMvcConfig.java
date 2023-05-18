package com.kafka.webbbs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 通过读取配置项获取的文件上传路径
    @Value("${file.uploadFolder}")
    private String basePath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Users/hugua/Pictures/uploadfile/**").addResourceLocations("file:" + basePath);
    }
}
