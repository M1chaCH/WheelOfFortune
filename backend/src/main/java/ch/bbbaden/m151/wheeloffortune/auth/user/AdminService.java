package ch.bbbaden.m151.wheeloffortune.auth.user;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepo repo;

    /**
     * @param username the username to search the admin by
     * @return the Admin with the given Username
     * @throws AccountNotFoundException when no admin with given username exists
     */
    public AdminUser getAdminByUsername(String username){
        return repo.findAdminByUsername(username).orElseThrow(() -> new AccountNotFoundException(username));
    }
}
