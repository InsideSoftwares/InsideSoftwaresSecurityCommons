package br.com.insidesoftwares.securitycommons.entity;

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
    private String treatmentName;
    private LocalDateTime logged;
    private LocalDateTime expiration;
    private Set<String> permissions;

    @TimeToLive
    private Long timeToLive;

}
