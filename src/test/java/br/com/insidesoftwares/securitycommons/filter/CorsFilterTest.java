package br.com.insidesoftwares.securitycommons.filter;

import br.com.insidesoftwares.securitycommons.LocaleUtilsBean;
import br.com.insidesoftwares.securitycommons.configuration.properties.CorsProperties;
import br.com.insidesoftwares.securitycommons.utils.FilterUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CorsFilter.class, FilterUtils.class, CorsProperties.class, LocaleUtilsBean.class, Gson.class})
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class CorsFilterTest {

    @Autowired
    private CorsFilter corsFilter;

    private final static String ALLOW_CREDENTIALS = "true";
    private final static String ALLOW_METHODS = "GET,POST,DELETE,PUT,OPTIONS";
    private final static String ALLOW_HEADERS = "*";
    private final static String MAX_AGE = "1800";
    private final static String ALLOW_ORIGIN = "*";

    @Test
    void doFilter() throws ServletException, IOException {

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        corsFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);

        assertEquals(ALLOW_ORIGIN, mockHttpServletResponse.getHeader("Access-Control-Allow-Origin"));
        assertEquals(ALLOW_METHODS, mockHttpServletResponse.getHeader("Access-Control-Allow-Methods"));
        assertEquals(ALLOW_HEADERS, mockHttpServletResponse.getHeader("Access-Control-Allow-Headers"));
        assertEquals(ALLOW_CREDENTIALS, mockHttpServletResponse.getHeader("Access-Control-Allow-Credentials"));
        assertEquals(MAX_AGE, mockHttpServletResponse.getHeader("Access-Control-Max-Age"));

        assertEquals(mockHttpServletRequest, filterChain.getRequest());
        assertEquals(mockHttpServletResponse, filterChain.getResponse());

    }
}