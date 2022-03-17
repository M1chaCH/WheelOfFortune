package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityServiceTest;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuestionServiceTest extends GenericAuthenticatedEntityServiceTest<Integer, QuestionDTO, Question, QuestionRepo> {

    @Autowired
    QuestionService service;

    @MockBean
    QuestionRepo repoMock;

    @MockBean
    CategoryService categoryServiceMock;

    @Test
    void getAllAsDtoByCategory_successTest() {
        final Integer categoryId = 1;
        List<Question> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenReturn(category);
        when(repoMock.findQuestionsByCategory(category)).thenReturn(entities);

        List<QuestionDTO> returnedDTOs = service.getAllAsDtoByCategory(categoryId);
        assertEquals(entities.size(), returnedDTOs.size());
        for (int i = 0; i < entities.size(); i++) {
            assertTrue(doesDTOEqualEntity(returnedDTOs.get(i), entities.get(i)));
        }
    }

    @Test
    void getAllAsDtoByCategory_deletedCategoryTest() {
        final Integer categoryId = 2;
        List<Question> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenThrow(EntityNotFoundException.class);
        when(repoMock.findQuestionsByCategory(category)).thenReturn(entities);

        assertThrows(EntityNotFoundException.class, () -> service.getAllAsDtoByCategory(categoryId));
    }

    @Override
    protected QuestionRepo getRepoMock() {
        return repoMock;
    }

    @Override
    protected GenericAuthenticatedEntityService<Integer, QuestionDTO, Question, QuestionRepo> getService() {
        return service;
    }

    @Override
    protected Question generateEntity() {
        return generateEntity(1);
    }

    private Question generateEntity(Integer id){
        Question question = new Question(
                "question" + id,
                "answerOne",
                "answerTwo",
                true,
                new Category("category")
        );
        question.setId(id);
        return question;
    }

    @Override
    protected List<Question> generateSomeEntities() {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            questions.add(generateEntity(i));
        }
        return questions;
    }

    @Override
    protected QuestionDTO generateDTO() {
        return new QuestionDTO(
                1,
                "question1",
                "answerOne",
                "answerTwo",
                true,
                new CategoryDTO(1, "category")
        );
    }

    @Override
    protected boolean doesDTOEqualEntity(QuestionDTO dto, Question entity) {
        return dto.getId().equals(entity.getId()) &&
                dto.getQuestion().equals(entity.getQuestion()) &&
                dto.getAnswerOne().equals(entity.getAnswerOne()) &&
                dto.getAnswerTwo().equals(entity.getAnswerTwo()) &&
                dto.isAnswerOneCorrect() == entity.isAnswerOneCorrect() &&
                dto.getCategoryDTO().equals(entity.getCategory().parseToDTO());
    }
}