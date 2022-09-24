package br.com.insidesoftwares.securitycommons.entity;

import br.com.insidesoftwares.commons.specification.PermissionRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public record InsideSoftwaresGrantedAuthority(PermissionRole permissionRole) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return Objects.nonNull(permissionRole) ? permissionRole.role() : "";
    }

    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) return false;
        if (this == obj) {
            return true;
        } else {
            return obj instanceof InsideSoftwaresGrantedAuthority && this.getAuthority().equals(((InsideSoftwaresGrantedAuthority) obj).getAuthority());
        }
    }

    public String toString() {
        return this.permissionRole.role();
    }
}

