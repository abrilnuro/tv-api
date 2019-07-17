package com.abril.tvapi.entity.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserDto {

    private String name;

    private String lastName;

    private String email;

    private String password;

    private Date birthday;

    private String role;
}
