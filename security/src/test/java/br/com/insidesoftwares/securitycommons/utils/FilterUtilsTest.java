package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.configuration.properties.CorsProperties;
import br.com.insidesoftwares.securitycommons.LocaleUtilsBean;
import br.com.insidesoftwares.utils.FilterUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {FilterUtils.class, CorsProperties.class, LocaleUtilsBean.class, Gson.class})
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class FilterUtilsTest {

    @InjectMocks
    private FilterUtils filterUtils;
    @Mock
    private CorsProperties corsProperties;

    private final static String ALLOW_CREDENTIALS = "true";
    private final static String ALLOW_METHODS = "GET,DELETE";
    private final static String ALLOW_HEADERS = "*";
    private final static String MAX_AGE = "1200";
    private final static String ALLOW_ORIGIN = "http://localhost:5596";

    @Test
    void setLocale() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("Accept-Language", "ja_JP");

        filterUtils.setLocale(httpServletRequest);

        Locale localeExpected = Locale.forLanguageTag("ja_JP");
        Locale localeResult = LocaleContextHolder.getLocale(LocaleContextHolder.getLocaleContext());
        assertEquals(localeExpected, localeResult);
    }

    @Test
    void setLocaleWithAcceptLanguageNull() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        filterUtils.setLocale(httpServletRequest);

        Locale localeExpected = Locale.forLanguageTag("pt-BR");
        Locale localeResult = LocaleContextHolder.getLocale(LocaleContextHolder.getLocaleContext());
        assertEquals(localeExpected, localeResult);
    }

    @Test
    void enableCors() {
        when(corsProperties.getAllowCredentials()).thenReturn(ALLOW_CREDENTIALS);
        when(corsProperties.getAllowMethods()).thenReturn(ALLOW_METHODS);
        when(corsProperties.getAllowHeaders()).thenReturn(ALLOW_HEADERS);
        when(corsProperties.getMaxAge()).thenReturn(MAX_AGE);
        when(corsProperties.getAllowOrigin()).thenReturn(ALLOW_ORIGIN);

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        filterUtils.enableCors(mockHttpServletResponse);
        assertEquals(ALLOW_ORIGIN, mockHttpServletResponse.getHeader("Access-Control-Allow-Origin"));
        assertEquals(ALLOW_METHODS, mockHttpServletResponse.getHeader("Access-Control-Allow-Methods"));
        assertEquals(ALLOW_HEADERS, mockHttpServletResponse.getHeader("Access-Control-Allow-Headers"));
        assertEquals(ALLOW_CREDENTIALS, mockHttpServletResponse.getHeader("Access-Control-Allow-Credentials"));
        assertEquals(MAX_AGE, mockHttpServletResponse.getHeader("Access-Control-Max-Age"));
    }
}