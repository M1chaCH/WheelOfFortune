package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

public class SecurityTokenNotFoundException extends RuntimeException{
    public SecurityTokenNotFoundException(String token) {
        super("security token [" + token + "] does not exist");
    }
}
