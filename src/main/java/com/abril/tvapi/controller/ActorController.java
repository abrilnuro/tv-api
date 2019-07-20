package com.abril.tvapi.controller;

import com.abril.tvapi.application.ActorApplication;
import com.abril.tvapi.entity.dto.ActorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/actor")
public class ActorController {

    @Autowired
    ActorApplication actorApplication;

    @PostMapping
    public ActorDto saveActor(ActorDto actorDto){
        return this.actorApplication.saveActor(actorDto);
    }

    @PutMapping
    public String updateActor(ActorDto actorDto){
        return this.actorApplication.updateActor(actorDto);
    }

    @DeleteMapping(value = "{idActor}/user")
    public String deleteActor(@PathVariable("idActor") Integer idDirector,
                                 @RequestParam("id") Integer idUser){
        return this.actorApplication.deleteActor(idDirector, idUser);
    }
}
