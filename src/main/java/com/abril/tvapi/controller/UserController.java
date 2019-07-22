package com.abril.tvapi.controller;

import com.abril.tvapi.application.UserApplication;
import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.LogInDto;
import com.abril.tvapi.entity.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserApplication userApplication;

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) throws Exception {
         return this.userApplication.saveUser(userDto);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto) throws Exception {
        return this.userApplication.updateUser(userDto);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<User> updateStatus(@PathVariable("id") Integer id,
                               @RequestParam("status") String status ) throws Exception {
        return this.userApplication.updateStatus(id, status);
    }

    @PostMapping("login")
    public ResponseEntity<LogInDto> logIn(@RequestBody LogInDto logInDto) throws Exception {
        return this.userApplication.logIn(logInDto);
    }
}
