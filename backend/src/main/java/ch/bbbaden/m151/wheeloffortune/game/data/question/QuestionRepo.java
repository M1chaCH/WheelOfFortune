package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepo extends CrudRepository<Question, Integer> {

    List<Question> findQuestionsByCategory(Category category);
}
