package br.com.insidesoftwares.securitycommons.implementation;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public interface PermissionService {

    Set<SimpleGrantedAuthority> permissionByToken(final String token);

}
