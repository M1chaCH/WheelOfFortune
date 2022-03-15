package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentenceService extends GenericAuthenticatedEntityService<Integer, SentenceDTO, Sentence, SentenceRepo> {

    private final CategoryService categoryService;

    @Autowired
    protected SentenceService(SecurityTokenService securityTokenService, SentenceRepo repo,
            CategoryService categoryService) {
        super(securityTokenService, repo);
        this.categoryService = categoryService;
    }

    /**
     * @param categoryId the id of the category to search
     * @return a list of {@link SentenceDTO}s that are in the searched category
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException when category
     * does not exist
     */
    public List<SentenceDTO> getAllAsDtoByCategory(Integer categoryId){
        Category category = categoryService.getById(categoryId);
        return repo.findSentencesByCategory(category).stream()
                .map(Sentence::parseToDTO)
                .collect(Collectors.toList());
    }
}
