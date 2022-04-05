package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/game/highscore")
@Tag(name = "HighScore")
public class HighScoreController {

    private final HighScoreService service;

    @Operation(summary = "Get all HighScores")
    @GetMapping()
    public ResponseEntity<List<HighScoreDTO>> readAll(){
        return ResponseEntity.ok(service.getAllSortedByScoreAsDto());
    }

    @Operation(summary = "edit a HighScore")
    @PutMapping()
    public ResponseEntity<BasicResponseDTO> editHighScore(
            @RequestHeader(name = CustomHTTPHeaders.AUTH) String auth,
            @RequestBody HighScoreDTO editedDTO){
        service.edit(auth, editedDTO);
        return ResponseEntity.ok(new BasicResponseDTO("successfully applied changes so HighScore"));
    }

    @Operation(summary = "Delete a HighScore")
    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDTO> deleteHighScore(
            @RequestHeader(name = CustomHTTPHeaders.AUTH) String auth,
            @PathVariable Integer id){
        service.delete(auth, id);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted HighScore with id: " + id));
    }
}
