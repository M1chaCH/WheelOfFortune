package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

public abstract class AuthException extends BasicWheelOfFortuneException {
    protected AuthException(Class<? extends AuthException> exception, String message) {
        super(exception, message);
    }
}
