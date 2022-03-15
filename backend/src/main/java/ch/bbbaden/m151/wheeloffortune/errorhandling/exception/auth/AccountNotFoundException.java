package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.AuthException;

public class AccountNotFoundException extends AuthException {
    public AccountNotFoundException(String username) {
        super(AccountNotFoundException.class, "Account with username " + username + " does not exist");
    }
}
