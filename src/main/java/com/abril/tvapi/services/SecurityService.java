package com.abril.tvapi.services;

import com.abril.tvapi.configuration.GlobalConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

@Service
public class SecurityService {

    @Autowired
    GlobalConfig globalConfig;

    @Autowired
    RedisService redisService;

    public String encode(String user) {
        Assert.notNull(user, "user no debe ser null");
        Assert.hasText(user, "user no debe ser vacio");

        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.globalConfig.getJwtSecret());
            token = JWT.create()
                    .withSubject(user)
                    .withExpiresAt(new Date())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTVerificationException("Error en token");
        }

        return token;
    }

    public String decode(String token) {
        Assert.notNull(token, "token no debe ser null");
        Assert.hasText(token, "token no debe ser vacio");

        String subject;
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.globalConfig.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            subject = jwt.getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error en token");
        }

        return subject;
    }

    public String getToken(String userName) throws Exception {
        Assert.notNull(userName, "user no debe ser null");
        Assert.hasText(userName, "user no debe ser vacio");

        String token = this.encode(userName);
        JSONObject jsonObject = new JSONObject().put("token", token);
        this.redisService.setIfIsAvailable(userName, jsonObject);

        return token;
    }
}
