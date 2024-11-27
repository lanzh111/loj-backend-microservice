package com.lan.lojbackendjudgeservice.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign拦截器
 */
@Slf4j
public class CustomFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //TODO  根据需求可以在拦截器中扩展需要的功能
        log.info("比如记录日志 /增加参数 / 修改路径 / 鉴权");
        if (requestTemplate.path().contains("/user/")){
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            String cookie = request.getHeader("Cookie");
            requestTemplate.header("Cookie",cookie);
        }


    }
}

