package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GameDTO {

    private final String gameId;
    private String username;
    private int roundCount;
    private int budget;
    private int hp;

    private GameField gameField;
    private GameState gameState;
    private WheelOfFortuneField currentWheelOfFortuneField;

    private List<Character> consonantLeftToGuess;
    private List<Character> vowelsLeftToGuess;
}
