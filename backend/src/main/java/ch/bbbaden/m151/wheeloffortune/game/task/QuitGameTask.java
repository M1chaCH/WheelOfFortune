package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreDTO;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class QuitGameTask implements GameTask{

    private final HighScoreService highScoreService;

    @Override
    public Game execute(Game game) {
        String positionMessage;
        int position = highScoreService.getPositionByScore(game.getScore());
        if(position == -1) {
            positionMessage = "Unfortunately you did not make it in to the HighScore List 😥";
        }else {
            positionMessage = "Congratulations! 🎉, you are position " + position + " of all time";
            HighScoreDTO highScoreDTO = new HighScoreDTO();
            highScoreDTO.setUsername(game.getUsername());
            highScoreDTO.setScore(game.getScore());
            highScoreService.addNew(highScoreDTO);
        }

        GameState endState = new GameState(GameState.State.END, List.of(GameState.Task.REPLAY, GameState.Task.DELETE),
                List.of(new TaskParameter(GameState.Task.LEAVE, positionMessage)));
        game.setGameState(endState);
        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.LEAVE;
    }
}
