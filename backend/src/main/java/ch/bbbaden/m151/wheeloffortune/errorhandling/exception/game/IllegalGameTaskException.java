package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;

public class IllegalGameTaskException extends BasicWheelOfFortuneException {
    public IllegalGameTaskException(GameState.Task task){
        super(IllegalGameTaskException.class, task.name() + " is currently not allowed");
    }
}
