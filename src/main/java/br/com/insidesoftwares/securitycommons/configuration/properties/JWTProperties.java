package br.com.insidesoftwares.securitycommons.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties
@RefreshScope
public class JWTProperties {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.valid}")
    private long valid;

}
