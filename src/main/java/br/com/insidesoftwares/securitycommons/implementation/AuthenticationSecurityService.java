package br.com.insidesoftwares.securitycommons.implementation;

import br.com.insidesoftwares.securitycommons.dto.AuthenticationDTO;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationSecurityService {

    AuthenticationDTO authentication(HttpServletRequest request);

}
