package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
public class SolvePuzzleGameTask implements GameTask{
    private final String givenSolution;


    @Override
    public Game execute(Game game) {
        GameState gameState = GameService.getDefaultPlayGameState(game);

        if(game.getCurrentSentence().getSentence().toLowerCase(Locale.ROOT).equals(givenSolution.toLowerCase(Locale.ROOT))){
            gameState = new GameState(GameState.State.FORCED, List.of(GameState.Task.LEAVE, GameState.Task.SENTENCE_COMPLETED),
                    List.of());
        }else{
            gameState.setTaskParameters(List.of(new TaskParameter(GameState.Task.MESSAGE, "You guessed the wrong solution! -1hp")));
            game.setHp(game.getHp() - 1);
        }

        if(game.getHp() == 0)
            gameState = new GameState(GameState.State.FORCED, List.of(GameState.Task.HP_DEATH, GameState.Task.LEAVE), List.of());

        game.setGameState(gameState);
        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.SOLVE_PUZZLE;
    }
}
