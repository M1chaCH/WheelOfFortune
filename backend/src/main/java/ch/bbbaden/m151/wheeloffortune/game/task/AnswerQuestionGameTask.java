package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.InvalidRiskAmountException;
import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.data.question.Question;
import ch.bbbaden.m151.wheeloffortune.game.entity.CorrectQuestionRequest;
import ch.bbbaden.m151.wheeloffortune.game.entity.Game;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameState;
import ch.bbbaden.m151.wheeloffortune.game.entity.TaskParameter;

import java.util.ArrayList;
import java.util.List;

public class AnswerQuestionGameTask implements GameTask{
    private final CorrectQuestionRequest request;

    public AnswerQuestionGameTask(CorrectQuestionRequest request) {
        this.request = request;
    }

    @Override
    public Game execute(Game game) {
        Question askedQuestion = game.getCurrentQuestion();
        game.getAvailableQuestions().remove(askedQuestion);
        int amount = request.getAmount();
        //if budget is < 100 you can only risk 100 --> if you lose you just have 0 budget afterward
        if((amount > game.getBudget() && game.getBudget() > 100)
                || (game.getBudget() <= 100 && amount > 100))
            throw new InvalidRiskAmountException(amount);

        GameState gameState = GameService.getDefaultPlayGameState(game);
        List<TaskParameter> taskProperties = new ArrayList<>();

        if(request.isSelectedAnswerOne() == askedQuestion.isAnswerOneCorrect()){
            game.setBudget(game.getBudget() + amount);
            taskProperties.add(new TaskParameter(GameState.Task.MESSAGE, "Correct answer! 🎉 --> +" + amount));
        }else{
            if(game.getBudget() > 100) game.setBudget(game.getBudget() - amount);
            else game.setBudget(0);

            String correctAnswer = askedQuestion.isAnswerOneCorrect() ? askedQuestion.getAnswerOne() : askedQuestion.getAnswerTwo();
            taskProperties.add(new TaskParameter(GameState.Task.MESSAGE, "Incorrect Answer ):. Correct answer is \"" + correctAnswer + "\" --> -" + amount));
        }

        gameState.setTaskParameters(taskProperties);
        game.setGameState(gameState);
        return game;
    }

    @Override
    public GameState.Task getRequiredTask() {
        return GameState.Task.RISK;
    }
}
