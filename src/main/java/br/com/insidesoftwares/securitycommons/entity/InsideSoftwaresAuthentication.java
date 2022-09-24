package br.com.insidesoftwares.securitycommons.entity;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Collection;

public class InsideSoftwaresAuthentication extends AbstractAuthenticationToken {

    private final String principal;
    private InsideSoftwaresCredentials credentials;

    private InsideSoftwaresAuthentication(String principal, InsideSoftwaresCredentials credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    private InsideSoftwaresAuthentication(String principal, InsideSoftwaresCredentials credentials, Collection<InsideSoftwaresGrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static InsideSoftwaresAuthentication unauthenticated(String principal, InsideSoftwaresCredentials credentials) {
        return new InsideSoftwaresAuthentication(principal, credentials);
    }

    public static InsideSoftwaresAuthentication authenticated(String principal, InsideSoftwaresCredentials credentials, Collection<InsideSoftwaresGrantedAuthority> authorities) {
        return new InsideSoftwaresAuthentication(principal, credentials, authorities);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
