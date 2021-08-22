package com.practice.e2021.validate2log.mq;

import com.practice.e2021.validate2log.bean.MessageDTO;
import com.practice.e2021.validate2log.config.PulsarConfig;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PulsarProducer {

    @Autowired
    private PulsarTemplate<MessageDTO> pulsarTemplate;

    @Autowired
    private PulsarTemplate<String> StringPulsarTemplate;

    public void send(MessageDTO message) {
        try {
            pulsarTemplate.send(PulsarConfig.bootObjTopic, message);
        } catch (PulsarClientException e) {
            log.error("[PulsarProducer-send err], params:" + message, e);
        }
    }

    public void sendStringMsg(String message) {
        try {
            StringPulsarTemplate.send(PulsarConfig.myStringTopic, message);
        } catch (PulsarClientException e) {
            log.error("[PulsarProducer-send err], params:" + message, e);
        }
    }
}
