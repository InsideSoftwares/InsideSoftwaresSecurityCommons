package br.com.insidesoftwares.securitycommons.entity;

import br.com.insidesoftwares.commons.enums.permissions.ProfileType;
import br.com.insidesoftwares.commons.specification.PermissionRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("AuthenticatedUser")
public class AuthenticatedUser implements Serializable {

    @Indexed
    private String id;
    @Indexed
    private String username;
    private String name;
    private ProfileType profileType;
    private String treatmentName;
    private LocalDateTime logged;
    private LocalDateTime expiration;
    private Set<PermissionRole> permissions;

    @TimeToLive
    private Long timeToLive;

    public Set<InsideSoftwaresGrantedAuthority> getSimpleGrantedAuthority(){
        return permissions.stream()
                .map(
                        InsideSoftwaresGrantedAuthority::new
                )
                .collect(Collectors.toSet());
    }

    public InsideSoftwaresCredentials createInsideSoftwaresCredentials(){
        return InsideSoftwaresCredentials.builder()
                .token(this.id)
                .username(this.username)
                .name(this.name)
                .profileType(this.profileType)
                .treatmentName(this.treatmentName)
                .build();
    }

}
