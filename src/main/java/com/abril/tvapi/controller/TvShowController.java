package com.abril.tvapi.controller;

import com.abril.tvapi.application.TvShowApplication;
import com.abril.tvapi.entity.dto.TvShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tvshow")
public class TvShowController {

    @Autowired
    TvShowApplication tvShowApplication;

    @PostMapping
    public ResponseEntity<TvShowDto> saveTvShow(@RequestBody TvShowDto tvShowDto) throws Exception {
        return this.tvShowApplication.saveTvShow(tvShowDto);
    }

    @PutMapping()
    public ResponseEntity<TvShowDto> updateTvShow(@RequestBody TvShowDto tvShowDto) throws Exception {
        return this.tvShowApplication.updateTvShow(tvShowDto);
    }

    @DeleteMapping(value = "{idTvShow}/user")
    public String deleteTvShow(@PathVariable("idTvShow") Integer idTvShow,
                               @RequestParam("id") Integer idUser) throws Exception {
        return this.tvShowApplication.deleteTvShow(idTvShow, idUser);
    }
}
