package com.sparta.zmsb.weekfiveteamproject.apiauth;

import com.sparta.zmsb.weekfiveteamproject.config.ApiKeyConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiKeyConfig apiKeyConfig;

    public ApiKeyInterceptor(ApiKeyConfig apiKeyConfig) {
        this.apiKeyConfig = apiKeyConfig;
    }

    public boolean prehandle(HttpServletRequest request, HttpServletResponse response, Object handler){
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
