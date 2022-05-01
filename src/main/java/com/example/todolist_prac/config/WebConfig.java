package com.example.todolist_prac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("http://localhost:3000")    // 이 주소는 cors 허용
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
    }
}
