package br.com.insidesoftwares.securitycommons.service;

import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;
import br.com.insidesoftwares.securitycommons.implementation.AccessTokenService;
import br.com.insidesoftwares.securitycommons.repository.AuthenticatedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisAccessTokenBean implements AccessTokenService {

    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Set<SimpleGrantedAuthority> permissionByToken(String token) {
        AtomicReference<Set<String>> permissions = new AtomicReference<>(Collections.emptySet());

        Optional<AuthenticatedUser> authenticatedUser = authenticatedUserRepository.findById(token);
        authenticatedUser.ifPresent(user -> permissions.set(user.getPermissions()));

        return permissions.get()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Long getExpirationTokenByToken(String token) {
        String key = String.format("AuthenticatedUser:%s",token);
        return redisTemplate.getExpire(key);
    }

    @Override
    public Optional<AuthenticatedUser> findTokenByUsername(String username) {
        return authenticatedUserRepository.findByUsername(username);
    }

    @Override
    public void saveToken(AuthenticatedUser authenticatedUser) {
        authenticatedUserRepository.save(authenticatedUser);
    }

    @Override
    public void deleteTokenByToken(String token) {
        authenticatedUserRepository.deleteById(token);
    }
}
