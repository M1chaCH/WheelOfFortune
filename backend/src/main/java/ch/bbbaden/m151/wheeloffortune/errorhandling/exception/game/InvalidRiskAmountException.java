package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class InvalidRiskAmountException extends BasicWheelOfFortuneException {
    public InvalidRiskAmountException(int amount){
        super(InvalidRiskAmountException.class, "risked amount [" + amount + "] is to big" );
    }
}
