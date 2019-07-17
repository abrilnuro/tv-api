package com.abril.tvapi.controller;

import com.abril.tvapi.application.UserApplication;
import com.abril.tvapi.entity.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserApplication userApplication;

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) throws Exception {
        return this.userApplication.saveUser(userDto);
    }
}
