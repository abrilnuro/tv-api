package com.abril.tvapi.controller;

import com.abril.tvapi.application.UserApplication;
import com.abril.tvapi.entity.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserApplication userApplication;

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) throws Exception {
        return this.userApplication.saveUser(userDto);
    }

    @PutMapping
    public String updateUser(@RequestBody UserDto userDto) throws Exception {
        return this.userApplication.updateUser(userDto);
    }

    @GetMapping
    public UserDto logIn(@RequestParam("email") String email,
                         @RequestParam("password") String password) throws Exception {
        return this.userApplication.logIn(email, password);
    }
}
