package com.example.todolist_prac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://todolist.dosee.site"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")       // 모든 헤더를 허용
                .exposedHeaders("Authorization", "Set-Cookie")
                .allowCredentials(true)    // 인증정보를 응답 헤더에 포함할지 여부
                .maxAge(3000);             // 3000초 동안 pre-flight 리퀘스트를 캐싱
    }
}
