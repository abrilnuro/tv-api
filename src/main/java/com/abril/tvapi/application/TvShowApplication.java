package com.abril.tvapi.application;

import com.abril.tvapi.entity.TvShow;
import com.abril.tvapi.entity.dto.TvShowDto;
import com.abril.tvapi.repository.TvShowRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TvShowApplication {

    @Autowired
    TvShowRepository tvShowRepository;

    private TvShow findByName(String name){
        Assert.hasText(name, "name no sebe ser empty");
        return this.tvShowRepository.findByName(name);
    }

    public TvShowDto saveTvShow(TvShowDto tvShowDto) throws Exception {
        Assert.notNull(tvShowDto, "tvShowDto no debe ser null");
        Assert.notNull(tvShowDto.getName(), "name no debe ser null");
        Assert.notNull(tvShowDto.getGenre(), "genre no debe ser null");
        Assert.notNull(tvShowDto.getLanguage(), "language no debe ser null");
        Assert.notNull(tvShowDto.getResume(), "resume no debe ser null");
        Assert.notNull(tvShowDto.getPoster(), "poster no debe ser null");
        Assert.hasText(tvShowDto.getName(), "name no debe ser empty");
        Assert.hasText(tvShowDto.getGenre(), "genre no debe ser empty");
        Assert.hasText(tvShowDto.getLanguage(), "language no debe ser empty");
        Assert.hasText(tvShowDto.getResume(), "resume no debe ser empty");
        Assert.hasText(tvShowDto.getPoster(), "poster no debe ser empty");

        TvShow tvShow = this.findByName(tvShowDto.getName());
        if(tvShow != null){
            throw new Exception("Ya existe un tv show con el nombre: " + tvShow.getName());
        }

        tvShow = new TvShow();
        BeanUtils.copyProperties(tvShowDto, tvShow);
        this.tvShowRepository.save(tvShow);
        return tvShowDto;
    }
}
