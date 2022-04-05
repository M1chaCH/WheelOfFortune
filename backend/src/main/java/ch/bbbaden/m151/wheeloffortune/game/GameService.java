package ch.bbbaden.m151.wheeloffortune.game;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.GameNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.IllegalGameTaskException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.UsernameAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.game.UsernameToLongException;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import ch.bbbaden.m151.wheeloffortune.game.data.question.QuestionService;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.SentenceService;
import ch.bbbaden.m151.wheeloffortune.game.entity.*;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreService;
import ch.bbbaden.m151.wheeloffortune.game.task.BuyVowelGameTask;
import ch.bbbaden.m151.wheeloffortune.game.task.GameTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * the core of the Game. The GameService class is responsible for dealing with the game.
 * Always treat this class as a stateless bean.
 */
@Service
@AllArgsConstructor
public class GameService {

    /** All consonants in English */
    public static final List<Character> CONSONANTS = List.of( 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' );

    /** All the vowels in English */
    public static final List<Character> VOWELS = List.of( 'a', 'e', 'i', 'o', 'u' );

    /** The price of a Vowel. */
    public static final int VOWEL_PRICE =  200;

    /** The Wheel Of Fortune, mainly used to find the next possible Field, or for visualisation in Frontend */
    public static final WheelOfFortuneField[] WHEEL_OF_FORTUNE = new WheelOfFortuneField[]{
            new WheelOfFortuneField(0, GameState.Task.GUESS_CONSONANT, 200),
            new WheelOfFortuneField(1, GameState.Task.BANKRUPT, -1),
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
    private static final Random random = new Random();

    /**
     * creates a game in PLAY state with the following available Tasks:
     * <ul>
     *     <li>Solve Puzzle</li>
     *     <li>Leave</li>
     *     <li>Spin (if {@link GameService#areAllConsonantsRevealed(Game)})</li>
     *     <li>Buy vowel (if {@link BuyVowelGameTask#canBuyVowel(Game)})</li>
     * </ul>
     * @param game a game entity with the current game state
     * @return the GameState prepared for the next default step
     */
    public static GameState getDefaultPlayGameState(Game game){
        GameState gameState = new GameState();
        gameState.setState(GameState.State.PLAY);

        List<GameState.Task> availableTasks = new ArrayList<>(List.of(GameState.Task.SOLVE_PUZZLE, GameState.Task.LEAVE));
        if(!areAllConsonantsRevealed(game))
            availableTasks.add(GameState.Task.SPIN);
        if(BuyVowelGameTask.canBuyVowel(game))
            availableTasks.add(GameState.Task.BUY_VOWEL);

        gameState.setAvailableTasks(availableTasks);
        return gameState;
    }

    /**
     * @param game the game containing the current sentence
     * @return true: all consonants in the current sentence are revealed
     */
    public static boolean areAllConsonantsRevealed(Game game){
        int consonantsInSentence = 0;
        int consonantsRevealed = 0;
        char[] sentence = game.getCurrentSentence().getSentence().toCharArray();
        char[] revealed = game.getGameField().getRevealedCharacters();
        for (int i = 0; i < sentence.length; i++) {
            if(!GameField.PUNCTUATIONS.contains(sentence[i]) && CONSONANTS.contains(Character.toLowerCase(sentence[i]))) {
                consonantsInSentence++;
                if (sentence[i] == revealed[i])
                    consonantsRevealed++;
            }
        }

        return consonantsInSentence == consonantsRevealed;
    }

    /**
     * @param game the game containing the current sentence
     * @return true: the entire sentence is revealed
     */
    public static boolean isSentenceComplete(Game game){
        return Arrays.equals(game.getCurrentSentence().getSentence().toCharArray(),
                game.getGameField().getRevealedCharacters());
    }

    /**
     * @param startGameRequest the {@link StartGameRequest} containing the desired username and category
     * @return a {@link GameDTO} with the newly created game.
     */
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
     * @throws GameNotFoundException if no game with the given id exists
     */
    public GameDTO restartGame(String id){
        Game game = validateGameId(id);
        deleteGame(id);
        List<CategoryDTO> categories = categoryService.getAllAsDto();
        int categoryId = getNextRandomInt(categories.size());
        return startNewGame(new StartGameRequest(game.getUsername(), categories.get(categoryId)));
    }

    /**
     * validates the given gameId, checks if the {@link GameTask} can be executed, executes the {@link GameTask} in the
     * game task & parses the returned Game to a DTO.
     * @param id the id of the game
     * @param task the task to execute
     * @return the new, processed GameDTO
     * @throws IllegalGameTaskException if the task is not one of the available tasks
     * @throws GameNotFoundException if no game with the given id exists
     */
    public GameDTO handleTask(String id, GameTask task){
        Game game = validateGameId(id);
        GameState.Task requiredTask = task.getRequiredTask();
        if(!game.getGameState().getAvailableTasks().contains(requiredTask))
            throw new IllegalGameTaskException(requiredTask);

        return prepareForResponse(task.execute(game));
    }

    /**
     * deletes a game by its id, with no validation
     * @param id the game id to delete
     */
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

    /**
     * @param gameId the gameId to search the game by
     * @return the game that has the given game id
     * @throws GameNotFoundException if no game with the given id exists
     */
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

    /**
     * just a helper method that executes {@link Random#nextInt(int)} to keep the Random instance to one
     * (requested by sonar🤷)
     */
    public static int getNextRandomInt(int bound){
        return random.nextInt(bound);
    }
}
