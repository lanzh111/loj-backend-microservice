package com.lan.lojbackendjudgeservice;

import com.lan.lojbackendjudgeservice.amqp.RabbitMqInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.lan.lojbackendcommonservice",
        "com.lan.lojbackendjudgeservice"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lan.lojbackendclientservice.service.inner")
public class LojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        RabbitMqInit.create();
        SpringApplication.run(LojBackendJudgeServiceApplication.class, args);
    }

}
