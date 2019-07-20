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

    private Optional<TvShow> findByName(String name){
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

        Optional<TvShow> optionalTvShow = this.findByName(tvShowDto.getName());
        if(optionalTvShow.isPresent()){
            throw new Exception("Ya existe un tv show con ese nombre");
        }

        TvShow tvShow = new TvShow();
        BeanUtils.copyProperties(tvShowDto, tvShow);
        this.tvShowRepository.save(tvShow);

        return tvShowDto;
    }

    public TvShowDto updateTvShow(Integer idTvShow, TvShowDto tvShowDto) throws Exception {
        Assert.notNull(tvShowDto, "idTvShow no debe ser null");
        Assert.notNull(idTvShow, "id no debe ser null");

        Optional<TvShow> optionalTvShow = this.tvShowRepository.findById(idTvShow);
        Assert.isTrue(optionalTvShow.isPresent(), "No existe tv show con ese id");

        TvShow tvShow = optionalTvShow.get();
        BeanUtils.copyProperties(tvShowDto, tvShow);
        tvShow.setId(idTvShow);
        this.tvShowRepository.save(tvShow);

        return tvShowDto;
    }

    public String deleteTvShow(Integer idTvShow, Integer idUser) throws Exception {
        Assert.notNull(idTvShow, "idTvShow no debe ser null");
        Assert.notNull(idUser, "idUser no debe ser null");

        Boolean existsTvShow = this.tvShowRepository.findById(idTvShow).isPresent();
        Assert.isTrue(existsTvShow, "No existe tv show con ese id");

        Optional<User> optionalUser = this.userRepository.findById(idUser);
        Assert.isTrue(optionalUser.isPresent(), "No existe usuario con ese id");

        User user = optionalUser.get();

        Boolean status = Optional.of(user.getStatus()).filter(y -> y.equals(User.USER_STATUS_ACTIVO)).isPresent();
        Assert.isTrue(status,"El usuario se encuentra con estatus inactivo");

        Boolean role = Optional.of(user.getRole()).filter(y -> y.equals(User.USER_ROLE_ADMIN)).isPresent();
        Assert.isTrue(role, "Los usuarios con rol de " + user.getRole() + " no pueden borrar registros");

        this.tvShowRepository.deleteById(idTvShow);

        return "Se borró registro";
    }
}


