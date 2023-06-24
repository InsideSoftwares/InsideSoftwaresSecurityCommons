package br.com.insidesoftwares.securitycommons.exception;

import br.com.insidesoftwares.commons.specification.LocaleUtils;
import br.com.insidesoftwares.exception.AccessDeniedExceptionHandler;
import br.com.insidesoftwares.securitycommons.LocaleUtilsBean;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AccessDeniedExceptionHandler.class, LocaleUtilsBean.class, Gson.class})
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class AccessDeniedExceptionHandlerTest {

    @InjectMocks
    private AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @Mock
    private LocaleUtils localeUtils;

    private final Gson gson = new Gson();

    private final static String MESSAGE_ERROR = "Message Error LocaleUtils";
    private final static String BODY_RESPONSE = "{\"data\":{\"message\":\"Message Error LocaleUtils\",\"codeError\":\"INS-004\"}}";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(accessDeniedExceptionHandler, "gson", gson);
    }

    @Test
    void handle() throws IOException {
        when(localeUtils.getMessage(anyString())).thenReturn(MESSAGE_ERROR);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("");

        accessDeniedExceptionHandler.handle(httpServletRequest, mockHttpServletResponse, accessDeniedException);

        assertEquals(BODY_RESPONSE, mockHttpServletResponse.getContentAsString());
        assertEquals(HttpStatus.UNAUTHORIZED.value(),mockHttpServletResponse.getStatus());
    }
}