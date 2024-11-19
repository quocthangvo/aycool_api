package com.example.shopapp_api.configurations;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class MvcConfig implements WebMvcConfigurer {
    //upload ảnh , thêm để cấu hình tệp yêu cầu upload
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/uploads/**")
                .allowedOrigins("http://localhost:4200") // URL frontend
                .allowedMethods("GET")
                .allowedHeaders("*");
    }


}
