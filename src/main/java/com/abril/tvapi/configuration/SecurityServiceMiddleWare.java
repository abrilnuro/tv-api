package com.abril.tvapi.configuration;

import com.abril.tvapi.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class SecurityServiceMiddleWare extends OncePerRequestFilter {

  @Autowired
  RedisService redisService;

  private static final String[] TO_EXCLUDE = {
      "/api/user/logIn"
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
    //validar que el token del header
    RedisService redisService = this.redisService.get(token);

    filterChain.doFilter(request, response);
  }

}
