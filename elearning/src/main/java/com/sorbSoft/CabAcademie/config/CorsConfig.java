package com.sorbSoft.CabAcademie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//public class CorsConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:4200")
//                .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS", "PATCH", "HEAD")
//                .allowedHeaders("username", "password")
//                .allowCredentials(false).maxAge(3600);
//    }
//}
