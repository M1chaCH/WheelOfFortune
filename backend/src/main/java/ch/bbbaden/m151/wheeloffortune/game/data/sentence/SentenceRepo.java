package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SentenceRepo extends CrudRepository<Sentence, Integer> {

    List<Sentence> findSentencesByCategory(Category category);
}
