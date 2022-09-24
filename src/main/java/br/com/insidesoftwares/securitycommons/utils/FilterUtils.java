package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.commons.enums.InsideSoftwaresExceptionCode;
import br.com.insidesoftwares.commons.specification.LocaleUtils;
import br.com.insidesoftwares.execption.model.ExceptionResponse;
import br.com.insidesoftwares.securitycommons.configuration.properties.CorsProperties;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final CorsProperties corsProperties;
    private final Gson gson;

    public void setLocale(HttpServletRequest request){
        String language = request.getHeader("Accept-Language");
        Locale locale = Objects.nonNull(language) ? Locale.forLanguageTag(language) : Locale.forLanguageTag("pt-BR");
        LocaleContextHolder.setDefaultLocale(locale);
    }

    public void enableCors(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", corsProperties.getAllowOrigin());
        response.setHeader("Access-Control-Allow-Methods", corsProperties.getAllowMethods());
        response.setHeader("Access-Control-Allow-Headers", corsProperties.getAllowHeaders());
        response.setHeader("Access-Control-Allow-Credentials", corsProperties.getAllowCredentials());
        response.setHeader("Access-Control-Max-Age", corsProperties.getMaxAge());
    }

    public void setUnauthorizedResponse(HttpServletResponse response, JWTErro error) throws IOException {

        try {
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .codeError(error.getCode())
                    .message(localeUtils.getMessage(error.getCode()))
                    .build();

            String body = gson.toJson(
                    AuthenticationUtils.createResponse(exceptionResponse)
            );

            AuthenticationUtils.createResponseHttpServlet(response, body);
        } catch (Exception e) {
            log.error("Error setUnauthorizedResponse", e);
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .codeError(InsideSoftwaresExceptionCode.GENERIC.getCode())
                    .message(localeUtils.getMessage(InsideSoftwaresExceptionCode.GENERIC.getCode()))
                    .build();
            String body = gson.toJson(
                    AuthenticationUtils.createResponse(exceptionResponse)
            );

            AuthenticationUtils.createResponseHttpServlet(response, body);
        }
    }

}
