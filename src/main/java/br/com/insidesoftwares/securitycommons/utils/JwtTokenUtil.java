package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.securitycommons.configuration.properties.JWTProperties;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final JWTProperties jwtProperties;

    private HMACSigner hmacSigner;

    @PostConstruct
    private void inicializa(){
        this.hmacSigner = HMACSigner.newSHA512Signer(jwtProperties.getSecret());
    }

    public String generateToken(String login,  String email) {
        return doGenerateToken(login, email);
    }

    private String doGenerateToken(String login,  String email) {
        JWT jwt = new JWT().setIssuer("ISSecurity")
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(login)
                .setUniqueId(email)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(jwtProperties.getValid()));
        return JWT.getEncoder().encode(jwt, hmacSigner);
    }


    public long timeExpiration(){
        return jwtProperties.getValid()*60;
    }

}
