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
    private final WheelOfFortuneField[] WHEEL_OF_FORTUNE = GameService.WHEEL_OF_FORTUNE;

    private final String gameId;
    private String username;
    private int roundCount = 0;
    private int budget = 0;
    private int score = 0;
    private int hp = Game.MAX_HP;

    private GameField gameField;
    private GameState gameState;

    private List<Character> consonantLeftToGuess;
    private List<Character> vowelsLeftToGuess;
}
