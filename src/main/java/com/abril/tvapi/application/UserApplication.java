package com.abril.tvapi.application;

import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.UserDto;
import com.abril.tvapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.Optional;

@Component
public class UserApplication {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
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

        String password = BCrypt.hashpw(userDto.getPassword() ,BCrypt.gensalt());
        user.setPassword(password);

        this.userRepository.save(user);

        return userDto;
    }

    public String updateUser(UserDto userDto) throws Exception {
        Assert.notNull(userDto, "userDto no debe ser null");
        Assert.notNull(userDto.getId(), "id no debe ser null");

        Optional<User> optionalUser = this.userRepository.findById(userDto.getId());
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        User user = optionalUser.get();
        BeanUtils.copyProperties(userDto, user);
        this.userRepository.save(user);

        return "se modificó user";
    }

    public String updateStatus(Integer id, String status) throws Exception {
        Assert.notNull(id, "id no debe ser null");
        Assert.notNull(status, "status no debe ser null");
        Assert.hasText(status, "status no debe ser vacio");

        Optional<User> optionalUser = this.userRepository.findById(id);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        if(!status.equals(User.USER_STATUS_ACTIVO) && !status.equals(User.USER_STATUS_INACTIVO)){
            throw new Exception("No existe el status " + status);
        }

        User user = optionalUser.get();
        user.setStatus(status);

        this.userRepository.save(user);

        return "se modificó status de user";
    }

    public UserDto logIn(String email, String password) throws Exception {
        Assert.notNull(email, "email no debe ser null");
        Assert.notNull(password, "password no debe ser null");
        Assert.hasText(email, "email no debe ser vacio");
        Assert.hasText(password, "password no debe ser vacio");

        User user = this.findByEmail(email);
        if(user == null){
            throw new Exception("No existe usuario con ese email");
        }

        Boolean correctPassword = BCrypt.checkpw(password, user.getPassword());
        if(!correctPassword){
            throw new Exception("La contraseña es incorrecta");
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }
}
