package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException(String username) {
        super("invalid login for username: " + username);
    }
}
