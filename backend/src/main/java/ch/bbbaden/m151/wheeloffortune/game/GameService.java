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
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GameService {

    public static final List<Character> CONSONANTS = List.of( 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' );

    public static final List<Character> VOWELS = List.of( 'a', 'e', 'i', 'o', 'u' );

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

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

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
        gameRepo.save(game);
        return game.parseDTO();
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

    public void deleteGame(String id){
        gameRepo.delete(id);
    }

    public GameDTO spin(String gameId){
        Game game = validateGameId(gameId);
        if(!game.getGameState().getAvailableTasks().contains(GameState.Task.SPIN))
            throw new IllegalGameTaskException(GameState.Task.SPIN);

        WheelOfFortuneField spinResult = WHEEL_OF_FORTUNE[random.nextInt(WHEEL_OF_FORTUNE.length)];

        GameState.State state;
        List<GameState.Task> availableTasks;
        EnumMap<GameState.Task, Object> taskProperties = new EnumMap<>(GameState.Task.class);
        switch (spinResult.getTask()){
            case GUESS_CONSONANT:
                state = GameState.State.PLAY;
                 availableTasks = List.of( GameState.Task.SPIN, GameState.Task.SOLVE_PUZZLE,
                        GameState.Task.GUESS_CONSONANT, GameState.Task.LEAVE );
                 taskProperties.put(GameState.Task.GUESS_CONSONANT, spinResult.getId());
                break;
            case RISK:
                state = GameState.State.FORCED;
                availableTasks = List.of( GameState.Task.RISK, GameState.Task.LEAVE );
                taskProperties.put(GameState.Task.RISK, game.getAvailableQuestions().get(0));
                break;
            case BANKRUPT:
                state = GameState.State.FORCED;
                availableTasks = List.of( GameState.Task.BANKRUPT );
                break;
            default:
                LOGGER.error("UNEXPECTED: failed to process spin returning default");
                state = GameState.State.PLAY;
                availableTasks = new ArrayList<>();
        }
        game.setGameState(new GameState(state, availableTasks, taskProperties));
        game.setRoundCount(game.getRoundCount() + 1);
        gameRepo.save(game);
        return game.parseDTO();
    }

    public GameDTO guessConsonant(String id, char guessedConsonant){
        Game game = validateGameId(id);
        if(!game.getGameState().getAvailableTasks().contains(GameState.Task.GUESS_CONSONANT))
            throw new IllegalGameTaskException(GameState.Task.GUESS_CONSONANT);

        int countConsonants = 0;
        char[] sentenceChars = game.getCurrentSentence().getSentence().toCharArray();
        char[] revealedChars = game.getGameField().getRevealedCharacters();
        for (int i = 0; i < sentenceChars.length; i++) {
            if(sentenceChars[i] == guessedConsonant){
                countConsonants++;
                revealedChars[i] = sentenceChars[i];
            }
        }

        GameState.State state = GameState.State.PLAY;
        List<GameState.Task> availableTasks =
                List.of(GameState.Task.SPIN, GameState.Task.SOLVE_PUZZLE, GameState.Task.LEAVE);

        EnumMap<GameState.Task, Object> taskProperties = new EnumMap<>(GameState.Task.class);
        if(countConsonants != 0){
            int win = Integer.parseInt(game.getGameState().getTaskParameters()
                    .get(GameState.Task.GUESS_CONSONANT).toString());

            game.getConsonantLeftToGuess().remove(guessedConsonant); //FIXME
            taskProperties.put(GameState.Task.GUESS_CONSONANT, "Guessed Correct! +" + win * countConsonants + " budget");
            game.setBudget(game.getBudget() + win * countConsonants);
        }else{
            taskProperties.put(GameState.Task.GUESS_CONSONANT, "You guessed wrong ): -1 hp");
            game.setHp(game.getHp() - 1);
        }

        if(game.getHp() > 0)
            game.setGameState(new GameState(state, availableTasks, taskProperties));
        else
            game.setGameState(new GameState(GameState.State.FORCED, List.of(GameState.Task.HP_DEATH),
                    new EnumMap<>(GameState.Task.class)));

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
