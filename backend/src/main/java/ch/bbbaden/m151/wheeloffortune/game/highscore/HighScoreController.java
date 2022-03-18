package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/game/highscore")
public class HighScoreController {

    private final HighScoreService service;

    @GetMapping()
    public ResponseEntity<List<HighScoreDTO>> readAll(){
        return ResponseEntity.ok(service.getAllSortedByDateAsDto());
    }

    @PutMapping()
    public ResponseEntity<BasicResponseDTO> editHighScore(
            @RequestHeader(name = CustomHTTPHeaders.AUTH) String auth,
            @RequestBody HighScoreDTO editedDTO){
        service.edit(auth, editedDTO);
        return ResponseEntity.ok(new BasicResponseDTO("successfully applied changed so HighScore"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDTO> deleteHighScore(
            @RequestHeader(name = CustomHTTPHeaders.AUTH) String auth,
            @PathVariable Integer id){
        service.delete(auth, id);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted HighScore with id: " + id));
    }
}
