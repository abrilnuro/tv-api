package com.abril.tvapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "tvShow")
public class TvShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String resume;

    private Date releaseDate;

    private String language;

    private String poster;

    private String genre;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Mazatlan")
    private Date created_at;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Mazatlan")
    private Date updated_at;
}
