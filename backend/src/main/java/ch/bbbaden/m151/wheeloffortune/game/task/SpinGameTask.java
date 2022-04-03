package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.data.question.Question;
import ch.bbbaden.m151.wheeloffortune.game.data.question.QuestionDTO;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;
import ch.bbbaden.m151.wheeloffortune.game.entity.WheelOfFortuneField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinGameTask implements GameTask{
    private static final Logger LOGGER = LoggerFactory.getLogger(SpinGameTask.class);

    @Override
    public Game execute(Game game) {
        WheelOfFortuneField spinResult = GameService.WHEEL_OF_FORTUNE[new Random().nextInt(GameService.WHEEL_OF_FORTUNE.length)];

        GameState.State state;
        List<GameState.Task> availableTasks;
        List<TaskParameter> taskProperties = new ArrayList<>();
        switch (spinResult.getTask()){
        case GUESS_CONSONANT:
            state = GameState.State.PLAY;
            availableTasks = new ArrayList<>(List.of(GameState.Task.SPIN, GameState.Task.SOLVE_PUZZLE,
                    GameState.Task.GUESS_CONSONANT, GameState.Task.LEAVE));
            if(BuyVowelGameTask.canBuyVowel(game))
                availableTasks.add(GameState.Task.BUY_VOWEL);

            taskProperties.add(new TaskParameter(GameState.Task.SPIN, spinResult.getId()));
            break;
        case RISK:
            state = GameState.State.FORCED;
            availableTasks = List.of( GameState.Task.RISK, GameState.Task.LEAVE );

            if(game.getAvailableQuestions().isEmpty()){
                game.setGameState(new GameState(GameState.State.FORCED, List.of(GameState.Task.WIN, GameState.Task.LEAVE),
                        List.of()));
                return game;
            }

            Question currentQuestion = game.getAvailableQuestions()
                    .get(GameService.getNextRandomInt(game.getAvailableQuestions().size()));
            game.setCurrentQuestion(currentQuestion); //removed on answered

            QuestionDTO questionDTO = currentQuestion.parseToDTO();
            questionDTO.setAnswerOneCorrect(true); //weak attempt to hide the correct answer for the frontend
            taskProperties.add(new TaskParameter(GameState.Task.RISK, questionDTO));
            taskProperties.add(new TaskParameter(GameState.Task.SPIN, spinResult.getId()));
            break;
        case BANKRUPT:
            state = GameState.State.FORCED;
            availableTasks = List.of( GameState.Task.BANKRUPT, GameState.Task.LEAVE );
            taskProperties.add(new TaskParameter(GameState.Task.SPIN, spinResult.getId()));
            break;
        default:
            LOGGER.error("UNEXPECTED: failed to process spin returning default");
            state = GameState.State.PLAY;
            availableTasks = new ArrayList<>();
        }
        game.setGameState(new GameState(state, availableTasks, taskProperties));
        game.setRoundCount(game.getRoundCount() + 1);
        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.SPIN;
    }
}
