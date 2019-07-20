package com.abril.tvapi.controller;

import com.abril.tvapi.application.DirectorApplication;
import com.abril.tvapi.entity.dto.DirectorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/director")
public class DirectorController {

    @Autowired
    DirectorApplication directorApplication;

    @PostMapping
    public DirectorDto saveDirector(DirectorDto directorDto){
        return this.directorApplication.saveDirector(directorDto);
    }

    @PutMapping
    public String updateDirector(DirectorDto directorDto){
        return this.directorApplication.updateDirector(directorDto);
    }

    @DeleteMapping(value = "{idDirector}/user")
    public String deleteDirector(@PathVariable("idDirector") Integer idDirector,
                                 @RequestParam("id") Integer idUser){
        return this.directorApplication.deleteDirector(idDirector, idUser);
    }
}
