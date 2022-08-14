package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.commons.specification.LocaleUtils;
import br.com.insidesoftwares.execption.enums.Exception;
import br.com.insidesoftwares.execption.model.ExceptionResponse;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Log4j2
public class FilterUtils {

    private final LocaleUtils localeUtils;
    private final Gson gson;

    public void setLocale(HttpServletRequest request){
        String language = request.getHeader("Accept-Language");
        Locale locale = Objects.nonNull(language) ? Locale.forLanguageTag(language) : Locale.forLanguageTag("pt-BR");
        LocaleContextHolder.setDefaultLocale(locale);
    }

    public void enableCors(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "1800");
    }

    public void setUnauthorizedResponse(HttpServletResponse response, JWTErro error) throws IOException {

        try {
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .codeError(error.getCode())
                    .message(localeUtils.getMessage(error.getCode()))
                    .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(gson.toJson(exceptionResponse));
        } catch (java.lang.Exception e) {
            log.error("Error setUnauthorizedResponse", e);
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .codeError(Exception.GENERIC.getCode())
                    .message(localeUtils.getMessage(Exception.GENERIC.getCode()))
                    .build();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(exceptionResponse));
        }
    }

}
