package com.practice.e2021.validate2log.controller;

import com.practice.e2021.validate2log.bean.RspDTO;
import com.practice.e2021.validate2log.dto.UserDTO;
import com.practice.e2021.validate2log.service.UserService;
import com.practice.e2021.validate2log.validator.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save/valid")
    public RspDTO save(@RequestBody @Validated UserDTO userDTO) {
        userService.save(userDTO);
        return RspDTO.success();
    }

    @PostMapping("/update/groups")
    public RspDTO update(@RequestBody @Validated(Update.class) UserDTO userDTO) {
        userService.updateById(userDTO);
        return RspDTO.success();
    }
}
