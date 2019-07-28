package com.abril.tvapi.configuration;

import com.abril.tvapi.services.RedisService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SecurityServiceMiddleWare extends OncePerRequestFilter {

  @Autowired
  RedisService redisService;

  @Autowired
  RedisConfig redisConfig;

  private static final String[] TO_EXCLUDE = {
      "/api/user/log-in", "/api/user/sign-in"
  };

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String currentEndpoint = request.getRequestURI();

    if (Arrays.stream(TO_EXCLUDE).anyMatch(currentEndpoint::equals)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = request.getHeader("token");
    Assert.notNull(token, "El token no debe ser nulo");
    Assert.hasText(token, "El token no debe ser vacio");

    String userName = request.getHeader("username");
    Assert.notNull(userName, "El userName no debe ser nulo");
    Assert.hasText(userName, "El userName no debe ser vacio");

    if(this.redisConfig.redisIsAvalible ()){
      JSONObject jsonObject = null;
      try {
        jsonObject = this.redisService.getValue(userName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Assert.notNull(jsonObject, "Ocurrió un problema con tu sesión");

      String storedToken = jsonObject.getString("token");
      Assert.notNull(storedToken, "El token almacenado es null");
      Assert.hasText(storedToken, "El token almacenado es vacio");

      Boolean correctToken = Optional.ofNullable(storedToken).filter(y -> y.equals(token)).isPresent();
      Assert.isTrue(correctToken, "El token recibido no es correcto");

      filterChain.doFilter(request, response);
    }
  }

}
