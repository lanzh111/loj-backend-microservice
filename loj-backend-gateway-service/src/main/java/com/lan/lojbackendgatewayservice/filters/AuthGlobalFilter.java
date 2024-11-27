package com.lan.lojbackendgatewayservice.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关请求过滤器
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GatewayFilter, Ordered {



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取请求
        ServerHttpRequest request = exchange.getRequest();

        //获取请求路径
        RequestPath requestPath = request.getPath();
        String path = requestPath.toString();
        //路径包含inner直接拒绝请求
        if (path.contains("/inner/")){
            return Mono.error(new Exception("无权限"));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
