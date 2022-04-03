package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreDTO;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreService;
import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class WinGameTask implements GameTask{

    private final HighScoreService highScoreService;

    @Override
    public Game execute(Game game) {
        game.setGameState(new GameState(GameState.State.END, List.of(GameState.Task.REPLAY, GameState.Task.DELETE),
                List.of(new TaskParameter(GameState.Task.LEAVE, "You won against the game! 👏"))));

        if(highScoreService.getPositionByScore(game.getBudget()) != -1) {HighScoreDTO highScoreDTO = new HighScoreDTO();
            highScoreDTO.setUsername(game.getUsername());
            highScoreDTO.setScore(game.getBudget());
            highScoreDTO.setPlayedRounds(game.getRoundCount());
            highScoreService.addNew(highScoreDTO);
        }

        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.WIN;
    }
}
