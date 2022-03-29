package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;

public class UsernameToLongException extends BasicWheelOfFortuneException {
    public UsernameToLongException(int length){
        super(UsernameToLongException.class, "Username to long! length: " + length + " | max length: "
                + Game.MAX_USERNAME_LENGTH);
    }
}
