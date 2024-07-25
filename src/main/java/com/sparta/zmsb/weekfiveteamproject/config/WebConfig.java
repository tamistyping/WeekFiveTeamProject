package com.sparta.zmsb.weekfiveteamproject.config;

import com.sparta.zmsb.weekfiveteamproject.apiauth.ApiKeyInterceptor;
import com.sparta.zmsb.weekfiveteamproject.logging.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.logging.Logger;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    private static final Logger logger = AppLogger.getLogger();
//    private final ApiKeyInterceptor apiKeyInterceptor;
//
//    @Autowired
//    public WebConfig(ApiKeyInterceptor apiKeyInterceptor) {
//        this.apiKeyInterceptor = apiKeyInterceptor;
//    }
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        logger.info("Adding interceptors");
//        registry.addInterceptor(apiKeyInterceptor).addPathPatterns("/api/countries/secure/**", "api/cities/secure/**","/api/languages/secure/**", "/api/keys/**");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
