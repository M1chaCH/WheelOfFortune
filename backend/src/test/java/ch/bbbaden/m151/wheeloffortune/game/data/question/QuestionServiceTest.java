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
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void getAllByCategory_successTest(){
        final Integer categoryId = 1;
        List<Question> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenReturn(category);
        when(repoMock.findQuestionsByCategory(category)).thenReturn(entities);

        List<Question> returnedEntities = service.getAllByCategory(categoryId);
        assertEquals(entities.size(), returnedEntities.size());
        for (int i = 0; i < entities.size(); i++) {
            assertEquals(returnedEntities.get(i), entities.get(i));
        }
    }

    @Test
    void getAllByCategory_deletedTest(){
        final Integer categoryId = 2;
        List<Question> entities = generateSomeEntities();
        Category category = new CategoryDTO(categoryId, "random").parseToEntity();
        when(categoryServiceMock.getById(categoryId)).thenThrow(EntityNotFoundException.class);
        when(repoMock.findQuestionsByCategory(category)).thenReturn(entities);

        assertThrows(EntityNotFoundException.class, () -> service.getAllByCategory(categoryId));
    }

    @Test
    void isEntityValid_trueTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("This is a cool Question?", "A1", "A2", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertTrue(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_questionShortTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("Question?", "A1", "A2", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertFalse(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_questionLongTest(){
        Category category = new Category("some dud");
        Question question = new Question("This is a way to long Question ey. Would you please help me fix that?",
                "A1", "A2", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertFalse(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_answerOneToShortTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("This is a cool Question?", "A", "A2", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertFalse(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_answerOneToLongTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("This is a cool Question?", "This is again a way to long answer",
                        "A2", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertFalse(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_answerTwoToShortTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("This is a cool Question?", "A1", "A", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertFalse(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_answerTwoToLongTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("This is a cool Question?", "A1",
                        "This is again a way to long answer", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
        assertFalse(service.isEntityValid(question));
    }

    @Test
    void isEntityValid_categoryInvalidTest(){
        Category category = new Category("some dud");
        Question question =
                new Question("This is a cool Question?", "A1", "A2", true, category);
        when(categoryServiceMock.isEntityValid(any())).thenReturn(false);
        assertFalse(service.isEntityValid(question));
    }

    @Override
    protected QuestionRepo getRepoMock() {
        return repoMock;
    }

    @Override
    protected GenericAuthenticatedEntityService<Integer, QuestionDTO, Question, QuestionRepo> getGenericService() {
        return service;
    }

    @Override
    protected void mockAdditionalBeans() {
        super.mockAdditionalBeans();
        when(categoryServiceMock.isEntityValid(any())).thenReturn(true);
    }

    @Override
    protected Question generateEntity() {
        return generateEntity(1);
    }

    private Question generateEntity(Integer id){
        Question question = new Question(
                "This is question with the number: " + id + "?",
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
                "This is question with the number: 1?",
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