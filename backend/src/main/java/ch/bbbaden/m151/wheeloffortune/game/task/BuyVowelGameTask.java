package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.IllegalGameTaskException;
import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BuyVowelGameTask implements GameTask{
    private final char boughtVowel;

    @Override
    public Game execute(Game game) {
        if(game.getBudget() < GameService.VOWEL_PRICE)
            throw new IllegalGameTaskException(GameState.Task.BUY_VOWEL);

        int countRevealedCharacters = game.getGameField()
                .revealCharacter(boughtVowel, game.getCurrentSentence().getSentence().toCharArray());
        game.getVowelsLeftToGuess().remove(Character.valueOf(boughtVowel));

        GameState gameState = GameService.getDefaultPlayGameState(game);

        if(countRevealedCharacters == 0){
            game.setHp(game.getHp() - 1);
            if(game.getHp() == 0){
                gameState.setState(GameState.State.FORCED);
                gameState.setAvailableTasks(List.of(GameState.Task.HP_DEATH, GameState.Task.LEAVE));
            }
        }else game.setBudget(game.getBudget() - GameService.VOWEL_PRICE);

        if(!hasEnoughMoneyForVowel(game))
            gameState.getAvailableTasks().remove(GameState.Task.BUY_VOWEL);

        game.setGameState(gameState);
        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.BUY_VOWEL;
    }

    public static boolean hasEnoughMoneyForVowel(Game game){
        return game.getBudget() >= GameService.VOWEL_PRICE;
    }
}
