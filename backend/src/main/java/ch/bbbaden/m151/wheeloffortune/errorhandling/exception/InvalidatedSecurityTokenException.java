package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

public class InvalidatedSecurityTokenException extends RuntimeException{
    public InvalidatedSecurityTokenException(String token) {
        super("Security token [" + token + "] is no longer valid (has already been refreshed)");
    }
}
