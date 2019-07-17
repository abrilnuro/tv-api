package com.abril.tvapi.application;

import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.UserDto;
import com.abril.tvapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class UserApplication {

    @Autowired
    private UserRepository userRepository;

    private User findByEmail(String email){
        Assert.notNull(email, "email no debe ser null");
        Assert.hasText(email, "email no debe ser vacio");
        return this.userRepository.findByEmail(email);
    }

    public UserDto saveUser(UserDto userDto) throws Exception {
        Assert.notNull(userDto, "userDto no debe ser null");
        Assert.notNull(userDto.getName(), "name no debe ser null");
        Assert.notNull(userDto.getLastName(), "lastName no debe ser null");
        Assert.notNull(userDto.getBirthday(), "birthday no debe ser null");
        Assert.notNull(userDto.getEmail(), "email no debe ser null");
        Assert.notNull(userDto.getPassword(), "password no debe ser null");
        Assert.notNull(userDto.getRole(), "role no debe ser null");
        Assert.hasText(userDto.getName(), "name no debe ser vacio");
        Assert.hasText(userDto.getLastName(), "lastName no debe ser vacio");
        Assert.hasText(userDto.getEmail(), "email no debe ser vacio");
        Assert.hasText(userDto.getPassword(), "password no debe ser vacio");
        Assert.hasText(userDto.getRole(), "role no debe ser vacio");

        User user = this.findByEmail(userDto.getEmail());
        if(user != null){
            throw new Exception("Ya existe un usuario con ese correo");
        }

        String role = userDto.getRole();
        if(!role.contains(User.USER_ROLE_ADMIN) && !role.contains(User.USER_ROLE_USER) ){
            throw new Exception("No existe el role de usuario: " + role);
        }

        user = new User();
        BeanUtils.copyProperties(userDto, user);
        this.userRepository.save(user);
        return userDto;
    }
}
