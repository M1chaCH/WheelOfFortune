package ch.bbbaden.m151.wheeloffortune.game;

import ch.bbbaden.m151.wheeloffortune.game.entity.CorrectQuestionRequest;
import ch.bbbaden.m151.wheeloffortune.game.entity.GameDTO;
import ch.bbbaden.m151.wheeloffortune.game.entity.StartGameRequest;
import ch.bbbaden.m151.wheeloffortune.game.highscore.HighScoreService;
import ch.bbbaden.m151.wheeloffortune.game.task.*;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/game")
@Tag(name = "Game")
public class GameController {

    private final GameService service;
    private final HighScoreService highScoreService;

    @Operation(summary = "start a new Game")
    @PostMapping()
    public ResponseEntity<GameDTO> startGame(@RequestBody StartGameRequest startGameRequest) {
        return ResponseEntity.ok(service.startNewGame(startGameRequest));
    }

    @Operation(summary = "load a Game by its id")
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> loadGame(@PathVariable String gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    @Operation(summary = "Game Task: Quit (doesn't delete)")
    @PostMapping("/{gameId}/quit")
    public ResponseEntity<GameDTO> quitGame(@PathVariable String gameId) {
        return ResponseEntity.ok(service.handleTask(gameId, new QuitGameTask(highScoreService)));
    }

    @Operation(summary = "actually delete a game")
    @DeleteMapping("/{gameId}/quit")
    public ResponseEntity<BasicResponseDTO> deleteGame(@PathVariable String gameId) {
        service.deleteGame(gameId);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted game"));
    }

    @Operation(summary = "Game Task: Spin")
    @GetMapping("/{gameId}/spin")
    public ResponseEntity<GameDTO> spin(@PathVariable String gameId) {
        return ResponseEntity.ok(service.handleTask(gameId, new SpinGameTask()));
    }

    @Operation(summary = "Game Task: Guess a Consonant")
    @PostMapping("/{gameId}/consonant/{consonant}")
    public ResponseEntity<GameDTO> guessConsonant(@PathVariable String gameId, @PathVariable char consonant) {
        return ResponseEntity.ok(service.handleTask(gameId, new GuessConsonantGameTask(consonant)));
    }

    @Operation(summary = "Game Task: Solve Puzzle")
    @PostMapping("/{gameId}/solve/{solution}")
    public ResponseEntity<GameDTO> solvePuzzle(@PathVariable String gameId, @PathVariable String solution){
        return ResponseEntity.ok(service.handleTask(gameId, new SolvePuzzleGameTask(solution)));
    }

    @Operation(summary = "Game Task: Game Task check answer on Question")
    @PostMapping("/{gameId}/risk")
    public ResponseEntity<GameDTO> answerQuestion(@PathVariable String gameId,
            @RequestBody CorrectQuestionRequest request) {
        return ResponseEntity.ok(service.handleTask(gameId, new AnswerQuestionGameTask(request)));
    }

    @Operation(summary = "Game Task: Buy a vowel")
    @PostMapping("/{gameId}/vowel/{vowel}")
    public ResponseEntity<GameDTO> buyVowel(@PathVariable String gameId, @PathVariable char vowel){
        return ResponseEntity.ok(service.handleTask(gameId, new BuyVowelGameTask(vowel)));
    }

    @Operation(summary = "Game Task: proceed to next Sentence")
    @PostMapping("/{gameId}/next")
    public ResponseEntity<GameDTO> nextSentence(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new NextSentenceGameTask()));
    }

    @Operation(summary = "Game Task: accept Bankruptcy")
    @PostMapping("/{gameId}/bankrupt")
    public ResponseEntity<GameDTO> acceptBankruptcy(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new BankruptGameTask()));
    }

    @Operation(summary = "Game Task: accept HP Death")
    @PostMapping("/{gameId}/hpdeath")
    public ResponseEntity<GameDTO> acceptHPDeath(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new HPDeathGameTask()));
    }

    @Operation(summary = "Game Task: accept Win over Game")
    @PostMapping("/{gameId}/win")
    public ResponseEntity<GameDTO> acceptWin(@PathVariable String gameId){
        return ResponseEntity.ok(service.handleTask(gameId, new WinGameTask(highScoreService)));
    }
}

