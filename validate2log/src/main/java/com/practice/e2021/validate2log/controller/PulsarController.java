package com.practice.e2021.validate2log.controller;

import com.practice.e2021.validate2log.bean.MessageDTO;
import com.practice.e2021.validate2log.bean.RspDTO;
import com.practice.e2021.validate2log.mq.PulsarProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消息pulsar")
@RequestMapping("pulsar")
@RestController
public class PulsarController {

    @Autowired
    private PulsarProducer pulsarProducer;

    @ApiOperation("发送对象消息")
    @PostMapping("send")
    public RspDTO sendObjMessage(@RequestBody MessageDTO messageDTO) {
        pulsarProducer.send(messageDTO);
        return RspDTO.success();
    }

    @ApiOperation("发送字符串消息")
    @PostMapping("sendStr")
    public RspDTO sendStrMessage(@RequestBody String messageDTO) {
        pulsarProducer.sendStringMsg(messageDTO);
        return RspDTO.success();
    }
}
