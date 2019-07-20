package com.abril.tvapi.repository;

import com.abril.tvapi.entity.TvShow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RestResource(exported = false)
public interface TvShowRepository extends CrudRepository<TvShow, Integer> {

    Optional<TvShow> findByName(@Param("name") String name);
}
