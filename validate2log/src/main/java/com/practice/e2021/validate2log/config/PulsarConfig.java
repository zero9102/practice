package com.practice.e2021.validate2log.config;

import com.practice.e2021.validate2log.bean.MessageDTO;
import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  https://www.jianshu.com/p/6d6fcfff43f0
 */
@Configuration
public class PulsarConfig {

    public static final String bootObjTopic = "bootTopic";

    public static final String myStringTopic = "myTopic";
//    @Bean
//    public ProducerFactory producerFactory() {
//        return new ProducerFactory()
//                .addProducer(bootObjTopic, MessageDTO.class)
//                .addProducer(myStringTopic, String.class);
//    }
}
