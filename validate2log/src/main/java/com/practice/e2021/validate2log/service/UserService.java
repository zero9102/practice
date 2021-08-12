package com.practice.e2021.validate2log.service;

import com.practice.e2021.validate2log.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {


    public void save(UserDTO userDTO) {
        log.info("UserService-save-userDTO: {}", userDTO);
    }

    public void updateById(UserDTO userDTO) {
        log.info("UserService-updateById-userDTO: {}", userDTO);
    }
}
