package ch.bbbaden.m151.wheeloffortune.game.entity;

import ch.bbbaden.m151.wheeloffortune.game.GameService;
import ch.bbbaden.m151.wheeloffortune.game.data.question.Question;
import ch.bbbaden.m151.wheeloffortune.game.data.sentence.Sentence;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Game {
    public static final int MAX_HP = 3;
    public static final int MAX_USERNAME_LENGTH = 30;

    private final String gameId;
    private String username;
    private int roundCount = 0;
    private int budget = 0;
    private int score = 0;
    private int hp = MAX_HP;

    private final List<Sentence> availableSentences;
    private Sentence currentSentence;
    private List<Character> consonantLeftToGuess;
    private List<Character> vowelsLeftToGuess;

    private final List<Question> availableQuestions;

    private GameField gameField;
    private GameState gameState;

    /**
     * constructor to use when starting a new game
     * @param username the username of the current player
     * @param sentences a list of all sentences in the category that the user chose
     */
    public Game(String username, List<Sentence> sentences, List<Question> questions){
        this.gameId = UUID.randomUUID().toString();
        this.username = username;

        this.availableSentences = sentences;
        this.setCurrentSentence(sentences.get(0));
        this.availableQuestions = questions;

        this.gameField = new GameField(currentSentence.getSentence());
        this.gameState = new GameState(GameState.State.PLAY, List.of(
                GameState.Task.SPIN,
                GameState.Task.SOLVE_PUZZLE,
                GameState.Task.LEAVE), new EnumMap<>(GameState.Task.class));
    }

    public GameDTO parseDTO(){
        return new GameDTO(gameId,
                username,
                roundCount,
                budget,
                score,
                hp,
                gameField,
                gameState,
                consonantLeftToGuess,
                vowelsLeftToGuess);
    }

    public void setCurrentSentence(Sentence sentence){
        this.currentSentence = sentence;
        consonantLeftToGuess = new ArrayList<>(GameService.CONSONANTS);
        vowelsLeftToGuess = new ArrayList<>(GameService.VOWELS);
    }
}
