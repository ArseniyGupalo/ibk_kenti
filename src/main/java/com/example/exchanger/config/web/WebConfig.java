package com.example.exchanger.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Дозволяємо CORS для всіх кінцевих точок
                .allowedOrigins("http://localhost:8080", "http://127.0.0.1:8080", "null")
                // "null" потрібен, якщо ви відкриваєте HTML-файл локально (file:///)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Дозволяємо потрібні методи
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}