package br.com.insidesoftwares.securitycommons.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


public class AuthenticationUtils {

    static final String HEADER_STRING = "Authorization";

    public static String getTokenAuthorization(final HttpServletRequest request){
        return request.getHeader(HEADER_STRING);
    }

}
