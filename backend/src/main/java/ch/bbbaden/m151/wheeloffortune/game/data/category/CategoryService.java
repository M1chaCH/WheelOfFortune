package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.util.BasicResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService extends GenericAuthenticatedEntityService<Integer, Category, CategoryRepo> {

    @Autowired
    protected CategoryService(SecurityTokenService securityTokenService, CategoryRepo repo) {
        super(securityTokenService, repo);
    }

    public List<CategoryDTO> getAllAndMapToDto(){
        return StreamSupport.stream(super.getAll().spliterator(), false)
                .map(Category::parseToDTO)
                .collect(Collectors.toList());
    }

    public BasicResponseDTO createCategory(String st, CategoryDTO dto){
        super.addNew(st, Category.fromDTO(dto));
        return new BasicResponseDTO("Category [" + dto.getId() + "] successfully created");
    }

    public BasicResponseDTO updateCategory(String st, CategoryDTO dto){
        super.edit(st, Category.fromDTO(dto));
        return new BasicResponseDTO("Category [" + dto.getId() + "] successfully updated");
    }

    public BasicResponseDTO deleteCategory(String st, CategoryDTO dto){
        super.delete(st, Category.fromDTO(dto));
        return new BasicResponseDTO("Category [" + dto.getId() + "] successfully deleted");
    }
}
