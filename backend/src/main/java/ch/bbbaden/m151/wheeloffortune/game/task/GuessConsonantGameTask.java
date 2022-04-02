package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.IllegalGameTaskException;
import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;

import java.util.ArrayList;
import java.util.List;

public class GuessConsonantGameTask implements GameTask{

    private final char guessedConsonant;

    public GuessConsonantGameTask(char guessedConsonant){
        this.guessedConsonant = guessedConsonant;
    }

    @Override
    public Game execute(Game game) {

        if(!game.getGameState().getAvailableTasks().contains(GameState.Task.GUESS_CONSONANT))
            throw new IllegalGameTaskException(GameState.Task.GUESS_CONSONANT);

        int countConsonants = 0;
        char[] sentenceChars = game.getCurrentSentence().getSentence().toCharArray();
        char[] revealedChars = game.getGameField().getRevealedCharacters();
        for (int i = 0; i < sentenceChars.length; i++) {
            if(Character.toLowerCase(sentenceChars[i]) == Character.toLowerCase(guessedConsonant)){
                countConsonants++;
                revealedChars[i] = sentenceChars[i];
            }
        }

        GameState.State state = GameState.State.PLAY;
        List<GameState.Task> availableTasks = new ArrayList<>(
                List.of(GameState.Task.SPIN, GameState.Task.SOLVE_PUZZLE, GameState.Task.LEAVE));

        List<TaskParameter> taskProperties = new ArrayList<>();
        if(countConsonants != 0){
            int win = GameService.WHEEL_OF_FORTUNE[Integer.parseInt(game.getGameState()
                    .getTaskParameterValue(GameState.Task.GUESS_CONSONANT).toString())].getReward();

            game.getConsonantLeftToGuess().remove((Character) guessedConsonant);
            taskProperties.add(new TaskParameter(GameState.Task.GUESS_CONSONANT, "Guessed Correct! +" + win * countConsonants + " budget"));
            game.setBudget(game.getBudget() + win * countConsonants);
        }else{
            taskProperties.add(new TaskParameter(GameState.Task.GUESS_CONSONANT, "You guessed wrong ): -1 hp"));
            game.setHp(game.getHp() - 1);
        }

        if(game.getHp() > 0) {
            if(game.getBudget() >= GameService.VOWEL_PRICE)
                availableTasks.add(GameState.Task.BUY_VOWEL);
            game.setGameState(new GameState(state, availableTasks, taskProperties));
        } else
            game.setGameState(new GameState(GameState.State.FORCED, List.of(GameState.Task.HP_DEATH), List.of()));
        return game;
    }
}
