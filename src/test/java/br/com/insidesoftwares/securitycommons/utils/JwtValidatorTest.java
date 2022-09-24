package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.securitycommons.configuration.properties.JWTProperties;
import br.com.insidesoftwares.securitycommons.dto.JwtDTO;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {JwtValidator.class, JWTProperties.class})
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class JwtValidatorTest {

    @Autowired
    private JwtValidator jwtValidator;

    @Mock
    private JWTProperties jwtProperties;

    private final static String TOKEN_VALID = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjk2NjE1Mzc0NTUsImlhdCI6MTY2MTUzMzg1NSwiaXNzIjoiSVNTZWN1cml0eSIsInN1YiI6IkFETUlOIiwianRpIjoiYWRtaW4uc3lzdGVtQGluc2lkZXNvZnR3YXJlLmNvbS5iciJ9.4n5BDpfQ_XxQ3tT1zMD3jFbUqof5HYuAoSuA7qYjIsS98DNLuMF4zxxonTcoGcEilCEZXCayk1uGUSfx9AQYkQ";
    private final static String TOKEN_ERROR_SIGNING = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjk2NjE1Mzc0NTUsImlhdCI6MTY2MTUzMzg1NSwiaXNzIjoiSVNTZWN1cml0eSIsInN1YiI6IkFETUlOIiwianRpIjoiYWRtaW4uc3lzdGVtQGluc2lkZXNvZnR3YXJlLmNvbS5iciJ9.QEzsGPv0ZXRitO8z750OertK4J262MGBoL9KFJkrKAz5XA5ObwbrJhxAEWn18ifeAsG5_1WhrbkjE6mg0rgLqw";
    private final static String TOKEN_ERROR_GENERIC = "eyJleHAiOjE2NjE1Mzc0NTUsImlhdCI6MTY2MTUzMzg1NSwiaXNzIjoiSVNTZWN1cml0eSIsInN1YiI6IkFETUlOIiwianRpIjoiYWRtaW4uc3lzdGVtQGluc2lkZXNvZnR3YXJlLmNvbS5iciJ9.udUfJHs-geYemhd0mpFrSvg99zLxKoKK_YFuhzlO-EPvaGK_vyDTfNsyyO6FrQWBeDmZ0pfKoQcn9y-FGcex7w";
    private final static String TOKEN_ERROR_EXPIRED = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjY2MTUzNzQ1NSwiaWF0IjoxNjYxNTMzODU1LCJpc3MiOiJJU1NlY3VyaXR5Iiwic3ViIjoiQURNSU4iLCJqdGkiOiJhZG1pbi5zeXN0ZW1AaW5zaWRlc29mdHdhcmUuY29tLmJyIn0._Jl4IC_NXYCctHJ8ixWZCSRoTUjL4iK9RITTIQLFYjySdJycTq-4LNB8iI-wiRKSMyw_Xvg2utimrxMj9yVDVA";

    @Test
    void validToken() {
        when(jwtProperties.getSecret()).thenReturn("SecretJWT");

        JwtDTO jwtDTO = jwtValidator.validToken(TOKEN_VALID);
        assertNotNull(jwtDTO.getJwt());
        assertEquals("ISSecurity", jwtDTO.getJwt().issuer);
        assertEquals("ADMIN", jwtDTO.getJwt().subject);
        assertEquals("admin.system@insidesoftware.com.br", jwtDTO.getJwt().uniqueId);
        assertNull(jwtDTO.getJwtErro());
    }

    @Test
    void validTokenWithErrorSIGNING_ERRO() {
        when(jwtProperties.getSecret()).thenReturn("Secret");

        JwtDTO jwtDTO = jwtValidator.validToken(TOKEN_ERROR_SIGNING);
        assertNull(jwtDTO.getJwt());
        assertEquals(JWTErro.SIGNING_ERRO, jwtDTO.getJwtErro());
    }

    @Test
    void validTokenWithErrorEXPIRED() {
        when(jwtProperties.getSecret()).thenReturn("SecretJWT");

        JwtDTO jwtDTO = jwtValidator.validToken(TOKEN_ERROR_EXPIRED);
        assertNull(jwtDTO.getJwt());
        assertEquals(JWTErro.EXPIRED, jwtDTO.getJwtErro());
    }

    @Test
    void validTokenWithGENERIC() {
        JwtDTO jwtDTO = jwtValidator.validToken(TOKEN_ERROR_GENERIC);

        assertEquals(JWTErro.GENERIC, jwtDTO.getJwtErro());
        assertNull(jwtDTO.getJwt());
    }
}