package br.com.insidesoftwares.filter;

import br.com.insidesoftwares.commons.enums.InsideSoftwaresExceptionCode;
import br.com.insidesoftwares.commons.specification.LocaleUtils;
import br.com.insidesoftwares.exception.model.ExceptionResponse;
import br.com.insidesoftwares.utils.AuthenticationUtils;
import com.google.gson.Gson;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Order(99)
@RequiredArgsConstructor
@Log4j2
public class AuthorizationRequiredFilter implements Filter {

    private final LocaleUtils localeUtils;
    private final Gson gson;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        if(req.getRequestURI().contains(contextPath+"/api")) {
            HttpServletResponse res = (HttpServletResponse) servletResponse;
            checkAuthorization(req, res);
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private void checkAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .codeError(InsideSoftwaresExceptionCode.TOKEN_NOT_PROVIDED.getCode())
                    .message(localeUtils.getMessage(InsideSoftwaresExceptionCode.TOKEN_NOT_PROVIDED.getCode()))
                    .build();

            String body = gson.toJson(
                    AuthenticationUtils.createResponse(exceptionResponse)
            );

            AuthenticationUtils.createResponseHttpServlet(response, body);
            ContentCachingResponseWrapper servletResponse = new ContentCachingResponseWrapper(response);
            servletResponse.copyBodyToResponse();
        }
    }
}
