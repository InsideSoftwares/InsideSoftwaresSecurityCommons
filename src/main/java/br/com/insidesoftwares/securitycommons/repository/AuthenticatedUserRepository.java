package br.com.insidesoftwares.securitycommons.repository;

import br.com.insidesoftwares.securitycommons.entity.AuthenticatedUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticatedUserRepository extends CrudRepository<AuthenticatedUser, String> {

    @Query("SELECT a FROM AuthenticatedUser a WHERE a.username = :username")
    Optional<AuthenticatedUser> findByUsername(final String username);

}
