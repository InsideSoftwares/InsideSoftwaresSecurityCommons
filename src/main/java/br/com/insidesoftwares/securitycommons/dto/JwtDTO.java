package br.com.insidesoftwares.securitycommons.dto;

import br.com.insidesoftwares.securitycommons.enums.JWTErro;
import io.fusionauth.jwt.domain.JWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDTO {

    private JWT jwt;
    private JWTErro jwtErro;

}
