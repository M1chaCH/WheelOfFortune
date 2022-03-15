package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

public abstract class BasicWheelOfFortuneException extends RuntimeException{
    protected BasicWheelOfFortuneException(Class<? extends BasicWheelOfFortuneException> exception, String message) {
        super(exception.getSimpleName() + " -> " + message);
    }
}
