package com.abril.tvapi.application;

import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.UserDto;
import com.abril.tvapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.Optional;

@Component
public class UserApplication {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email){
        Assert.notNull(email, "email no debe ser null");
        Assert.hasText(email, "email no debe ser vacio");
        return this.userRepository.findByEmail(email);
    }

    public ResponseEntity<User> saveUser(UserDto userDto) throws Exception {
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


        Optional<User> optionalUser = this.findByEmail(userDto.getEmail());
        if(optionalUser.isPresent()){
            throw new Exception("Ya existe un usuario con ese correo");
        }

        String role = userDto.getRole();
        Boolean existsRole = Optional.of(role).filter(y -> y.equals(User.USER_ROLE_ADMIN) ||
                y.equals(User.USER_ROLE_USER)).isPresent();
        Assert.isTrue(existsRole, "No existe el role de usuario: " + role);

        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        String password = BCrypt.hashpw(userDto.getPassword() ,BCrypt.gensalt());
        user.setPassword(password);

        User save = this.userRepository.save(user);

        if(save == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    public ResponseEntity<User> updateUser(UserDto userDto) {
        Assert.notNull(userDto, "userDto no debe ser null");
        Assert.notNull(userDto.getId(), "id no debe ser null");

        Optional<User> optionalUser = this.userRepository.findById(userDto.getId());
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        User user = optionalUser.get();
        BeanUtils.copyProperties(userDto, user);

        if(userDto.getPassword() != null){
            user.setPassword(BCrypt.hashpw(userDto.getPassword() ,BCrypt.gensalt()));
        }

        User update = this.userRepository.save(user);
        if(update == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    public ResponseEntity<User> updateStatus(Integer id, String status) throws Exception {
        Assert.notNull(id, "id no debe ser null");
        Assert.notNull(status, "status no debe ser null");
        Assert.hasText(status, "status no debe ser vacio");

        Optional<User> optionalUser = this.userRepository.findById(id);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        Boolean statusIsPresent = Optional.of(status).filter(y -> y.equals(User.USER_STATUS_ACTIVO) ||
                y.equals(User.USER_STATUS_INACTIVO)).isPresent();
        Assert.isTrue(statusIsPresent, "No existe el status " + status);

        User user = optionalUser.get();
        user.setStatus(status);

        User update = this.userRepository.save(user);
        if(update == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    public ResponseEntity<User> logIn(String email, String password) throws Exception {
        Assert.notNull(email, "email no debe ser null");
        Assert.notNull(password, "password no debe ser null");
        Assert.hasText(email, "email no debe ser vacio");
        Assert.hasText(password, "password no debe ser vacio");

        Optional<User> optionalUser = this.findByEmail(email);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese email");

        User user = optionalUser.get();

        Boolean correctPassword = BCrypt.checkpw(password, user.getPassword());
        Assert.isTrue(correctPassword, "La contraseña es incorrecta");

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
