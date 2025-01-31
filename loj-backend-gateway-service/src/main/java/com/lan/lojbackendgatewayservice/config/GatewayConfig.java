package com.lan.lojbackendgatewayservice.config;

import com.lan.lojbackendgatewayservice.filters.AuthGlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, StringRedisTemplate stringRedisTemplate) {
        return builder.routes()
                .route(p -> p
                        .path("/api/user/**")
                        .filters(f-> f.filter(new AuthGlobalFilter()))
                        .uri("lb://loj-backend-user-service"))
                .route(p -> p
                        .path("/api/question/**")
                        .filters(f-> f.filter(new AuthGlobalFilter()))
                        .uri("lb://loj-backend-question-service"))
                .route(p -> p
                        .path("/api/judge/**")
                        .filters(f-> f.filter(new AuthGlobalFilter()))
                        .uri("lb://loj-backend-judge-service"))
                        .build();
    }
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOriginPattern("*");
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        //允许的方法 可设置* 即允许全部http请求方法类型
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }


}
