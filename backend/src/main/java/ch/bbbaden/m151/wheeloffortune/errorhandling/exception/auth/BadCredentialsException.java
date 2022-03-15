package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.AuthException;

public class BadCredentialsException extends AuthException {
    public BadCredentialsException(String username) {
        super(BadCredentialsException.class, "invalid login for username: " + username);
    }
}
