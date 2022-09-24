package br.com.insidesoftwares.securitycommons.service;

import br.com.insidesoftwares.securitycommons.dto.AuthenticationDTO;
import br.com.insidesoftwares.securitycommons.dto.JwtDTO;
import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;
import br.com.insidesoftwares.securitycommons.entity.InsideSoftwaresAuthentication;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import br.com.insidesoftwares.securitycommons.implementation.AccessTokenService;
import br.com.insidesoftwares.securitycommons.implementation.AuthenticationSecurityService;
import br.com.insidesoftwares.securitycommons.utils.AuthenticationUtils;
import br.com.insidesoftwares.securitycommons.utils.JwtValidator;
import io.fusionauth.jwt.domain.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
class AuthenticationSecurityBean implements AuthenticationSecurityService {

    private final JwtValidator jwtValidator;
    private final AccessTokenService accessTokenService;

    @Override
    public AuthenticationDTO authentication(final HttpServletRequest request){
        String identifier = null;
        boolean valid = false;
        JWTErro jwtErro = null;
        Authentication authentication = null;

        try{
            String token = AuthenticationUtils.getTokenAuthorization(request);
            if (Objects.nonNull(token)) {
                if (token.matches("(Bearer\\s)(.+)")) {
                    token = token.replace("Bearer ", "");
                    JwtDTO jwtDTO = jwtValidator.validToken(token);
                    jwtErro = jwtDTO.getJwtErro();
                    if (Objects.nonNull(jwtDTO.getJwt())) {
                        JWT jwt = jwtDTO.getJwt();
                        identifier = jwt.uniqueId;
                        AuthenticatedUser authenticatedUser = accessTokenService.findAuthenticatedUserByToken(token);
                        authentication = InsideSoftwaresAuthentication.authenticated(
                                authenticatedUser.getUsername(),
                                authenticatedUser.createInsideSoftwaresCredentials(),
                                authenticatedUser.getSimpleGrantedAuthority()
                        );
                        valid = true;
                    }
                }
            } else {
                jwtErro = JWTErro.NOT_HAS_TOKEN;
            }
        } catch (Exception e){
            log.error("Error authentication valid", e);
            jwtErro = JWTErro.GENERIC;
        }
        return AuthenticationDTO.builder()
                .identifier(identifier)
                .valid(valid)
                .jwtErro(jwtErro)
                .authentication(authentication)
                .build();
    }

}
