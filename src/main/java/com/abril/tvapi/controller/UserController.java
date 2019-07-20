package com.abril.tvapi.controller;

import com.abril.tvapi.application.UserApplication;
import com.abril.tvapi.entity.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PutMapping(value = "{id}")
    public String updateStatus(@PathVariable("id") Integer id,
                               @RequestParam("status") String status ) throws Exception {
        return this.userApplication.updateStatus(id, status);
    }

    @GetMapping
    public UserDto logIn(@RequestParam("email") String email,
                         @RequestParam("password") String password) throws Exception {
        return this.userApplication.logIn(email, password);
    }
}
