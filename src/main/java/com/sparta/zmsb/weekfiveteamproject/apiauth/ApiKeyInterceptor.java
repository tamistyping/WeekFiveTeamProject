package com.sparta.zmsb.weekfiveteamproject.apiauth;

import com.sparta.zmsb.weekfiveteamproject.logging.AppLogger;
import com.sparta.zmsb.weekfiveteamproject.service.KeyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.logging.Logger;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger logger = AppLogger.getLogger();


    private final KeyService keyService;

    @Autowired
    public ApiKeyInterceptor(KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        logger.info("Entered preHandle method");
        String requestApiKey = request.getHeader("x-api-key");
        if(keyService.isValidApiKey(requestApiKey)) {
            return true;
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
