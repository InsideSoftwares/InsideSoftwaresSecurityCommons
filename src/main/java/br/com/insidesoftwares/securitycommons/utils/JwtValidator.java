package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.securitycommons.configuration.properties.JWTProperties;
import br.com.insidesoftwares.securitycommons.dto.JwtDTO;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import io.fusionauth.jwt.InvalidJWTSignatureException;
import io.fusionauth.jwt.JWTException;
import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtValidator {

    private final JWTProperties jwtProperties;

    public JwtDTO validToken(String token) {
        Verifier verifier = HMACVerifier.newVerifier(jwtProperties.getSecret());
        JWT jwt = null;
        JWTErro jwtValidation = null;
        try {
            jwt = JWT.getDecoder().decode(token, verifier);
        } catch (InvalidJWTSignatureException invalidJWTSignatureException){
            jwtValidation = JWTErro.SIGNING_ERRO;
            log.error("Error InvalidJWTSignatureException", invalidJWTSignatureException);
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
