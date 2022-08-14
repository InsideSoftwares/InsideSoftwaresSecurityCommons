package br.com.insidesoftwares.securitycommons.enums;

public enum JWTErro {
    GENERIC("AUTH-001"),
    EXPIRED("AUTH-002"),
    SIGNING_ERRO("AUTH-003"),
    VERIFIER_ERRO("AUTH-004"),
    NOT_HAS_TOKEN("AUTH-005"),
    USER_TOKEN_INVALID("AUTH-006");

    private final String code;

    JWTErro(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
