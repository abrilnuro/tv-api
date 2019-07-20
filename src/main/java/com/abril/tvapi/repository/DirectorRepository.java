package com.abril.tvapi.repository;

import com.abril.tvapi.entity.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RestResource(exported = false)
public interface DirectorRepository extends CrudRepository<Director, Integer> {

    Optional<Director> findByName(@Param("name") String name);

}
