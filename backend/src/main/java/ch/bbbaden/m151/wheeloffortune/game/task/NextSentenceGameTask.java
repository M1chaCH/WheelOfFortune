package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameField;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;

import java.util.List;

public class NextSentenceGameTask implements GameTask{

    @Override
    public Game execute(Game game) {
        if(game.getAvailableSentences().isEmpty()){
            game.setGameState(new GameState(GameState.State.FORCED, List.of(GameState.Task.WIN, GameState.Task.LEAVE),
                    List.of()));
            return game;
        }

        game.setCurrentSentence(game.getAvailableSentences().get(0)); //TODO: make more random
        game.getAvailableSentences().remove(game.getCurrentSentence());
        game.setGameField(new GameField(game.getCurrentSentence().getSentence()));
        game.setGameState(GameService.getDefaultPlayGameState(game));

        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.SENTENCE_COMPLETED;
    }
}
