package com.practice.e2021.validate2log.mq;

import com.practice.e2021.validate2log.bean.MessageDTO;
import com.practice.e2021.validate2log.config.PulsarConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PulsarConsumer {

    @io.github.majusko.pulsar.annotation.PulsarConsumer(topic = PulsarConfig.bootObjTopic,
            clazz = MessageDTO.class)
    public void consumer(MessageDTO message) {
        log.info("接收到对象消息:content: {}",message);
    }

    @io.github.majusko.pulsar.annotation.PulsarConsumer(topic = PulsarConfig.myStringTopic,
            clazz = String.class)
    public void consumerStringMsg(String message) {
        log.info("接收到字符串消息:content: {}",message);
    }

}
