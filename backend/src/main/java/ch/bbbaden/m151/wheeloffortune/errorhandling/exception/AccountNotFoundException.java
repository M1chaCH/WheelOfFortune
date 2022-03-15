package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String username) {
        super("Account with username " + username + " does not exist");
    }
}
