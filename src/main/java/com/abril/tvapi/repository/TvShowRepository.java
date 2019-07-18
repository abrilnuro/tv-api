package com.abril.tvapi.repository;

import com.abril.tvapi.entity.TvShow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface TvShowRepository extends CrudRepository<TvShow, Integer> {

    TvShow findByName( @Param("name") String name);

    TvShow findTvShowById( @Param("id") Integer id);
}
