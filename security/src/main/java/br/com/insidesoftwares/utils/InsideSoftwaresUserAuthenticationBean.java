package br.com.insidesoftwares.utils;

import br.com.insidesoftwares.commons.specification.InsideSoftwaresUserAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InsideSoftwaresUserAuthenticationBean implements InsideSoftwaresUserAuthentication {
    @Override
    public String findUserAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
