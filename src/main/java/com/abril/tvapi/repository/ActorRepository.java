package com.abril.tvapi.repository;

import com.abril.tvapi.entity.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.Optional;

@RestResource(exported = false)
public interface ActorRepository extends CrudRepository<Actor, Integer> {

    Optional<Actor> findByName(@Param("name") String name);
}
