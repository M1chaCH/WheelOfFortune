package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.AuthException;

public class InvalidatedSecurityTokenException extends AuthException {
    public InvalidatedSecurityTokenException(String token) {
        super(InvalidatedSecurityTokenException.class, "Security token [" + token + "] is no longer valid (has already been refreshed)");
    }
}
