package ch.bbbaden.m151.wheeloffortune.game;

import ch.bbbaden.m151.wheeloffortune.game.entity.GameDTO;
import ch.bbbaden.m151.wheeloffortune.game.entity.StartGameRequest;
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

    /**
     * starts a new game with the given username
     */
    @GetMapping()
    public ResponseEntity<GameDTO> startGame(@RequestBody StartGameRequest startGameRequest){
        return ResponseEntity.ok(service.startNewGame(startGameRequest));
    }

    /**
     * get the GameDTO from a game id, can be used after page reload
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> loadGame(@PathVariable String gameId){
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    /**
     * restart a game
     */
    @PostMapping("/{gameId}")
    public ResponseEntity<GameDTO> restartGame(@PathVariable String gameId){
        return ResponseEntity.ok(service.restartGame(gameId));
    }

    /**
     * delete a game
     */
    @DeleteMapping("/{gameId}")
    public ResponseEntity<BasicResponseDTO> deleteGame(@PathVariable String gameId){
        service.deleteGame(gameId);
        return ResponseEntity.ok(new BasicResponseDTO("Game successfully deleted"));
    }

    @GetMapping("/{gameId}/spin")
    public ResponseEntity<GameDTO> spin(@PathVariable String gameId){
        return ResponseEntity.ok(service.spin(gameId));
    }

    @PostMapping("/{gameId}/consonant/{consonant}")
    public ResponseEntity<GameDTO> guessConsonant(@PathVariable String gameId, @PathVariable char consonant){
        return ResponseEntity.ok(service.guessConsonant(gameId, Character.toLowerCase(consonant)));
    }
}
