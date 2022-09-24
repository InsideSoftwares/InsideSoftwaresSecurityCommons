package br.com.insidesoftwares.securitycommons.service;

import br.com.insidesoftwares.commons.enums.InsideSoftwaresExceptionCode;
import br.com.insidesoftwares.execption.error.InsideSoftwaresException;
import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;
import br.com.insidesoftwares.securitycommons.implementation.AccessTokenService;
import br.com.insidesoftwares.securitycommons.repository.AuthenticatedUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
class RedisAccessTokenBean implements AccessTokenService {

    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public AuthenticatedUser findAuthenticatedUserByToken(final String token) {
        Optional<AuthenticatedUser> authenticatedUser = authenticatedUserRepository.findById(token);
        return authenticatedUser.orElseThrow(() -> new InsideSoftwaresException(InsideSoftwaresExceptionCode.GENERIC));
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
