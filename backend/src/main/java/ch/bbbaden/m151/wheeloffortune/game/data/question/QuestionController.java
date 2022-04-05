package ch.bbbaden.m151.wheeloffortune.game.data.question;

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
@RequestMapping("/game/data/question")
@Tag(name = "Questions")
public class QuestionController {

    private final QuestionService service;

    @Operation(summary = "Get all questions")
    @GetMapping()
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(){
        return ResponseEntity.ok(service.getAllAsDto());
    }

    @Operation(summary = "Get all questions from a category")
    @GetMapping("/{categoryId}")
    public ResponseEntity<List<QuestionDTO>> getAllByCategories(@PathVariable(name = "categoryId") Integer categoryId){
        return ResponseEntity.ok(service.getAllAsDtoByCategory(categoryId));
    }

    @Operation(summary = "save a new question")
    @PostMapping()
    public ResponseEntity<QuestionDTO> createQuestion(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @RequestBody QuestionDTO dto){
        return ResponseEntity.ok(service.addNew(st, dto));
    }

    @Operation(summary = "edit a question")
    @PutMapping()
    public ResponseEntity<QuestionDTO> updateQuestion(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @RequestBody QuestionDTO dto){
        return ResponseEntity.ok(service.edit(st, dto));
    }

    @Operation(summary = "delete a question")
    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDTO> deleteQuestion(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @PathVariable Integer id){
        service.delete(st, id);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted question ["
                + id + "]"));
    }
}
