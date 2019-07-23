package com.abril.tvapi.entity.dto;

import lombok.Data;

@Data
public class SignInDto {

    private Integer id;

    private String userName;

    private String token;
}
