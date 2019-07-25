package com.abril.tvapi.application;

import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.LogInDto;
import com.abril.tvapi.entity.dto.SignInDto;
import com.abril.tvapi.entity.dto.UserDto;
import com.abril.tvapi.repository.UserRepository;
import com.abril.tvapi.services.SecurityService;
import com.abril.tvapi.services.RedisService;
import org.json.JSONObject;
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

    @Autowired
    private RedisService redisService;

    @Autowired
    private SecurityService securityService;

    public ResponseEntity<SignInDto> saveUser(UserDto userDto) throws Exception {
        Assert.notNull(userDto, "userDto no debe ser null");
        Assert.notNull(userDto.getName(), "name no debe ser null");
        Assert.notNull(userDto.getLastName(), "lastName no debe ser null");
        Assert.notNull(userDto.getUserName(), "userName no debe ser null");
        Assert.notNull(userDto.getBirthday(), "birthday no debe ser null");
        Assert.notNull(userDto.getEmail(), "email no debe ser null");
        Assert.notNull(userDto.getPassword(), "password no debe ser null");
        Assert.notNull(userDto.getRole(), "role no debe ser null");
        Assert.hasText(userDto.getName(), "name no debe ser vacio");
        Assert.hasText(userDto.getLastName(), "lastName no debe ser vacio");
        Assert.hasText(userDto.getUserName(), "userName no debe ser vacio");
        Assert.hasText(userDto.getEmail(), "email no debe ser vacio");
        Assert.hasText(userDto.getPassword(), "password no debe ser vacio");
        Assert.hasText(userDto.getRole(), "role no debe ser vacio");

        Boolean existsUserName = this.userRepository.existsByUserName(userDto.getUserName());
        if(existsUserName){
            throw new Exception("Ya existe un usuario con ese userName");
        }

        Boolean existsEmail = this.userRepository.existsByEmail(userDto.getEmail());
        if(existsEmail){
            throw new Exception("Ya existe un usuario con ese email");
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

        String token = this.securityService.encode(save.getUserName());
        this.redisService.saveValue(save.getUserName(), token);

        SignInDto signInDto = new SignInDto();
        signInDto.setToken(token);
        signInDto.setId(save.getId());
        signInDto.setUserName(save.getUserName());

        return new ResponseEntity<>(signInDto, HttpStatus.OK);
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

        Boolean statusIsPresent = Optional.of(status).filter(y -> y.equals(User.USER_STATUS_ACTIVE) ||
                y.equals(User.USER_STATUS_INACTIVE)).isPresent();
        Assert.isTrue(statusIsPresent, "No existe el status " + status);

        User user = optionalUser.get();
        user.setStatus(status);

        User update = this.userRepository.save(user);
        if(update == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    public ResponseEntity<LogInDto> logIn(LogInDto logInDto) throws Exception {
        Assert.notNull(logInDto, "logInDto no debe ser null");

        String userName = logInDto.getUserName();
        String password = logInDto.getPassword();

        Assert.notNull(userName, "email no debe ser null");
        Assert.notNull(password, "password no debe ser null");
        Assert.hasText(userName, "email no debe ser vacio");
        Assert.hasText(password, "password no debe ser vacio");

        Optional<User> optionalUser = this.userRepository.findByUserName(userName);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese userName");

        User user = optionalUser.get();

        Boolean correctStatus = Optional.of(user.getStatus()).filter(y -> y.equals(User.USER_STATUS_ACTIVE)).isPresent();
        Assert.isTrue(correctStatus, "El usuario debe tener status " + User.USER_STATUS_ACTIVE);

        Boolean correctPassword = BCrypt.checkpw(password, user.getPassword());
        Assert.isTrue(correctPassword, "La contraseña es incorrecta");

        String token = "";
        Optional<JSONObject> redisUser = Optional.ofNullable(this.redisService.getValue(userName));

        if(redisUser.isPresent()){
            token = redisUser.get().getString("token");
        }else{
            token = this.securityService.getToken(userName);
        }

        logInDto.setToken(token);
        logInDto.setId(user.getId());

        return new ResponseEntity<>(logInDto, HttpStatus.OK);
    }
}
