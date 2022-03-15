package ch.bbbaden.m151.wheeloffortune.auth.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends CrudRepository<AdminUser, Integer> {
    Optional<AdminUser> findAdminByUsername(String username);
}
