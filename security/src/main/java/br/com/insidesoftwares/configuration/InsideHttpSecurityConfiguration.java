package br.com.insidesoftwares.configuration;

import br.com.insidesoftwares.exception.AccessDeniedExceptionHandler;
import br.com.insidesoftwares.filter.InsideCorsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.SessionManagementFilter;

@AutoConfiguration
@RequiredArgsConstructor
public class InsideHttpSecurityConfiguration {

    private final InsideCorsFilter corsFilter;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @Primary
    @Bean("InsideHttpSecurityConfiguration")
    public HttpSecurity insideHttpSecurityConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/swagger-ui/**", "**/api-docs/**", "/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedExceptionHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(
                        corsFilter,
                        SessionManagementFilter.class
                );
        return httpSecurity;
    }

}
