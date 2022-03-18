package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.config.CustomHTTPHeaders;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/game/data/category")
public class CategoryController {

    private final CategoryService service;

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return ResponseEntity.ok(service.getAllAsDto());
    }

    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @RequestBody CategoryDTO dto){
        return ResponseEntity.ok(service.addNew(st, dto));
    }

    @PutMapping()
    public ResponseEntity<CategoryDTO> updateCategory(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @RequestBody CategoryDTO dto){
        return ResponseEntity.ok(service.edit(st, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDTO> deleteCategory(
            @RequestHeader(CustomHTTPHeaders.AUTH) String st,
            @PathVariable Integer id){
        service.delete(st, id);
        return ResponseEntity.ok(new BasicResponseDTO("successfully deleted category ["
                + id + "]"));
    }
}
