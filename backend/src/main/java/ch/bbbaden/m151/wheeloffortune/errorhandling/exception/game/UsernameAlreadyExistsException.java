package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class UsernameAlreadyExistsException extends BasicWheelOfFortuneException {
    public UsernameAlreadyExistsException(String username) {
        super(UsernameAlreadyExistsException.class, "Username " + username + " already exists");
    }
}
