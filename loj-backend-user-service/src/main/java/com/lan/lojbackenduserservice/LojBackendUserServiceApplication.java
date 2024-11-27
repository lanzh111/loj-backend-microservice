package com.lan.lojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.lan.lojbackendclientservice.mapper")
@ComponentScan(basePackages = {"com.lan.lojbackendcommonservice",
        "com.lan.lojbackenduserservice"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lan.lojbackendclientservice.service.inner")
public class LojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LojBackendUserServiceApplication.class, args);
    }

}