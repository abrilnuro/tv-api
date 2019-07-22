package com.abril.tvapi.repository;

import com.abril.tvapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.Optional;

@RestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUserName(@Param("userName") String userName);

    Boolean existsByUserName(@Param("userName") String userName);

    Boolean existsByEmail(@Param("email") String email);
}
