package br.com.insidesoftwares.securitycommons.utils;

import br.com.insidesoftwares.commons.dto.response.InsideSoftwaresResponse;
import br.com.insidesoftwares.exception.model.ExceptionResponse;
import br.com.insidesoftwares.utils.AuthenticationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthenticationUtilsTest {

    private final static String ACCESS_TOKEN = "Access Token";
    private final static String CODE_ERRO = "Access Token";
    private final static String MESSAGE_ERRO = "Access Token";
    private final static String BODY_RESPONSE = "{\"error\": \"ERROR\";}";

    @Test
    void getTokenAuthorization() {
        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.addHeader(AuthenticationUtils.HEADER_STRING, ACCESS_TOKEN);
        String tokenAuthorizationResult = AuthenticationUtils.getTokenAuthorization(requestMock);

        assertEquals(ACCESS_TOKEN, tokenAuthorizationResult);
    }

    @Test
    void createResponse() {
        InsideSoftwaresResponse<ExceptionResponse> insideSoftwaresResponseExpected =  InsideSoftwaresResponse.<ExceptionResponse>builder()
                .data(createExceptionResponse())
                .build();

        InsideSoftwaresResponse<ExceptionResponse> insideSoftwaresResponseResult = AuthenticationUtils.createResponse(createExceptionResponse());

        assertEquals(insideSoftwaresResponseExpected.getData().getCodeError(), insideSoftwaresResponseResult.getData().getCodeError());
        assertEquals(insideSoftwaresResponseExpected.getData().getMessage(), insideSoftwaresResponseResult.getData().getMessage());
        assertNull(insideSoftwaresResponseResult.getData().getValidationErrors());
    }

    @Test
    void createResponseHttpServlet() throws IOException {
        MockHttpServletResponse responseMock = new MockHttpServletResponse();

        AuthenticationUtils.createResponseHttpServlet(responseMock, BODY_RESPONSE);

        assertEquals(BODY_RESPONSE, responseMock.getContentAsString());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseMock.getStatus());
    }

    private ExceptionResponse createExceptionResponse() {
        return ExceptionResponse.builder()
                .codeError(CODE_ERRO)
                .message(MESSAGE_ERRO)
                .build();
    }
}