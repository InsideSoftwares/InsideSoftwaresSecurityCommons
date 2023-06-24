package br.com.insidesoftwares.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Data
@Component
@ConfigurationProperties
@RefreshScope
public class CorsProperties {

    @Value("${cors-security.allowOrigin:*}")
    private String allowOrigin;
    @Value("${cors-security.allowMethods:GET,POST,DELETE,PUT,OPTIONS}")
    private String allowMethods;
    @Value("${cors-security.allowHeaders:*}")
    private String allowHeaders;
    @Value("${cors-security.allowCredentials:true}")
    private String allowCredentials;
    @Value("${cors-security.maxAge:1800}")
    private String maxAge;

    public List<String> allowOrigin() {
        return Arrays.stream(allowOrigin.split(",")).toList();
    }

    public List<String> allowMethods() {
        return Arrays.stream(allowMethods.split(",")).toList();
    }

    public List<String> allowHeaders() {
        return Arrays.stream(allowHeaders.split(",")).toList();
    }

    public Long maxAge() {
        return Long.parseLong(maxAge);
    }

    public Boolean allowCredentials() {
        return Boolean.parseBoolean(allowCredentials);
    }
}
