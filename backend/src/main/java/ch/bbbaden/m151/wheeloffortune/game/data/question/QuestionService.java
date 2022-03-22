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

    public static final int MAX_QUESTION_LENGTH = 50;
    public static final int MIN_QUESTION_LENGTH = 10;
    public static final int MAX_QUESTION_ANSWER_LENGTH = 20;
    public static final int MIN_QUESTION_ANSWER_LENGTH = 2;

    private final CategoryService categoryService;

    @Autowired
    public QuestionService(SecurityTokenService securityTokenService, QuestionRepo repo, CategoryService categoryService) {
        super(securityTokenService, repo);
        this.categoryService = categoryService;
    }

    @Override
    public boolean isEntityValid(Question entity) {
        try{
            String question = entity.getQuestion();
            String answerOne = entity.getAnswerOne();
            String answerTwo = entity.getAnswerTwo();

            return question.length() >= MIN_QUESTION_LENGTH
                    && question.length() <= MAX_QUESTION_LENGTH
                    && answerOne.length() >= MIN_QUESTION_ANSWER_LENGTH
                    && answerOne.length() <= MAX_QUESTION_ANSWER_LENGTH
                    && answerTwo.length() >= MIN_QUESTION_ANSWER_LENGTH
                    && answerTwo.length() <= MAX_QUESTION_ANSWER_LENGTH
                    // don't give the category directly because need to check if actually exists
                    && categoryService.isEntityValid(categoryService.getById(entity.getCategory().getId()));
        }catch (Exception e){
            return false;
        }
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
