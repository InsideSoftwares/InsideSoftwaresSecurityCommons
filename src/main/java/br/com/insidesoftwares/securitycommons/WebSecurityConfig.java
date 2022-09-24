package br.com.insidesoftwares.securitycommons;

import br.com.insidesoftwares.securitycommons.exception.AccessDeniedExceptionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;

public class WebSecurityConfig {

    public static SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AccessDeniedExceptionHandler accessDeniedExceptionHandler,
            GenericFilterBean genericFilterBean,
            Filter corsFilter
    ) throws Exception {
        httpSecurity.authorizeHttpRequests(
                auth -> auth
                        .antMatchers("/**").permitAll()
                        .antMatchers("/api/**").denyAll()
                        .antMatchers("/api/authentication/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .addFilterBefore(
                        corsFilter,
                        SessionManagementFilter.class
                )
                .addFilterBefore(
                        genericFilterBean,
                        UsernamePasswordAuthenticationFilter.class
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
