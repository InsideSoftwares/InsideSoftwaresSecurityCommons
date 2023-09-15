package br.com.insidesoftwares.exception;

import br.com.insidesoftwares.commons.enums.InsideSoftwaresExceptionCode;
import br.com.insidesoftwares.commons.specification.LocaleService;
import br.com.insidesoftwares.exception.model.ExceptionResponse;
import br.com.insidesoftwares.utils.AuthenticationUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private final LocaleService localeService;
    private final Gson gson;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .codeError(InsideSoftwaresExceptionCode.ACCESS_DENIED.getCode())
                .message(localeService.getMessage(InsideSoftwaresExceptionCode.ACCESS_DENIED.getCode()))
                .build();

        String body = gson.toJson(
                AuthenticationUtils.createResponse(exceptionResponse)
        );

        AuthenticationUtils.createResponseHttpServlet(response, body);

    }
}