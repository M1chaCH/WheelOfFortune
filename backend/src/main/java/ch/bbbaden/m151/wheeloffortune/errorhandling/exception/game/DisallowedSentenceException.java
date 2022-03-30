package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class DisallowedSentenceException extends BasicWheelOfFortuneException {
    public DisallowedSentenceException(String sentenceString){
        super(DisallowedSentenceException.class, sentenceString + " contains disallowed chars");
    }
}
