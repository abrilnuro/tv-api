package com.abril.tvapi.application;

import com.abril.tvapi.entity.Actor;
import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.ActorDto;
import com.abril.tvapi.repository.ActorRepository;
import com.abril.tvapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.Optional;

@Component
public class ActorApplication {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Actor> findByName(String name){
        Assert.notNull(name, "name no debe ser null");
        Assert.hasText(name, "name no debe ser vacio");
        return this.actorRepository.findByName(name);
    }

    public ActorDto saveActor(ActorDto actorDto){
        Assert.notNull(actorDto, "actorDto no debe ser null");
        Assert.notNull(actorDto.getName(), "name no debe ser null");
        Assert.notNull(actorDto.getLastName(), "last name no debe ser null");
        Assert.notNull(actorDto.getBirthday(), "birthday no debe ser null");
        Assert.notNull(actorDto.getNationality(), "nationality no debe ser null");
        Assert.hasText(actorDto.getName(), "name no debe ser vacio");
        Assert.hasText(actorDto.getLastName(), "last name no debe ser vacio");
        Assert.hasText(actorDto.getNationality(), "nationality no debe ser vacio");

        Actor actor = new Actor();
        BeanUtils.copyProperties(actorDto, actor);
        this.actorRepository.save(actor);

        return actorDto;
    }

    public String updateActor(ActorDto actorDto){
        Assert.notNull(actorDto, "actorDto no debe ser null");

        Optional<Actor> optionalActor = this.actorRepository.findById(actorDto.getId());
        Assert.notNull(optionalActor.isPresent(), "No existe actor con ese id");

        Actor actor = new Actor();
        BeanUtils.copyProperties(actorDto, actor);

        this.actorRepository.save(actor);

        return "se modificó el actor";
    }

    public String deleteActor(Integer idActor, Integer idUser){
        Assert.notNull(idActor, "idActor no debe ser null");
        Assert.notNull(idUser, "idUser no debe ser null");

        Optional<Actor> optionalActor = this.actorRepository.findById(idActor);
        Assert.isTrue(optionalActor.isPresent(), "No existe actor con ese id");

        Optional<User> optionalUser = this.userRepository.findById(idUser);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        User user = optionalUser.get();

        Boolean userStatus = Optional.of(user.getStatus()).filter(y -> y.equals(User.USER_STATUS_ACTIVO)).isPresent();
        Assert.isTrue(userStatus,"El usuario se encuentra con estatus inactivo");

        Boolean userRole = Optional.of(user.getRole()).filter(y -> y.equals(User.USER_ROLE_ADMIN)).isPresent();
        Assert.isTrue(userRole, "Los usuarios con rol " + user.getRole() + " no pueden borrar registros");

        this.actorRepository.deleteById(idActor);

        return "se eliminó actor";
    }

}
