package br.com.insidesoftwares.securitycommons.filter;

import br.com.insidesoftwares.commons.specification.LocaleUtils;
import br.com.insidesoftwares.securitycommons.configuration.properties.CorsProperties;
import br.com.insidesoftwares.securitycommons.dto.AuthenticationDTO;
import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import br.com.insidesoftwares.securitycommons.implementation.AuthenticationSecurityService;
import br.com.insidesoftwares.securitycommons.utils.FilterUtils;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class AuthenticationFilterTest {

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private AuthenticationSecurityService authenticationSecurityService;

    @Mock
    private LocaleUtils localeUtils;
    @Mock
    private CorsProperties corsProperties;
    private final Gson gson = new Gson();

    @BeforeEach
    void init(){
        FilterUtils filterUtils = new FilterUtils(localeUtils, corsProperties, gson);
        ReflectionTestUtils.setField(authenticationFilter, "filterUtils", filterUtils);
    }

    @Test
    void doFilterNotAuthentication() throws ServletException, IOException {

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        authenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);

        assertEquals(mockHttpServletRequest, filterChain.getRequest());
        assertEquals(mockHttpServletResponse, filterChain.getResponse());

    }

    @Test
    void doFilterWithAuthenticationSuccess() throws ServletException, IOException {

        when(authenticationSecurityService.authentication(any())).thenReturn(createAuthenticationDTO(true));

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        mockHttpServletRequest.setServletPath("test/api/v1/test_authentication/user");

        authenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);

        assertEquals("",  mockHttpServletResponse.getContentAsString());
        assertEquals(mockHttpServletRequest, filterChain.getRequest());
        assertEquals(mockHttpServletResponse, filterChain.getResponse());

    }

    @Test
    void doFilterWithNotAuthenticationSuccess() throws ServletException, IOException {

        when(authenticationSecurityService.authentication(any())).thenReturn(createAuthenticationDTO(false));

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        mockHttpServletRequest.setServletPath("test/api/v1/test_authentication/user");

        authenticationFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, filterChain);

        assertEquals("{\"data\":{\"codeError\":\"AUTH-001\"}}",  mockHttpServletResponse.getContentAsString());
        assertNull(filterChain.getRequest());
        assertNull(filterChain.getResponse());

    }

    private AuthenticationDTO createAuthenticationDTO(boolean success){
        return AuthenticationDTO.builder()
                .valid(success)
                .identifier("User")
                .authentication(
                        new UsernamePasswordAuthenticationToken("User", null, Collections.emptySet())
                )
                .jwtErro(JWTErro.GENERIC)
                .build();
    }
}