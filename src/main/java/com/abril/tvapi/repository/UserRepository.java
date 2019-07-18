package com.abril.tvapi.repository;

import com.abril.tvapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    User findUserById(@Param("id") Integer id);
}
