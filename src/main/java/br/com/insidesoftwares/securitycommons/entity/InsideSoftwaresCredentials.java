package br.com.insidesoftwares.securitycommons.entity;

import br.com.insidesoftwares.commons.enums.permissions.ProfileType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InsideSoftwaresCredentials {

    private String token;
    private String username;
    private String name;
    private ProfileType profileType;
    private String treatmentName;

}
