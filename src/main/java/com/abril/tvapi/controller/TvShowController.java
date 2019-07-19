package com.abril.tvapi.controller;

import com.abril.tvapi.application.TvShowApplication;
import com.abril.tvapi.entity.dto.TvShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tvshow")
public class TvShowController {

    @Autowired
    TvShowApplication tvShowApplication;

    @PostMapping
    public TvShowDto saveTvShow(@RequestBody TvShowDto tvShowDto) throws Exception {
        return this.tvShowApplication.saveTvShow(tvShowDto);
    }

    @PutMapping(value = "{id}")
    public TvShowDto updateTvShow(@PathVariable("id") Integer id,
                                  @RequestBody TvShowDto tvShowDto) throws Exception {
        return this.tvShowApplication.updateTvShow(id, tvShowDto);
    }

    @DeleteMapping
    public String deleteTvShow(@PathVariable("idTvShow") Integer idTvShow,
                               @RequestParam("id") Integer idUser) throws Exception {
        return this.tvShowApplication.deleteTvShow(idTvShow, idUser);
    }
}
