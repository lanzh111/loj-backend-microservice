package com.lan.lojbackendjudgeservice.amqp;

import com.lan.lojbackendcommonservice.constant.JudgeConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqInit {

  public static void create() {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
      Connection connection = null;
      try {
        connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(JudgeConstants.JUDGE_QUEUE_NAME, false, false, false, null);
        //声明交换机
        channel.exchangeDeclare(JudgeConstants.EXCHANGE_NAME,"direct",false);
        //把第一个队列绑定到direct_exchange交换机上，并设置了routingKey judge
        channel.queueBind(JudgeConstants.JUDGE_QUEUE_NAME,
                JudgeConstants.EXCHANGE_NAME,
                JudgeConstants.ROUTING_KEY);
      } catch (IOException | TimeoutException e) {
          throw new RuntimeException(e);
      }

      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


  }

}