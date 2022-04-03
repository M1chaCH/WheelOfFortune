package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SentenceService extends GenericAuthenticatedEntityService<Integer, SentenceDTO, Sentence, SentenceRepo> {

    public static final int MAX_SENTENCE_LENGTH = 35;
    public static final int MIN_SENTENCE_LENGTH = 10;

    private final CategoryService categoryService;

    @Autowired
    protected SentenceService(SecurityTokenService securityTokenService, SentenceRepo repo,
            CategoryService categoryService) {
        super(securityTokenService, repo);
        this.categoryService = categoryService;
    }

    @Override
    protected boolean isEntityValid(Sentence entity) {
        try{
            String sentence = entity.getSentence();
            return sentence.length() >= MIN_SENTENCE_LENGTH
                    && sentence.length() <= MAX_SENTENCE_LENGTH
                    && sentence.matches("[A-Za-z \\-.,!\"'?;öäü]*")
                    // don't give the category directly because need to check if actually exists
                    && categoryService.isEntityValid(categoryService.getById(entity.getCategory().getId()));
        }catch (Exception e){
            return false;
        }
    }

    /**
     * @param categoryId the id of the category to search
     * @return a list of {@link Sentence}s that are in the searched category
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException when category
     * does not exist
     */
    public List<Sentence> getAllByCategory(Integer categoryId){
        Category category = categoryService.getById(categoryId);
        return new ArrayList<>(repo.findSentencesByCategory(category));
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

    public Map<CategoryDTO, List<SentenceDTO>> getAllSortedByCategory(){
        List<CategoryDTO> categories = categoryService.getAllAsDto();
        Map<CategoryDTO, List<SentenceDTO>> map = new HashMap<>();
        for (CategoryDTO category : categories) {
            map.put(category, getAllAsDtoByCategory(category.getId()));
        }
        return map;
    }
}
