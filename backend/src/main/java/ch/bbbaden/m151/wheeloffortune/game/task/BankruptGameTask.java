package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;

import java.util.List;

public class BankruptGameTask implements GameTask{
    @Override
    public Game execute(Game game) {

        game.setGameState(new GameState(GameState.State.END, List.of(GameState.Task.REPLAY, GameState.Task.DELETE),
                List.of(new TaskParameter(GameState.Task.LEAVE, "You went bankrupt! 😖"))));
        game.setBudget(0);
        game.setScore(0);

        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.BANKRUPT;
    }
}
