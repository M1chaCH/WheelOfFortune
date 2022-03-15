package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.AuthException;

public class SecurityTokenNotFoundException extends AuthException {
    public SecurityTokenNotFoundException(String token) {
        super(SecurityTokenNotFoundException.class, "security token [" + token + "] does not exist");
    }
}
