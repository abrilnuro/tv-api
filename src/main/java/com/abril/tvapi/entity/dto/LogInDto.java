package com.abril.tvapi.entity.dto;

import lombok.Data;

@Data
public class LogInDto {

    private Integer id;

    private String userName;

    private String password;

    private String token;
}
