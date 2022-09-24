package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.securitycommons.configuration.properties.JWTProperties;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {JwtTokenUtil.class, JWTProperties.class})
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JWTProperties jwtProperties;

    private final static String LOGIN = "LOGIN";
    private final static String EMAIL = "email@email.com";

    private final static LocalDateTime DATE_NOW = LocalDateTime.of(2022,8,26, 15, 25, 55);
    private final static ZonedDateTime ZONED_DATE_TIME_NOW = ZonedDateTime.of(DATE_NOW, ZoneId.of("America/Sao_Paulo"));

    @Test
    void generateToken() {

        try (MockedStatic<ZonedDateTime> utilities = Mockito.mockStatic(ZonedDateTime.class)) {
            utilities.when(() -> ZonedDateTime.now(ZoneOffset.UTC)).thenReturn(ZONED_DATE_TIME_NOW);

            when(jwtProperties.getSecret()).thenReturn("SecretJWT");
            when(jwtProperties.getValid()).thenReturn(26L);

            HMACSigner hmacSigner = HMACSigner.newSHA512Signer(jwtProperties.getSecret());
            JWT jwt = new JWT().setIssuer("ISSecurity")
                    .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                    .setSubject(LOGIN)
                    .setUniqueId(EMAIL)
                    .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(jwtProperties.getValid()));
            String jwtExpected = JWT.getEncoder().encode(jwt, hmacSigner);

            String jwtResult = jwtTokenUtil.generateToken(LOGIN, EMAIL);

            assertEquals(jwtExpected, jwtResult);
        }
    }

    @Test
    void timeExpiration() {
        when(jwtProperties.getValid()).thenReturn(60L);
        long timeExpirationExpected = jwtProperties.getValid() * 60;

        assertEquals(timeExpirationExpected, jwtTokenUtil.timeExpiration());
    }
}