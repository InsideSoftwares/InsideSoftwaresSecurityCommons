package br.com.insidesoftwares.securitycommons.implementation;

import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;
import java.util.Set;

public interface AccessTokenService {

    Set<SimpleGrantedAuthority> permissionByToken(final String token);
    Long getExpirationTokenByToken(final String token);

    Optional<AuthenticatedUser> findTokenByUsername(final String username);

    void saveToken(final AuthenticatedUser authenticatedUser);
    void deleteTokenByToken(final String token);

}
