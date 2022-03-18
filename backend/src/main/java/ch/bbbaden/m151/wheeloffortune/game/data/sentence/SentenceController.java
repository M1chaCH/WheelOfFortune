package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/game/data/sentence")
public class SentenceController {

    private final SentenceService service;

    @GetMapping()
    public ResponseEntity<List<SentenceDTO>> getAllSentences(){
        return ResponseEntity.ok(service.getAllAsDto());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<SentenceDTO>> getAllByCategories(@PathVariable(name = "categoryId") Integer categoryId){
        return ResponseEntity.ok(service.getAllAsDtoByCategory(categoryId));
    }

    @PostMapping()
    public ResponseEntity<SentenceDTO> createSentences(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @RequestBody SentenceDTO dto){
        return ResponseEntity.ok(service.addNew(st, dto));
    }

    @PutMapping()
    public ResponseEntity<SentenceDTO> updateSentences(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @RequestBody SentenceDTO dto){
        return ResponseEntity.ok(service.edit(st, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDTO> deleteSentences(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @PathVariable Integer id){
        service.delete(st, id);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted sentence ["
                + id + "]"));
    }
}
