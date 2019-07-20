package com.abril.tvapi.configuration;

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

  private static final String[] TO_EXCLUDE = {
      "/api/user"
  };

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String currentEndpoint = request.getRequestURI();

    if (Arrays.stream(TO_EXCLUDE).anyMatch(currentEndpoint::equals)) {
      filterChain.doFilter(request, response);
      return;
    }

    filterChain.doFilter(request, response);
  }

}
