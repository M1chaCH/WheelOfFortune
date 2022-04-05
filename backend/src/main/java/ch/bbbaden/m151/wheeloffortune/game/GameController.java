package ch.bbbaden.m151.wheeloffortune.game;

import ch.bbbaden.m151.wheeloffortune.game.entity.CorrectQuestionRequest;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameDTO;
import ch.bbbaden.m151.wheeloffortune.game.entity.StartGameRequest;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreService;
import ch.bbbaden.m151.wheeloffortune.game.task.*;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService service;
    private final HighScoreService highScoreService;

    /**
     * starts a new game with the given username
     */
    @PostMapping()
    public ResponseEntity<GameDTO> startGame(@RequestBody StartGameRequest startGameRequest) {
        return ResponseEntity.ok(service.startNewGame(startGameRequest));
    }

    /**
     * get the GameDTO from a game id, can be used after page reload
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> loadGame(@PathVariable String gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    /**
     * quit game
     */
    @PostMapping("/{gameId}/quit")
    public ResponseEntity<GameDTO> quitGame(@PathVariable String gameId) {
        return ResponseEntity.ok(service.handleTask(gameId, new QuitGameTask(highScoreService)));
    }

    /**
     * restarts game
     */
    @PutMapping("/{gameId}/quit")
    public ResponseEntity<GameDTO> restartGame(@PathVariable String gameId) {
        return ResponseEntity.ok(service.restartGame(gameId));
    }

    /**
     * delete game
     */
    @DeleteMapping("/{gameId}/quit")
    public ResponseEntity<BasicResponseDTO> deleteGame(@PathVariable String gameId) {
        service.deleteGame(gameId);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted game"));
    }

    @GetMapping("/{gameId}/spin")
    public ResponseEntity<GameDTO> spin(@PathVariable String gameId) {
        return ResponseEntity.ok(service.handleTask(gameId, new SpinGameTask()));
    }

    @PostMapping("/{gameId}/consonant/{consonant}")
    public ResponseEntity<GameDTO> guessConsonant(@PathVariable String gameId, @PathVariable char consonant) {
        return ResponseEntity.ok(service.handleTask(gameId, new GuessConsonantGameTask(consonant)));
    }

    @PostMapping("/{gameId}/solve/{solution}")
    public ResponseEntity<GameDTO> solvePuzzle(@PathVariable String gameId, @PathVariable String solution){
        return ResponseEntity.ok(service.handleTask(gameId, new SolvePuzzleGameTask(solution)));
    }

    @PostMapping("/{gameId}/risk")
    public ResponseEntity<GameDTO> answerQuestion(@PathVariable String gameId,
            @RequestBody CorrectQuestionRequest request) {
        return ResponseEntity.ok(service.handleTask(gameId, new AnswerQuestionGameTask(request)));
    }

    @PostMapping("/{gameId}/vowel/{vowel}")
    public ResponseEntity<GameDTO> buyVowel(@PathVariable String gameId, @PathVariable char vowel){
        return ResponseEntity.ok(service.handleTask(gameId, new BuyVowelGameTask(vowel)));
    }

    @PostMapping("/{gameId}/next")
    public ResponseEntity<GameDTO> nextSentence(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new NextSentenceGameTask()));
    }

    @PostMapping("/{gameId}/bankrupt")
    public ResponseEntity<GameDTO> acceptBankruptcy(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new BankruptGameTask()));
    }

    @PostMapping("/{gameId}/hpdeath")
    public ResponseEntity<GameDTO> acceptHPDeath(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new HPDeathGameTask()));
    }

    @PostMapping("/{gameId}/win")
    public ResponseEntity<GameDTO> acceptWin(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new WinGameTask(highScoreService)));
    }
}

