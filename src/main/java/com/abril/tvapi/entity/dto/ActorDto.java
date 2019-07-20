package com.abril.tvapi.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ActorDto {

    private Integer id;

    private String name;

    private String lastName;

    private Date birthday;

    private String nationality;
}
