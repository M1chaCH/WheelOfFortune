package ch.bbbaden.m151.wheeloffortune.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.GameNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.UsernameAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.UsernameToLongException;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import ch.bbbaden.m151.wheeloffortune.game.data.question.QuestionService;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.SentenceService;
import ch.bbbaden.m151.wheeloffortune.game.entity.*;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreService;
import ch.bbbaden.m151.wheeloffortune.game.task.GameTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class GameService {

    public static final List<Character> CONSONANTS = List.of( 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' );

    public static final List<Character> VOWELS = List.of( 'a', 'e', 'i', 'o', 'u' );

    public static final int VOWEL_PRICE =  400;

    public static final WheelOfFortuneField[] WHEEL_OF_FORTUNE = new WheelOfFortuneField[]{
            new WheelOfFortuneField(0, GameState.Task.BANKRUPT, -1),
            new WheelOfFortuneField(1, GameState.Task.GUESS_CONSONANT, 200),
            new WheelOfFortuneField(2, GameState.Task.GUESS_CONSONANT, 100),
            new WheelOfFortuneField(3, GameState.Task.RISK, -1),
            new WheelOfFortuneField(4, GameState.Task.GUESS_CONSONANT, 300),
            new WheelOfFortuneField(5, GameState.Task.GUESS_CONSONANT, 400),
            new WheelOfFortuneField(6, GameState.Task.GUESS_CONSONANT, 100),
            new WheelOfFortuneField(7, GameState.Task.GUESS_CONSONANT, 200),
            new WheelOfFortuneField(8, GameState.Task.RISK, -1),
            new WheelOfFortuneField(9, GameState.Task.GUESS_CONSONANT, 300),
            new WheelOfFortuneField(10, GameState.Task.GUESS_CONSONANT, 100),
            new WheelOfFortuneField(11, GameState.Task.RISK, -1),
            new WheelOfFortuneField(12, GameState.Task.GUESS_CONSONANT, 100),
            new WheelOfFortuneField(13, GameState.Task.GUESS_CONSONANT, 200),
            new WheelOfFortuneField(14, GameState.Task.GUESS_CONSONANT, 100),
    };

    private final HighScoreService highScoreService;
    private final SentenceService sentenceService;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final GameRepo gameRepo = GameRepo.getInstance();
    private final Random random = new Random();

    public GameDTO startNewGame(StartGameRequest startGameRequest){
        checkUsername(startGameRequest.getUsername());

        Game game = new Game(startGameRequest.getUsername(),
                sentenceService.getAllByCategory(startGameRequest.getCategory().getId()),
                questionService.getAllByCategory(startGameRequest.getCategory().getId()));
        return prepareForResponse(game);
    }

    public GameDTO getByGameId(String id){
        return validateGameId(id).parseDTO();
    }

    /**
     * deletes the old game and starts a new one with the same username and a random category
     * @param id the game id
     * @return the newly created game
     */
    public GameDTO restartGame(String id){
        Game game = validateGameId(id);
        deleteGame(id);
        List<CategoryDTO> categories = categoryService.getAllAsDto();
        int categoryId = random.nextInt(categories.size());
        return startNewGame(new StartGameRequest(game.getUsername(), categories.get(categoryId)));
    }

    public GameDTO handleTask(String id, GameTask task){
        return prepareForResponse(task.execute(validateGameId(id)));
    }

    public void deleteGame(String id){
        gameRepo.delete(id);
    }

    /**
     * saves the given game and returns the game parsed as DTO
     */
    private GameDTO prepareForResponse(Game game){
        gameRepo.save(game);
        return game.parseDTO();
    }

    private Game validateGameId(String gameId){
        return gameRepo.getGameById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }

    /**
     * checks if the username is valid
     * @param username the username to validate
     * @throws UsernameAlreadyExistsException when the username already exists
     * @throws UsernameToLongException when the username is too long
     */
    private void checkUsername(String username){
        if(highScoreService.getByUsername(username).isPresent()
                || gameRepo.getByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);

        if(username.length() > Game.MAX_USERNAME_LENGTH)
            throw new UsernameToLongException(username.length());
    }
}
