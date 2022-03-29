package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class GameNotFoundException extends BasicWheelOfFortuneException {

    public GameNotFoundException(String id) {
        super(GameNotFoundException.class, "Game with " + id + " not found");
    }
}
