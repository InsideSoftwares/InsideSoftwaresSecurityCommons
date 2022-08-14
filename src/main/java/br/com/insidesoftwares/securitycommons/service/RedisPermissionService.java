package br.com.insidesoftwares.securitycommons.service;

import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;
import br.com.insidesoftwares.securitycommons.implementation.PermissionService;
import br.com.insidesoftwares.securitycommons.repository.AuthenticatedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class RedisPermissionService implements PermissionService {

    private final AuthenticatedUserRepository userRepository;

    @Override
    public Set<SimpleGrantedAuthority> permissionByToken(String token) {
        AtomicReference<Set<String>> permissions = new AtomicReference<>(Collections.emptySet());

        Optional<AuthenticatedUser> authenticatedUser = userRepository.findById(token);
        authenticatedUser.ifPresent(user -> permissions.set(user.getPermissions()));

        return permissions.get()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
