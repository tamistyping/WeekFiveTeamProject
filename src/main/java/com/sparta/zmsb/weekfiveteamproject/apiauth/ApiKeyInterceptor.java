package com.sparta.zmsb.weekfiveteamproject.apiauth;

import com.sparta.zmsb.weekfiveteamproject.config.ApiKeyConfig;
import com.sparta.zmsb.weekfiveteamproject.logging.AppLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.logging.Logger;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger logger = AppLogger.getLogger();

    @Autowired
    private ApiKeyConfig apiKeyConfig;

    public ApiKeyInterceptor(ApiKeyConfig apiKeyConfig) {
        this.apiKeyConfig = apiKeyConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        logger.info("Entered preHandle method");
        String requestApiKey = request.getHeader("x-api-key");
        if(apiKeyConfig.getApiKey().equals(requestApiKey)) {
            return true;
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
