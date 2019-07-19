package com.abril.tvapi.application;

import com.abril.tvapi.entity.TvShow;
import com.abril.tvapi.entity.User;
import com.abril.tvapi.entity.dto.TvShowDto;
import com.abril.tvapi.repository.TvShowRepository;
import com.abril.tvapi.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
public class TvShowApplication {

    @Autowired
    TvShowRepository tvShowRepository;

    @Autowired
    UserRepository userRepository;

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

    public TvShowDto updateTvShow(Integer idTvShow, TvShowDto tvShowDto) throws Exception {
        Assert.notNull(tvShowDto, "idTvShow no debe ser null");
        Assert.notNull(idTvShow, "id no debe ser null");

        TvShow tvShow = this.tvShowRepository.findTvShowById(idTvShow);
        if(tvShow == null){
            throw new Exception("No existe tv show con ese id");
        }

        BeanUtils.copyProperties(tvShowDto, tvShow);
        tvShow.setId(idTvShow);
        this.tvShowRepository.save(tvShow);

        return tvShowDto;
    }

    public String deleteTvShow(Integer idTvShow, Integer idUser) throws Exception {
        Assert.notNull(idTvShow, "idTvShow no debe ser null");
        Assert.notNull(idUser, "idUser no debe ser null");

        Optional<User> optionalUser = this.userRepository.findById(idUser);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        User user = optionalUser.get();

        if(user.getStatus().equals(User.USER_STATUS_INACTIVO)){
            throw new Exception("El usuario se encuentra con estatus inactivo");
        }

        if(user.getRole().equals(User.USER_ROLE_USER)){
            throw new Exception("Los usuarios con rol de " + User.USER_ROLE_USER + " no pueden borrar registros");
        }

        this.tvShowRepository.deleteById(idTvShow);
        return "Se borró registro";
    }
}


