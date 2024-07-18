package com.sparta.zmsb.weekfiveteamproject.config;

import com.sparta.zmsb.weekfiveteamproject.apiauth.ApiKeyInterceptor;
import com.sparta.zmsb.weekfiveteamproject.logging.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = AppLogger.getLogger();
    private ApiKeyInterceptor apiKeyInterceptor;

    @Autowired
    public WebConfig(ApiKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("Adding interceptors");
        registry.addInterceptor(apiKeyInterceptor).addPathPatterns("/api/countries/secure/**").addPathPatterns("/api/languages/secure/**").addPathPatterns("api/cities/secure/**");
    }
}
