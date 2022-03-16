package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityServiceTest;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionServiceTest extends GenericAuthenticatedEntityServiceTest<Integer, QuestionDTO, Question, QuestionRepo> {

    @Autowired
    QuestionService service;

    @MockBean
    QuestionRepo repoMock;

    //TODO:
    @Test
    void getAllAsDtoByCategory() {
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