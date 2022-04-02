package ch.bbbaden.m151.wheeloffortune.game.task;

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
        int countConsonants = game.getGameField()
                .revealCharacter(guessedConsonant, game.getCurrentSentence().getSentence().toCharArray());
        game.getConsonantLeftToGuess().remove(Character.valueOf(guessedConsonant));

        GameState gameState = GameService.getDefaultPlayGameState(game);
        List<TaskParameter> taskProperties = new ArrayList<>();

        if(countConsonants != 0){
            int win = GameService.WHEEL_OF_FORTUNE[Integer.parseInt(game.getGameState()
                    .getTaskParameterValue(GameState.Task.SPIN).toString())].getReward();

            game.getConsonantLeftToGuess().remove((Character) guessedConsonant);
            taskProperties.add(new TaskParameter(GameState.Task.MESSAGE, "Guessed Correct! +" + win * countConsonants + " budget"));
            game.setBudget(game.getBudget() + win * countConsonants);
        }else{
            taskProperties.add(new TaskParameter(GameState.Task.MESSAGE, "You guessed wrong ): -1 hp"));
            game.setHp(game.getHp() - 1);
        }
        gameState.setTaskParameters(taskProperties);

        if(game.getHp() == 0)
            gameState = new GameState(GameState.State.FORCED, List.of(GameState.Task.HP_DEATH, GameState.Task.LEAVE), List.of());

        game.setGameState(gameState);
        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.GUESS_CONSONANT;
    }
}
