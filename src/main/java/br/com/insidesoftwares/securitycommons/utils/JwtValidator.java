package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.securitycommons.dto.JwtDTO;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import io.fusionauth.jwt.*;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtValidator {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.valid}")
    private long valid;

    public JwtDTO validToken(String token) {
        Verifier verifier = HMACVerifier.newVerifier(secret);
        JWT jwt = null;
        JWTErro jwtValidation = null;
        try {
            jwt = JWT.getDecoder().decode(token, verifier);
        } catch (JWTSigningException jwtSigningException){
            jwtValidation = JWTErro.SIGNING_ERRO;
            log.error("Error JWTSigningException", jwtSigningException);
        } catch (JWTVerifierException jwtVerifierException){
            jwtValidation = JWTErro.VERIFIER_ERRO;
            log.error("Error jwtVerifierException", jwtVerifierException);
        } catch (JWTExpiredException jwtExpiredException){
            jwtValidation = JWTErro.EXPIRED;
            log.error("Error jwtExpiredException", jwtExpiredException);
        } catch (JWTException jwtException){
            jwtValidation = JWTErro.GENERIC;
            log.error("Error jwtException", jwtException);
        }
        return JwtDTO.builder()
                .jwt(jwt)
                .jwtErro(jwtValidation)
                .build();
    }

}
