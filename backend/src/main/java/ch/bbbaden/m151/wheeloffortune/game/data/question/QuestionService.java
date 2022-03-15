package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService extends GenericAuthenticatedEntityService<Integer, QuestionDTO, Question, QuestionRepo> {

    private final CategoryService categoryService;

    @Autowired
    public QuestionService(SecurityTokenService securityTokenService, QuestionRepo repo, CategoryService categoryService) {
        super(securityTokenService, repo);
        this.categoryService = categoryService;
    }

    /**
     * @param categoryId the id of the category to search
     * @return a list of {@link QuestionDTO}s that are in the searched category
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException when category
     * does not exist
     */
    public List<QuestionDTO> getAllAsDtoByCategory(Integer categoryId){
        Category category = categoryService.getById(categoryId);
        return repo.findQuestionsByCategory(category).stream()
                .map(Question::parseToDTO)
                .collect(Collectors.toList());
    }
}
