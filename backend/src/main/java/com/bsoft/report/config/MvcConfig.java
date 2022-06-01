package com.bsoft.report.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.config
 * @Description:
 * @date : 2021/11/27 12:35
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private static final long MAX_AGE_SECS = 3600;

    @Autowired
    private ServletWebServerApplicationContext context;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath/static/**").resourceChain(true);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
