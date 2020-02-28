package com.lt.fly.utils;

import com.auth0.jwt.JWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ContextHolderUtil {
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static Object getSpringBean(Class requiredType) {
        return getContext().getBean(requiredType);
    }
    public static Object getSpringBean(String beanName) {
        return  getContext().getBean(beanName);
    }
    public static WebApplicationContext getContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }


    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession();
        return session;
    }


    public static Long getTokenUserId() {
        String token = getRequest().getHeader("token");
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        String tokenValue = JWT.decode(token).getAudience().get(0);
        if(StringUtils.isEmpty(tokenValue)) {
            return null;
        }
        String[] split = tokenValue.split("-");
        if(null == split || split.length != 2) {
            return null;
        }
        long userId = Long.parseLong(tokenValue.split("-")[0]);
        return userId;
    }
}
