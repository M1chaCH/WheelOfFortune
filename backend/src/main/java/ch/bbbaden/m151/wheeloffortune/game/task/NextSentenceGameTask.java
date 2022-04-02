package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameField;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;

public class NextSentenceGameTask implements GameTask{

    @Override
    public Game execute(Game game) {
        game.setCurrentSentence(game.getAvailableSentences().get(0)); //TODO: make more random
        game.getAvailableSentences().remove(game.getCurrentSentence());
        game.setGameState(GameService.getDefaultPlayGameState(game));
        game.setGameField(new GameField(game.getCurrentSentence().getSentence()));

        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.SENTENCE_COMPLETED;
    }
}
