package ch.bbbaden.m151.wheeloffortune.errorhandling.exception;

/**
 * returns this message to the RuntimeException constructor: <br>
 * "SomeClassName" -> "the given message"
 */
public abstract class BasicWheelOfFortuneException extends RuntimeException{
    protected BasicWheelOfFortuneException(Class<? extends BasicWheelOfFortuneException> exception, String message) {
        super(exception.getSimpleName() + " -> " + message);
    }
}
