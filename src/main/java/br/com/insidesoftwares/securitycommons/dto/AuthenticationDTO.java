package br.com.insidesoftwares.securitycommons.dto;

import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {

    private Authentication authentication;
    private boolean valid;
    private JWTErro jwtErro;
    private String identifier;

}
