package com.abril.tvapi.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TvShowDto {

    private Integer id;

    private String name;

    private String language;

    private String genre;

    private String resume;

    private String poster;

    private Date releaseDate;
}
