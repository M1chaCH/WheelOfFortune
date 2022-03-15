package ch.bbbaden.m151.wheeloffortune.auth.token;

import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SecurityTokenRepo extends CrudRepository<SecurityToken, Integer> {
    Optional<SecurityToken> findTokenByToken(String token);
    Collection<SecurityToken> findTokensByAdmin(AdminUser admin);
}
