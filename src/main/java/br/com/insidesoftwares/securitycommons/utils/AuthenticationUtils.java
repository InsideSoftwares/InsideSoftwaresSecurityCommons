package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.commons.dto.response.InsideSoftwaresResponse;
import br.com.insidesoftwares.execption.model.ExceptionResponse;
import br.com.insidesoftwares.securitycommons.entity.InsideSoftwaresCredentials;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationUtils {

    static final String HEADER_STRING = "Authorization";

    public static String getTokenAuthorization(final HttpServletRequest request){
        return request.getHeader(HEADER_STRING);
    }

    public static InsideSoftwaresResponse<ExceptionResponse> createResponse(ExceptionResponse exceptionResponse){
        return InsideSoftwaresResponse.<ExceptionResponse>builder()
                .data(exceptionResponse)
                .build();
    }

    public static void createResponseHttpServlet(HttpServletResponse response, String body) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }

    public static InsideSoftwaresCredentials getInsideSoftwaresCredentials(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (InsideSoftwaresCredentials) auth.getCredentials();
    }
}
