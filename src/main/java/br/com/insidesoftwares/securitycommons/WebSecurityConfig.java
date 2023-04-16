package br.com.insidesoftwares.securitycommons;

import br.com.insidesoftwares.securitycommons.exception.AccessDeniedExceptionHandler;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionManagementFilter;

import jakarta.servlet.Filter;

public class WebSecurityConfig {

    public static SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AccessDeniedExceptionHandler accessDeniedExceptionHandler,
            Filter corsFilter
    ) throws Exception {

        httpSecurity.apply(AadResourceServerHttpSecurityConfigurer.aadResourceServer())
                .and()
                // All the paths that match `/api/**`(configurable) work as the resource server. Other paths work as the web application.
                .securityMatcher("/api/**")
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .addFilterBefore(
                        corsFilter,
                        SessionManagementFilter.class
                )
               .exceptionHandling().accessDeniedHandler(accessDeniedExceptionHandler);
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.sessionManagement() // dont create a session for this configuration
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);

        return httpSecurity.build();
    }

    public static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

}
