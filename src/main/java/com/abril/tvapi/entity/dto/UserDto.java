package com.abril.tvapi.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class UserDto {

    private Integer id;

    private String name;

    private String lastName;

    private String email;

    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Mazatlan")
    private Date birthday;

    private String role;
}
