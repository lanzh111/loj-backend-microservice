package com.lan.lojbackendjudgeservice.amqp;


import com.lan.lojbackendclientservice.service.JudgeService;
import com.lan.lojbackendclientservice.service.inner.QuestionServiceInner;
import com.lan.lojbackendcommonservice.common.ErrorCode;
import com.lan.lojbackendcommonservice.constant.JudgeConstants;
import com.lan.lojbackendmodelservice.entity.Question;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class JudgeConsumer {


    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionServiceInner questionServiceInner;

    /**
     * 监听JUDGE_QUEUE_NAME队列
     * @param message
     * @param channel
     * @param tag
     * @throws IOException
     */
    @RabbitListener(queues = JudgeConstants.JUDGE_QUEUE_NAME, ackMode = "MANUAL")
    public void doJudge(String message, Channel channel,
                       @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        //消息校验
        if (StringUtils.isBlank(message)){
            channel.basicNack(tag,false,false);
        }
        long id = Long.parseLong(message);
        //题目是否存在

        Question question = questionServiceInner.getById(id);
        if (question==null){
            channel.basicNack(tag,false,false);
        }
        try {
            //判题
             judgeService.doJudge(id);

        }catch (Exception e){

            channel.basicNack(tag,false,
                    ErrorCode.SYSTEM_ERROR.getMessage().equals(e.getMessage()));

        }

        channel.basicAck(tag,false);
    }

}
