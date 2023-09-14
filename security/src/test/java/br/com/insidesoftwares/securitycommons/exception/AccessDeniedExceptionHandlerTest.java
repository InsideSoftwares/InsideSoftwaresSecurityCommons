package br.com.insidesoftwares.securitycommons.exception;

import br.com.insidesoftwares.commons.specification.LocaleService;
import br.com.insidesoftwares.exception.AccessDeniedExceptionHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessDeniedExceptionHandlerTest {

    @InjectMocks
    private AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @Mock
    private LocaleService localeService;

    private final Gson gson = new Gson();

    private final static String MESSAGE_ERROR = "Message Error LocaleUtils";
    private final static String BODY_RESPONSE = "{\"data\":{\"message\":\"Message Error LocaleUtils\",\"codeError\":\"INS-004\"}}";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(accessDeniedExceptionHandler, "gson", gson);
    }

    @Test
    void handle() throws IOException {
        when(localeService.getMessage(anyString())).thenReturn(MESSAGE_ERROR);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("");

        accessDeniedExceptionHandler.handle(httpServletRequest, mockHttpServletResponse, accessDeniedException);

        assertEquals(BODY_RESPONSE, mockHttpServletResponse.getContentAsString());
        assertEquals(HttpStatus.UNAUTHORIZED.value(),mockHttpServletResponse.getStatus());
    }
}