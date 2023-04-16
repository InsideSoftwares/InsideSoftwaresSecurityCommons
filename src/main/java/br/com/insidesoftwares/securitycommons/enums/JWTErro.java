package br.com.insidesoftwares.securitycommons.enums;

import br.com.insidesoftwares.commons.specification.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JWTErro implements ExceptionCode {
    GENERIC("AUTH-001"),
    EXPIRED("AUTH-002"),
    SIGNING_ERRO("AUTH-003"),
    VERIFIER_ERRO("AUTH-004"),
    NOT_HAS_TOKEN("AUTH-005"),
    USER_TOKEN_INVALID("AUTH-006");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }
}
