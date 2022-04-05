package ch.bbbaden.m151.wheeloffortune.game.entity;

import ch.bbbaden.m151.wheeloffortune.game.GameService;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GameDTO {
    @SuppressWarnings("java:S1170") //should not be static because jackson ignores static fields
    private final WheelOfFortuneField[] wheelOfFortune = GameService.WHEEL_OF_FORTUNE;

    private final String gameId;
    private String username;
    private int roundCount;
    private int budget;
    private int hp;

    private GameField gameField;
    private GameState gameState;

    private List<Character> consonantLeftToGuess;
    private List<Character> vowelsLeftToGuess;
}
