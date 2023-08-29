package br.com.insidesoftwares.azureactivedirectory;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(@Qualifier("InsideHttpSecurityConfiguration") HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .apply(AadResourceServerHttpSecurityConfigurer.aadResourceServer());
        return httpSecurity.build();
    }

}
