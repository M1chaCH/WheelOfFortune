package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WheelOfFortuneField {
    private int id;
    private GameState.Task task;
    /**
     * this field will only be used when the task is GUESS_CONSONANT
     */
    private int reward = -1;
}
