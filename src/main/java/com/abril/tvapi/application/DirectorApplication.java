package com.abril.tvapi.application;

import com.abril.tvapi.entity.Director;
import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.DirectorDto;
import com.abril.tvapi.repository.DirectorRepository;
import com.abril.tvapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.Optional;

@Component
public class DirectorApplication {

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    UserRepository userRepository;

    public Optional<Director> findByName(String name){
        Assert.notNull(name, "name no debe ser null");
        Assert.hasText(name, "name no debe ser vacio");
        return this.directorRepository.findByName(name);
    }

    public DirectorDto saveDirector(DirectorDto directorDto){
        Assert.notNull(directorDto, "directorDto no debe ser null");
        Assert.notNull(directorDto.getName(), "name no debe ser null");
        Assert.notNull(directorDto.getLastName(), "last name no debe ser null");
        Assert.notNull(directorDto.getBirthday(), "birthday no debe ser null");
        Assert.notNull(directorDto.getNationality(), "nationality no debe ser null");
        Assert.hasText(directorDto.getName(), "name no debe ser vacio");
        Assert.hasText(directorDto.getLastName(), "last name no debe ser vacio");
        Assert.hasText(directorDto.getNationality(), "nationality no debe ser vacio");

        Director director = new Director();
        BeanUtils.copyProperties(directorDto, director);
        this.directorRepository.save(director);

        return directorDto;
    }

    public String updateDirector(DirectorDto directorDto){
        Assert.notNull(directorDto, "directorDto no debe ser null");

        Optional<Director> optionalDirector = this.directorRepository.findById(directorDto.getId());
        Assert.notNull(optionalDirector.isPresent(), "No existe director con ese id");

        Director director = new Director();
        BeanUtils.copyProperties(directorDto, director);

        this.directorRepository.save(director);

        return "se modificó el director";
    }

    public String deleteDirector(Integer idDirector, Integer idUser){
        Assert.notNull(idDirector, "idDirector no debe ser null");
        Assert.notNull(idUser, "idUser no debe ser null");

        Optional<Director> optionalDirector = this.directorRepository.findById(idDirector);
        Assert.isTrue(optionalDirector.isPresent(), "No existe director con ese id");

        Optional<User> optionalUser = this.userRepository.findById(idUser);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        User user = optionalUser.get();

        Boolean userStatus = Optional.of(user.getStatus()).filter(y -> y.equals(User.USER_STATUS_ACTIVE)).isPresent();
        Assert.isTrue(userStatus,"El usuario se encuentra con estatus inactivo");

        Boolean userRole = Optional.of(user.getRole()).filter(y -> y.equals(User.USER_ROLE_ADMIN)).isPresent();
        Assert.isTrue(userRole, "Los usuarios con rol " + user.getRole() + " no pueden borrar registros");

        this.directorRepository.deleteById(idDirector);

        return "se eliminó actor";
    }

}
