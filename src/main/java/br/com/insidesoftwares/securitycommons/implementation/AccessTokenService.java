package br.com.insidesoftwares.securitycommons.implementation;

import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;

import java.util.Optional;

public interface AccessTokenService {

    AuthenticatedUser findAuthenticatedUserByToken(final String token);
    Long getExpirationTokenByToken(final String token);

    Optional<AuthenticatedUser> findTokenByUsername(final String username);

    void saveToken(final AuthenticatedUser authenticatedUser);
    void deleteTokenByToken(final String token);

}
