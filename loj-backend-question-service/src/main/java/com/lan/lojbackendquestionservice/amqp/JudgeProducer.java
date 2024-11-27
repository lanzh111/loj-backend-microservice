package com.lan.lojbackendquestionservice.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Component
public class JudgeProducer {


    @Resource
    private AmqpTemplate template;

    /**
     * 生产者发送消息
     * @param message
     */
    public void send(String message,String exchange,String key){
        template.send(exchange,key,new Message(message.getBytes(StandardCharsets.UTF_8)));
    }
}
